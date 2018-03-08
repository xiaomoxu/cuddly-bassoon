import { Routes, RouterModule } from '@angular/router';

import { KlineComponent } from './kline.component';

const routes: Routes = [
  {
    path: '',
    component: KlineComponent
  }
];

export const routing = RouterModule.forChild(routes);