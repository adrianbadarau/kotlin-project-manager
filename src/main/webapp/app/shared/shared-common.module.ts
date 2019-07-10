import { NgModule } from '@angular/core';

import { PmAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { CustomFieldManageMultipleComponent } from 'app/shared/custom-field-manage-multiple/custom-field-manage-multiple.component';
import { ButtonModule, DialogModule, InputTextModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { CustomFieldManageComponent } from 'app/shared/custom-field-manage/custom-field-manage.component';

@NgModule({
  imports: [PmAppSharedLibsModule, TableModule, DialogModule, ButtonModule, InputTextModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent, CustomFieldManageMultipleComponent, CustomFieldManageComponent],
  exports: [
    PmAppSharedLibsModule,
    JhiAlertComponent,
    JhiAlertErrorComponent,
    CustomFieldManageMultipleComponent,
    CustomFieldManageComponent
  ]
})
export class PmAppSharedCommonModule {}
