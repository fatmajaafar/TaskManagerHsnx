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
      },
      {
        path: 'country-hsnx',
        loadChildren: () => import('./country-hsnx/country-hsnx.module').then(m => m.TaskManagerHsnxCountryHsnxModule)
      },
      {
        path: 'notification-hsnx',
        loadChildren: () => import('./notification-hsnx/notification-hsnx.module').then(m => m.TaskManagerHsnxNotificationHsnxModule)
      },
      {
        path: 'taskmanagement-hsnx',
        loadChildren: () => import('./taskmanagement-hsnx/taskmanagement-hsnx.module').then(m => m.TaskManagerHsnxTaskmanagementHsnxModule)
      }

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TaskManagerHsnxEntityModule {}
