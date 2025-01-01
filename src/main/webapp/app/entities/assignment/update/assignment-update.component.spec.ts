import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { IAssignment } from '../assignment.model';
import { AssignmentService } from '../service/assignment.service';
import { AssignmentFormService } from './assignment-form.service';

import { AssignmentUpdateComponent } from './assignment-update.component';

describe('Assignment Management Update Component', () => {
  let comp: AssignmentUpdateComponent;
  let fixture: ComponentFixture<AssignmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assignmentFormService: AssignmentFormService;
  let assignmentService: AssignmentService;
  let appUserService: AppUserService;
  let studentClassService: StudentClassService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssignmentUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AssignmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssignmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assignmentFormService = TestBed.inject(AssignmentFormService);
    assignmentService = TestBed.inject(AssignmentService);
    appUserService = TestBed.inject(AppUserService);
    studentClassService = TestBed.inject(StudentClassService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const assignment: IAssignment = { id: 456 };
      const appUser: IAppUser = { id: 30831 };
      assignment.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 11122 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assignment });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining),
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StudentClass query and add missing value', () => {
      const assignment: IAssignment = { id: 456 };
      const studentClasses: IStudentClass[] = [{ id: 27843 }];
      assignment.studentClasses = studentClasses;

      const studentClassCollection: IStudentClass[] = [{ id: 18564 }];
      jest.spyOn(studentClassService, 'query').mockReturnValue(of(new HttpResponse({ body: studentClassCollection })));
      const additionalStudentClasses = [...studentClasses];
      const expectedCollection: IStudentClass[] = [...additionalStudentClasses, ...studentClassCollection];
      jest.spyOn(studentClassService, 'addStudentClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assignment });
      comp.ngOnInit();

      expect(studentClassService.query).toHaveBeenCalled();
      expect(studentClassService.addStudentClassToCollectionIfMissing).toHaveBeenCalledWith(
        studentClassCollection,
        ...additionalStudentClasses.map(expect.objectContaining),
      );
      expect(comp.studentClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assignment: IAssignment = { id: 456 };
      const appUser: IAppUser = { id: 7991 };
      assignment.appUser = appUser;
      const studentClasses: IStudentClass = { id: 19833 };
      assignment.studentClasses = [studentClasses];

      activatedRoute.data = of({ assignment });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContain(appUser);
      expect(comp.studentClassesSharedCollection).toContain(studentClasses);
      expect(comp.assignment).toEqual(assignment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssignment>>();
      const assignment = { id: 123 };
      jest.spyOn(assignmentFormService, 'getAssignment').mockReturnValue(assignment);
      jest.spyOn(assignmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assignment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assignment }));
      saveSubject.complete();

      // THEN
      expect(assignmentFormService.getAssignment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assignmentService.update).toHaveBeenCalledWith(expect.objectContaining(assignment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssignment>>();
      const assignment = { id: 123 };
      jest.spyOn(assignmentFormService, 'getAssignment').mockReturnValue({ id: null });
      jest.spyOn(assignmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assignment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assignment }));
      saveSubject.complete();

      // THEN
      expect(assignmentFormService.getAssignment).toHaveBeenCalled();
      expect(assignmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssignment>>();
      const assignment = { id: 123 };
      jest.spyOn(assignmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assignment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assignmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppUser', () => {
      it('Should forward to appUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appUserService, 'compareAppUser');
        comp.compareAppUser(entity, entity2);
        expect(appUserService.compareAppUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStudentClass', () => {
      it('Should forward to studentClassService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(studentClassService, 'compareStudentClass');
        comp.compareStudentClass(entity, entity2);
        expect(studentClassService.compareStudentClass).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
