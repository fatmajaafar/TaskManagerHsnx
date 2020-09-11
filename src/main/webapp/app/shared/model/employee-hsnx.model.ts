import { Moment } from 'moment';

export interface IEmployeeHsnx {
  id?: number;
  employeename?: string;
  employeelastname?: string;
  employeephone?: string;
  employeefax?: string;
  employeeaddress?: string;
  employeeemail?: string;
  employeehiredate?: Moment;
  tblJobJobTitle?: string;
  tblJobId?: number;
  tblDepartmentDeptName?: string;
  tblDepartmentId?: number;
  tblBranchBranchName?: string;
  tblBranchId?: number;
}

export class EmployeeHsnx implements IEmployeeHsnx {
  constructor(
    public id?: number,
    public employeename?: string,
    public employeelastname?: string,
    public employeephone?: string,
    public employeefax?: string,
    public employeeaddress?: string,
    public employeeemail?: string,
    public employeehiredate?: Moment,
    public tblJobJobTitle?: string,
    public tblJobId?: number,
    public tblDepartmentDeptName?: string,
    public tblDepartmentId?: number,
    public tblBranchBranchName?: string,
    public tblBranchId?: number
  ) {}
}
