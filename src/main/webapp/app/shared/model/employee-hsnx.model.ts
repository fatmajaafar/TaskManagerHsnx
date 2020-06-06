import { Moment } from 'moment';

export interface IEmployeeHsnx {
  id?: number;
  branchName?: string;
  branchPhone?: string;
  branchFax?: string;
  branchAddress?: string;
  branchEmail?: string;
  branchHiredate?: Moment;
}

export class EmployeeHsnx implements IEmployeeHsnx {
  constructor(
    public id?: number,
    public branchName?: string,
    public branchPhone?: string,
    public branchFax?: string,
    public branchAddress?: string,
    public branchEmail?: string,
    public branchHiredate?: Moment
  ) {}
}
