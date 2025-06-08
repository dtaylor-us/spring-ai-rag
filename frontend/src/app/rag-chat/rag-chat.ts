import { Component, ViewChild, ElementRef, signal, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RagService } from '../rag';

type Sender = 'user' | 'bot';
interface ChatMessage {
  sender: Sender;
  text: string;
}

@Component({
  selector: 'app-rag-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rag-chat.html',
  styleUrls: ['./rag-chat.css']
})
export class RagChatComponent implements AfterViewChecked {
  question = signal('');
  messages = signal<ChatMessage[]>([]);

  selectedFile: File | null = null;
  uploading = false;
  uploadProgress = 0;

  @ViewChild('chatWindow') private chatWindow!: ElementRef;

  constructor(private ragService: RagService) {}

  sendMessage() {
    const userInput = this.question().trim();
    if (!userInput) return;

    this.messages.update(msgs => [...msgs, { sender: 'user', text: userInput }]);
    this.question.set('');

    const typingMessage: ChatMessage = { sender: 'bot', text: '...' };
    this.messages.update(msgs => [...msgs, typingMessage]);

    this.ragService.askQuestion(userInput).subscribe({
      next: res => {
        this.messages.update(msgs =>
          msgs.map(m => (m === typingMessage ? { sender: 'bot', text: res } : m))
        );
      },
      error: () => {
        this.messages.update(msgs =>
          msgs.map(m => (m === typingMessage ? { sender: 'bot', text: '⚠️ Error from AI.' } : m))
        );
      }
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onUpload() {
    if (!this.selectedFile) return;
    this.uploading = true;
    this.ragService.uploadFile(this.selectedFile).subscribe({
      next: progress => (this.uploadProgress = progress),
      complete: () => {
        this.uploading = false;
        this.uploadProgress = 0;
        alert('Upload complete!');
        this.selectedFile = null;
      },
      error: err => {
        this.uploading = false;
        alert('Upload failed: ' + err.message);
      }
    });
  }

  ngAfterViewChecked() {
    if (this.chatWindow) {
      this.chatWindow.nativeElement.scrollTop = this.chatWindow.nativeElement.scrollHeight;
    }
  }
}
