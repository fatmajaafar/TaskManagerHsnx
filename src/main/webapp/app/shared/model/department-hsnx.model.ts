export interface IDepartmentHsnx {
  id?: number;
  deptName?: string;
  deptNote?: string;
  tblCountryCountryName?: string;
  tblCountryId?: number;
}

export class DepartmentHsnx implements IDepartmentHsnx {
  constructor(
    public id?: number,
    public deptName?: string,
    public deptNote?: string,
    public tblCountryCountryName?: string,
    public tblCountryId?: number
  ) {}
}
