package com.mycompany.myapp.service.dto;

public class TeacherDashboardDTO {

    private long totalClasses;
    private long activeQuestions;
    private long pendingSubmissions;

    // Getters and setters
    public long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(long totalClasses) {
        this.totalClasses = totalClasses;
    }

    public long getActiveQuestions() {
        return activeQuestions;
    }

    public void setActiveQuestions(long activeQuestions) {
        this.activeQuestions = activeQuestions;
    }

    public long getPendingSubmissions() {
        return pendingSubmissions;
    }

    public void setPendingSubmissions(long pendingSubmissions) {
        this.pendingSubmissions = pendingSubmissions;
    }
}
