export interface ITaskScheduler {
  id?: number;
  tasktitle?: string;
  taskdescription?: string;
  taskstatus?: number;
  taskpriority?: number;
}

export class TaskScheduler implements ITaskScheduler {
  constructor(
    public id?: number,
    public tasktitle?: string,
    public taskdescription?: string,
    public taskstatus?: number,
    public taskpriority?: number
  ) {}
}
