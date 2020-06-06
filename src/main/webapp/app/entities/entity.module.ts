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
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TaskManagerHsnxEntityModule {}
