import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { QuestionDifficulty } from 'app/entities/enumerations/question-difficulty.model';
import { ProgrammingLanguage } from 'app/entities/enumerations/programming-language.model';

export interface IQuestion {
  id: number;
  title?: string | null;
  difficulty?: keyof typeof QuestionDifficulty | null;
  description?: string | null;
  language?: keyof typeof ProgrammingLanguage | null;
  testCases?: string | null;
  maxScore?: number | null;
  studentClass?: Pick<IStudentClass, 'id'> | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
