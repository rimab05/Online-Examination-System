package com.onlineexam.onlineexamination.repository;

import com.onlineexam.onlineexamination.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
