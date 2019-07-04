import { NgModule } from '@angular/core';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { KotlinPmSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { CustomFieldManageMultipleComponent } from 'app/shared/custom-field-manage-multiple/custom-field-manage-multiple.component';

@NgModule({
  imports: [KotlinPmSharedLibsModule, TableModule, DialogModule, ButtonModule, InputTextModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent, CustomFieldManageMultipleComponent],
  exports: [KotlinPmSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent, CustomFieldManageMultipleComponent]
})
export class KotlinPmSharedCommonModule {}
