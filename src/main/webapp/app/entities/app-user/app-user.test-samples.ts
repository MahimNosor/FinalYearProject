import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 8097,
  name: 'mid joshingly norm',
  email: 'Marley_Wisoky65@yahoo.com',
  password: 'deduction rigidly',
  roles: 'towards',
};

export const sampleWithPartialData: IAppUser = {
  id: 21790,
  name: 'because alongside exalted',
  email: 'Lorenz12@gmail.com',
  password: 'realistic',
  roles: 'of preclude',
  points: 13709,
};

export const sampleWithFullData: IAppUser = {
  id: 31406,
  name: 'pertinent till',
  email: 'Santa.Fadel@hotmail.com',
  password: 'above from',
  roles: 'before unnaturally vice',
  points: 30229,
};

export const sampleWithNewData: NewAppUser = {
  name: 'bludgeon drat',
  email: 'Kody89@hotmail.com',
  password: 'up because stormy',
  roles: 'nudge',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
