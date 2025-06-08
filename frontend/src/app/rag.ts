import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpEventType } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RagService {
  private baseUrl = 'http://localhost:8080/api/v1/rag';

  constructor(private http: HttpClient) {}

  askQuestion(question: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/ask`, { question }, { responseType: 'text' });
  }

  uploadFile(file: File): Observable<number> {
  const formData = new FormData();
  formData.append('file', file);

  return this.http.post<{ message: string }>(
    `${this.baseUrl}/upload`,
    formData,
    {
      observe: 'events',
      reportProgress: true,
      responseType: 'json'
    }
  ).pipe(
    map(event => {
      if (event.type === HttpEventType.UploadProgress && event.total) {
        return Math.round((event.loaded / event.total) * 100);
      } else if (event.type === HttpEventType.Response) {
        console.log('✅ Upload response:', event.body?.message);
        return 100;
      } else {
        return 0;
      }
    })
  );
}

}
