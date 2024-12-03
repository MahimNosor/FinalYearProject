import { ITestCase, NewTestCase } from './test-case.model';

export const sampleWithRequiredData: ITestCase = {
  id: 3816,
  input: 'yuck mmm',
  expectedOutput: 'consequently beautifully',
};

export const sampleWithPartialData: ITestCase = {
  id: 6539,
  input: 'federate exonerate',
  expectedOutput: 'aircraft yum inasmuch',
  description: 'gee',
};

export const sampleWithFullData: ITestCase = {
  id: 24877,
  input: 'poorly',
  expectedOutput: 'towards cop kindheartedly',
  description: 'commonly',
};

export const sampleWithNewData: NewTestCase = {
  input: 'readies without',
  expectedOutput: 'because black inasmuch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
