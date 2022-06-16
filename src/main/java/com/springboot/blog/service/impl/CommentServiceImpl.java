package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        //convert Dto to entity
        Comment comment = mapToEntity(commentDto);

        //find post by id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //save post to comment
        comment.setPost(post);

        //save comment
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        //retrieve Comments by post id
        List<Comment> allComments = commentRepository.findByPostId(postId);

        //convert coments list to commentDto list
        return allComments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByPostId(long postId, long commentId) {

        //retrieve post by id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        long cid = comment.getPost().getId();
        long pid = post.getId();
        if(cid!=pid)
        {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }



        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId,long commentId, CommentDto commentDto) {

        //retrieve the post
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve the comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        long cid = comment.getPost().getId();
        long pid = post.getId();
        if(cid!=pid)
        {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setComment(commentDto.getComment());

        Comment updatedComment=commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteCommentbyId(long postId, long commentId) {

        //retrieve the post
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve the comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        long cid = comment.getPost().getId();
        long pid = post.getId();
        if(cid!=pid)
        {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }

        commentRepository.delete(comment);
    }

    //convert entity to dto
    private CommentDto mapToDTO(Comment comment)
    {
        CommentDto commentDto=modelMapper.map(comment,CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setComment(comment.getComment());
        return commentDto;
    }

    //convert DTO to entity
    private Comment mapToEntity(CommentDto commentDto)
    {
        Comment comment = modelMapper.map(commentDto,Comment.class);
//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setComment(commentDto.getComment());
        return comment;
    }
}
