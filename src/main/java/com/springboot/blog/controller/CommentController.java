package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //create Comment
    @PostMapping("posts/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") long postId, @RequestBody CommentDto commentDto)
    {
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    //get comments by post
    @GetMapping("/posts/{postId}/comment")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable(value = "postId") long postId)
    {
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId),HttpStatus.OK);
    }

    //get comment by Post


    @GetMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDto> getCommentByPostId(@PathVariable(value = "postId") long postId,
                                                         @PathVariable(value = "commentId") long commentId)
    {
        CommentDto commentDto = commentService.getCommentByPostId(postId,commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    //Update Comment by post id
    @PutMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "commentId") long commentId,
                                                    @RequestBody CommentDto commentDto)
    {
        return new ResponseEntity<>(commentService.updateComment(postId,commentId,commentDto), HttpStatus.OK);
    }

    //delete Comment
    @DeleteMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
                                          @PathVariable(value = "commentId") long commentId)
    {
        commentService.deleteCommentbyId(postId,commentId);
        return new ResponseEntity<>("Comment Deleted Successfully",HttpStatus.OK);
    }
}
