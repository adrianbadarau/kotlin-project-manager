import { NgModule } from '@angular/core';

import { PmAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [PmAppSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [PmAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class PmAppSharedCommonModule {}
