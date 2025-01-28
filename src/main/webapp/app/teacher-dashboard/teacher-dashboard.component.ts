import { Component, OnInit } from '@angular/core';
import { AppUserService } from '../entities/app-user/service/app-user.service';
import { TeacherDashboardService } from 'app/services/teacher-dashboard.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-teacher-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './teacher-dashboard.component.html',
  styleUrl: './teacher-dashboard.component.scss',
})
export class TeacherDashboardComponent implements OnInit {
  metrics: { totalClasses: number; totalAssignments: number; totalStudents: number } = {
    totalClasses: 0,
    totalAssignments: 0,
    totalStudents: 0,
  };

  students: any[] = [];

  constructor(
    private appUserService: AppUserService,
    private router: Router,
    private teacherDashboardService: TeacherDashboardService
  ) {}

  ngOnInit(): void {
    this.loadMetrics();
    this.loadStudents();
  }

  navigateToAssignmentManagement(): void {
    this.router.navigate(['teacher/assignment-management']);
  }

  navigateToClassManagement(): void {
    this.router.navigate(['class-management']);
  }

  private loadMetrics(): void {
    this.teacherDashboardService.getMetrics().subscribe({
      next: data => {
        this.metrics = data; // Assign fetched metrics data
        console.log('Metrics from API:', this.metrics);

      },
      error: err => {
        console.error('Error fetching dashboard metrics:', err); // Log error
      },
    });
  }

  private loadStudents(): void {
    this.teacherDashboardService.getDistinctStudents().subscribe({
      next: data => {
        this.students = data; // Assign fetched students data
        console.log('Distinct students from API:', this.students);
      },
      error: err => {
        console.error('Error fetching distinct students:', err); // Log error
      },
    });
  }
}
