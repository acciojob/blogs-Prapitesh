package com.driver.test;

import com.driver.controller.*;
import com.driver.models.*;
import com.driver.repositories.*;
import com.driver.services.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCases {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private BlogService blogService;

    @InjectMocks
    private ImageService imageService;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private BlogController blogController;

    @InjectMocks
    private ImageController imageController;

    /* -------- USER -------- */

    @Test
    public void createUserTest() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser("john", "123");

        assertEquals("john", result.getUsername());
    }

    /* -------- BLOG -------- */

    @Test
    public void createBlogTest() {
        User user = new User();
        user.setId(1);

        Blog blog = new Blog();
        blog.setTitle("blog");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog result = blogService.createAndReturnBlog(1, "blog", "content");

        assertEquals("blog", result.getTitle());
    }

    /* -------- IMAGE -------- */

    @Test
    public void countImagesInScreenTest() {
        Image i1 = new Image();
        i1.setWidth(100);
        i1.setHeight(100);

        Image i2 = new Image();
        i2.setWidth(800);
        i2.setHeight(800);

        Blog blog = new Blog();
        blog.setImages(Arrays.asList(i1, i2));

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));

        int count = imageService.countImagesInScreen(1, "500x500");

        assertEquals(1, count);
    }

    /* -------- CONTROLLERS -------- */

    @Test
    public void userControllerTest() {
        when(userService.createUser(anyString(), anyString()))
                .thenReturn(new User());

        assertEquals(
                201,
                userController.createUser("u", "p").getStatusCodeValue()
        );
    }

    @Test
    public void blogControllerTest() {
        when(blogService.createAndReturnBlog(anyInt(), anyString(), anyString()))
                .thenReturn(new Blog());

        assertEquals(
                201,
                blogController.createBlog(1, "t", "c").getStatusCodeValue()
        );
    }

    @Test
    public void imageControllerTest() {
        when(imageService.addImage(anyInt(), anyString(), anyString()))
                .thenReturn(new Image());

        String body =
                imageController.addImage(1, "img", "100x100").getBody();

        assertEquals("Added image successfully", body);
    }
}
