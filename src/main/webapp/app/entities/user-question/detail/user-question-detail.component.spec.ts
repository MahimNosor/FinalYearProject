import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserQuestionDetailComponent } from './user-question-detail.component';

describe('UserQuestion Management Detail Component', () => {
  let comp: UserQuestionDetailComponent;
  let fixture: ComponentFixture<UserQuestionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserQuestionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./user-question-detail.component').then(m => m.UserQuestionDetailComponent),
              resolve: { userQuestion: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserQuestionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserQuestionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userQuestion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserQuestionDetailComponent);

      // THEN
      expect(instance.userQuestion()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
