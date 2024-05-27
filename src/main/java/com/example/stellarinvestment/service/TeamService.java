package com.example.stellarinvestment.service;

import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.model.project.Team;
import com.example.stellarinvestment.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team getTeamById(Integer id) {
        return teamRepository.findById(id).get();
    }

    public void updateTeam(Team team) {
        teamRepository.save(team);
    }

    public long getCountOfCandidateNeeded(Project project) {
        long count = 0;
        List<Team> teamList = teamRepository.findAllByProject(project);

        for (Team team:
             teamList) {
            count += team.getQuantity();
        }

        return count;
    }
}
