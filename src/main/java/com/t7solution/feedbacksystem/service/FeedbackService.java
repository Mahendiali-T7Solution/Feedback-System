package com.t7solution.feedbacksystem.service;

import com.t7solution.feedbacksystem.dto.FeedbackDto;
import com.t7solution.feedbacksystem.model.Feedback;
import com.t7solution.feedbacksystem.model.User;
import com.t7solution.feedbacksystem.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback createFeedback(FeedbackDto feedbackDto, User user) {
        // Check duplicate
        feedbackRepository.findByTitleAndCreatedBy(feedbackDto.getTitle(), user).ifPresent(f -> {
            throw new IllegalArgumentException("Feedback with the same title already exists.");
        });

        Feedback feedback = new Feedback();
        feedback.setTitle(feedbackDto.getTitle());
        feedback.setDescription(feedbackDto.getDescription());
        feedback.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        feedback.setCreatedBy(user);
        return feedbackRepository.save(feedback);
    }

    public Feedback getFeedbackForUser(User user) {
        return feedbackRepository.findByCreatedByAndIsDeletedFalse(user);
    }

    public List<Feedback> getTeamFeedback(User manager) {
        return feedbackRepository.findTeamFeedback(manager);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findByIsDeletedFalse();
    }



    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<Feedback> getFeedbackByIdAndUser(Long id, User user) {
        return feedbackRepository.findByIdAndCreatedByAndIsDeletedFalse(id, user);
    }

    public Optional<Feedback> getFeedbackByIdAndManager(Long feedbackId, User manager) {
        // Check if feedback exists with the id AND createdBy user belongs to manager's team
        return feedbackRepository.findByIdAndCreatedBy_ManagerAndIsDeletedFalse(feedbackId, manager);
    }


//    softDelete by Id
    public boolean softDeleteFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findByIdAndIsDeletedFalse(id);

        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            feedback.setDeleted(true); // set isDeleted = true
            feedbackRepository.save(feedback);
            return true;
        }

        return false;
    }

//    update feedback
    public Feedback updateFeedback(Long id, Feedback updatedFeedback, User currentUser) {
        Feedback existingFeedback = feedbackRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found or deleted"));

        // Check if current user is the creator of the feedback
        if (!existingFeedback.getCreatedBy().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You can only update your own feedback.");
        }

        // Update only allowed fields
        existingFeedback.setTitle(updatedFeedback.getTitle());
        existingFeedback.setDescription(updatedFeedback.getDescription());
        // updatedAt will auto-update if using @UpdateTimestamp

        return feedbackRepository.save(existingFeedback);
    }
//    show deleted feedback
    public List<Feedback> getDeletedFeedback() {
        return feedbackRepository.findByIsDeletedTrue();
    }
}
