/*eslint-disable*/
import { Injectable, OnInit } from '@angular/core';
import { SortableSpec, DraggedItem } from '@angular-skyhook/sortable';
import { ItemTypes } from './item-types';
import { produce } from 'immer';
import { TaskHsnxService } from 'app/entities/task-hsnx/task-hsnx.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { EmployeeHsnxService } from 'app/entities/employee-hsnx/employee-hsnx.service';
import { Observable } from 'rxjs';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';

export type CardTree = Array<CardList>;
export interface CardList {
  id: number;
  title: string;
  cards: Array<Card>;
}

export interface Card {
  listId?: number;
  id?: number;
  title?: string;
}

@Injectable({
  providedIn: 'root'
})
export class SortableSpecService {
  boardSpec: SortableSpec<CardList> = {
    type: ItemTypes.LIST,
    trackBy: list => list.id,
    hover: item => {
      this.tree = this.moveList(item);
    },
    drop: item => {
      this.tree = this.savedTree = this.moveList(item);
    },
    endDrag: _item => {
      this.tree = this.savedTree;
    }
  };

  listSpec: SortableSpec<Card> = {
    type: ItemTypes.CARD,
    trackBy: card => card.id,
    hover: item => {
      this.tree = this.moveCard(item);
    },
    drop: item => {
      this.tree = this.savedTree = this.moveCard(item);
    },
    endDrag: _item => {
      this.tree = this.savedTree;
    }
  };

  cards: Card[] = [];

  private initialTree: CardTree = [
    {
      id: 1,
      title: 'To Do',
      cards: [{ title: '' }, { title: '' }]
    },
    {
      id: 2,
      title: 'Doing',
      cards: []
    },
    {
      id: 3,
      title: 'Done',
      cards: []
    }
  ];

  constructor(protected taskService: TaskHsnxService, private http: HttpClient, protected employeeService: EmployeeHsnxService) {}

  getCards(): Observable<any[]> {
    this.cards = [];
    this.taskService
      .query({
        size: 10000
      })
      .subscribe(
        (res: HttpResponse<ITaskHsnx[]>) => {
          if (res.body) {
            // let i = 0;

            res.body.forEach(element => {
              //i += 1;

              const task: Card = {};
              task.listId = element.taskstatus;
              //task.listId= 1;
              task.id = element.id;
              task.title = element.tasktitle;
              this.cards.push(task);
            });
          }
        },
        () => ''
      );
    return new Observable(observer => {
      setTimeout(() => {
        observer.next(this.cards);
      }, 200);
    });
  }
  private savedTree = this.initialTree;
  public tree = this.initialTree;

  moveList(item: DraggedItem<CardList>) {
    return produce(this.savedTree, (draft: any[]) => {
      if (item.isInternal) {
        draft.splice(item.index, 1);
      }
      draft.splice(item.hover.index, 0, item.data);
    });
  }

  moveCard(item: DraggedItem<Card>) {
    return produce(this.savedTree, (draft: any[]) => {
      const { listId: from, index: fromIndex } = item;
      const { listId: to, index: toIndex } = item.hover;
      const fromList = draft.find((x: { id: any }) => x.id === from);
      const toList = draft.find((x: { id: any }) => x.id === to);
      if (!fromList) return;
      if (item.isInternal) {
        fromList.cards.splice(fromIndex, 1);
      }
      if (!toList) return;
      const neu = {
        ...item.data,
        listId: to
      };
      toList.cards.splice(toIndex, 0, neu);
    });
  }
}
