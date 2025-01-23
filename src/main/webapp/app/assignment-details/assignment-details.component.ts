import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentService } from 'app/entities/assignment/service/assignment.service';
import { IAssignment } from 'app/entities/assignment/assignment.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import * as ace from 'ace-builds';
import { Judge0Service } from 'app/services/judge0.service';

// Import remains unchanged

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
  baseUrl: string = 'https://judge0-ce.p.rapidapi.com';
  testCase: { input: string; expectedOutput: string } | null = null;
  formattedDescription: string = '';



  constructor(
    private route: ActivatedRoute,
    private assignmentService: AssignmentService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.assignmentService.getAssignmentById(id).subscribe({
      next: (data) => {
        this.assignment = data;
        this.formattedDescription = this.assignment.description
        ? this.assignment.description.replace(/\n/g, '<br>') // Replace newlines with <br>
        : '';

        // Ensure no redundant "Description:" text in the formatted content
        if (this.formattedDescription.startsWith('Description:')) {
          this.formattedDescription = this.formattedDescription.replace('Description:', '').trim();
        }
        try {
          if (this.assignment.testCases) {
            console.log('Raw testCases:', this.assignment.testCases);
            const parsedTestCase = JSON.parse(this.assignment.testCases);
        
            // Map backend keys to expected frontend keys
            if (parsedTestCase.input && parsedTestCase.expected_output) {
              this.testCase = {
                input: parsedTestCase.input,
                expectedOutput: parsedTestCase.expected_output, // Map to expectedOutput
              };
              console.log('Mapped Test Case:', this.testCase); // Log mapped test case
            } else {
              throw new Error('Parsed testCase does not have the required keys (input or expected_output)');
            }
          } else {
            throw new Error('testCases is null or undefined');
          }
        } catch (err) {
          console.error('Error parsing test cases:', err);
          this.testCase = null; // Fallback if parsing fails
        }
      },
      error: (err) => {
        console.error('Error fetching assignment:', err);
      },
    });
    this.initializeEditor();
  }
  

 

  initializeEditor(): void {
    // Ensure basePath matches assets structure
    ace.config.set("basePath", "http://localhost:9000/assets/ace");

    console.log("Ace basePath:", ace.config.get("basePath"));
  
    const editorElement = document.getElementById('editor-container');
    if (editorElement) {
      this.editor = ace.edit(editorElement, {
         // Set language mode
         // Editor theme
        value: '// Start coding here',
        fontSize: 14,
        showPrintMargin: false,
        tabSize: 2,
      });
      this.editor.setOptions({
        highlightActiveLine: true,
        highlightSelectedWord: true,
        fontSize: '14pt',
        fontFamily: 'monospace',
      });
      this.editor.container.style.color = '#ffffff'; // Set text color to white
      this.editor.container.style.backgroundColor = '#1e1e1e'; // Dark background for contrast
    }
  }
  
/**
  // Map selected language to Ace mode
  getAceMode(language: string): string {
    const languageMap: { [key: string]: string } = {
      javascript: 'javascript',
      python: 'python',
      java: 'java',
      csharp: 'csharp',
      c: 'c',
      cpp: 'c_cpp',
    };
    return languageMap[language.toLowerCase()] || 'text';
  }

  // Update Ace mode when language changes
    updateLanguage(): void {
      if (this.editor && this.editor.session) {
        const modePath = `ace/mode/${this.getAceMode(this.selectedLanguage)}`;
        this.editor.session.setMode(modePath);
        console.log(`Editor mode updated to: ${modePath}`);
      } else {
        console.error('Editor is not initialized or session is missing.');
      }
    }
  */

    submitCodeToJudge0(): void {
      if (!this.editor?.getValue() || !this.testCase || !this.testCase.input) {
        console.error('No code to submit or test case is invalid');
        return;
      }
    
      const sourceCode = this.editor?.getValue()
 // Get the raw editor content without wrapping quotes
      const languageId = this.mapLanguageToJudge0(this.selectedLanguage || 'javascript');
    
      console.log('Source Code:', sourceCode);
      console.log('Language ID:', languageId);
      console.log('Test Case Input:', this.testCase?.input);
      console.log('Test Case Expected Output:', this.testCase?.expectedOutput);
    
      const payload = {
        source_code: sourceCode, // Send raw code
        language_id: languageId,
        stdin: this.testCase.input,
      };
    
      const headers = {
        'Content-Type': 'application/json',
        'X-RapidAPI-Key': '2acf077ff4msh1cb7090d7e40a86p133c1fjsn59933ab13673',
        'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com',
      };
    
      this.http.post(`${this.baseUrl}/submissions`, payload, { headers }).subscribe({
        next: (response: any) => {
          console.log('Submission sent, token:', response.token);
          setTimeout(() => this.getSubmissionResult(response.token), 2000);
        },
        error: (err) => {
          console.error('Error submitting code:', err);
        },
      });
    }
    
    getSubmissionResult(token: string): void {
      const headers = {
        'Content-Type': 'application/json',
        'X-RapidAPI-Key': '2acf077ff4msh1cb7090d7e40a86p133c1fjsn59933ab13673',
        'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com',
      };
    
      this.http.get(`${this.baseUrl}/submissions/${token}`, { headers }).subscribe({
        next: (result: any) => {
          if (!this.testCase || !this.testCase.expectedOutput) {
            console.error('Test case or expectedOutput is null. Cannot proceed.');
            return;
          }
    
          const actualOutput = result.stdout?.trim();
          const expectedOutput = this.testCase.expectedOutput.trim();
    
          if (actualOutput === expectedOutput) {
            console.log('Test case passed!');
            // Proceed to update user points (next step)
          } else {
            console.log('Test case failed.');
          }
    
          this.submissionResult = {
            passed: actualOutput === expectedOutput,
            actualOutput,
            expectedOutput,
          };
        },
        error: (err) => {
          console.error('Error fetching submission result:', err);
        },
      });
    }
    
    
    

    mapLanguageToJudge0(language: string): number {
      const languageMap: { [key: string]: number } = {
        javascript: 63,
        python: 71,
        java: 62,
        c: 50,
        cpp: 54,
        csharp: 51,
      };
      return languageMap[language.toLowerCase()] || 63;
    }
    
  
}

