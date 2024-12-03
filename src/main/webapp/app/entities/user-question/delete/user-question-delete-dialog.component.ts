import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserQuestion } from '../user-question.model';
import { UserQuestionService } from '../service/user-question.service';

@Component({
  standalone: true,
  templateUrl: './user-question-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserQuestionDeleteDialogComponent {
  userQuestion?: IUserQuestion;

  protected userQuestionService = inject(UserQuestionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userQuestionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
