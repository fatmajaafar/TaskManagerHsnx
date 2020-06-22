export interface IProfile {
  id?: number;
  tasktitle?: string;
  taskdescription?: string;
  taskstatus?: number;
  taskpriority?: number;
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public tasktitle?: string,
    public taskdescription?: string,
    public taskstatus?: number,
    public taskpriority?: number
  ) {}
}
