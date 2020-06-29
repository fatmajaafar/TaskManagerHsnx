import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { TaskManagerHsnxCoreModule } from 'app/core/core.module';
import { TaskManagerHsnxAppRoutingModule } from './app-routing.module';
import { TaskManagerHsnxHomeModule } from './home/home.module';
import { TaskManagerHsnxEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTabsModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
@NgModule({
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    TaskManagerHsnxSharedModule,
    TaskManagerHsnxCoreModule,
    TaskManagerHsnxHomeModule,

    // jhipster-needle-angular-add-module JHipster will add new module here
    TaskManagerHsnxEntityModule,
    TaskManagerHsnxAppRoutingModule,
    MatTabsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class TaskManagerHsnxAppModule {}
