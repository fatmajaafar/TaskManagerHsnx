export interface IDepartmentHsnx {
  id?: number;
  deptName?: string;
  deptNote?: string;
}

export class DepartmentHsnx implements IDepartmentHsnx {
  constructor(public id?: number, public deptName?: string, public deptNote?: string) {}
}
