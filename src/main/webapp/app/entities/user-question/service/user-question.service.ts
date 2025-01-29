import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IUserQuestion, NewUserQuestion } from '../user-question.model';

export type PartialUpdateUserQuestion = Partial<IUserQuestion> & Pick<IUserQuestion, 'id'>;

type RestOf<T extends IUserQuestion | NewUserQuestion> = Omit<T, 'submissionDate'> & {
  submissionDate?: string | null;
};

export type RestUserQuestion = RestOf<IUserQuestion>;

export type NewRestUserQuestion = RestOf<NewUserQuestion>;

export type PartialUpdateRestUserQuestion = RestOf<PartialUpdateUserQuestion>;

export type EntityResponseType = HttpResponse<IUserQuestion>;
export type EntityArrayResponseType = HttpResponse<IUserQuestion[]>;

@Injectable({ providedIn: 'root' })
export class UserQuestionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-questions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/user-questions/_search');

  create(userQuestion: NewUserQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userQuestion);
    return this.http
      .post<RestUserQuestion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userQuestion: IUserQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userQuestion);
    return this.http
      .put<RestUserQuestion>(`${this.resourceUrl}/${this.getUserQuestionIdentifier(userQuestion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userQuestion: PartialUpdateUserQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userQuestion);
    return this.http
      .patch<RestUserQuestion>(`${this.resourceUrl}/${this.getUserQuestionIdentifier(userQuestion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserQuestion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestUserQuestion[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IUserQuestion[]>()], asapScheduler)),
    );
  }

  getUserQuestionIdentifier(userQuestion: Pick<IUserQuestion, 'id'>): number {
    return userQuestion.id;
  }

  compareUserQuestion(o1: Pick<IUserQuestion, 'id'> | null, o2: Pick<IUserQuestion, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserQuestionIdentifier(o1) === this.getUserQuestionIdentifier(o2) : o1 === o2;
  }

  addUserQuestionToCollectionIfMissing<Type extends Pick<IUserQuestion, 'id'>>(
    userQuestionCollection: Type[],
    ...userQuestionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userQuestions: Type[] = userQuestionsToCheck.filter(isPresent);
    if (userQuestions.length > 0) {
      const userQuestionCollectionIdentifiers = userQuestionCollection.map(userQuestionItem =>
        this.getUserQuestionIdentifier(userQuestionItem),
      );
      const userQuestionsToAdd = userQuestions.filter(userQuestionItem => {
        const userQuestionIdentifier = this.getUserQuestionIdentifier(userQuestionItem);
        if (userQuestionCollectionIdentifiers.includes(userQuestionIdentifier)) {
          return false;
        }
        userQuestionCollectionIdentifiers.push(userQuestionIdentifier);
        return true;
      });
      return [...userQuestionsToAdd, ...userQuestionCollection];
    }
    return userQuestionCollection;
  }

  protected convertDateFromClient<T extends IUserQuestion | NewUserQuestion | PartialUpdateUserQuestion>(userQuestion: T): RestOf<T> {
    return {
      ...userQuestion,
      submissionDate: userQuestion.submissionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserQuestion: RestUserQuestion): IUserQuestion {
    return {
      ...restUserQuestion,
      submissionDate: restUserQuestion.submissionDate ? dayjs(restUserQuestion.submissionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserQuestion>): HttpResponse<IUserQuestion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserQuestion[]>): HttpResponse<IUserQuestion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  getStudentSubmissions(): Observable<IUserQuestion[]> {
    return this.http.get<IUserQuestion[]>(`/api/user-questions`);
}

}
