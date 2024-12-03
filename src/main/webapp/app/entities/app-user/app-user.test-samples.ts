import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 934,
  name: 'eek daughter',
  email: 'Sadie.Goyette55@gmail.com',
  password: 'yuck ick woot',
  roles: 'oof inasmuch boring',
};

export const sampleWithPartialData: IAppUser = {
  id: 30122,
  name: 'capitalise',
  email: 'Christa_Roberts25@gmail.com',
  password: 'junior woot',
  roles: 'discourse lieu astride',
  points: 14323,
};

export const sampleWithFullData: IAppUser = {
  id: 3428,
  name: 'however',
  email: 'Ashton_Gibson@yahoo.com',
  password: 'kindly',
  roles: 'innocently hungry',
  points: 4530,
};

export const sampleWithNewData: NewAppUser = {
  name: 'hippodrome not modulo',
  email: 'Dixie72@gmail.com',
  password: 'forenenst hexagon',
  roles: 'shocked always',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
