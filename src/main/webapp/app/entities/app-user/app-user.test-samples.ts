import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 21991,
  name: 'although yahoo garage',
  email: 'Rogelio.Daugherty@hotmail.com',
  password: 'opera informal lest',
  roles: 'duh faraway',
};

export const sampleWithPartialData: IAppUser = {
  id: 1230,
  name: 'bah',
  email: 'Tommie.Senger25@hotmail.com',
  password: 'down',
  roles: 'outside promptly',
};

export const sampleWithFullData: IAppUser = {
  id: 19451,
  name: 'which',
  email: 'Karelle.Padberg@yahoo.com',
  password: 'near thoughtfully',
  roles: 'phew into',
  points: 8690,
};

export const sampleWithNewData: NewAppUser = {
  name: 'space pillory',
  email: 'Justice40@gmail.com',
  password: 'far-flung given',
  roles: 'plus',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
