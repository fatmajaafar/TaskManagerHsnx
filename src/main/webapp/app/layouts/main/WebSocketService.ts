import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Notificationglobal } from 'app/shared/model/notification.model';
const SockJs = require('sockjs-client');
const Stomp = require('stompjs');

@Injectable()
export class WebSocketService {
  resourceUrl = 'https://erp.hosinox.net:8080/api/notification/notify';
  constructor(protected http: HttpClient) {}

  public connect() {
    const socket = new SockJs(`https://erp.hosinox.net:8080/socket`);

    const stompClient = Stomp.over(socket);

    return stompClient;
  }

  createTask(notif: string, idbranch: number, status: number): Observable<Notificationglobal> {
    return this.http.get<Notificationglobal>(`${this.resourceUrl}/CreateTask/${notif},${idbranch},${status}`);
  }
}
