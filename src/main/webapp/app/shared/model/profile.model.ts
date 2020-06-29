import { Moment } from 'moment';
export interface IProfile {
  id?: number;
  employeename?: string;
  employeephone?: string;
  employeefax?: string;
  employeeaddress?: string;
  employeeemail?: string;
  employeehiredate?: Moment;
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public employeename?: string,
    public employeephone?: string,
    public employeefax?: string,
    public employeeaddress?: string,
    public employeeemail?: string,
    public employeehiredate?: Moment
  ) {}
}
