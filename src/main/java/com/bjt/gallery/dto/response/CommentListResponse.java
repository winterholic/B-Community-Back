package com.bjt.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListResponse {

    @Builder.Default
    private List<CommentResponse> comments = new ArrayList<>(); // 전체 댓글 (트리 구조)

    @Builder.Default
    private List<CommentResponse> hotComments = new ArrayList<>(); // 인기 댓글 top 3
}
