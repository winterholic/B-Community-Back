package com.bjt.gallery.dto.request;

import com.bjt.gallery.entity.PostTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자 이하여야 합니다")
    private String title;

    @NotNull(message = "태그는 필수입니다")
    private PostTag tag;

    @NotBlank(message = "내용은 필수입니다")
    private String content;
}
