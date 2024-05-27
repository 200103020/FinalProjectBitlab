package com.example.stellarinvestment.model.project;

import com.example.stellarinvestment.model.IdBasedEntity;
import com.example.stellarinvestment.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "candidates")
public class Candidate extends IdBasedEntity {
    @Column(name = "cv_link")
    private String cvLink;

    @Column(name = "phone_number")
    private String phoneNumber;

    private boolean result;

    private int status; // 0-Waiting 1-Approved 2-Rejected

    @Column(name = "applied_time")
    private Date appliedTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_team_id")
    private Team projectTeam;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public String getCvLink() {
        return cvLink;
    }

    public void setCvLink(String cvLink) {
        this.cvLink = cvLink;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getProjectTeam() {
        return projectTeam;
    }

    public void setProjectTeam(Team projectTeam) {
        this.projectTeam = projectTeam;
    }

    public Date getAppliedTime() {
        return appliedTime;
    }

    public void setAppliedTime(Date appliedTime) {
        this.appliedTime = appliedTime;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
