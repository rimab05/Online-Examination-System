package com.onlineexam.onlineexamination.dto;

public class StudentAnswerResponse {

    private Long id;
    private Long attemptId;
    private Long questionId;
    private String selectedOption;

    public StudentAnswerResponse() {
    }

    public StudentAnswerResponse(
            Long id,
            Long attemptId,
            Long questionId,
            String selectedOption) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public Long getId() {
        return id;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }
}