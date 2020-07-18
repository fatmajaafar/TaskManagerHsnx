export interface IJobHsnx {
  id?: number;
  jobTitle?: string;
}

export class JobHsnx implements IJobHsnx {
  constructor(public id?: number, public jobTitle?: string) {}
}
