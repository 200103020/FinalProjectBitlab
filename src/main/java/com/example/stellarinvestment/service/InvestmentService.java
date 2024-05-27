package com.example.stellarinvestment.service;

import com.example.stellarinvestment.exception.ProjectNotFoundException;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Investment;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TariffService tariffService;

    public void saveInvestment(Investment investment) {
        investmentRepository.save(investment);
    }

    public void deleteInvestment(Investment investment) {
        investmentRepository.delete(investment);
    }


    public Investment getInvestmentById(Integer id) {
        return investmentRepository.findById(id).get();
    }

    public List<Investment> getAllInvestmentsOfUserInBasket(User user) {
        return investmentRepository.findAllByUserAndPaidIsFalse(user);
    }

    public List<Investment> getAllInvestmentsOfUserPaid(User user) {
        return investmentRepository.findAllByUserAndPaidIsTrue(user);
    }

    public List<Investment> getAllInvestmentsByProject(Project project) {
        return investmentRepository.findAllByProjectAndPaidIsTrue(project);
    }



    public Map<String, Integer> calculateTotalInvestmentAndInvestors(Project project) {
        List<Investment> investments = getAllInvestmentsByProject(project);
        int totalInvestment = 0;
        Set<Integer> uniqueInvestors = new HashSet<>();

        for (Investment investment : investments) {
            totalInvestment += investment.getInvestedMoney();
            uniqueInvestors.add(investment.getUser().getId());
        }

        int totalInvestors = uniqueInvestors.size();

        Map<String, Integer> result = new HashMap<>();
        result.put("totalInvestment", totalInvestment);
        result.put("totalInvestors", totalInvestors);
        return result;
    }

    public void setTheIntermediateValues(List<Project> projectList) {
        for (Project project : projectList) {
            getValuesAndSet(project);
        }
    }


    public void setTheIntermediateValue(Project project) {
        getValuesAndSet(project);
    }

    private void getValuesAndSet(Project project) {
        Map<String, Integer> result = calculateTotalInvestmentAndInvestors(project);
        int totalInvestment = result.get("totalInvestment");
        int totalInvestors = result.get("totalInvestors");
        project.setTotalInvestment(totalInvestment);
        project.setInvestorsCount(totalInvestors);

        double percent = ((double) totalInvestment / project.getAmountNeeded()) * 100;
        double roundedPercent = ((int) (percent * 10)) / 10.0;
        project.setPercent(roundedPercent);
    }


    public Investment createInvestment(User user, Integer projectId, Integer tariffId) throws ProjectNotFoundException {
        Investment investment = new Investment();
        investment.setInvestedTime(new Date());
        investment.setUser(user);
        investment.setProject(projectService.get(projectId));
        investment.setPaid(false);
        investment.setInvestedMoney(0);
        investment.setProjectTariff(tariffService.getTariffById(tariffId));
        return investment;
    }

    public String generateRandomNumber() {
        Random random = new Random();
        int firstPart = random.nextInt(999999) + 100000;
        int secondPart = random.nextInt(999999999) + 100000000;
        return firstPart + "_" + secondPart;
    }
}
