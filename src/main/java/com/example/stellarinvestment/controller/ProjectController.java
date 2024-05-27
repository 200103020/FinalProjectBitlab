package com.example.stellarinvestment.controller;

import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Candidate;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.exception.ProjectNotFoundException;
import com.example.stellarinvestment.model.project.ProjectStatus;
import com.example.stellarinvestment.model.project.Team;
import com.example.stellarinvestment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private InvestmentService investmentService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/")
    public String projectCreateStartPage(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        return "Create_page";
    }

    @GetMapping(value = "/create/new/")
    public String projectCreateNewPage(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        return "Create_page_start";
    }

    @GetMapping(value = "/my/")
    public String allMyProjects(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);

        List<Project> projectList = projectService.getProjectsWithStatus(ProjectStatus.TEAM_FORMATION);
        for (Project project:
                projectList) {
            if (teamService.getCountOfCandidateNeeded(project) == candidateService.getProjectCountOfCandidates(project)) {
                project.setStatus(ProjectStatus.WAITING);
                projectService.saveProject(project);
            }
        }

        model.addAttribute("myProjects", projectService.getProjectsCreatedByUser(authenticatedUser));
        model.addAttribute("myInvestmentProjects", projectService.getProjectsCreatedByUserAndStatus(authenticatedUser, ProjectStatus.IN_PROGRESS));
        return "My_Projects";
    }

    @GetMapping(value = "/my/created/")
    public String allMyCreatedProjects(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);

        List<Project> allMyCreatedProjects = projectService.getProjectsCreatedByUser(authenticatedUser);
        investmentService.setTheIntermediateValues(allMyCreatedProjects);

        model.addAttribute("myProjects", allMyCreatedProjects);
        return "My_PROJECT_list";
    }

    @GetMapping(value = "/all/team/")
    public String allProjectsTeam(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        if (userService.hasRole(authenticatedUser, "User")) {
            model.addAttribute("teamProjects", projectService.getProjectsWithStatus(ProjectStatus.TEAM_FORMATION));
        }
        return "client_projects";
    }

    @GetMapping(value = "/details/{project_id}")
    public String detailOfProjectPage(HttpServletRequest request, Model model, @PathVariable(name = "project_id") Integer id) throws ProjectNotFoundException {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        Project project = projectService.get(id);

        for (Team team : project.getTeams()) {
            long count = candidateService.getAppliedCandidates(team);
            team.setCountOfApproved((int) count);
            teamService.updateTeam(team);
        }

        investmentService.setTheIntermediateValue(project);

        model.addAttribute("teamMembers", candidateService.getAllApprovedCandidates(project));
        model.addAttribute("project", project);
        return "Detail";
    }

    @PostMapping("/create_new")
    public String createNewProject(@RequestParam("tariffsList") String tariffsListJson, @RequestParam("personsList") String personsListJson,
                                   @RequestParam(name = "amountNeeded") float amountNeeded,
                                   @RequestParam(name = "shortDescription") String shortDescription,
                                   @RequestParam(name = "title") String title,
                                   @RequestParam(name = "longDescription") String longDescription,
                                   @RequestParam(name = "aboutCreator") String aboutCreator,
                                   @RequestParam("finishTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishTime,
                                   @RequestParam(name = "userEmail") String userEmail,
                                   @RequestParam("fileImage") MultipartFile mainImageMultipart,
                                   @RequestParam("extraImage") MultipartFile[] extraImageMultiparts) throws IOException {

        Project project = projectService.createNewProject(tariffsListJson, personsListJson, amountNeeded, shortDescription, title, longDescription, aboutCreator, finishTime, userEmail);

        projectService.setMainImageName(mainImageMultipart, project);
        projectService.setExtraImageNames(extraImageMultiparts, project);
        projectService.saveUploadedImages(mainImageMultipart, extraImageMultiparts, project);

        projectService.saveProject(project);

        return "redirect:/project/my/";
    }

    @PostMapping("/apply")
    public String candidateApplyToProject(@RequestParam(name = "cvLink") String cvLink,
                                          @RequestParam(name = "phoneNumber") String phoneNumber,
                                          @RequestParam(name = "userEmail") String userEmail,
                                          @RequestParam(name = "teamId") Integer teamId,
                                          @RequestParam(name = "projectId") Integer projectId) {
        String redirectMessage;
        try {
            User user = userService.getUserByEmail(userEmail);
            if (candidateService.getCandidateByTeamId(teamId, user)) {
                redirectMessage = "appliedAlready";
            } else if (candidateService.getApprovedCandidateInProject(projectId, user.getId())) {
                redirectMessage = "memberAlready";
            } else if (candidateService.checkAppliedOnce(projectId, user.getId())) {
                redirectMessage = "applyOnlyOne";
            } else {
                Candidate candidate = candidateService.firstCreateCandidate(cvLink, phoneNumber, user, teamId, projectId);
                candidateService.createCandidate(candidate);
                redirectMessage = "successfullyApplied";
            }
        } catch (ProjectNotFoundException e) {
            redirectMessage = "projectNotFound";
        }
        return "redirect:/main/account_details/?" + redirectMessage;
    }


    @PostMapping("/approve")
    public String approveTheCandidate(@RequestParam(name = "applicantId") Integer applicantId) {
        Candidate candidate = candidateService.getById(applicantId);
        long count = candidateService.getAppliedCandidates(candidate.getProjectTeam());
        String redirectMessage;

        if (candidate.getProjectTeam().getQuantity() > count) {
            candidate.setResult(true);
            candidate.setStatus(1);
            candidateService.update(candidate);
            redirectMessage = "approved";
        } else {
            redirectMessage = "full";
        }

        return "redirect:/project/my/?" + redirectMessage;
    }

    @PostMapping("/refuse")
    public String refuseTheCandidate(@RequestParam(name = "refuseapplicantId") Integer applicantId) {
        Candidate candidate = candidateService.getById(applicantId);
        if (candidate != null) {
            candidate.setResult(false);
            candidate.setStatus(2);
            candidateService.update(candidate);
        }

        return "redirect:/project/my/?refused";
    }
}
