import { Moment } from 'moment';

export interface IEventHsnx {
  id?: number;
  Description?: string;
  starttime?: Moment;
  startdate?: Moment;
  endtime?: Moment;
  enddate?: Moment;
}

export class EventHsnx implements IEventHsnx {
  constructor(
    public id?: number,
    public Description?: string,
    public starttime?: Moment,
    public startdate?: Moment,
    public endtime?: Moment,
    public enddate?: Moment
  ) {}
}
