import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AssignmentResolve from './route/assignment-routing-resolve.service';

const assignmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/assignment.component').then(m => m.AssignmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/assignment-detail.component').then(m => m.AssignmentDetailComponent),
    resolve: {
      assignment: AssignmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/assignment-update.component').then(m => m.AssignmentUpdateComponent),
    resolve: {
      assignment: AssignmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/assignment-update.component').then(m => m.AssignmentUpdateComponent),
    resolve: {
      assignment: AssignmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assignmentRoute;
