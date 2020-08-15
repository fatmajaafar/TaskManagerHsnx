import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { TaskKanbanComponent } from './task-kanban/task-kanban.component';
import { TaskHsnx } from 'app/shared/model/task-hsnx.model';

export const TaskKanbanRoute: Routes = [
  {
    path: 'task-kanban',
    component: TaskKanbanComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN']
    },
    /* resolve: {
      task: TaskHsnx
    },*/
    canActivate: [UserRouteAccessService]
  }
];
