package com.bjt.gallery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChildCommentRequest {

    @NotNull(message = "게시글 ID는 필수입니다")
    private Long postId;

    @NotNull(message = "부모 댓글 ID는 필수입니다")
    private Long parentId;

    @NotBlank(message = "댓글 내용은 필수입니다")
    private String content;
}
