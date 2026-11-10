package com.onlineexam.onlineexamination.service;

import com.onlineexam.onlineexamination.entity.Exam;
import com.onlineexam.onlineexamination.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public Exam createExam(Exam exam) {

        if (exam.getTitle() == null
                || exam.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Exam title cannot be empty");
        }

        if (exam.getDuration() <= 0) {
            throw new RuntimeException("Exam duration must be greater than zero");
        }

        if (exam.getTotalMarks() <= 0) {
            throw new RuntimeException("Total marks must be greater than zero");
        }

        return examRepository.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam not found"));
    }
}