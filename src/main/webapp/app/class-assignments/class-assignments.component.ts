import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-class-assignments',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './class-assignments.component.html',
  styleUrl: './class-assignments.component.scss'
})
export class ClassAssignmentsComponent {
  classId: number | null = null; // To store the current class ID
  assignments: IAssignment[] = []; // Array to store the assignments
  classLeaderboard: any[] = [];

  constructor(private route: ActivatedRoute, private assignmentService: AssignmentService, private http: HttpClient) {}

  ngOnInit(): void {
    // Extract the classId from the route
    this.route.params.subscribe(params => {
      this.classId = +params['id'];
      if (this.classId) {
        this.loadAssignments(this.classId);
        this.getClassLeaderboard(this.classId);
      }
    });
  }

  // Fetch assignments from the service
  loadAssignments(classId: number): void {
    this.assignmentService.getAssignmentsByClass(classId).subscribe(data => {
      this.assignments = data;
    });
  }

  getClassLeaderboard(classId: number): void {
    const apiUrl = `/api/app-users/leaderboard/class/${classId}`;
    this.http.get<any[]>(apiUrl).subscribe({
      next: (data: any[]) => {
        this.classLeaderboard = data; // Store leaderboard data
        console.log('Class Leaderboard:', this.classLeaderboard);
      },
      error: (err: any) => {
        console.error('Error fetching class leaderboard:', err);
      },
    });
  }
  
  
}
