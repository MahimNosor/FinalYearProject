import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserQuestion, NewUserQuestion } from '../user-question.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserQuestion for edit and NewUserQuestionFormGroupInput for create.
 */
type UserQuestionFormGroupInput = IUserQuestion | PartialWithRequiredKeyOf<NewUserQuestion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserQuestion | NewUserQuestion> = Omit<T, 'submissionDate'> & {
  submissionDate?: string | null;
};

type UserQuestionFormRawValue = FormValueOf<IUserQuestion>;

type NewUserQuestionFormRawValue = FormValueOf<NewUserQuestion>;

type UserQuestionFormDefaults = Pick<NewUserQuestion, 'id' | 'submissionDate'>;

type UserQuestionFormGroupContent = {
  id: FormControl<UserQuestionFormRawValue['id'] | NewUserQuestion['id']>;
  score: FormControl<UserQuestionFormRawValue['score']>;
  submissionDate: FormControl<UserQuestionFormRawValue['submissionDate']>;
  status: FormControl<UserQuestionFormRawValue['status']>;
  appUser: FormControl<UserQuestionFormRawValue['appUser']>;
  question: FormControl<UserQuestionFormRawValue['question']>;
  assignment: FormControl<UserQuestionFormRawValue['assignment']>;
};

export type UserQuestionFormGroup = FormGroup<UserQuestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserQuestionFormService {
  createUserQuestionFormGroup(userQuestion: UserQuestionFormGroupInput = { id: null }): UserQuestionFormGroup {
    const userQuestionRawValue = this.convertUserQuestionToUserQuestionRawValue({
      ...this.getFormDefaults(),
      ...userQuestion,
    });
    return new FormGroup<UserQuestionFormGroupContent>({
      id: new FormControl(
        { value: userQuestionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      score: new FormControl(userQuestionRawValue.score),
      submissionDate: new FormControl(userQuestionRawValue.submissionDate),
      status: new FormControl(userQuestionRawValue.status),
      appUser: new FormControl(userQuestionRawValue.appUser),
      question: new FormControl(userQuestionRawValue.question),
      assignment: new FormControl(userQuestionRawValue.assignment),
    });
  }

  getUserQuestion(form: UserQuestionFormGroup): IUserQuestion | NewUserQuestion {
    return this.convertUserQuestionRawValueToUserQuestion(form.getRawValue() as UserQuestionFormRawValue | NewUserQuestionFormRawValue);
  }

  resetForm(form: UserQuestionFormGroup, userQuestion: UserQuestionFormGroupInput): void {
    const userQuestionRawValue = this.convertUserQuestionToUserQuestionRawValue({ ...this.getFormDefaults(), ...userQuestion });
    form.reset(
      {
        ...userQuestionRawValue,
        id: { value: userQuestionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserQuestionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submissionDate: currentTime,
    };
  }

  private convertUserQuestionRawValueToUserQuestion(
    rawUserQuestion: UserQuestionFormRawValue | NewUserQuestionFormRawValue,
  ): IUserQuestion | NewUserQuestion {
    return {
      ...rawUserQuestion,
      submissionDate: dayjs(rawUserQuestion.submissionDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserQuestionToUserQuestionRawValue(
    userQuestion: IUserQuestion | (Partial<NewUserQuestion> & UserQuestionFormDefaults),
  ): UserQuestionFormRawValue | PartialWithRequiredKeyOf<NewUserQuestionFormRawValue> {
    return {
      ...userQuestion,
      submissionDate: userQuestion.submissionDate ? userQuestion.submissionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
