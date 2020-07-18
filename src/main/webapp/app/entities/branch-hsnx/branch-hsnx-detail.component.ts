import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBranchHsnx } from 'app/shared/model/branch-hsnx.model';

@Component({
  selector: '-branch-hsnx-detail',
  templateUrl: './branch-hsnx-detail.component.html'
})
export class BranchHsnxDetailComponent implements OnInit {
  branch: IBranchHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ branch }) => (this.branch = branch));
  }

  previousState(): void {
    window.history.back();
  }
}
