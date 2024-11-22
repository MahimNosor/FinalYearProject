import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  private apiUrl = '/api/teacher'; // Backend API base URL for teacher-related endpoints

  constructor(private http: HttpClient) {}

  /**
   * Fetches the dashboard statistics for the teacher.
   */
  getDashboardStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard`);
  }
}
