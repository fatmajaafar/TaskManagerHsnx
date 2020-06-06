import { Moment } from 'moment';

export interface ITaskHsnx {
  id?: number;
  tasktitle?: string;
  taskdescription?: string;
  dateStart?: Moment;
  timeStart?: Moment;
  dateEnd?: Moment;
  timeEnd?: Moment;
  taskstatus?: number;
  taskpriority?: number;
  DueDate?: Moment;
}

export class TaskHsnx implements ITaskHsnx {
  constructor(
    public id?: number,
    public tasktitle?: string,
    public taskdescription?: string,
    public dateStart?: Moment,
    public timeStart?: Moment,
    public dateEnd?: Moment,
    public timeEnd?: Moment,
    public taskstatus?: number,
    public taskpriority?: number,
    public DueDate?: Moment
  ) {}
}
