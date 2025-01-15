import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-assignment-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './assignment-details.component.html',
  styleUrls: ['./assignment-details.component.scss'], // Fixed typo
})
export class AssignmentDetailsComponent implements OnInit {
  assignment: IAssignment | null = null; // Added missing assignment property

  constructor(
    private route: ActivatedRoute,
    private assignmentService: AssignmentService // Injected AssignmentService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.assignmentService.getAssignmentById(id).subscribe({
      next: (data) => {
        this.assignment = data; // Assign data to assignment property
      },
      error: (err) => {
        console.error('Error fetching assignment details:', err); // Handle errors
      },
    });
  }
}
