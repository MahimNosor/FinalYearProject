import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 18533,
  title: 'license',
  difficulty: 'BEGINNER',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVA',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 3130,
};

export const sampleWithPartialData: IQuestion = {
  id: 26371,
  title: 'and inasmuch',
  difficulty: 'MEDIUM',
  description: '../fake-data/blob/hipster.txt',
  language: 'CPP',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 28365,
};

export const sampleWithFullData: IQuestion = {
  id: 30230,
  title: 'consequently',
  difficulty: 'MEDIUM',
  description: '../fake-data/blob/hipster.txt',
  language: 'PYTHON',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 27785,
};

export const sampleWithNewData: NewQuestion = {
  title: 'lotion',
  difficulty: 'BEGINNER',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVASCRIPT',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 16662,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
