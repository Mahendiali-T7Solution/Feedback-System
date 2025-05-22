package com.t7solution.feedbacksystem.controller;

import com.t7solution.feedbacksystem.dto.FeedbackDto;
import com.t7solution.feedbacksystem.model.Feedback;
import com.t7solution.feedbacksystem.model.User;
import com.t7solution.feedbacksystem.repository.UserRepository;
import com.t7solution.feedbacksystem.repository.UserRepository;
import com.t7solution.feedbacksystem.service.FeedbackService;
import com.t7solution.feedbacksystem.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/feedback")
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackDto feedbackDto, Principal principal) {
        String email = principal.getName(); // or extract from SecurityContext
        User currentUser = userRepository.findByEmail(email); // method to fetch from DB
        Feedback feedback = feedbackService.createFeedback(feedbackDto, currentUser);
        return ResponseEntity.ok(feedback);
    }


    @GetMapping("/feedback")
    public ResponseEntity<?> getFeedback(Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());

        switch (currentUser.getRole().getName()) {
            case "Admin" -> {
                return ResponseEntity.ok(feedbackService.getAllFeedback());
            }
            case "Manager" -> {
                return ResponseEntity.ok(feedbackService.getTeamFeedback(currentUser));
            }
            default -> {
                return ResponseEntity.ok(feedbackService.getFeedbackForUser(currentUser));
            }
        }
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());

        Feedback feedback;

        switch (currentUser.getRole().getName()) {
            case "Admin":
                // Admin can access any feedback by id
                feedback = feedbackService.getFeedbackById(id)
                        .orElseThrow(() -> new RuntimeException("Feedback not found"));
                break;

            case "Manager":
                // Manager can access feedback only if it belongs to their team
                feedback = feedbackService.getFeedbackByIdAndManager(id, currentUser)
                        .orElseThrow(() -> new RuntimeException("Feedback not found or access denied"));
                break;

            default:
                // Employee can access feedback only if they created it
                feedback = feedbackService.getFeedbackByIdAndUser(id, currentUser)
                        .orElseThrow(() -> new RuntimeException("Feedback not found or access denied"));
                break;
        }

        return ResponseEntity.ok(feedback);
    }

    //soft deleteby Id
    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<?> softDeleteFeedback(@PathVariable Long id, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());

        if (!"Admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can delete feedback.");
        }

        boolean deleted = feedbackService.softDeleteFeedbackById(id);

        if (deleted) {
            return ResponseEntity.ok("Feedback deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Feedback not found or already deleted.");
        }
    }

    //feedback update
    @PutMapping("/feedback/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable Long id,
                                            @RequestBody Feedback updatedFeedback,
                                            Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());

        try {
            Feedback feedback = feedbackService.updateFeedback(id, updatedFeedback, currentUser);
            return ResponseEntity.ok(feedback);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //view deleted feedback
    @GetMapping("/feedback/deleted")
    public ResponseEntity<?> getDeletedFeedback(Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());

        if (!"Admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized for this url");
        }

        return ResponseEntity.ok(feedbackService.getDeletedFeedback());
    }
}
