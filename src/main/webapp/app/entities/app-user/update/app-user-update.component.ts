import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { IAppUser } from '../app-user.model';
import { AppUserService } from '../service/app-user.service';
import { AppUserFormGroup, AppUserFormService } from './app-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-app-user-update',
  templateUrl: './app-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppUserUpdateComponent implements OnInit {
  isSaving = false;
  appUser: IAppUser | null = null;

  studentClassesSharedCollection: IStudentClass[] = [];

  protected appUserService = inject(AppUserService);
  protected appUserFormService = inject(AppUserFormService);
  protected studentClassService = inject(StudentClassService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppUserFormGroup = this.appUserFormService.createAppUserFormGroup();

  compareStudentClass = (o1: IStudentClass | null, o2: IStudentClass | null): boolean =>
    this.studentClassService.compareStudentClass(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appUser }) => {
      this.appUser = appUser;
      if (appUser) {
        this.updateForm(appUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appUser = this.appUserFormService.getAppUser(this.editForm);
    if (appUser.id !== null) {
      this.subscribeToSaveResponse(this.appUserService.update(appUser));
    } else {
      this.subscribeToSaveResponse(this.appUserService.create(appUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppUser>>): void {
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

  protected updateForm(appUser: IAppUser): void {
    this.appUser = appUser;
    this.appUserFormService.resetForm(this.editForm, appUser);

    this.studentClassesSharedCollection = this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
      this.studentClassesSharedCollection,
      ...(appUser.classes ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.studentClassService
      .query()
      .pipe(map((res: HttpResponse<IStudentClass[]>) => res.body ?? []))
      .pipe(
        map((studentClasses: IStudentClass[]) =>
          this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(studentClasses, ...(this.appUser?.classes ?? [])),
        ),
      )
      .subscribe((studentClasses: IStudentClass[]) => (this.studentClassesSharedCollection = studentClasses));
  }
}
