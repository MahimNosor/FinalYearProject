import { IAssignment, NewAssignment } from './assignment.model';

export const sampleWithRequiredData: IAssignment = {
  id: 24729,
  title: 'frequent preregister',
  difficulty: 'HARD',
  description: '../fake-data/blob/hipster.txt',
  language: 'PYTHON',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 17428,
};

export const sampleWithPartialData: IAssignment = {
  id: 6910,
  title: 'sternly glisten',
  difficulty: 'HARD',
  description: '../fake-data/blob/hipster.txt',
  language: 'JAVASCRIPT',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 31265,
  isPreloaded: false,
};

export const sampleWithFullData: IAssignment = {
  id: 29710,
  title: 'politely',
  difficulty: 'BEGINNER',
  description: '../fake-data/blob/hipster.txt',
  language: 'CSHARP',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 5543,
  isPreloaded: true,
};

export const sampleWithNewData: NewAssignment = {
  title: 'nor steeple woot',
  difficulty: 'BEGINNER',
  description: '../fake-data/blob/hipster.txt',
  language: 'PYTHON',
  testCases: '../fake-data/blob/hipster.txt',
  maxScore: 11282,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
