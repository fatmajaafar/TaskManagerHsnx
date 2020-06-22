import { Component, OnInit } from '@angular/core';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { IEventHsnx } from 'app/shared/model/event-hsnx.model';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';
import { EventHsnxService } from '../event-hsnx/event-hsnx.service';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { Account } from 'app/core/user/account.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  tasks: ITaskHsnx[] = [];
  events: IEventHsnx[] = [];
  currentAccount: Account | null = null;
  users: User[] | null = null;
  constructor(
    protected taskService: TaskHsnxService,
    protected eventService: EventHsnxService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private userService: UserService
  ) {}

  ngOnInit(): void {}
}
