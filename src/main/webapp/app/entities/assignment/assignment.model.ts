import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { difficulty } from 'app/entities/enumerations/difficulty.model';
import { ProgrammingLanguage } from 'app/entities/enumerations/programming-language.model';

export interface IAssignment {
  id: number;
  title?: string | null;
  difficulty?: keyof typeof difficulty | null;
  description?: string | null;
  language?: keyof typeof ProgrammingLanguage | null;
  testCases?: string | null;
  maxScore?: number | null;
  isPreloaded?: boolean | null;
  appUser?: Pick<IAppUser, 'id' | 'name'> | null;
  studentClasses?: Pick<IStudentClass, 'id' | 'className'>[] | null;
}

export type NewAssignment = Omit<IAssignment, 'id'> & { id: null };
