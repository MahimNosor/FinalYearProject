import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { QuestionDifficulty } from 'app/entities/enumerations/question-difficulty.model';
import { ProgrammingLanguage } from 'app/entities/enumerations/programming-language.model';
import { QuestionService } from '../service/question.service';
import { IQuestion } from '../question.model';
import { QuestionFormGroup, QuestionFormService } from './question-form.service';

@Component({
  standalone: true,
  selector: 'jhi-question-update',
  templateUrl: './question-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;
  question: IQuestion | null = null;
  questionDifficultyValues = Object.keys(QuestionDifficulty);
  programmingLanguageValues = Object.keys(ProgrammingLanguage);

  studentClassesSharedCollection: IStudentClass[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected questionService = inject(QuestionService);
  protected questionFormService = inject(QuestionFormService);
  protected studentClassService = inject(StudentClassService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuestionFormGroup = this.questionFormService.createQuestionFormGroup();

  compareStudentClass = (o1: IStudentClass | null, o2: IStudentClass | null): boolean =>
    this.studentClassService.compareStudentClass(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      this.question = question;
      if (question) {
        this.updateForm(question);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('finalYearProjectApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const question = this.questionFormService.getQuestion(this.editForm);
    if (question.id !== null) {
      this.subscribeToSaveResponse(this.questionService.update(question));
    } else {
      this.subscribeToSaveResponse(this.questionService.create(question));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(question: IQuestion): void {
    this.question = question;
    this.questionFormService.resetForm(this.editForm, question);

    this.studentClassesSharedCollection = this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
      this.studentClassesSharedCollection,
      question.studentClass,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.studentClassService
      .query()
      .pipe(map((res: HttpResponse<IStudentClass[]>) => res.body ?? []))
      .pipe(
        map((studentClasses: IStudentClass[]) =>
          this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(studentClasses, this.question?.studentClass),
        ),
      )
      .subscribe((studentClasses: IStudentClass[]) => (this.studentClassesSharedCollection = studentClasses));
  }
}
