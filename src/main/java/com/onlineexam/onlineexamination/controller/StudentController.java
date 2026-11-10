
package com.onlineexam.onlineexamination.controller;

import com.onlineexam.onlineexamination.entity.Student;
import com.onlineexam.onlineexamination.service.StudentService;
import com.onlineexam.onlineexamination.dto.LoginRequest;
import com.onlineexam.onlineexamination.dto.UpdateProfileRequest;
import com.onlineexam.onlineexamination.dto.LoginResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PostMapping("/register")
    public Student registerStudent(@RequestBody Student student) {
        return studentService.registerStudent(student);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return studentService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
    }

    // Only admin can view all students
    @GetMapping
    public List<Student> getAllStudents(Authentication authentication) {

        if (!isAdmin(authentication)) {
            throw new AccessDeniedException(
                    "Only admin can view all students");
        }

        return studentService.getAllStudents();
    }

    // Student can view own profile; admin can view any profile
    @GetMapping("/{username}")
    public Student getStudentByUsername(
            @PathVariable String username,
            Authentication authentication) {

        if (!isAdmin(authentication)
                && !authentication.getName().equals(username)) {

            throw new AccessDeniedException(
                    "You are not allowed to view this student profile");
        }

        return studentService.getStudentByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));
    }

    // Student can update own profile; admin can update any profile
    @PutMapping("/{username}/profile")
    public Student updateProfile(
            @PathVariable String username,
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        if (!isAdmin(authentication)
                && !authentication.getName().equals(username)) {

            throw new AccessDeniedException(
                    "You are not allowed to update this student profile");
        }

        return studentService.updateProfile(username, request);
    }
}
