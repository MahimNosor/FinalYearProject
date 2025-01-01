import dayjs from 'dayjs/esm';

import { IUserQuestion, NewUserQuestion } from './user-question.model';

export const sampleWithRequiredData: IUserQuestion = {
  id: 28875,
};

export const sampleWithPartialData: IUserQuestion = {
  id: 1955,
  submissionDate: dayjs('2024-11-18T09:41'),
};

export const sampleWithFullData: IUserQuestion = {
  id: 25726,
  score: 8090,
  submissionDate: dayjs('2024-11-18T21:44'),
  status: 'COMPLETED',
};

export const sampleWithNewData: NewUserQuestion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
