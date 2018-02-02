import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {TablesComponent} from './tables.component';
import {Union3050TableComponent} from './union/union3050-table.component';
import {Sz50TableComponent} from './sz50/sz50-table.component'
import {Hs300TableComponent} from './hs300/hs300-table.component';
import {Zz500TableComponent} from './zz500/zz500-table.component';

const routes: Routes = [{
  path: '',
  component: TablesComponent,
  children: [{
    path: 'hs300',
    component: Hs300TableComponent,
  }, {
    path: 'sz50',
    component: Sz50TableComponent,
  }, {
      path: 'zz500',
      component: Zz500TableComponent,
  }, {
      path: 'union',
      component: Union3050TableComponent,
     },
  ],
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TablesRoutingModule {
}

export const routedComponents = [
  TablesComponent,
  Union3050TableComponent,
  Sz50TableComponent,
  Hs300TableComponent,
  Zz500TableComponent,
];
