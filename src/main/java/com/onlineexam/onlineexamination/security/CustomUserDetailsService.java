package com.onlineexam.onlineexamination.security;

import com.onlineexam.onlineexamination.entity.Student;
import com.onlineexam.onlineexamination.repository.StudentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public CustomUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Student not found"));

        return User.withUsername(student.getUsername())
                .password(student.getPassword())
                .roles(student.getRole())
                .build();
    }
}