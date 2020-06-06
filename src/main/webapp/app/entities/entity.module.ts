import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'department-hsnx',
        loadChildren: () => import('./department-hsnx/department-hsnx.module').then(m => m.TaskManagerHsnxDepartmentHsnxModule)
      },
      {
        path: 'employee-hsnx',
        loadChildren: () => import('./employee-hsnx/employee-hsnx.module').then(m => m.TaskManagerHsnxEmployeeHsnxModule)
      },
      {
        path: 'event-hsnx',
        loadChildren: () => import('./event-hsnx/event-hsnx.module').then(m => m.TaskManagerHsnxEventHsnxModule)
      },
      {
        path: 'task-hsnx',
        loadChildren: () => import('./task-hsnx/task-hsnx.module').then(m => m.TaskManagerHsnxTaskHsnxModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TaskManagerHsnxEntityModule {}
