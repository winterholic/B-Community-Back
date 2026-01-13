package com.bjt.gallery.controller;

import com.bjt.gallery.dto.request.CreateChildCommentRequest;
import com.bjt.gallery.dto.request.CreateCommentRequest;
import com.bjt.gallery.dto.request.CreatePostRequest;
import com.bjt.gallery.dto.response.CommentListResponse;
import com.bjt.gallery.dto.response.PostDetailResponse;
import com.bjt.gallery.dto.response.PostListResponse;
import com.bjt.gallery.service.CommentService;
import com.bjt.gallery.service.PostService;
import com.bjt.gallery.util.IpExtractUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final IpExtractUtil ipExtractUtil;

    @PostMapping("/posts")
    public ResponseEntity<Long> createPost(
            @Valid @RequestBody CreatePostRequest request,
            HttpServletRequest httpRequest
    ) {
        String clientIp = ipExtractUtil.getClientIp(httpRequest);
        Long postId = postService.createPost(request, clientIp);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostListResponse>> findPost(
            @PageableDefault(size = 15) Pageable pageable,
            @RequestParam(defaultValue = "latest") String sortType
    ) {
        List<PostListResponse> posts = postService.findPostList(pageable, sortType);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<PostListResponse>> searchPost(
            @RequestParam String keyword,
            @PageableDefault(size = 15) Pageable pageable
    ) {
        List<PostListResponse> posts = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/posts/{postId}/view")
    public ResponseEntity<Void> addPostViewCount(@PathVariable Long postId) {
        postService.increaseViewCount(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentListResponse> findCommentOfPost(@PathVariable Long postId) {
        CommentListResponse comments = commentService.findCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/posts/{postId}/upvote")
    public ResponseEntity<Void> addUpVotePost(@PathVariable Long postId) {
        postService.increaseUpvote(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/posts/{postId}/downvote")
    public ResponseEntity<Void> addDownVotePost(@PathVariable Long postId) {
        postService.increaseDownvote(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/posts/{postId}/report")
    public ResponseEntity<Void> addReportPost(@PathVariable Long postId) {
        postService.increaseReport(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments")
    public ResponseEntity<Long> createParentComment(
            @Valid @RequestBody CreateCommentRequest request,
            HttpServletRequest httpRequest
    ) {
        String clientIp = ipExtractUtil.getClientIp(httpRequest);
        Long commentId = commentService.createParentComment(request, clientIp);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @PostMapping("/comments/reply")
    public ResponseEntity<Long> createChildComment(
            @Valid @RequestBody CreateChildCommentRequest request,
            HttpServletRequest httpRequest
    ) {
        String clientIp = ipExtractUtil.getClientIp(httpRequest);
        Long commentId = commentService.createChildComment(request, clientIp);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @PatchMapping("/comments/{commentId}/upvote")
    public ResponseEntity<Void> addUpVoteComment(@PathVariable Long commentId) {
        commentService.increaseUpvote(commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/comments/{commentId}/downvote")
    public ResponseEntity<Void> addDownVoteComment(@PathVariable Long commentId) {
        commentService.increaseDownvote(commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/comments/{commentId}/report")
    public ResponseEntity<Void> addReportComment(@PathVariable Long commentId) {
        commentService.increaseReport(commentId);
        return ResponseEntity.ok().build();
    }
}
