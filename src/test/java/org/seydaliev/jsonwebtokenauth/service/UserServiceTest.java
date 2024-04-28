package org.seydaliev.jsonwebtokenauth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seydaliev.jsonwebtokenauth.model.Role;
import org.seydaliev.jsonwebtokenauth.model.User;
import org.seydaliev.jsonwebtokenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        user.setRoles(Collections.singleton(Role.USER));

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));


        StepVerifier.create(userService.createUser(user))
                .expectNextMatches(createdUser -> createdUser.getUsername().equals("testUser"))
                .verifyComplete();
    }

    @Test
    public void testFindByUsername() {

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        user.setRoles(Collections.singleton(Role.USER));

        when(userRepository.findByUsername("testUser")).thenReturn(Mono.just(user));

        StepVerifier.create(userService.findByUsername("testUser"))
                .expectNextMatches(foundUser -> foundUser.getUsername().equals("testUser"))
                .verifyComplete();
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        user.setRoles(Collections.singleton(Role.USER));

        when(userRepository.findById("testId")).thenReturn(Mono.just(user));

        StepVerifier.create(userService.findById("testId"))
                .expectNextMatches(foundUser -> foundUser.getUsername().equals("testUser"))
                .verifyComplete();
    }
}
