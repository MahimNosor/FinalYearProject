import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { IUserQuestion } from '../user-question.model';
import { UserQuestionService } from '../service/user-question.service';
import { UserQuestionFormService } from './user-question-form.service';

import { UserQuestionUpdateComponent } from './user-question-update.component';

describe('UserQuestion Management Update Component', () => {
  let comp: UserQuestionUpdateComponent;
  let fixture: ComponentFixture<UserQuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userQuestionFormService: UserQuestionFormService;
  let userQuestionService: UserQuestionService;
  let appUserService: AppUserService;
  let questionService: QuestionService;
  let assignmentService: AssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserQuestionUpdateComponent],
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
      .overrideTemplate(UserQuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserQuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userQuestionFormService = TestBed.inject(UserQuestionFormService);
    userQuestionService = TestBed.inject(UserQuestionService);
    appUserService = TestBed.inject(AppUserService);
    questionService = TestBed.inject(QuestionService);
    assignmentService = TestBed.inject(AssignmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const userQuestion: IUserQuestion = { id: 456 };
      const appUser: IAppUser = { id: 11801 };
      userQuestion.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 14782 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userQuestion });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining),
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Question query and add missing value', () => {
      const userQuestion: IUserQuestion = { id: 456 };
      const question: IQuestion = { id: 8561 };
      userQuestion.question = question;

      const questionCollection: IQuestion[] = [{ id: 32006 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [question];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userQuestion });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        questionCollection,
        ...additionalQuestions.map(expect.objectContaining),
      );
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Assignment query and add missing value', () => {
      const userQuestion: IUserQuestion = { id: 456 };
      const assignment: IAssignment = { id: 21927 };
      userQuestion.assignment = assignment;

      const assignmentCollection: IAssignment[] = [{ id: 3204 }];
      jest.spyOn(assignmentService, 'query').mockReturnValue(of(new HttpResponse({ body: assignmentCollection })));
      const additionalAssignments = [assignment];
      const expectedCollection: IAssignment[] = [...additionalAssignments, ...assignmentCollection];
      jest.spyOn(assignmentService, 'addAssignmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userQuestion });
      comp.ngOnInit();

      expect(assignmentService.query).toHaveBeenCalled();
      expect(assignmentService.addAssignmentToCollectionIfMissing).toHaveBeenCalledWith(
        assignmentCollection,
        ...additionalAssignments.map(expect.objectContaining),
      );
      expect(comp.assignmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userQuestion: IUserQuestion = { id: 456 };
      const appUser: IAppUser = { id: 31807 };
      userQuestion.appUser = appUser;
      const question: IQuestion = { id: 27026 };
      userQuestion.question = question;
      const assignment: IAssignment = { id: 6602 };
      userQuestion.assignment = assignment;

      activatedRoute.data = of({ userQuestion });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContain(appUser);
      expect(comp.questionsSharedCollection).toContain(question);
      expect(comp.assignmentsSharedCollection).toContain(assignment);
      expect(comp.userQuestion).toEqual(userQuestion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserQuestion>>();
      const userQuestion = { id: 123 };
      jest.spyOn(userQuestionFormService, 'getUserQuestion').mockReturnValue(userQuestion);
      jest.spyOn(userQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userQuestion }));
      saveSubject.complete();

      // THEN
      expect(userQuestionFormService.getUserQuestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userQuestionService.update).toHaveBeenCalledWith(expect.objectContaining(userQuestion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserQuestion>>();
      const userQuestion = { id: 123 };
      jest.spyOn(userQuestionFormService, 'getUserQuestion').mockReturnValue({ id: null });
      jest.spyOn(userQuestionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userQuestion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userQuestion }));
      saveSubject.complete();

      // THEN
      expect(userQuestionFormService.getUserQuestion).toHaveBeenCalled();
      expect(userQuestionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserQuestion>>();
      const userQuestion = { id: 123 };
      jest.spyOn(userQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userQuestionService.update).toHaveBeenCalled();
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

    describe('compareQuestion', () => {
      it('Should forward to questionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionService, 'compareQuestion');
        comp.compareQuestion(entity, entity2);
        expect(questionService.compareQuestion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAssignment', () => {
      it('Should forward to assignmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(assignmentService, 'compareAssignment');
        comp.compareAssignment(entity, entity2);
        expect(assignmentService.compareAssignment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
