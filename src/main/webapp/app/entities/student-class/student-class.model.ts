import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IAssignment } from 'app/entities/assignment/assignment.model';

export interface IStudentClass {
  id: number;
  className?: string | null;
  users?: Pick<IAppUser, 'id'>[] | null;
  assignments?: Pick<IAssignment, 'id'>[] | null;
}

export type NewStudentClass = Omit<IStudentClass, 'id'> & { id: null };
