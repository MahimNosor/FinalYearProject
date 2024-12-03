import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'finalYearProjectApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'app-user',
    data: { pageTitle: 'finalYearProjectApp.appUser.home.title' },
    loadChildren: () => import('./app-user/app-user.routes'),
  },
  {
    path: 'user-question',
    data: { pageTitle: 'finalYearProjectApp.userQuestion.home.title' },
    loadChildren: () => import('./user-question/user-question.routes'),
  },
  {
    path: 'student-class',
    data: { pageTitle: 'finalYearProjectApp.studentClass.home.title' },
    loadChildren: () => import('./student-class/student-class.routes'),
  },
  {
    path: 'question',
    data: { pageTitle: 'finalYearProjectApp.question.home.title' },
    loadChildren: () => import('./question/question.routes'),
  },
  {
    path: 'test-case',
    data: { pageTitle: 'finalYearProjectApp.testCase.home.title' },
    loadChildren: () => import('./test-case/test-case.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
