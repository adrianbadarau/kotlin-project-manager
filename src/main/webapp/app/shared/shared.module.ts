import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { KotlinPmSharedLibsModule, KotlinPmSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [KotlinPmSharedLibsModule, KotlinPmSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [KotlinPmSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KotlinPmSharedModule {
  static forRoot() {
    return {
      ngModule: KotlinPmSharedModule
    };
  }
}
