package com.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostDto {

    private long id;

    @NotEmpty
    @Size(min=3, message="Post title should conatain atleast 3 characters")
    private  String title;

    @NotEmpty
    @Size(min=10,message="Post Description should contain atleast 10 characters")
    private String description;

    @NotEmpty
    @Size(message = "Content should not be blank")
    private String Content;

    private Set<CommentDto> comments;
}
