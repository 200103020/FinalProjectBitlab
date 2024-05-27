package com.example.stellarinvestment.model.project;

import com.example.stellarinvestment.model.IdBasedEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tariffs")
public class Tariff extends IdBasedEntity {
    @Column(name = "tariff_amount")
    private int tariffAmount;

    @Column(length = 256, nullable = false, name = "tariff_name")
    private String tariffName;

    @Column(length = 512, nullable = false, name = "tariff_info")
    private String tariffInfo;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectTariff", fetch = FetchType.LAZY)
    private List<Investment> investments;

    public int getTariffAmount() {
        return tariffAmount;
    }

    public void setTariffAmount(int tariffAmount) {
        this.tariffAmount = tariffAmount;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getTariffInfo() {
        return tariffInfo;
    }

    public void setTariffInfo(String tariffInfo) {
        this.tariffInfo = tariffInfo;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(List<Investment> investments) {
        this.investments = investments;
    }
}
