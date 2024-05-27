package com.example.stellarinvestment.service;

import com.example.stellarinvestment.exception.UserNotFoundException;
import com.example.stellarinvestment.mail.Utility;
import com.example.stellarinvestment.model.AuthUserDetails;
import com.example.stellarinvestment.model.AuthenticationType;
import com.example.stellarinvestment.model.Role;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.repository.UserRepository;
import com.example.stellarinvestment.setting.EmailSettingBag;
import com.example.stellarinvestment.setting.SettingService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingService settingService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(String token, String newPassword) throws UserNotFoundException {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new UserNotFoundException("No user found: invalid token");
        }

        user.setPassword(newPassword);
        user.setResetPasswordToken(null);
        encodePassword(user);

        userRepository.save(user);
    }

    public String[] updateAuthPassword(User user, String currentPassword, String newPassword) {
        String message;
        String path;

        String userExistingPassword = user.getPassword();

        if (passwordEncoder.matches(currentPassword, userExistingPassword)) {
            user.setPassword(newPassword);
            encodePassword(user);
            userRepository.save(user);
            message = "Successfully changed password.";
            path = "changed";
        } else {
            message = "Your current password is incorrect.";
            path = "incorrect";
        }

        return new String[]{message, path};
    }


    public boolean registerUser(User user) {
        boolean check = false;
        if (user != null) {
            if (userRepository.findByEmail(user.getEmail()) == null) {
                encodePassword(user);
                user.setEnabled(false);
                user.setCreatedTime(new Date());
                user.setAuthenticationType(AuthenticationType.DATABASE);

                String randomCode = RandomString.make(64);
                user.setVerificationCode(randomCode);

                userRepository.save(user);
                check = true;
            }
        }

        return check;
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public void sendVerificationEmail(HttpServletRequest request, User user)
            throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = user.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());

        String verifyURL = Utility.getSiteURL(request) + "/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("to Address: " + toAddress);
        System.out.println("Verify URL: " + verifyURL);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void update(User userInForm) {
        userRepository.save(userInForm);
    }


    public String updateResetPasswordToken(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = RandomString.make(30);
            user.setResetPasswordToken(token);
            userRepository.save(user);
            return token;
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    public void updateNameForAuthenticatedCustomer(User user, HttpServletRequest request) {
        Object principal = request.getUserPrincipal();

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            AuthUserDetails userDetails = getAuthUserDetailsObject(principal);
            User authenticatedUser = userDetails.getUser();
            authenticatedUser.setFirstName(user.getFirstName());
            authenticatedUser.setLastName(user.getLastName());
            if (user.getPhotos() != null) {
                authenticatedUser.setPhotos(user.getPhotos());
            }
            update(authenticatedUser);
        }
    }

    public AuthUserDetails getAuthUserDetailsObject(Object principal) {
        AuthUserDetails userDetails = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            userDetails = (AuthUserDetails) token.getPrincipal();
        } else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            userDetails = (AuthUserDetails) token.getPrincipal();
        }

        return userDetails;
    }


    public String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        String userEmail = null;

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            userEmail = request.getUserPrincipal().getName();
        }

        return userEmail;
    }

    public User getCurrentAuthUser(HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        AuthUserDetails userDetails = getAuthUserDetailsObject(principal);

        return userDetails.getUser();
    }

    public boolean hasRole(User user, String roleName) {
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

}