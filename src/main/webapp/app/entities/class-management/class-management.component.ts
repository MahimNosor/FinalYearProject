import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-class-management',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './class-management.component.html',
  styleUrl: './class-management.component.scss',
})

export class ClassManagementComponent implements OnInit {
  classes: any[] = []; // Array to store classes

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadClasses();
  }

  navigateToCreateClass(): void {
    this.router.navigate(['/teacher/class-management/new']);
  }

  loadClasses(): void {
    this.http.get<any[]>('/api/student-classes/teacher/classes').subscribe({
      next: response => {
        this.classes = response; // Store the response
      },
      error: err => {
        console.error('Error loading classes:', err);
      },
    });
  }

  editClass(classItem: any): void {
    this.router.navigate(['student-class', classItem.id, 'edit']);
  }
  

  deleteClass(classId: number): void {
    if (confirm('Are you sure you want to delete this class?')) {
      this.http.delete(`/api/student-classes/${classId}`).subscribe({
        next: () => {
          this.classes = this.classes.filter(c => c.id !== classId);
          alert('Class deleted successfully.');
        },
        error: err => {
          console.error('Error deleting class:', err);
          alert('Failed to delete class.');
        },
      });
    }
  }  
}
