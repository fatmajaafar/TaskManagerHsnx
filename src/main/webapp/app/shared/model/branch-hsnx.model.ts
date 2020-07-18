export interface IBranchHsnx {
  id?: number;
  branchName?: string;
  branchPhone?: string;
  branchFax?: string;
  branchAddress?: string;
  branchEmail?: string;
  branchBanner?: string;
  branchDefaultExercice?: number;
}

export class BranchHsnx implements IBranchHsnx {
  constructor(
    public id?: number,
    public branchName?: string,
    public branchPhone?: string,
    public branchFax?: string,
    public branchAddress?: string,
    public branchEmail?: string,
    public branchBanner?: string,
    public branchDefaultExercice?: number
  ) {}
}
