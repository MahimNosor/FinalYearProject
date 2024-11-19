import dayjs from 'dayjs/esm';

import { IUserQuestion, NewUserQuestion } from './user-question.model';

export const sampleWithRequiredData: IUserQuestion = {
  id: 19859,
};

export const sampleWithPartialData: IUserQuestion = {
  id: 6868,
  submissionDate: dayjs('2024-11-18T05:07'),
  status: 'SUBMITTED',
};

export const sampleWithFullData: IUserQuestion = {
  id: 19513,
  score: 20622,
  submissionDate: dayjs('2024-11-18T14:23'),
  status: 'SUBMITTED',
};

export const sampleWithNewData: NewUserQuestion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
