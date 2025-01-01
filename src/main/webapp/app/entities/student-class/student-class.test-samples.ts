import { IStudentClass, NewStudentClass } from './student-class.model';

export const sampleWithRequiredData: IStudentClass = {
  id: 5173,
  className: 'fowl',
};

export const sampleWithPartialData: IStudentClass = {
  id: 26873,
  className: 'regular',
};

export const sampleWithFullData: IStudentClass = {
  id: 7211,
  className: 'near why',
};

export const sampleWithNewData: NewStudentClass = {
  className: 'reproachfully instead',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
