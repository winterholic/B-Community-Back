package com.bjt.gallery.service;

import com.bjt.gallery.dto.request.CreateChildCommentRequest;
import com.bjt.gallery.dto.request.CreateCommentRequest;
import com.bjt.gallery.dto.response.CommentListResponse;
import com.bjt.gallery.dto.response.CommentResponse;
import com.bjt.gallery.entity.Comment;
import com.bjt.gallery.entity.Post;
import com.bjt.gallery.repository.CommentMapper;
import com.bjt.gallery.repository.CommentRepository;
import com.bjt.gallery.repository.PostRepository;
import com.bjt.gallery.util.IpHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final IpHashUtil ipHashUtil;

    @Transactional
    public Long createParentComment(CreateCommentRequest request, String clientIp) {
        Post post = postRepository.findByIdAndIsDeletedFalse(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String authorHash = ipHashUtil.hashIp(clientIp);

        Comment comment = Comment.builder()
                .post(post)
                .parent(null)
                .content(request.getContent())
                .authorHash(authorHash)
                .depth(0)
                .build();

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long createChildComment(CreateChildCommentRequest request, String clientIp) {
        Post post = postRepository.findByIdAndIsDeletedFalse(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment parentComment = commentRepository.findByIdAndIsDeletedFalse(request.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

        String authorHash = ipHashUtil.hashIp(clientIp);

        Comment childComment = Comment.builder()
                .post(post)
                .parent(parentComment)
                .content(request.getContent())
                .authorHash(authorHash)
                .depth(parentComment.getDepth() + 1)
                .build();

        Comment savedComment = commentRepository.save(childComment);
        parentComment.addChild(savedComment);

        return savedComment.getId();
    }

    public CommentListResponse findCommentsByPostId(Long postId) {
        // MyBatis로 평면 구조로 조회
        List<CommentResponse> flatComments = commentMapper.findCommentsByPostId(postId);
        List<CommentResponse> hotComments = commentMapper.findHotCommentsByPostId(postId);

        // 평면 구조를 트리 구조로 변환
        List<CommentResponse> treeComments = buildCommentTree(flatComments);

        // authorHash를 7자로 축약
        shortenAuthorHash(treeComments);
        shortenAuthorHash(hotComments);

        return CommentListResponse.builder()
                .comments(treeComments)
                .hotComments(hotComments)
                .build();
    }

    @Transactional
    public void increaseUpvote(Long commentId) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.increaseUpvoteCount();
    }

    @Transactional
    public void increaseDownvote(Long commentId) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.increaseDownvoteCount();
    }

    @Transactional
    public void increaseReport(Long commentId) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.increaseReportCount();
    }

    private List<CommentResponse> buildCommentTree(List<CommentResponse> flatComments) {
        Map<Long, CommentResponse> commentMap = new HashMap<>();

        // 1. 모든 댓글을 Map에 저장
        for (CommentResponse comment : flatComments) {
            commentMap.put(comment.getId(), comment);
        }

        // 2. 부모-자식 관계 설정
        for (CommentResponse comment : flatComments) {
            if (comment.getDepth() > 0 && comment.getParentId() != null) {
                // 자식 댓글인 경우, 부모 찾아서 children에 추가
                CommentResponse parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment);
                }
            }
        }

        // 3. depth가 0인 루트 댓글만 반환
        return flatComments.stream()
                .filter(comment -> comment.getDepth() == 0)
                .toList();
    }


    private void shortenAuthorHash(List<CommentResponse> comments) {
        for (CommentResponse comment : comments) {
            String shortHash = ipHashUtil.getShortHash(comment.getAuthorHash());
            comment.setAuthorHash(shortHash);
            shortenAuthorHash(comment.getChildren());
        }
    }
}
