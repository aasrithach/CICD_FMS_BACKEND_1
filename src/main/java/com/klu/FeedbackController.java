package com.klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback_management")
@CrossOrigin
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Create
    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody Feedback feedback) {
        try {
            Feedback saved = feedbackService.saveFeedback(feedback);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }

    // Read all
    @GetMapping("/customer-feedback")
    public ResponseEntity<List<Feedback>> getFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    // Read one by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        Feedback f = feedbackService.getById(id);
        if (f == null) return ResponseEntity.status(404).body("Feedback not found");
        return ResponseEntity.ok(f);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable Long id, @RequestBody Feedback updated) {
        try {
            Feedback f = feedbackService.updateFeedback(id, updated);
            if (f == null) return ResponseEntity.status(404).body("Feedback not found");
            return ResponseEntity.ok(f);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            boolean deleted = feedbackService.deleteFeedback(id);
            if (!deleted) return ResponseEntity.status(404).body("Feedback not found");
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }
}
