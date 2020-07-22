import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { FindLanguageFromKeyPipe } from 'app/shared/language/find-language-from-key.pipe';
import { WebSocketService } from './WebSocketService';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html'
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private findLanguageFromKeyPipe: FindLanguageFromKeyPipe,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
    private webSocketService: WebSocketService
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    const DemandNotif = this.webSocketService.connect();
    DemandNotif.connect({}, (frame: any) => {
      DemandNotif.subscribe('/topic/UpdateDemandStatus', (CreateDemandNotif: { body: string }) => {
        this.toastr.info('Name : ' + JSON.parse(CreateDemandNotif.body).demandName, 'New Demand Created', { timeOut: 3000 });
      });
    });

    const TaskNotif = this.webSocketService.connect();
    TaskNotif.connect({}, (frame: any) => {
      TaskNotif.subscribe('/topic/CreateTask', (CreateTaskNotif: { body: string }) => {
        this.toastr.info('Name : ' + JSON.parse(CreateTaskNotif.body).message, 'New Task Created', { timeOut: 3000 });
      });
    });
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();

      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);

      this.updatePageDirection();
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  private updatePageDirection(): void {
    this.renderer.setAttribute(
      document.querySelector('html'),
      'dir',
      this.findLanguageFromKeyPipe.isRTL(this.translateService.currentLang) ? 'rtl' : 'ltr'
    );
  }
}
