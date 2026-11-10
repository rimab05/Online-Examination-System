package com.onlineexam.onlineexamination.service;

import com.onlineexam.onlineexamination.entity.Student;
import com.onlineexam.onlineexamination.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.onlineexam.onlineexamination.dto.UpdateProfileRequest;
import com.onlineexam.onlineexamination.dto.LoginResponse;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    public StudentService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Student registerStudent(Student student) {

        if (studentRepository.existsByUsername(student.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        student.setRole("STUDENT");

        student.setPassword(
                passwordEncoder.encode(student.getPassword())
        );

        return studentRepository.save(student);
    }
    public LoginResponse login(String username, String password) {

        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, student.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return new LoginResponse(
                student.getId(),
                student.getUsername(),
                student.getDisplayName(),
                "Login successful"
        );
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
    public Student updateProfile(
            String username,
            UpdateProfileRequest request) {

        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        if (request.getDisplayName() == null
                || request.getDisplayName().isBlank()) {
            throw new RuntimeException("Display name cannot be empty");
        }

        student.setDisplayName(request.getDisplayName().trim());

        return studentRepository.save(student);
    }
}
