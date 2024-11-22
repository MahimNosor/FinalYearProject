package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.StudentClassRepository; // Corrected import
import com.mycompany.myapp.repository.UserQuestionRepository; // Corrected import
import com.mycompany.myapp.service.dto.TeacherDashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherDashboardService {

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private UserQuestionRepository userQuestionRepository;

    public TeacherDashboardDTO getDashboardStats() {
        TeacherDashboardDTO stats = new TeacherDashboardDTO();

        stats.setTotalClasses(studentClassRepository.count()); // Correct method

        return stats;
    }
}
