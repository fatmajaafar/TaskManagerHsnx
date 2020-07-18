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
  dueDate?: Moment;
  taskcategory?: string;
  taskstate?: number;
  tblEmployeeEmployeename?: string;
  tblEmployeeId?: number;
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
    public dueDate?: Moment,
    public taskcategory?: string,
    public taskstate?: number,
    public tblEmployeeEmployeename?: string,
    public tblEmployeeId?: number
  ) {}
}
