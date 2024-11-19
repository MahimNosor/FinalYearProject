import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';
import { UserQuestionService } from '../service/user-question.service';
import { IUserQuestion } from '../user-question.model';
import { UserQuestionFormGroup, UserQuestionFormService } from './user-question-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-question-update',
  templateUrl: './user-question-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserQuestionUpdateComponent implements OnInit {
  isSaving = false;
  userQuestion: IUserQuestion | null = null;
  submissionStatusValues = Object.keys(SubmissionStatus);

  appUsersSharedCollection: IAppUser[] = [];
  questionsSharedCollection: IQuestion[] = [];

  protected userQuestionService = inject(UserQuestionService);
  protected userQuestionFormService = inject(UserQuestionFormService);
  protected appUserService = inject(AppUserService);
  protected questionService = inject(QuestionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserQuestionFormGroup = this.userQuestionFormService.createUserQuestionFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareQuestion = (o1: IQuestion | null, o2: IQuestion | null): boolean => this.questionService.compareQuestion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userQuestion }) => {
      this.userQuestion = userQuestion;
      if (userQuestion) {
        this.updateForm(userQuestion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userQuestion = this.userQuestionFormService.getUserQuestion(this.editForm);
    if (userQuestion.id !== null) {
      this.subscribeToSaveResponse(this.userQuestionService.update(userQuestion));
    } else {
      this.subscribeToSaveResponse(this.userQuestionService.create(userQuestion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserQuestion>>): void {
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

  protected updateForm(userQuestion: IUserQuestion): void {
    this.userQuestion = userQuestion;
    this.userQuestionFormService.resetForm(this.editForm, userQuestion);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      userQuestion.appUser,
    );
    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing<IQuestion>(
      this.questionsSharedCollection,
      userQuestion.question,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.userQuestion?.appUser)),
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing<IQuestion>(questions, this.userQuestion?.question),
        ),
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));
  }
}
