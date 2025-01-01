import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 3393,
  title: 'drat cleave',
  difficulty: 'HARD',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVA',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 32667,
};

export const sampleWithPartialData: IQuestion = {
  id: 10991,
  title: 'crafty',
  difficulty: 'MEDIUM',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVA',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 10814,
};

export const sampleWithFullData: IQuestion = {
  id: 13576,
  title: 'discontinue yesterday',
  difficulty: 'HARD',
  description: '../fake-data/blob/hipster.txt',
  language: 'PYTHON',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 19829,
};

export const sampleWithNewData: NewQuestion = {
  title: 'disclosure um scotch',
  difficulty: 'MEDIUM',
  description: '../fake-data/blob/hipster.txt',
  language: 'CSHARP',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 19464,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
