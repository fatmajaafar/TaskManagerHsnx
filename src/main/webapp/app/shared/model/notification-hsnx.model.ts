export interface INotificationHsnx {
  id?: number;
  message?: string;
  handled?: boolean;
  notifFrom?: string;
  notifto?: string;
}

export class NotificationHsnx implements INotificationHsnx {
  constructor(public id?: number, public message?: string, public handled?: boolean, public notifFrom?: string, public notifto?: string) {
    this.handled = this.handled || false;
  }
}
