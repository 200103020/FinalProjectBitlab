package com.example.stellarinvestment.repository;

import com.example.stellarinvestment.model.Role;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Project;
import com.example.stellarinvestment.model.project.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByUser(User user);

    List<Project> findAllByEnabledIsTrue();

    List<Project> findAllByUserAndStatus(User user, ProjectStatus status);
}
