import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IStudentClass {
  id: number;
  className?: string | null;
  users?: Pick<IAppUser, 'id'>[] | null;
}

export type NewStudentClass = Omit<IStudentClass, 'id'> & { id: null };
