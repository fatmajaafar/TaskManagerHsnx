export interface INotificationglobal {
  message: string;
  title: string;
}
export class Notificationglobal implements INotificationglobal {
  constructor(public message: string, public title: string) {}
}
