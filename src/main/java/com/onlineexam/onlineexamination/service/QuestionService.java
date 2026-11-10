
package com.onlineexam.onlineexamination.service;

import com.onlineexam.onlineexamination.entity.Question;
import com.onlineexam.onlineexamination.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import com.onlineexam.onlineexamination.dto.QuestionResponse;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question addQuestion(Question question) {

        if (question.getQuestionText() == null
                || question.getQuestionText().trim().isEmpty()) {
            throw new RuntimeException("Question text cannot be empty");
        }
        if (question.getOptionA() == null || question.getOptionA().trim().isEmpty()
                || question.getOptionB() == null || question.getOptionB().trim().isEmpty()
                || question.getOptionC() == null || question.getOptionC().trim().isEmpty()
                || question.getOptionD() == null || question.getOptionD().trim().isEmpty()) {

            throw new RuntimeException("All four options are required");
        }

        if (question.getCorrectOption() == null
                || !question.getCorrectOption().matches("(?i)[ABCD]")) {
            throw new RuntimeException(
                    "Correct option must be A, B, C, or D");
        }

        return questionRepository.save(question);
    }

    public List<QuestionResponse> getAllQuestions() {

        return questionRepository.findAll()
                .stream()
                .map(question -> new QuestionResponse(
                        question.getId(),
                        question.getQuestionText(),
                        question.getOptionA(),
                        question.getOptionB(),
                        question.getOptionC(),
                        question.getOptionD()
                ))
                .collect(Collectors.toList());
    }

    public QuestionResponse getQuestionById(Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));

        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD()
        );
    }
    public Question updateQuestion(Long id, Question updatedQuestion) {

        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));

        if (updatedQuestion.getQuestionText() == null
                || updatedQuestion.getQuestionText().trim().isEmpty()) {
            throw new RuntimeException("Question text cannot be empty");
        }

        if (updatedQuestion.getOptionA() == null || updatedQuestion.getOptionA().trim().isEmpty()
                || updatedQuestion.getOptionB() == null || updatedQuestion.getOptionB().trim().isEmpty()
                || updatedQuestion.getOptionC() == null || updatedQuestion.getOptionC().trim().isEmpty()
                || updatedQuestion.getOptionD() == null || updatedQuestion.getOptionD().trim().isEmpty()) {
            throw new RuntimeException("All four options are required");
        }

        if (updatedQuestion.getCorrectOption() == null
                || !updatedQuestion.getCorrectOption().matches("(?i)[ABCD]")) {
            throw new RuntimeException("Correct option must be A, B, C, or D");
        }

        existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
        existingQuestion.setOptionA(updatedQuestion.getOptionA());
        existingQuestion.setOptionB(updatedQuestion.getOptionB());
        existingQuestion.setOptionC(updatedQuestion.getOptionC());
        existingQuestion.setOptionD(updatedQuestion.getOptionD());
        existingQuestion.setCorrectOption(
                updatedQuestion.getCorrectOption().toUpperCase());

        return questionRepository.save(existingQuestion);
    }
    public void deleteQuestion(Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));

        questionRepository.delete(question);
    }
}
