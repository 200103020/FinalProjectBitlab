package com.example.stellarinvestment.repository;


import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.model.project.Investment;
import com.example.stellarinvestment.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InvestmentRepository extends JpaRepository<Investment, Integer> {

    List<Investment> findAllByUser(User user);

    List<Investment> findAllByUserAndPaidIsTrue(User user);

    List<Investment> findAllByUserAndPaidIsFalse(User user);

    List<Investment> findAllByProjectAndPaidIsTrue(Project project);
}
