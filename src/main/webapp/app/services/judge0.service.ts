import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Judge0Service {
  private readonly baseUrl = 'http://localhost:2358'; 

  constructor(private http: HttpClient) {}

  // Submit code to Judge0
  submitCode(sourceCode: string, languageId: number): Observable<any> {
    const payload = {
      source_code: sourceCode,
      language_id: languageId,
      stdin: '', // Optional: Input for the program, if any
    };

    return this.http.post(`${this.baseUrl}/submissions?base64_encoded=false&wait=true`, payload);
  }

  submitCodeToJudge0(code: string, languageId: number): void {
    const submissionPayload = {
      source_code: code,
      language_id: languageId,
      stdin: 'Test input if required',
    };
  
    this.http.post(`${this.baseUrl}/submissions?base64_encoded=false`, submissionPayload).subscribe({
      next: (response: any) => {
        console.log('Submission response:', response);
      },
      error: (err) => {
        console.error('Error submitting code to Judge0:', err);
      },
    });
  }
  
}
