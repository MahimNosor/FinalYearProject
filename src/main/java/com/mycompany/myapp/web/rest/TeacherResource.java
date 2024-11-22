package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.AppUserService;
import com.mycompany.myapp.service.dto.TeacherDashboardDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherResource {

    private final AppUserService appUserService;

    public TeacherResource(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Fetches dashboard statistics for a teacher.
     */
    @GetMapping("/dashboard")
    public TeacherDashboardDTO getDashboard(Authentication authentication) {
        return appUserService.getDashboardStats(authentication);
    }
}
