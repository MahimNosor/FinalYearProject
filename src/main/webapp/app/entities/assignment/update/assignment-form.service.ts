import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAssignment, NewAssignment } from '../assignment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssignment for edit and NewAssignmentFormGroupInput for create.
 */
type AssignmentFormGroupInput = IAssignment | PartialWithRequiredKeyOf<NewAssignment>;

type AssignmentFormDefaults = Pick<NewAssignment, 'id' | 'isPreloaded' | 'studentClasses'>;

type AssignmentFormGroupContent = {
  id: FormControl<IAssignment['id'] | NewAssignment['id']>;
  title: FormControl<IAssignment['title']>;
  difficulty: FormControl<IAssignment['difficulty']>;
  description: FormControl<IAssignment['description']>;
  language: FormControl<IAssignment['language']>;
  testCases: FormControl<IAssignment['testCases']>;
  maxScore: FormControl<IAssignment['maxScore']>;
  isPreloaded: FormControl<IAssignment['isPreloaded']>;
  appUser: FormControl<IAssignment['appUser']>;
  studentClasses: FormControl<IAssignment['studentClasses']>;
};

export type AssignmentFormGroup = FormGroup<AssignmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssignmentFormService {
  createAssignmentFormGroup(assignment: AssignmentFormGroupInput = { id: null }): AssignmentFormGroup {
    const assignmentRawValue = {
      ...this.getFormDefaults(),
      ...assignment,
    };
    return new FormGroup<AssignmentFormGroupContent>({
      id: new FormControl(
        { value: assignmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(assignmentRawValue.title, {
        validators: [Validators.required],
      }),
      difficulty: new FormControl(assignmentRawValue.difficulty, {
        validators: [Validators.required],
      }),
      description: new FormControl(assignmentRawValue.description, {
        validators: [Validators.required],
      }),
      language: new FormControl(assignmentRawValue.language, {
        validators: [Validators.required],
      }),
      testCases: new FormControl(assignmentRawValue.testCases, {
        validators: [Validators.required],
      }),
      maxScore: new FormControl(assignmentRawValue.maxScore, {
        validators: [Validators.required],
      }),
      isPreloaded: new FormControl(assignmentRawValue.isPreloaded),
      appUser: new FormControl(assignmentRawValue.appUser, {
        validators: [Validators.required],
      }),
      studentClasses: new FormControl(assignmentRawValue.studentClasses ?? []),
    });
  }

  getAssignment(form: AssignmentFormGroup): IAssignment | NewAssignment {
    return form.getRawValue() as IAssignment | NewAssignment;
  }

  resetForm(form: AssignmentFormGroup, assignment: AssignmentFormGroupInput): void {
    const assignmentRawValue = { ...this.getFormDefaults(), ...assignment };
    form.reset(
      {
        ...assignmentRawValue,
        id: { value: assignmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssignmentFormDefaults {
    return {
      id: null,
      isPreloaded: false,
      studentClasses: [],
    };
  }
}
