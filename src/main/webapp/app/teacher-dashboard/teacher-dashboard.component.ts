import { Component, OnInit } from '@angular/core';
import { AppUserService } from '../entities/app-user/service/app-user.service';
import { TeacherDashboardService } from 'app/services/teacher-dashboard.service';

@Component({
  selector: 'jhi-teacher-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './teacher-dashboard.component.html',
  styleUrl: './teacher-dashboard.component.scss',
})
export class TeacherDashboardComponent implements OnInit {
  stats = {
    totalClasses: 0,
    activeQuestions: 0,
    pendingSubmissions: 0,
  };

  constructor(private appUserService: AppUserService) {}

  ngOnInit(): void {
    this.loadDashboardStats();
  }

  private loadDashboardStats(): void {
    this.appUserService.getDashboardStats().subscribe(data => {
      this.stats = data;
    });
  }
}
