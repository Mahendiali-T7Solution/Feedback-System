package com.t7solution.feedbacksystem.repository;

import com.t7solution.feedbacksystem.model.Feedback;
import com.t7solution.feedbacksystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByTitleAndCreatedByEmailAndIsDeletedFalse(String title, String email);
    Optional<Feedback> findByTitleAndCreatedBy(String title, User user);
    Feedback findByCreatedByAndIsDeletedFalse(User manager);
    List<Feedback> findByIsDeletedFalse();

    // For MANAGER (custom query to fetch team feedback)
    @Query("SELECT f FROM Feedback f WHERE f.isDeleted = false AND f.createdBy.manager = :manager")
    List<Feedback> findTeamFeedback(@Param("manager") User manager);


    Optional<Feedback> findByIdAndIsDeletedFalse(Long id);

    Optional<Feedback> findByIdAndCreatedByAndIsDeletedFalse(Long id, User createdBy);

    Optional<Feedback> findByIdAndCreatedBy_ManagerAndIsDeletedFalse(Long id, User manager);
    List<Feedback> findByIsDeletedTrue();
}