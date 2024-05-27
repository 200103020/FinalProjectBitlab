package com.example.stellarinvestment.controller;

import com.example.stellarinvestment.exception.UserNotFoundException;
import com.example.stellarinvestment.mail.Utility;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.repository.RoleRepository;
import com.example.stellarinvestment.service.ProjectService;
import com.example.stellarinvestment.service.UserService;
import com.example.stellarinvestment.setting.EmailSettingBag;
import com.example.stellarinvestment.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login_user";
    }

    @GetMapping(value = "/resetPassword")
    public String resetPasswordPage(@Param("token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        if (user != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("invalidToken", "Invalid Token");
            return "Forgot_password";
        }

        return "reset_password";
    }

    @GetMapping(value = "/forgotPassword")
    public String forgotPasswordPage() {
        return "Forgot_password";
    }

    @GetMapping(value = "/main")
    public String mainPage(Model model) {
        model.addAttribute("projects", projectService.getAllProjectsEnabled());
        model.addAttribute("roles", null);
        return "Main_page";
    }

    @GetMapping(value = "/register")
    public String registerPage() {
        return "register_user";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code) {
        String redirect = "";
        if (userService.verify(code)) {
            redirect = "redirect:/auth/login/?verify_success";
        } else {
            redirect = "redirect:/auth/login/?verify_fail";
        }

        return redirect;
    }

    @PostMapping(value = "/register")
    public String registerPage(@RequestParam(name = "reg_email") String email,
                               @RequestParam(name = "reg_password") String password,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "surname") String surname,
                               @RequestParam(name = "role") String role,
                               HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        String redirect = "";

        User user = new User();
        user.setFirstName(name);
        user.setLastName(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.getRoles().add(roleRepository.findByName(role));

        if (userService.registerUser(user)) {
            userService.sendVerificationEmail(request, user);
            redirect = "redirect:/auth/login/?afterRegistration";
        } else {
            redirect = "redirect:/auth/login/?accountExist";
        }

        return redirect;
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(@RequestParam(name = "forgot_email") String email, HttpServletRequest request, Model model) {
        try {
            String token = userService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/auth/resetPassword?token=" + token;
            sendEmail(link, email);

            model.addAttribute("message", "We have sent a reset password link to your email."
                    + " Please check.");
        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Could not send email");
        }

        return "Forgot_password";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("reset_password");

        try {
            userService.updatePassword(token, password);
            model.addAttribute("message", "You have successfully changed your password.");

        } catch (UserNotFoundException e) {
            model.addAttribute("invalidToken", e.getMessage());
        }

        return "login_user";
    }

    private void sendEmail(String link, String email)
            throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(email);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

}
