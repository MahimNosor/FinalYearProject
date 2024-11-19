import { ITestCase, NewTestCase } from './test-case.model';

export const sampleWithRequiredData: ITestCase = {
  id: 25348,
  input: 'a',
  expectedOutput: 'cripple than',
};

export const sampleWithPartialData: ITestCase = {
  id: 2591,
  input: 'over geez fondly',
  expectedOutput: 'deduction suspiciously',
};

export const sampleWithFullData: ITestCase = {
  id: 27588,
  input: 'place',
  expectedOutput: 'ah front truly',
  description: 'smug monumental',
};

export const sampleWithNewData: NewTestCase = {
  input: 'shadowy why nifty',
  expectedOutput: 'hm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
