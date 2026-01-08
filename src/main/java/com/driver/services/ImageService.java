package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    BlogRepository blogRepository2;
    @Autowired
    ImageRepository imageRepository2;

    public Image addImage(Integer blogId, String description, String dimensions){
        //add an image to the blog
       Blog blog=blogRepository2.findById(blogId).orElseThrow();
       String ar[]=dimensions.split("x");
       int w=Integer.parseInt(ar[0]);
       int h=Integer.parseInt(ar[1]);
       Image image=new Image();
       image.setImageUrl(description);
       image.setHeight(h);
       image.setWidth(w);
       image.setBlog(blog);
       return imageRepository2.save(image);
    }

    public void deleteImage(Integer id){
imageRepository2.deleteById(id);
    }

    public int countImagesInScreen(Integer id, String screenDimensions) {
        //Find the number of images of given dimensions that can fit in a screen having `screenDimensions`
        Optional<Blog> optionalBlog=blogRepository2.findById(id);
        if(optionalBlog.isEmpty()){
            return 0;
        }
        Blog blog=optionalBlog.get();
        List<Image> images=blog.getImages();
        String[] screenDim = screenDimensions.split("x");
        int screenWidth = Integer.parseInt(screenDim[0]);
        int screenHeight = Integer.parseInt(screenDim[1]);
        int count=0;
        for(Image image:images){
            if(image.getHeight()<=screenHeight&&image.getWidth()<=screenWidth){
                count++;
            }
        }
        return count;
    }
}
