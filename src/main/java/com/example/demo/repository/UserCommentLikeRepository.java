package com.example.demo.repository;

import com.example.demo.entity.UserCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLike, Integer> {
    UserCommentLike findOneByUserIdAndCommentId(int user_id, int comment_id);

    List<UserCommentLike> findByCommentId(int commentId);
}
