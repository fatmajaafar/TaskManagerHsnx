export interface ITaskmanagementHsnx {
  id?: number;
  tasktitle?: string;
  taskdescription?: string;
  taskstatus?: number;
  taskpriority?: number;
}

export class TaskmanagementHsnx implements ITaskmanagementHsnx {
  constructor(
    public id?: number,
    public tasktitle?: string,
    public taskdescription?: string,
    public taskstatus?: number,
    public taskpriority?: number
  ) {}
}
