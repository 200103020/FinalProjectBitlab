package com.example.stellarinvestment.repository;

import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.model.project.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findAllByProject(Project project);
}
