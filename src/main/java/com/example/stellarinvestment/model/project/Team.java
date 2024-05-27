package com.example.stellarinvestment.model.project;

import com.example.stellarinvestment.model.IdBasedEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team extends IdBasedEntity {
    @Column(length = 256, nullable = false)
    private String position;

    @Column(length = 256, nullable = false)
    private String experience;

    private int quantity;

    @Column(name = "count_of_approved")
    private Integer countOfApproved;

    @Column(length = 512, nullable = false, name = "additional_info")
    private String additionalInfo;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectTeam", fetch = FetchType.LAZY)
    private List<Candidate> candidates;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getCountOfApproved() {
        return countOfApproved;
    }

    public void setCountOfApproved(Integer countOfApproved) {
        this.countOfApproved = countOfApproved;
    }
}
