import { NgModule } from '@angular/core';

import { KotlinPmSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [KotlinPmSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [KotlinPmSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class KotlinPmSharedCommonModule {}
