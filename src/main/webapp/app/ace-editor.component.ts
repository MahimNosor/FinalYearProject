import { Component, ElementRef, Input, Output, EventEmitter, OnInit } from '@angular/core';
import * as ace from 'ace-builds';

ace.config.set("basePath", "node_modules/ace-builds/src-noconflict");

@Component({
  selector: 'app-ace-editor',
  template: `
    <div id="editor" style="height: 400px; width: 100%;"></div>
    <div class="controls">
      <label for="language">Language:</label>
      <select id="language" [(ngModel)]="language" (change)="updateLanguage()">
        <option value="python">Python</option>
        <option value="javascript">JavaScript</option>
        <option value="java">Java</option>
        <option value="cpp">C++</option>
        <option value="c">C</option>
      </select>
      <button (click)="submitCode()" class="btn-primary">Submit Code</button>
    </div>
  `,
  styleUrls: ['./ace-editor.component.scss'],
})
export class AceEditorComponent implements OnInit {
  @Input() initialCode: string = ''; // Default code in the editor
  @Output() codeSubmit = new EventEmitter<{ code: string; language: string }>();

  private editorInstance: ace.Ace.Editor | null = null;
  language: string = 'python'; // Default language

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    const editor = ace.edit(this.el.nativeElement.querySelector('#editor') as HTMLElement);
    editor.setTheme('ace/theme/monokai');
    editor.session.setMode('ace/mode/python');
    editor.setValue(this.initialCode);
    this.editorInstance = editor;
  }

  updateLanguage(): void {
    if (this.editorInstance) {
      this.editorInstance.session.setMode(`ace/mode/${this.language}`);
    }
  }

  submitCode(): void {
    if (this.editorInstance) {
      const code = this.editorInstance.getValue();
      this.codeSubmit.emit({ code, language: this.language });
    }
  }
}
