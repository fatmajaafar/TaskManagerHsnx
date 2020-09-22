/*eslint-disable*/
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { SingleDataSet, Label } from 'ng2-charts';
import { ChartDataSets, ChartType, ChartOptions } from 'chart.js';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import { TaskHsnxService } from 'app/entities/task-hsnx/task-hsnx.service';
import { HttpResponse } from '@angular/common/http';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
  account: Account | null = null;
  authSubscription?: Subscription;
  tasks: ITaskHsnx[] = [];

  //chart init

  /**!!!!!!!!!!!!!**/
  public pieChartOptions: ChartOptions = {
    responsive: true,
    legend: {
      position: 'bottom'
    },
    plugins: {
      datalabels: {
        anchor: 'end',
        align: 'end'
      }
    }
  };
  public pieChartLabels: Label[] = [['To Do'], ['In Progress'], ['In Review'], ['Done']];
  public pieChartData: number[] = [300, 100, 500, 100];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartPlugins = [pluginDataLabels];
  public pieChartColors = [
    {
      backgroundColor: ['rgba(255,0,0,0.3)', 'rgba(0,255,0,0.3)', 'rgba(0,0,255,0.3)', 'rgba(255, 248, 181, 1)']
    }
  ];
  /** *******************************************/

  public polarAreaChartLabels: Label[] = ['Download Sales', 'In-Store Sales', 'Mail Sales', 'Telesales', 'Corporate Sales'];
  public polarAreaChartData: SingleDataSet = [300, 500, 100, 40, 120];

  public polarAreaLegend = true;

  public polarAreaChartType: ChartType = 'polarArea';
  /************************* */

  public barChartOptions: ChartOptions = {
    responsive: true,
    // We use these empty structures as placeholders for dynamic theming.
    scales: { xAxes: [{}], yAxes: [{}] },
    plugins: {
      datalabels: {
        anchor: 'end',
        align: 'end'
      }
    }
  };
  public barChartLabels: Label[] = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;

  public barChartData: ChartDataSets[] = [{ data: [65, 59, 80, 81, 56, 55, 40, 12, 50, 30, 22, 58], label: 'Completed' }];

  constructor(private accountService: AccountService, private loginModalService: LoginModalService, private taskService: TaskHsnxService) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    console.clear();
    console.log('------');
    this.taskService
      .query({
        'taskstatus.specified': true,
        size: 1000
      })
      .subscribe((res: HttpResponse<ITaskHsnx[]>) => {
        this.tasks = res.body || [];
        let i = 0;
        this.pieChartData = [0, 0, 0, 0];

        res.body?.forEach(element => {
          i += 1;
          if (element.taskstatus == 1) this.pieChartData[0] += 1;
          if (element.taskstatus == 2) this.pieChartData[1] += 1;
          if (element.taskstatus == 3) this.pieChartData[2] += 1;
          if (element.taskstatus == 4) this.pieChartData[3] += 1;
        });
        console.clear();
        console.log(this.pieChartData);
      });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
