package com.bjt.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_created_at", columnList = "created_at DESC")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PostTag tag;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, length = 64)
    private String authorHash;

    @Column(nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long upvoteCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long downvoteCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long reportCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    // 비즈니스 로직
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseUpvoteCount() {
        this.upvoteCount++;
    }

    public void decreaseUpvoteCount() {
        if (this.upvoteCount > 0) {
            this.upvoteCount--;
        }
    }

    public void increaseDownvoteCount() {
        this.downvoteCount++;
    }

    public void decreaseDownvoteCount() {
        if (this.downvoteCount > 0) {
            this.downvoteCount--;
        }
    }

    public void increaseReportCount() {
        this.reportCount++;
        if (this.reportCount >= 10) {
            this.isDeleted = true;
        }
    }

    public void delete() {
        this.isDeleted = true;
    }
}
