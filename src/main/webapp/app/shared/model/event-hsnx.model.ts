import { Moment } from 'moment';

export interface IEventHsnx {
  id?: number;
  eventdescription?: string;
  starttime?: Moment;
  startdate?: Moment;
  endtime?: Moment;
  enddate?: Moment;
}

export class EventHsnx implements IEventHsnx {
  constructor(
    public id?: number,
    public eventdescription?: string,
    public starttime?: Moment,
    public startdate?: Moment,
    public endtime?: Moment,
    public enddate?: Moment
  ) {}
}
