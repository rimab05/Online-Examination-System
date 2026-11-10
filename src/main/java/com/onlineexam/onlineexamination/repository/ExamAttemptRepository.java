package com.onlineexam.onlineexamination.repository;

import com.onlineexam.onlineexamination.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    List<ExamAttempt> findByUsernameOrderByStartTimeDesc(String username);
}
