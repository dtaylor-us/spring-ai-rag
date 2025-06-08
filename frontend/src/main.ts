import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { RagChatComponent } from './app/rag-chat/rag-chat';

bootstrapApplication(RagChatComponent, {
  providers: [
    provideHttpClient()
  ]
}).catch(err => console.error(err));
