import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { AppUserService } from '../../app/entities/app-user/service/app-user.service';
import { StudentClassService } from '../../app/entities/student-class/service/student-class.service';
import { HttpClient } from '@angular/common/http';
import { UserQuestionService } from 'app/entities/user-question/service/user-question.service';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  // Public properties
  account: Account | null = null;
  user: any;
  leaderboard: any[] = [];
  studentClasses: any[] = [];
  isStudent = false;
  isTeacher = false;
  submissions: any[] = [];

  // Private instance fields
  private readonly destroy$ = new Subject<void>();
  private accountService = inject(AccountService);
  private router = inject(Router);
  private http = inject(HttpClient);

  constructor(
    private appUserService: AppUserService,
    private studentClassService: StudentClassService,
    private userQuestionService: UserQuestionService,
    private assignmentService: AssignmentService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;

        if (account) {
          // Determine user roles
          this.isStudent = account.authorities.includes('ROLE_USER');
          this.isTeacher = account.authorities.includes('ROLE_TEACHER');
          this.fetchAccountDetails();
          // Fetch leaderboard data
          this.fetchLeaderboard();

          // Fetch student classes if the user is a student
          this.fetchStudentClasses();
          this.fetchStudentSubmissions();
        }
      });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  fetchLeaderboard(): void {
    this.appUserService.getLeaderboard().subscribe(data => {
      this.leaderboard = data;
    });
  }

  fetchStudentClasses(): void {
    this.studentClassService.getStudentClasses().subscribe(classes => {
      this.studentClasses = classes;
      console.log('Fetched classes:', this.studentClasses); // Debugging
    });
  }    
  

  fetchAccountDetails(): void {
    this.http.get<any>('/api/app-users/account').subscribe(accountData => {
      this.account = accountData;
      this.user = accountData;
    });
  }

  navigateToClass(classId: number): void {
    this.router.navigate(['/class', classId]);
  }

  fetchStudentSubmissions(): void {
    this.userQuestionService.getStudentSubmissions().subscribe(submissions => {
        this.submissions = submissions.reverse(); // Reverse order for latest submissions first

        this.submissions.forEach(submission => {
            if (submission.assignment && submission.assignment.id) {
                this.assignmentService.getAssignmentById(submission.assignment.id).subscribe(
                    assignment => {
                        submission.assignmentTitle = assignment.title || 'Unknown Assignment';
                    },
                    error => {
                        console.error('Error fetching assignment title:', error);
                        submission.assignmentTitle = 'Unknown Assignment';
                    }
                );
            } else {
                submission.assignmentTitle = 'Unknown Assignment';
            }
        });

        console.log('Updated Submissions with Titles:', this.submissions); // Debugging
    });
}



}
