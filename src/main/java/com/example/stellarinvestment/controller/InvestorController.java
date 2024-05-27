package com.example.stellarinvestment.controller;

import com.example.stellarinvestment.exception.ProjectNotFoundException;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Investment;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.model.project.ProjectStatus;
import com.example.stellarinvestment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/investor")
public class InvestorController {

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private InvestmentService investmentService;

    @GetMapping(value = "/all/projects/")
    public String allProjects(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);

        List<Project> projectList = projectService.getProjectsWithStatus(ProjectStatus.IN_PROGRESS);

        investmentService.setTheIntermediateValues(projectList);

        model.addAttribute("allProjects", projectList);
        return "Projects_for_Investor";
    }

    @GetMapping(value = "/basket")
    public String basketPage(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        model.addAttribute("investments", investmentService.getAllInvestmentsOfUserInBasket(authenticatedUser));

        return "Basket_Investor";
    }

    @GetMapping(value = "/my/investments")
    public String myInvestments(HttpServletRequest request, Model model) {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        model.addAttribute("user", authenticatedUser);
        model.addAttribute("investments", investmentService.getAllInvestmentsOfUserPaid(authenticatedUser));

        return "My_Investments";
    }


    @GetMapping(value = "/details/{project_id}")
    public String detailOfProjectPage(HttpServletRequest request, Model model, @PathVariable(name = "project_id") Integer id) throws ProjectNotFoundException {
        User authenticatedUser = userService.getCurrentAuthUser(request);
        Project project = projectService.get(id);

        investmentService.setTheIntermediateValue(project);

        model.addAttribute("user", authenticatedUser);
        model.addAttribute("teamMembers", candidateService.getAllApprovedCandidates(project));
        model.addAttribute("project", project);
        return "Detail_page_Investor";
    }

    @PostMapping("/addToBasket")
    public String addToBasket(@RequestParam(name = "projectId") Integer projectId,
                              @RequestParam(name = "tariffId") Integer tariffId,
                              @RequestParam(name = "userEmail") String userEmail) throws ProjectNotFoundException {
        String redirectMessage = "added";
        User user = userService.getUserByEmail(userEmail);

        if (user != null) {
            Investment investment = investmentService.createInvestment(user, projectId, tariffId);
            investmentService.saveInvestment(investment);
        }

        return "redirect:/investor/basket/?" + redirectMessage;
    }

    @PostMapping("/invest")
    public String investMoneyToThatProject(@RequestParam(name = "investmentId") Integer investmentId,
                                           @RequestParam(name = "investmentMoney") Integer investmentMoney,
                                           @RequestParam("bank") String bank) {
        String redirectMessage = "paid";
        Investment investment = investmentService.getInvestmentById(investmentId);

        if (investment != null) {
            investment.setInvestedMoney(investmentMoney);
            investment.setPaid(true);
            investment.setBanked(bank);
            investment.setIdentifier(investmentService.generateRandomNumber());
        } else {
            redirectMessage = "notPaid";
        }

        investmentService.saveInvestment(investment);

        return "redirect:/investor/my/investments/?" + redirectMessage;
    }

    @PostMapping("/deleteFromBasket")
    public String deleteFromBasket(@RequestParam(name = "investmentId") Integer investmentId) {
        Investment investment = investmentService.getInvestmentById(investmentId);
        if (investment != null) {
            investmentService.deleteInvestment(investment);
        }

        return "redirect:/investor/basket/?deleted";
    }
}
