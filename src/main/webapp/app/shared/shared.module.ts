import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { PmAppSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [PmAppSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [PmAppSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppSharedModule {
  static forRoot() {
    return {
      ngModule: PmAppSharedModule
    };
  }
}
