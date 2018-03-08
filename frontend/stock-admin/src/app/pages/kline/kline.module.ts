import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';

import { KlineComponent } from './kline.component';
import { routing } from './kline.routing';

import { NgxEchartsModule } from 'ngx-echarts';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    routing,
    NgxEchartsModule,
  ],
  declarations: [
    KlineComponent
  ]
})
export class KlineModule {
    
}