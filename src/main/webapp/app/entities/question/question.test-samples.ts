import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 12347,
  title: 'ha bah',
  difficulty: 'MEDIUM',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVASCRIPT',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 13464,
};

export const sampleWithPartialData: IQuestion = {
  id: 1270,
  title: 'whose',
  difficulty: 'HARD',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVASCRIPT',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 17486,
};

export const sampleWithFullData: IQuestion = {
  id: 27303,
  title: 'chairperson amused',
  difficulty: 'BEGINNER',
  description: '../fake-data/blob/hipster.txt',
  language: 'C',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 9277,
};

export const sampleWithNewData: NewQuestion = {
  title: 'notwithstanding',
  difficulty: 'HARD',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVASCRIPT',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 25561,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
