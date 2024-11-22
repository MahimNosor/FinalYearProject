package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.TeacherDashboardService;
import com.mycompany.myapp.service.dto.TeacherDashboardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher-dashboard")
public class TeacherDashboardResource {

    @Autowired
    private TeacherDashboardService teacherDashboardService;

    private static final Logger LOG = LoggerFactory.getLogger(TeacherDashboardResource.class);

    @GetMapping("/stats")
    public ResponseEntity<TeacherDashboardDTO> getTeacherDashboardStats() {
        LOG.info("Received request for Teacher Dashboard stats.");
        TeacherDashboardDTO stats = teacherDashboardService.getDashboardStats();
        LOG.info("Returning stats: {}", stats);
        return ResponseEntity.ok(stats);
    }
}
