import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { StudentClassService } from '../service/student-class.service';
import { IStudentClass } from '../student-class.model';
import { StudentClassFormGroup, StudentClassFormService } from './student-class-form.service';

@Component({
  standalone: true,
  selector: 'jhi-student-class-update',
  templateUrl: './student-class-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StudentClassUpdateComponent implements OnInit {
  isSaving = false;
  studentClass: IStudentClass | null = null;

  appUsersSharedCollection: IAppUser[] = [];
  assignmentsSharedCollection: IAssignment[] = [];

  protected studentClassService = inject(StudentClassService);
  protected studentClassFormService = inject(StudentClassFormService);
  protected appUserService = inject(AppUserService);
  protected assignmentService = inject(AssignmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StudentClassFormGroup = this.studentClassFormService.createStudentClassFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareAssignment = (o1: IAssignment | null, o2: IAssignment | null): boolean => this.assignmentService.compareAssignment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentClass }) => {
      this.studentClass = studentClass;
      if (studentClass) {
        this.updateForm(studentClass);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentClass = this.studentClassFormService.getStudentClass(this.editForm);
    if (studentClass.id !== null) {
      this.subscribeToSaveResponse(this.studentClassService.update(studentClass));
    } else {
      this.subscribeToSaveResponse(this.studentClassService.create(studentClass));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentClass>>): void {
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

  protected updateForm(studentClass: IStudentClass): void {
    this.studentClass = studentClass;
    this.studentClassFormService.resetForm(this.editForm, studentClass);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      ...(studentClass.users ?? []),
    );
    this.assignmentsSharedCollection = this.assignmentService.addAssignmentToCollectionIfMissing<IAssignment>(
      this.assignmentsSharedCollection,
      ...(studentClass.assignments ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) =>
          this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, ...(this.studentClass?.users ?? [])),
        ),
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.assignmentService
      .query()
      .pipe(map((res: HttpResponse<IAssignment[]>) => res.body ?? []))
      .pipe(
        map((assignments: IAssignment[]) =>
          this.assignmentService.addAssignmentToCollectionIfMissing<IAssignment>(assignments, ...(this.studentClass?.assignments ?? [])),
        ),
      )
      .subscribe((assignments: IAssignment[]) => (this.assignmentsSharedCollection = assignments));
  }
}
