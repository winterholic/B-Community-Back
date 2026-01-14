package com.bjt.gallery.dto.response;

import com.bjt.gallery.entity.PostTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {

    private Long id;
    private String title;
    private PostTag tag;
    private String authorHash; // 7자 short hash
    private Long voteScore; // upvoteCount - downvoteCount
    private Long commentCount;
    private LocalDateTime createdAt;
    private String contentPreview; // HTML 태그 제거된 텍스트
}
