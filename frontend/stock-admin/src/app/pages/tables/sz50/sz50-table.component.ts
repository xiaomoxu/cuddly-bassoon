import {Component} from '@angular/core';
import {Http} from '@angular/http';
import {ServerDataSource} from 'ng2-smart-table';
import {serverURL} from '../../../app.module';

@Component({
  selector: 'ngx-smart-table',
  templateUrl: './sz50-table.component.html',
  styles: [`
    nb-card {
      transform: translate3d(0, 0, 0);
    }
  `],
})
export class Sz50TableComponent {


  settings = {
    actions: false,
    hideSubHeader: true,
    pager: {
      display: true,
      perPage: 10,
    },
    columns: {
      code: {
        title: '代码',
      },
      name: {
        title: '名称',
      },
      market: {
        title: '行业',
      },
      region: {
        title: '地区',
      },
      belongTo: {
        title: '板块',
      },
      weight: {
        title: '权重',
      },
      currentPrice: {
        title: '最新价',
      },
      eps: {
        title: '每股收益(元)',
      },
      bvps: {
        title: '每股净资产(元)',
      },
      roe: {
        title: '净资产收益率(%)',
      },
      totalStock: {
        title: '总股本(亿股)',
      },
      liqui: {
        title: '流通股本(亿股)',
      },
      ltsz: {
        title: '流通市值(亿元)',
      },
    },
  };

  source: ServerDataSource;

  constructor(http: Http) {
    this.source = new ServerDataSource(http, {endPoint: serverURL + 'stocklist/SZ50'});
  }


  onDeleteConfirm(event): void {
    if (window.confirm('Are you sure you want to delete?')) {
      event.confirm.resolve();
    } else {
      event.confirm.reject();
    }
  }
}
