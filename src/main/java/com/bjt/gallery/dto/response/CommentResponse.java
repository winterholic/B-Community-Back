package com.bjt.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long parentId; // 부모 댓글 ID (트리 구조 구성용)
    private String authorHash; // 7자 short hash
    private LocalDateTime createdAt; // 시간까지
    private String content;
    private Long upvoteCount;
    private Long downvoteCount;
    private Integer depth;

    @Builder.Default
    private List<CommentResponse> children = new ArrayList<>();

    // authorHash를 축약하기 위한 setter
    public void setAuthorHash(String authorHash) {
        this.authorHash = authorHash;
    }
}
