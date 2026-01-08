package com.driver.test;

import com.driver.controller.BlogController;
import com.driver.controller.ImageController;
import com.driver.controller.UserController;
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

    /* ===================== USER ===================== */

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

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1);
        userService.deleteUser(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    /* ===================== BLOG ===================== */

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    public void testCreateBlog() {
        User user = new User();
        user.setId(1);

        Blog blog = new Blog();
        blog.setTitle("My Blog");
        blog.setContent("Content");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog savedBlog = blogService.createAndReturnBlog(1, "My Blog", "Content");

        assertEquals("My Blog", savedBlog.getTitle());
    }

    @Test
    public void testDeleteBlog() {
        doNothing().when(blogRepository).deleteById(1);
        blogService.deleteBlog(1);
        verify(blogRepository, times(1)).deleteById(1);
    }

    /* ===================== IMAGE ===================== */

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    public void testAddImage() {
        Blog blog = new Blog();
        blog.setId(1);

        Image image = new Image();
        image.setImageUrl("img1");
        image.setHeight(100);
        image.setWidth(100);

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        Image savedImage = imageService.addImage(1, "img1", "100x100");

        assertNotNull(savedImage);
        assertEquals("img1", savedImage.getImageUrl());
    }

    @Test
    public void testDeleteImage() {
        doNothing().when(imageRepository).deleteById(1);
        imageService.deleteImage(1);
        verify(imageRepository, times(1)).deleteById(1);
    }

    @Test
    public void testCountImagesInScreen() {
        Image image1 = new Image();
        image1.setWidth(200);
        image1.setHeight(200);
        Image image2 = new Image();
        image2.setWidth(800);
        image2.setHeight(800);
        List<Image> images = Arrays.asList(image1, image2);

        Blog blog = new Blog();
        blog.setImages(images);

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));

        int count = imageService.countImagesInScreen(1, "500x500");

        assertEquals(1, count);
    }

    /* ===================== CONTROLLERS ===================== */

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private BlogController blogController;

    @InjectMocks
    private ImageController imageController;

    @Test
    public void testUserControllerCreate() {
        User user = new User();
        user.setUsername("alex");

        when(userService.createUser(anyString(), anyString())).thenReturn(user);

        String  response = userController.createUser("alex", "123").getStatusCode().toString();

        assertEquals("201", response);
    }

    @Test
    public void testBlogControllerCreate() {
        Blog blog = new Blog();
        blog.setTitle("Demo");

        when(blogService.createAndReturnBlog(anyInt(), anyString(), anyString()))
                .thenReturn(blog);

        String response = blogController.createBlog(1, "Demo", "Content").getStatusCode().toString();

        assertEquals("201", response);
    }

    @Test
    public void testImageControllerAdd() {
        Image image = new Image();
        image.setImageUrl("img");

        when(imageService.addImage(anyInt(), anyString(), anyString()))
                .thenReturn(image);

       String response = imageController.addImage(1, "img", "100x100").getBody();

        assertEquals("img", response);
    }
}
