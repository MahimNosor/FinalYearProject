import { ITestCase, NewTestCase } from './test-case.model';

export const sampleWithRequiredData: ITestCase = {
  id: 16957,
  input: 'wavy',
  expectedOutput: 'low lest',
};

export const sampleWithPartialData: ITestCase = {
  id: 14991,
  input: 'at lone furthermore',
  expectedOutput: 'circle',
  description: 'exhausted serialize',
};

export const sampleWithFullData: ITestCase = {
  id: 11816,
  input: 'wherever atop stark',
  expectedOutput: 'yesterday oh',
  description: 'forecast',
};

export const sampleWithNewData: NewTestCase = {
  input: 'birth barring',
  expectedOutput: 'fondly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
