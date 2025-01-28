import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeacherDashboardService {
  private metricsUrl = '/api/app-users/teacher-dashboard/metrics';
  private studentsUrl = '/api/app-users/teacher-dashboard/students';

  constructor(private http: HttpClient) {}

  /**
   * Fetch teacher dashboard metrics
   */
  getMetrics(): Observable<any> {
    return this.http.get(this.metricsUrl);
  }

  /**
   * Fetch distinct students for the teacher
   */
  getDistinctStudents(): Observable<any[]> {
    return this.http.get<any[]>(this.studentsUrl);
  }
}
