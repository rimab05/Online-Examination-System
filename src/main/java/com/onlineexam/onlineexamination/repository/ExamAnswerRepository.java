package com.onlineexam.onlineexamination.repository;

import com.onlineexam.onlineexamination.entity.ExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAnswerRepository
        extends JpaRepository<ExamAnswer, Long> {

    List<ExamAnswer> findByExamAttempt_Id(Long attemptId);
}