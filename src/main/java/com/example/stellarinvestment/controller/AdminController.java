package com.example.stellarinvestment.controller;

import com.example.stellarinvestment.exception.ProjectNotFoundException;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.model.project.ProjectStatus;
import com.example.stellarinvestment.service.CandidateService;
import com.example.stellarinvestment.service.ProjectService;
import com.example.stellarinvestment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CandidateService candidateService;


    @GetMapping(value = "/projects")
    public String adminPage(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        model.addAttribute("incomingProjects", projectService.getProjectsWithStatus(ProjectStatus.WAITING));
        model.addAttribute("approvedProjects", projectService.getProjectsWithStatus(ProjectStatus.IN_PROGRESS));
        model.addAttribute("rejectedProjects", projectService.getProjectsWithStatus(ProjectStatus.REJECTED));
        return "Project_admin";
    }

    @GetMapping(value = "/details/{project_id}")
    public String detailOfProjectPage(HttpServletRequest request, Model model, @PathVariable(name = "project_id") Integer id) throws ProjectNotFoundException {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        Project project = projectService.get(id);

        model.addAttribute("teamMembers", candidateService.getAllApprovedCandidates(project));
        model.addAttribute("project", project);
        return "Detail_page_admin";
    }

    @PostMapping("/approve")
    public String approveTheProject(@RequestParam(name = "projectIdToApprove") Integer projectIdToApprove) throws ProjectNotFoundException {
        Project project = projectService.get(projectIdToApprove);

        if (project != null) {
            project.setEnabled(true);
            project.setStatus(ProjectStatus.IN_PROGRESS);
        }

        projectService.saveProject(project);

        assert project != null;
        return "redirect:/admin/projects/?approved";
    }

    @PostMapping("/reject")
    public String rejectTheProject(@RequestParam(name = "projectIdToReject") Integer projectIdToReject) throws ProjectNotFoundException {
        Project project = projectService.get(projectIdToReject);

        if (project != null) {
            project.setEnabled(false);
            project.setStatus(ProjectStatus.REJECTED);
        }

        projectService.saveProject(project);

        assert project != null;
        return "redirect:/admin/projects?rejected";
    }
}
