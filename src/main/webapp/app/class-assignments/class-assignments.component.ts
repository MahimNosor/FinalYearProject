import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-class-assignments',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './class-assignments.component.html',
  styleUrl: './class-assignments.component.scss'
})
export class ClassAssignmentsComponent {
  classId: number | null = null; // To store the current class ID
  assignments: IAssignment[] = []; // Array to store the assignments

  constructor(private route: ActivatedRoute, private assignmentService: AssignmentService) {}

  ngOnInit(): void {
    // Extract the classId from the route
    this.route.params.subscribe(params => {
      this.classId = +params['id'];
      if (this.classId) {
        this.loadAssignments(this.classId);
      }
    });
  }

  // Fetch assignments from the service
  loadAssignments(classId: number): void {
    this.assignmentService.getAssignmentsByClass(classId).subscribe(data => {
      this.assignments = data;
    });
  }
}
