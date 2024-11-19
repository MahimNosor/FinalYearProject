import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IQuestion } from 'app/entities/question/question.model';
import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';

export interface IUserQuestion {
  id: number;
  score?: number | null;
  submissionDate?: dayjs.Dayjs | null;
  status?: keyof typeof SubmissionStatus | null;
  appUser?: Pick<IAppUser, 'id'> | null;
  question?: Pick<IQuestion, 'id'> | null;
}

export type NewUserQuestion = Omit<IUserQuestion, 'id'> & { id: null };
