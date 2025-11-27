package com.klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Save a new feedback
    public Feedback saveFeedback(Feedback feedback) {
        feedback.setTimestamp(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    // Get all feedback
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Feedback getById(Long id) {
        Optional<Feedback> f = feedbackRepository.findById(id);
        return f.orElse(null);
    }

    public Feedback updateFeedback(Long id, Feedback updated) {
        Optional<Feedback> opt = feedbackRepository.findById(id);
        if (!opt.isPresent()) return null;
        Feedback f = opt.get();
        // update fields selectively
        if (updated.getRating() != 0) f.setRating(updated.getRating());
        if (updated.getComment() != null) f.setComment(updated.getComment());
        if (updated.getService() != null) f.setService(updated.getService());
        f.setTimestamp(LocalDateTime.now());
        return feedbackRepository.save(f);
    }

    public boolean deleteFeedback(Long id) {
        Optional<Feedback> opt = feedbackRepository.findById(id);
        if (!opt.isPresent()) return false;
        feedbackRepository.deleteById(id);
        return true;
    }
}
