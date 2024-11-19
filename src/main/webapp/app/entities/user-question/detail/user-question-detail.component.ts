import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserQuestion } from '../user-question.model';

@Component({
  standalone: true,
  selector: 'jhi-user-question-detail',
  templateUrl: './user-question-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserQuestionDetailComponent {
  userQuestion = input<IUserQuestion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
