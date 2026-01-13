package com.bjt.gallery.repository;

import com.bjt.gallery.dto.response.PostListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface PostMapper {

    List<PostListResponse> findPostList(@Param("pageable") Pageable pageable,
                                        @Param("sortType") String sortType);

    List<PostListResponse> searchPosts(@Param("keyword") String keyword,
                                       @Param("pageable") Pageable pageable);

    Long countCommentsByPostId(@Param("postId") Long postId);
}
