import { IStudentClass } from 'app/entities/student-class/student-class.model';

export interface IAppUser {
  id: number;
  name?: string | null;
  email?: string | null;
  password?: string | null;
  roles?: string | null;
  points?: number | null;
  classes?: Pick<IStudentClass, 'id'>[] | null;
  userId?: number;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
