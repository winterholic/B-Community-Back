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
public class PostDetailResponse {

    private Long id;
    private String title;
    private PostTag tag;
    private String authorHash; // 7자 short hash
    private LocalDateTime createdAt; // 시간까지
    private Long viewCount;
    private String content; // HTML 그대로
    private Long upvoteCount;
    private Long downvoteCount;
}
