import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import * as ace from 'ace-builds';

@Component({
  selector: 'jhi-assignment-details',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './assignment-details.component.html',
  styleUrls: ['./assignment-details.component.scss'],
})
export class AssignmentDetailsComponent implements OnInit {
  assignment: IAssignment | null = null; // Stores assignment details
  editor: ace.Ace.Editor | null = null; // Ace Editor instance
  selectedLanguage: string = 'javascript'; // Default language
  submissionResult: any = null;

  constructor(
    private route: ActivatedRoute,
    private assignmentService: AssignmentService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    // Fetch assignment details
    const id = this.route.snapshot.params['id'];
    this.assignmentService.getAssignmentById(id).subscribe({
      next: (data) => {
        this.assignment = data; // Assign data to the assignment property
      },
      error: (err) => {
        console.error('Error fetching assignment details:', err);
      },
    });

    // Initialize Ace Editor
    this.initializeEditor();
  }

  initializeEditor(): void {
    const editorElement = document.getElementById('editor-container');
    if (editorElement) {
      this.editor = ace.edit(editorElement, {
        mode: `ace/mode/${this.getAceMode(this.selectedLanguage)}`, // Set language mode
        theme: 'ace/theme/monokai', // Editor theme
        value: '// Start coding here',
        fontSize: 14,
        showPrintMargin: false,
        showGutter: true,
        tabSize: 2,
        useWorker: false,
      });
  
      // Set editor styles for better visibility
      this.editor.setOptions({
        highlightActiveLine: true, // Highlight active line
        highlightSelectedWord: true, // Highlight selected word
        fontSize: '14pt',
        fontFamily: 'monospace',
      });
  
      // Apply additional styles
      this.editor.container.style.color = '#ffffff'; // Set text color to white
      this.editor.container.style.backgroundColor = '#1e1e1e'; // Ensure the background matches the theme
    }
  }
  

  // Update Ace mode when language changes
  updateLanguage(): void {
    if (this.editor) {
      this.editor.session.setMode(`ace/mode/${this.getAceMode(this.selectedLanguage)}`);
    }
  }

  // Map selected language to Ace mode
  getAceMode(language: string): string {
    const languageMap: { [key: string]: string } = {
      javascript: 'javascript',
      python: 'python',
      java: 'java',
      csharp: 'csharp',
      c: 'c_cpp',
      cpp: 'c_cpp',
    };
    return languageMap[language.toLowerCase()] || 'text';
  }

  submitCode(): void {
    if (!this.editor) {
      console.error('Editor is not initialized');
      return;
    }

    const code = this.editor.getValue(); // Get code from the editor
    const language = this.assignment?.language?.toLowerCase() || 'javascript'; // Get language
    const payload = {
      source_code: code,
      language_id: this.mapLanguageToJudge0(language),
      stdin: '', // Input if required
    };

    this.http.post('https://api.judge0.com/submissions', payload).subscribe({
      next: (response) => {
        this.submissionResult = response; // Display response in the UI
      },
      error: (err) => {
        console.error('Error submitting code:', err);
      },
    });
  }

  // Map languages to Judge0 language IDs
  mapLanguageToJudge0(language: string): number {
    const languageMap: { [key: string]: number } = {
      javascript: 63,
      python: 71,
      java: 62,
      c: 50,
      cpp: 54,
      csharp: 51,
    };
    return languageMap[language] || 63; // Default to JavaScript
  }
}
