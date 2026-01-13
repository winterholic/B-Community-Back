package com.bjt.gallery.service;

import com.bjt.gallery.dto.request.CreatePostRequest;
import com.bjt.gallery.dto.response.PostDetailResponse;
import com.bjt.gallery.dto.response.PostListResponse;
import com.bjt.gallery.entity.Post;
import com.bjt.gallery.repository.PostMapper;
import com.bjt.gallery.repository.PostRepository;
import com.bjt.gallery.util.IpHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final IpHashUtil ipHashUtil;

    @Transactional
    public Long createPost(CreatePostRequest request, String clientIp) {
        String authorHash = ipHashUtil.hashIp(clientIp);

        Post post = Post.builder()
                .title(request.getTitle())
                .tag(request.getTag())
                .content(request.getContent())
                .authorHash(authorHash)
                .build();

        return postRepository.save(post).getId();
    }

    public List<PostListResponse> findPostList(Pageable pageable, String sortType) {
        return postMapper.findPostList(pageable, sortType);
    }

    public List<PostListResponse> searchPosts(String keyword, Pageable pageable) {
        return postMapper.searchPosts(keyword, pageable);
    }

    public PostDetailResponse getPost(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String shortHash = ipHashUtil.getShortHash(post.getAuthorHash());

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .tag(post.getTag())
                .authorHash(shortHash)
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .content(post.getContent())
                .upvoteCount(post.getUpvoteCount())
                .downvoteCount(post.getDownvoteCount())
                .build();
    }

    @Transactional
    public void increaseViewCount(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.increaseViewCount();
    }

    @Transactional
    public void increaseUpvote(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.increaseUpvoteCount();
    }

    @Transactional
    public void increaseDownvote(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.increaseDownvoteCount();
    }

    @Transactional
    public void increaseReport(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.increaseReportCount();
    }
}
