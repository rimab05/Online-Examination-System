package com.onlineexam.onlineexamination.service;

import com.onlineexam.onlineexamination.entity.ExamAttempt;
import com.onlineexam.onlineexamination.repository.ExamAttemptRepository;
import com.onlineexam.onlineexamination.entity.Question;
import com.onlineexam.onlineexamination.repository.QuestionRepository;
import com.onlineexam.onlineexamination.dto.StudentAnswerRequest;
import org.springframework.stereotype.Service;
import com.onlineexam.onlineexamination.repository.StudentRepository;
import com.onlineexam.onlineexamination.entity.ExamAnswer;
import com.onlineexam.onlineexamination.repository.ExamAnswerRepository;
import com.onlineexam.onlineexamination.dto.ExamResultResponse;
import com.onlineexam.onlineexamination.repository.ExamRepository;
import com.onlineexam.onlineexamination.entity.Exam;
import com.onlineexam.onlineexamination.dto.StudentAnswerResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamAttemptService {

    private final ExamAttemptRepository examAttemptRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final ExamAnswerRepository examAnswerRepository;
    private final ExamRepository examRepository;

    public ExamAttemptService(
            ExamAttemptRepository examAttemptRepository,
            QuestionRepository questionRepository,
            StudentRepository studentRepository,
            ExamAnswerRepository examAnswerRepository,
            ExamRepository examRepository) {

        this.examAttemptRepository = examAttemptRepository;
        this.questionRepository = questionRepository;
        this.studentRepository = studentRepository;
        this.examAnswerRepository = examAnswerRepository;
        this.examRepository = examRepository;
    }

    public ExamAttempt startExam(String username, Long examId) {

        if (!studentRepository.existsByUsername(username)) {
            throw new RuntimeException("Student not found");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() ->
                        new RuntimeException("Exam not found"));

        if (!exam.isActive()) {
            throw new RuntimeException("This exam is not active");
        }

        ExamAttempt attempt = new ExamAttempt();

        attempt.setUsername(username);
        attempt.setExam(exam);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus("IN_PROGRESS");
        attempt.setScore(0);
        attempt.setDurationMinutes(exam.getDuration());

        return examAttemptRepository.save(attempt);
    }

    public ExamAttempt saveExamAttempt(ExamAttempt examAttempt) {
        return examAttemptRepository.save(examAttempt);
    }

    public List<ExamAttempt> getAttemptsByUsername(String username) {
        return examAttemptRepository
                .findByUsernameOrderByStartTimeDesc(username);
    }

    public List<ExamAttempt> getAllAttempts() {
        return examAttemptRepository.findAll();
    }
    public List<StudentAnswerResponse> getAnswersByAttemptId(Long attemptId) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new RuntimeException("Exam attempt not found"));

        List<ExamAnswer> answers =
                examAnswerRepository.findByExamAttempt_Id(attemptId);

        return answers.stream()
                .map(answer -> new StudentAnswerResponse(
                        answer.getId(),
                        attempt.getId(),
                        answer.getQuestion().getId(),
                        answer.getSelectedOption()
                ))
                .toList();
    }
    public StudentAnswerResponse saveAnswer(
            Long attemptId,
            StudentAnswerRequest request) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new RuntimeException("Exam attempt not found"));

        if (!"IN_PROGRESS".equals(attempt.getStatus())) {
            throw new RuntimeException("Exam is not in progress");
        }

        if (attempt.getStartTime() != null
                && attempt.getDurationMinutes() != null) {

            LocalDateTime deadline = attempt.getStartTime()
                    .plusMinutes(attempt.getDurationMinutes());

            if (LocalDateTime.now().isAfter(deadline)) {
                throw new RuntimeException("Exam time has expired");
            }
        }

        if (request.getQuestionId() == null) {
            throw new RuntimeException("Question ID is required");
        }

        if (request.getSelectedOption() == null
                || !request.getSelectedOption().matches("(?i)[ABCD]")) {
            throw new RuntimeException(
                    "Selected option must be A, B, C, or D");
        }

        Question question = questionRepository
                .findById(request.getQuestionId())
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));

        List<ExamAnswer> existingAnswers =
                examAnswerRepository.findByExamAttempt_Id(attemptId);

        ExamAnswer savedAnswer = null;

        for (ExamAnswer existingAnswer : existingAnswers) {
            if (existingAnswer.getQuestion().getId()
                    .equals(question.getId())) {

                existingAnswer.setSelectedOption(
                        request.getSelectedOption().toUpperCase());

                existingAnswer.setIsCorrect(
                        question.getCorrectOption() != null
                                && question.getCorrectOption()
                                .equalsIgnoreCase(request.getSelectedOption()));

                savedAnswer = examAnswerRepository.save(existingAnswer);
                break;
            }
        }

        if (savedAnswer == null) {

            ExamAnswer examAnswer = new ExamAnswer();

            examAnswer.setExamAttempt(attempt);
            examAnswer.setQuestion(question);
            examAnswer.setSelectedOption(
                    request.getSelectedOption().toUpperCase());

            boolean correct = question.getCorrectOption() != null
                    && question.getCorrectOption()
                    .equalsIgnoreCase(request.getSelectedOption());

            examAnswer.setIsCorrect(correct);

            savedAnswer = examAnswerRepository.save(examAnswer);
        }

        return new StudentAnswerResponse(
                savedAnswer.getId(),
                attempt.getId(),
                question.getId(),
                savedAnswer.getSelectedOption()
        );
    }

    public ExamResultResponse getExamResult(
            Long attemptId, String loggedInUsername, boolean isAdmin) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new RuntimeException("Exam attempt not found"));

        // Check whether the logged-in user owns this exam attempt
        if (!isAdmin && !attempt.getUsername().equals(loggedInUsername)) {
            throw new RuntimeException(
                    "You are not allowed to access this result");
        }

        // Get all submitted answers
        List<ExamAnswer> answers =
                examAnswerRepository.findByExamAttempt_Id(attemptId);

        int correct = 0;

        for (ExamAnswer answer : answers) {
            if (Boolean.TRUE.equals(answer.getIsCorrect())) {
                correct++;
            }
        }

        int totalAttempted = answers.size();
        int incorrect = totalAttempted - correct;

        ExamResultResponse result = new ExamResultResponse();

        result.setAttemptId(attempt.getId());
        result.setUsername(attempt.getUsername());
        result.setTotalAttempted(totalAttempted);
        result.setCorrectAnswers(correct);
        result.setIncorrectAnswers(incorrect);
        result.setScore(attempt.getScore());
        result.setStatus(attempt.getStatus());

        long timeTakenSeconds = 0;

        if (attempt.getStartTime() != null
                && attempt.getEndTime() != null) {

            timeTakenSeconds = java.time.Duration.between(
                    attempt.getStartTime(),
                    attempt.getEndTime()
            ).getSeconds();
        }

        result.setTimeTakenSeconds(timeTakenSeconds);

        return result;
    }


    public ExamAttempt completeExam(Long id) {

        ExamAttempt examAttempt = examAttemptRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam attempt not found"));

        if (!"IN_PROGRESS".equals(examAttempt.getStatus())) {
            throw new RuntimeException("Exam is not in progress");
        }

        if (examAttempt.getStartTime() != null
                && examAttempt.getDurationMinutes() != null) {

            LocalDateTime deadline = examAttempt.getStartTime()
                    .plusMinutes(examAttempt.getDurationMinutes());

            if (LocalDateTime.now().isAfter(deadline)) {
                throw new RuntimeException("Exam time has expired");
            }
        }

        List<ExamAnswer> answers =
                examAnswerRepository.findByExamAttempt_Id(id);

        if (answers.isEmpty()) {
            throw new RuntimeException("No answers submitted");
        }

        int finalScore = 0;

        for (ExamAnswer answer : answers) {

            Question question = answer.getQuestion();

            boolean correct = question.getCorrectOption() != null
                    && question.getCorrectOption()
                    .equalsIgnoreCase(answer.getSelectedOption());

            answer.setIsCorrect(correct);

            if (correct) {
                finalScore++;
            }

            examAnswerRepository.save(answer);
        }

        examAttempt.setScore(finalScore);
        examAttempt.setEndTime(LocalDateTime.now());
        examAttempt.setStatus("COMPLETED");

        return examAttemptRepository.save(examAttempt);
    }
    public int calculateScore(List<StudentAnswerRequest> answers) {

        if (answers == null || answers.isEmpty()) {
            throw new RuntimeException("Answers cannot be empty");
        }

        int score = 0;

        java.util.Set<Long> answeredQuestions = new java.util.HashSet<>();

        for (StudentAnswerRequest answer : answers) {

            if (answer.getQuestionId() == null) {
                throw new RuntimeException("Question ID is required");
            }

            if (answer.getSelectedOption() == null
                    || !answer.getSelectedOption()
                    .matches("(?i)[ABCD]")) {
                throw new RuntimeException(
                        "Selected option must be A, B, C, or D");
            }

            if (!answeredQuestions.add(answer.getQuestionId())) {
                throw new RuntimeException(
                        "Duplicate question answers are not allowed");
            }

            Question question = questionRepository
                    .findById(answer.getQuestionId())
                    .orElseThrow(() ->
                            new RuntimeException("Question not found"));

            if (question.getCorrectOption() != null
                    && question.getCorrectOption()
                    .equalsIgnoreCase(answer.getSelectedOption())) {
                score++;
            }
        }

        return score;
    }
    public void checkAttemptOwnership(
            Long attemptId,
            String loggedInUsername,
            boolean isAdmin) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new RuntimeException("Exam attempt not found"));

        if (!isAdmin &&
                !attempt.getUsername().equals(loggedInUsername)) {

            throw new RuntimeException(
                    "You are not allowed to access this exam attempt");
        }
    }
}