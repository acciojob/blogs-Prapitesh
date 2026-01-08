package com.driver.test;

import com.driver.services.UserService;
import com.driver.repositories.UserRepository;
import com.driver.models.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCases {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("1234");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.createUser("john", "1234");

        assertNotNull(savedUser);
        assertEquals("john", savedUser.getUsername());
    }
}
