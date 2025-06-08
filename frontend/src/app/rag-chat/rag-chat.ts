import { Component, ViewChild, ElementRef, signal, AfterViewChecked, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RagService } from '../rag';

const STORAGE_KEY = 'SPRING_AI_RAG_CHAT_HISTORY';

function loadHistory(): ChatMessage[] {
  const raw = localStorage.getItem(STORAGE_KEY);
  return raw ? JSON.parse(raw) : [];
}

function saveHistory(messages: ChatMessage[]): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(messages));
}

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
  messages = signal<ChatMessage[]>(loadHistory());
  uploadMessage = signal('');

  selectedFile: File | null = null;
  uploading = false;
  uploadProgress = 0;

  @ViewChild('chatWindow') private chatWindow!: ElementRef;

  constructor(private ragService: RagService) {
    effect(() => {
      saveHistory(this.messages());
    });
  }

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

  clearHistory() {
    this.messages.set([]);
    localStorage.removeItem(STORAGE_KEY);
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
        this.selectedFile = null;
        window.alert('Upload complete: PDF has been indexed successfully.');
      },
      error: err => {
        this.uploading = false;
        window.alert('Upload failed: ' + err.message);
      }
    });
  }


  ngAfterViewChecked() {
    if (this.chatWindow) {
      this.chatWindow.nativeElement.scrollTop = this.chatWindow.nativeElement.scrollHeight;
    }
  }
}
