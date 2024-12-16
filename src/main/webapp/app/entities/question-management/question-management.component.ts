import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'jhi-question-management',
  standalone: true,
  templateUrl: './question-management.component.html',
  styleUrls: ['./question-management.component.scss'],
})
export class QuestionManagementComponent implements OnInit {
  questions: any[] = []; // Array to store questions

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadQuestions();
  }

  loadQuestions(): void {
    this.http.get<any[]>('/api/teacher/questions').subscribe({
      next: response => {
        this.questions = response; // Store the response
      },
      error: err => {
        console.error('Error loading questions:', err);
      },
    });
  }

  deleteQuestion(questionId: number): void {
    if (confirm('Are you sure you want to delete this question?')) {
      this.http.delete(`/api/questions/${questionId}`).subscribe({
        next: () => {
          this.questions = this.questions.filter(q => q.id !== questionId);
          alert('Question deleted successfully.');
        },
        error: err => {
          console.error('Error deleting question:', err);
          alert('Failed to delete question.');
        },
      });
    }
  }

  navigateToCreateQuestion(): void {
    this.router.navigate(['/teacher/question-management/new']);
  }
}
