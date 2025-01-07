import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import { TeacherDashboardComponent } from './teacher-dashboard/teacher-dashboard.component';
import { QuestionManagementComponent } from './entities/question-management/question-management.component';
import { ClassManagementComponent } from './entities/class-management/class-management.component';
import { StudentClassUpdateComponent } from './entities/student-class/update/student-class-update.component';
import { AssignmentManagementComponent } from './teacher/assignment-management/assignment-management.component';
import { AssignmentUpdateComponent } from './entities/assignment/update/assignment-update.component';
const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  {
    path: 'teacher-dashboard',
    component: TeacherDashboardComponent,
    canActivate: [UserRouteAccessService],
    data: {
      authorities: ['ROLE_TEACHER'],
    },
  },
  {
    path: 'class-management',
    component: ClassManagementComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_TEACHER'] },
  },
  {
    path: 'teacher/class-management/new',
    component: StudentClassUpdateComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_TEACHER'] },
  },
  {
    path: 'teacher/assignment-management',
    component: AssignmentManagementComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_TEACHER'] },
  },
  {
    path: 'teacher/assignment-management/new',
    component: AssignmentUpdateComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_TEACHER'] },
  },
  ...errorRoute,
];
export default routes;
