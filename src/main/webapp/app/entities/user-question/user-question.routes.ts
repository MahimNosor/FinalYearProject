import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserQuestionResolve from './route/user-question-routing-resolve.service';

const userQuestionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-question.component').then(m => m.UserQuestionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-question-detail.component').then(m => m.UserQuestionDetailComponent),
    resolve: {
      userQuestion: UserQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-question-update.component').then(m => m.UserQuestionUpdateComponent),
    resolve: {
      userQuestion: UserQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-question-update.component').then(m => m.UserQuestionUpdateComponent),
    resolve: {
      userQuestion: UserQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userQuestionRoute;
