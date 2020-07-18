import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBranchHsnx } from 'app/shared/model/branch-hsnx.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BranchHsnxService } from './branch-hsnx.service';
import { BranchHsnxDeleteDialogComponent } from './branch-hsnx-delete-dialog.component';

@Component({
  selector: 'jhi-branch-hsnx',
  templateUrl: './branch-hsnx.component.html'
})
export class BranchHsnxComponent implements OnInit, OnDestroy {
  branches?: IBranchHsnx[];
  eventSubscriber?: Subscription;
  currentSearch: string;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected branchService: BranchHsnxService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    if (this.currentSearch) {
      this.branchService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<IBranchHsnx[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
          () => this.onError()
        );
      return;
    }

    this.branchService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IBranchHsnx[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInBranches();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBranchHsnx): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBranches(): void {
    this.eventSubscriber = this.eventManager.subscribe('branchListModification', () => this.loadPage());
  }

  delete(branch: IBranchHsnx): void {
    const modalRef = this.modalService.open(BranchHsnxDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.branch = branch;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IBranchHsnx[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    this.router.navigate(['/branch-hsnx'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.branches = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
