import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserQuestion } from '../user-question.model';
import { UserQuestionService } from '../service/user-question.service';

const userQuestionResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserQuestion> => {
  const id = route.params.id;
  if (id) {
    return inject(UserQuestionService)
      .find(id)
      .pipe(
        mergeMap((userQuestion: HttpResponse<IUserQuestion>) => {
          if (userQuestion.body) {
            return of(userQuestion.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userQuestionResolve;
