package com.example.stellarinvestment.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.stellarinvestment.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole() {
        Role roleAdmin = new Role("Admin", "accept/reject requested projects");
        Role savedRole = roleRepository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles() {
        Role roleInvestor = new Role("Investor", "can invest money for, "
                + "individual and team projects");

        Role roleUser = new Role("User", "can create individual projects or, "
                + "collect the team for feature investment");

        roleRepository.saveAll(List.of(roleInvestor, roleUser));
    }
}
