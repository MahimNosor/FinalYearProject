import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-assignment-management',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './assignment-management.component.html',
  styleUrl: './assignment-management.component.scss'
})
export class AssignmentManagementComponent implements OnInit {
  assignments: any[] = []; // Store teacher-specific assignments

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadAssignments();
  }

  loadAssignments(): void {
    this.http.get<any[]>('/api/assignments/teacher/assignments').subscribe({
      next: response => {
        this.assignments = response; // Store fetched assignments
      },
      error: err => {
        console.error('Error loading assignments:', err);
      },
    });
  }

  createAssignment(): void {
    this.router.navigate(['/teacher/assignment-management/new']);
  }

  editAssignment(assignment: any): void {
    if (!assignment.isPreloaded) {
      this.router.navigate(['assignment', assignment.id, 'edit']);
    } else {
      alert('Preloaded assignments cannot be edited.');
    }
  }

  deleteAssignment(assignmentId: number): void {
    if (confirm('Are you sure you want to delete this assignment?')) {
      this.http.delete(`/api/assignments/${assignmentId}`).subscribe({
        next: () => {
          this.assignments = this.assignments.filter(a => a.id !== assignmentId);
          alert('Assignment deleted successfully.');
        },
        error: err => {
          console.error('Error deleting assignment:', err);
          alert('Failed to delete assignment.');
        },
      });
    }
  }
}
