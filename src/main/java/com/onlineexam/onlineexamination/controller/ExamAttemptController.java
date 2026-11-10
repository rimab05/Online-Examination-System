
package com.onlineexam.onlineexamination.controller;

import com.onlineexam.onlineexamination.entity.ExamAttempt;
import com.onlineexam.onlineexamination.service.ExamAttemptService;
import org.springframework.web.bind.annotation.*;
import com.onlineexam.onlineexamination.dto.StudentAnswerRequest;
import com.onlineexam.onlineexamination.dto.ExamResultResponse;
import com.onlineexam.onlineexamination.dto.StudentAnswerResponse;
import java.util.List;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/exam-attempts")
public class ExamAttemptController {

    private final ExamAttemptService examAttemptService;

    public ExamAttemptController(ExamAttemptService examAttemptService) {
        this.examAttemptService = examAttemptService;
    }

    @PostMapping
    public ExamAttempt saveExamAttempt(
            @RequestBody ExamAttempt examAttempt,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Only admin can create exam attempts directly");
        }

        return examAttemptService.saveExamAttempt(examAttempt);
    }

    @GetMapping
    public List<ExamAttempt> getAllAttempts(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Only admin can view all exam attempts");
        }

        return examAttemptService.getAllAttempts();
    }
    @GetMapping("/{attemptId}/answers")
    public List<StudentAnswerResponse> getAnswersByAttemptId(
            @PathVariable Long attemptId,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        examAttemptService.checkAttemptOwnership(
                attemptId, authentication.getName(), isAdmin);

        return examAttemptService.getAnswersByAttemptId(attemptId);
    }


    @GetMapping("/{attemptId}/result")
    public ExamResultResponse getExamResult(
            @PathVariable Long attemptId,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return examAttemptService.getExamResult(
                attemptId,
                authentication.getName(),
                isAdmin
        );
    }


    @GetMapping("/{username}")
    public List<ExamAttempt> getAttemptsByUsername(
            @PathVariable String username,
            Authentication authentication) {

        if (!authentication.getName().equals(username)
                && !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You are not allowed to access these attempts");
        }

        return examAttemptService.getAttemptsByUsername(username);
    }
    @PostMapping("/start/{username}/{examId}")
    public ExamAttempt startExam(
            @PathVariable String username,
            @PathVariable Long examId,
            Authentication authentication) {

        if (!authentication.getName().equals(username)
                && !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You are not allowed to start an exam for this student");
        }

        return examAttemptService.startExam(username, examId);
    }
    @PostMapping("/{attemptId}/answers")
    public StudentAnswerResponse saveAnswer(
            @PathVariable Long attemptId,
            @RequestBody StudentAnswerRequest request,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        examAttemptService.checkAttemptOwnership(
                attemptId, authentication.getName(), isAdmin);

        return examAttemptService.saveAnswer(attemptId, request);
    }

    @PostMapping("/complete/{id}")
    public ExamAttempt completeExam(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        examAttemptService.checkAttemptOwnership(
                id, authentication.getName(), isAdmin);

        return examAttemptService.completeExam(id);
    }
}
