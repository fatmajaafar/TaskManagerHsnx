import { Moment } from 'moment';

export interface IEmployeeHsnx {
  id?: number;
  employeename?: string;
  employeephone?: string;
  employeefax?: string;
  employeeaddress?: string;
  employeeemail?: string;
  employeehiredate?: Moment;
}

export class EmployeeHsnx implements IEmployeeHsnx {
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
