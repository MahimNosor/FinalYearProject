import { IStudentClass, NewStudentClass } from './student-class.model';

export const sampleWithRequiredData: IStudentClass = {
  id: 27535,
  className: 'apropos',
};

export const sampleWithPartialData: IStudentClass = {
  id: 24094,
  className: 'yet',
};

export const sampleWithFullData: IStudentClass = {
  id: 9887,
  className: 'instead embed',
};

export const sampleWithNewData: NewStudentClass = {
  className: 'sans',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
