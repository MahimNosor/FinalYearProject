import { Component, OnInit } from '@angular/core';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IQuestion, NewQuestion } from 'app/entities/question/question.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-question-management',
  templateUrl: './question-management.component.html',
  styleUrls: ['./question-management.component.scss'],
})
export class QuestionManagementComponent implements OnInit {
  questions?: IQuestion[];

  constructor(
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
  ) {}

  ngOnInit(): void {
    this.loadTeacherQuestions();
  }

  createQuestion(): void {
    // Redirecting to the creation form
    this.router.navigate(['/question/new']);
  }

  editQuestion(question: IQuestion): void {
    // Redirecting to the edit form for the selected question
    this.router.navigate(['/question', question.id, 'edit']);
  }

  deleteQuestion(id: number): void {
    // Deleting the question and reloading the list
    this.questionService.delete(id).subscribe(() => {
      this.loadTeacherQuestions();
    });
  }

  private loadTeacherQuestions(): void {
    // Use the teacher-specific endpoint to get questions
    this.questionService.getQuestionsForTeacher().subscribe({
      next: (questions: IQuestion[]) => {
        this.questions = questions;
      },
      error() {
        console.error('Error loading questions for the teacher');
      },
    });
  }
}
