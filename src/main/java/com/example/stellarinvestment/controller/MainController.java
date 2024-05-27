package com.example.stellarinvestment.controller;

import com.example.stellarinvestment.amazon.AmazonS3Util;
import com.example.stellarinvestment.model.Role;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.service.CandidateService;
import com.example.stellarinvestment.service.InvestmentService;
import com.example.stellarinvestment.service.ProjectService;
import com.example.stellarinvestment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/main")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CandidateService candidateService;

    @GetMapping(value = "/")
    public String authMainPage(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        if (authenticatedUser != null) {
            Set<String> roles = authenticatedUser.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet());

            List<Project> allProjectsWithStatus = projectService.getAllProjectsWithStatus(authenticatedUser);
            investmentService.setTheIntermediateValues(allProjectsWithStatus);
            model.addAttribute("roles", roles);
            model.addAttribute("user", authenticatedUser);
            model.addAttribute("projects", allProjectsWithStatus);
        }

        return "Main_page";
    }

    @GetMapping(value = "/forbidden")
    public String forbiddenPage() {
        return "forbiddenPage";
    }

    @GetMapping(value = "/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {
        String email = userService.getEmailOfAuthenticatedCustomer(request);
        User user = userService.getUserByEmail(email);
        model.addAttribute("myRequests", candidateService.getAllMyRequests(user));
        model.addAttribute("user", user);
        return "profile_client";
    }

    @PostMapping("/update_password")
    public String updateUserPassword(@RequestParam(name = "currentPassword") String currentPassword,
                                     @RequestParam(name = "newPassword") String newPassword,
                                     @RequestParam(name = "email") String email,
                                     Model model) {
        User authenticatedUser = userService.getUserByEmail(email);
        String[] result = userService.updateAuthPassword(authenticatedUser, currentPassword, newPassword);
        String messagePassword = result[0];
        String path = result[1];
        model.addAttribute("messagePassword", messagePassword);

        return "redirect:/main/account_details?" + path;
    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, User user,
                                       HttpServletRequest request, @RequestParam("profile_url") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);

            String uploadDir = "user-ava-photos/" + user.getId();

            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }

        model.addAttribute("message", "Your account details have been updated.");

        userService.updateNameForAuthenticatedCustomer(user, request);

        return "redirect:/main/account_details";
    }


}