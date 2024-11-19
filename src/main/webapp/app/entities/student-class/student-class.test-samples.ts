import { IStudentClass, NewStudentClass } from './student-class.model';

export const sampleWithRequiredData: IStudentClass = {
  id: 24472,
  className: 'depart',
};

export const sampleWithPartialData: IStudentClass = {
  id: 7962,
  className: 'ponder dull',
};

export const sampleWithFullData: IStudentClass = {
  id: 13149,
  className: 'amid',
};

export const sampleWithNewData: NewStudentClass = {
  className: 'oof jazz',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
