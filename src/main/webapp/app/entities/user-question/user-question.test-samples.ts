import dayjs from 'dayjs/esm';

import { IUserQuestion, NewUserQuestion } from './user-question.model';

export const sampleWithRequiredData: IUserQuestion = {
  id: 27651,
};

export const sampleWithPartialData: IUserQuestion = {
  id: 28591,
  status: 'APPROVED',
};

export const sampleWithFullData: IUserQuestion = {
  id: 12936,
  score: 15178,
  submissionDate: dayjs('2024-11-18T00:35'),
  status: 'SUBMITTED',
};

export const sampleWithNewData: NewUserQuestion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
