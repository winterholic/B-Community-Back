package com.bjt.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_post_id", columnList = "post_id"),
        @Index(name = "idx_parent_id", columnList = "parent_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 64)
    private String authorHash;

    @Column(nullable = false)
    @Builder.Default
    private Integer depth = 0;

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

    public void addChild(Comment child) {
        this.children.add(child);
    }
}
