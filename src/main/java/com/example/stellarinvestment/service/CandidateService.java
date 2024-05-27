package com.example.stellarinvestment.service;

import com.example.stellarinvestment.exception.ProjectNotFoundException;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Candidate;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.model.project.Team;
import com.example.stellarinvestment.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ProjectService projectService;

    public void createCandidate(Candidate candidate) {
        Team team = candidate.getProjectTeam();

        long approvedCandidatesCount = candidateRepository.countByProjectTeamAndResultIsTrue(team);

        if (approvedCandidatesCount < team.getQuantity()) {
            candidateRepository.save(candidate);
        } else {
            throw new RuntimeException("Team's candidate quantity limit reached");
        }
    }


    public Candidate firstCreateCandidate(String cvLink, String phoneNumber, User user, Integer teamId, Integer projectId) throws ProjectNotFoundException {
        Candidate candidate = new Candidate();
        candidate.setAppliedTime(new Date());
        candidate.setCvLink(cvLink);
        candidate.setStatus(0);
        candidate.setResult(false);
        candidate.setPhoneNumber(phoneNumber);
        candidate.setProject(projectService.get(projectId));
        candidate.setProjectTeam(teamService.getTeamById(teamId));
        candidate.setUser(user);
        return candidate;
    }

    public boolean getCandidateByTeamId(Integer id, User user) {
        boolean check = false;
        Candidate candidate = candidateRepository.getCandidateByProjectTeamIdAndUser(id, user);
        if (candidate != null) {
            return true;
        }
        return check;
    }

    public List<Candidate> getAllMyRequests(User user) {
        return candidateRepository.findAllByUser(user);
    }

    public List<Candidate> getAllApprovedCandidates(Project project) {
        return candidateRepository.findAllByProjectAndResultIsTrue(project);
    }

    public long getAppliedCandidates(Team team) {
        return candidateRepository.countByProjectTeamAndResultIsTrue(team);
    }

    public long getProjectCountOfCandidates(Project project) {
        return candidateRepository.countByProjectAndResultIsTrue(project);
    }

    public Candidate getById(Integer id) {
        return candidateRepository.findById(id).get();
    }

    public boolean getApprovedCandidateInProject(Integer projectId, Integer userId) {
        boolean check = false;
        Candidate candidate = candidateRepository.getCandidateByProjectIdAndUserIdAndResultIsTrue(projectId, userId);
        if (candidate != null) {
            return true;
        }
        return check;
    }

    public boolean checkAppliedOnce(Integer projectId, Integer userId) {
        boolean check = false;
        Candidate candidate = candidateRepository.getCandidateByProjectIdAndUserId(projectId, userId);
        if (candidate != null) {
            return true;
        }
//        If user can apply another position, if rejected from another
//        if (candidate != null && candidate.getStatus() == 0) {
//            return true;
//        }
        return check;
    }

    public void update(Candidate candidate) {
        candidateRepository.save(candidate);
    }
}
