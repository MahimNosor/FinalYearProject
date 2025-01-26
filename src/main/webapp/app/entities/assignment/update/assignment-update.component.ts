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
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { difficulty } from 'app/entities/enumerations/difficulty.model';
import { ProgrammingLanguage } from 'app/entities/enumerations/programming-language.model';
import { AssignmentService } from '../service/assignment.service';
import { IAssignment } from '../assignment.model';
import { AssignmentFormGroup, AssignmentFormService } from './assignment-form.service';
import { AccountService } from 'app/core/auth/account.service';
@Component({
  standalone: true,
  selector: 'jhi-assignment-update',
  templateUrl: './assignment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssignmentUpdateComponent implements OnInit {
  isSaving = false;
  assignment: IAssignment | null = null;
  difficultyValues = Object.keys(difficulty);
  programmingLanguageValues = Object.keys(ProgrammingLanguage);

  appUsersSharedCollection: IAppUser[] = [];
  studentClassesSharedCollection: IStudentClass[] = [];
  isAdmin = false;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected assignmentService = inject(AssignmentService);
  protected assignmentFormService = inject(AssignmentFormService);
  protected appUserService = inject(AppUserService);
  protected studentClassService = inject(StudentClassService);
  protected activatedRoute = inject(ActivatedRoute);
  protected accountService = inject(AccountService);


  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssignmentFormGroup = this.assignmentFormService.createAssignmentFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareStudentClass = (o1: IStudentClass | null, o2: IStudentClass | null): boolean =>
    this.studentClassService.compareStudentClass(o1, o2);

  ngOnInit(): void {

    this.accountService.identity().subscribe(account => {
      this.isAdmin = account?.authorities?.includes('ROLE_ADMIN') ?? false;
      console.log('Is Admin:', this.isAdmin); // Debug to verify role
    });

    this.activatedRoute.data.subscribe(({ assignment }) => {
      this.assignment = assignment;
      if (assignment) {
        this.updateForm(assignment);
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
    const assignment = this.assignmentFormService.getAssignment(this.editForm);
    if (assignment.id !== null) {
      this.subscribeToSaveResponse(this.assignmentService.update(assignment));
    } else {
      this.subscribeToSaveResponse(this.assignmentService.create(assignment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssignment>>): void {
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

  protected updateForm(assignment: IAssignment): void {
    this.assignment = assignment;
    this.assignmentFormService.resetForm(this.editForm, assignment);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      assignment.appUser,
    );
    this.studentClassesSharedCollection = this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
      this.studentClassesSharedCollection,
      ...(assignment.studentClasses ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.assignment?.appUser)),
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.studentClassService
      .query()
      .pipe(map((res: HttpResponse<IStudentClass[]>) => res.body ?? []))
      .pipe(
        map((studentClasses: IStudentClass[]) =>
          this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
            studentClasses,
            ...(this.assignment?.studentClasses ?? []),
          ),
        ),
      )
      .subscribe((studentClasses: IStudentClass[]) => (this.studentClassesSharedCollection = studentClasses));
  }
}
