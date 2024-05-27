package com.example.stellarinvestment.repository;
;
import com.example.stellarinvestment.model.Role;
import com.example.stellarinvestment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User user = new User("nurassyl07@gmail.com", "Aa1234", "Nurassyl", "Kozhan");
        user.addRole(roleAdmin);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User user = new User("omar_polat@gmail.com", "omar2002", "Omar", "Polat");
        user.setEnabled(true);
        Role roleInvestor = new Role(2);
        Role roleUser = new Role(3);

        user.addRole(roleInvestor);
        user.addRole(roleUser);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(System.out::println);
    }

    @Test
    public void testGetUserById() {
        Optional<User> user = userRepository.findById(1);
        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User user = userRepository.findById(1).orElseThrow(() -> new RuntimeException("User with ID 1 not found"));
        user.setEnabled(true);
        user.setEmail("nurassyl_07@gmail.com");

        userRepository.save(user);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = userRepository.findById(2).orElseThrow(() -> new RuntimeException("User with ID 2 not found"));
        Role roleAdmin = new Role(1);
        Role roleUser = new Role(3);

        user.getRoles().remove(roleUser);
        user.addRole(roleAdmin);

        userRepository.save(user);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }
}
