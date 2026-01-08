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

    /* ================= USER ================= */

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

        User saved = userService.createUser("john", "1234");

        assertNotNull(saved);
        assertEquals("john", saved.getUsername());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1);
        userService.deleteUser(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    /* ================= BLOG ================= */

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    public void testCreateBlog() {
        User user = new User();
        user.setId(1);

        Blog blog = new Blog();
        blog.setTitle("Test Blog");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog saved = blogService.createAndReturnBlog(1, "Test Blog", "Content");

        assertEquals("Test Blog", saved.getTitle());
    }

    @Test
    public void testDeleteBlog() {
        when(blogRepository.existsById(1)).thenReturn(true);
        doNothing().when(blogRepository).deleteById(1);

        blogService.deleteBlog(1);

        verify(blogRepository).deleteById(1);
    }

    /* ================= IMAGE ================= */

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    public void testAddImage() {
        Blog blog = new Blog();
        blog.setId(1);

        Image image = new Image();
        image.setImageUrl("img");
        image.setWidth(100);
        image.setHeight(100);

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        Image saved = imageService.addImage(1, "img", "100x100");

        assertEquals("img", saved.getImageUrl());
    }

    @Test
    public void testDeleteImage() {
        doNothing().when(imageRepository).deleteById(1);
        imageService.deleteImage(1);
        verify(imageRepository).deleteById(1);
    }

    @Test
    public void testCountImagesInScreen() {
        Image i1 = new Image();
        i1.setWidth(200);
        i1.setHeight(200);

        Image i2 = new Image();
        i2.setWidth(800);
        i2.setHeight(800);

        Blog blog = new Blog();
        blog.setImages(Arrays.asList(i1, i2));

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));

        int count = imageService.countImagesInScreen(1, "500x500");

        assertEquals(1, count);
    }

    /* ================= CONTROLLERS ================= */

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private BlogController blogController;

    @InjectMocks
    private ImageController imageController;

    @Test
    public void testUserControllerCreate() {
        when(userService.createUser(anyString(), anyString()))
                .thenReturn(new User());

        assertEquals(201,
                userController.createUser("a", "b").getStatusCodeValue());
    }

    @Test
    public void testBlogControllerCreate() {
        when(blogService.createAndReturnBlog(anyInt(), anyString(), anyString()))
                .thenReturn(new Blog());

        assertEquals(201,
                blogController.createBlog(1, "t", "c").getStatusCodeValue());
    }

    @Test
    public void testImageControllerAdd() {
        when(imageService.addImage(anyInt(), anyString(), anyString()))
                .thenReturn(new Image());

        String body =
                imageController.addImage(1, "img", "100x100").getBody();

        assertEquals("Added image successfully", body);
    }
}
