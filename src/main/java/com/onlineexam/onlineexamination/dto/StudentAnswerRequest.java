package com.onlineexam.onlineexamination.dto;

public class StudentAnswerRequest {

    private Long questionId;
    private String selectedOption;

    public StudentAnswerRequest() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
}