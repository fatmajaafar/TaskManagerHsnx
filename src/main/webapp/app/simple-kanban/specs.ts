import { Injectable } from '@angular/core';
import { SortableSpec, DraggedItem } from '@angular-skyhook/sortable';
import { ItemTypes } from './item-types';
import { produce } from 'immer';

export type CardTree = Array<CardList>;

export interface CardList {
  id: number;
  title: string;
  cards: Array<Card>;
}

export interface Card {
  listId: number;
  id: number;
  title: string;
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

  private initialTree: CardTree = [
    {
      id: 1,
      title: 'To Do',
      cards: [
        { listId: 1, id: 10, title: 'Task number one' },
        { listId: 1, id: 20, title: 'Put the lyrics to music' }
      ]
    },
    { id: 2, title: 'Doing', cards: [{ listId: 2, id: 30, title: 'Rig up the speakers' }] },
    { id: 3, title: 'Done', cards: [] }
  ];

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
