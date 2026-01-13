package com.bjt.gallery.repository;

import com.bjt.gallery.dto.response.CommentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentResponse> findCommentsByPostId(@Param("postId") Long postId);

    List<CommentResponse> findHotCommentsByPostId(@Param("postId") Long postId);
}
