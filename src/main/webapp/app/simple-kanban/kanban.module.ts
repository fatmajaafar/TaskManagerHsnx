import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SkyhookDndModule, SkyhookDndService } from '@angular-skyhook/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SkyhookMultiBackendModule } from '@angular-skyhook/multi-backend';
import { SkyhookSortableModule } from '@angular-skyhook/sortable';

import { ContainerComponent } from './container.component';
import { KanbanBoardComponent } from './kanban-board/kanban-board.component';
import { KanbanListComponent } from './kanban-list/kanban-list.component';
import { KanbanCardComponent } from './kanban-card/kanban-card.component';
import { SortableSpecService } from './specs';
import { default as HTML5 } from 'react-dnd-html5-backend';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material';
@NgModule({
  declarations: [ContainerComponent, KanbanBoardComponent, KanbanListComponent, KanbanCardComponent],
  imports: [
    CommonModule,
    SkyhookDndModule.forRoot({ backend: HTML5 }),
    SkyhookMultiBackendModule,
    SkyhookSortableModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    RouterModule.forChild([{ path: 'kanban', component: ContainerComponent }])
  ],
  providers: [SkyhookDndService, SortableSpecService]
})
export class KanbanModule {}
