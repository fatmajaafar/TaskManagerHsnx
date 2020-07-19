import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
  ElasticsearchReindexComponent,
  ElasticsearchReindexModalComponent,
  ElasticsearchReindexService,
  elasticsearchReindexRoute
} from './';
import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';

const ADMIN_ROUTES = [elasticsearchReindexRoute];

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forRoot(ADMIN_ROUTES, { useHash: true })],
  declarations: [ElasticsearchReindexComponent, ElasticsearchReindexModalComponent],
  entryComponents: [ElasticsearchReindexModalComponent],
  providers: [ElasticsearchReindexService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TaskManagerHsnxElasticsearchReindexModule {}
