package com.example.stellarinvestment.model.project;

import com.example.stellarinvestment.model.IdBasedEntity;
import com.example.stellarinvestment.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "investments")
public class Investment extends IdBasedEntity {
    private boolean paid;

    @Column(name = "invested_time")
    private Date investedTime;

    private String banked;

    private String identifier;

    @Column(name = "invested_money")
    private int investedMoney;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff projectTariff;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getInvestedTime() {
        return investedTime;
    }

    public void setInvestedTime(Date investedTime) {
        this.investedTime = investedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tariff getProjectTariff() {
        return projectTariff;
    }

    public void setProjectTariff(Tariff projectTariff) {
        this.projectTariff = projectTariff;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getInvestedMoney() {
        return investedMoney;
    }

    public void setInvestedMoney(int investedMoney) {
        this.investedMoney = investedMoney;
    }

    public String getBanked() {
        return banked;
    }

    public void setBanked(String banked) {
        this.banked = banked;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
