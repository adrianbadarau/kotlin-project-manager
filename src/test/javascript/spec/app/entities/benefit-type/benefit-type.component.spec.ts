/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { BenefitTypeComponent } from 'app/entities/benefit-type/benefit-type.component';
import { BenefitTypeService } from 'app/entities/benefit-type/benefit-type.service';
import { BenefitType } from 'app/shared/model/benefit-type.model';

describe('Component Tests', () => {
  describe('BenefitType Management Component', () => {
    let comp: BenefitTypeComponent;
    let fixture: ComponentFixture<BenefitTypeComponent>;
    let service: BenefitTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BenefitTypeComponent],
        providers: []
      })
        .overrideTemplate(BenefitTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BenefitTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BenefitTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new BenefitType('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.benefitTypes[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
