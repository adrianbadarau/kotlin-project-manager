/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { BusinessCaseComponent } from 'app/entities/business-case/business-case.component';
import { BusinessCaseService } from 'app/entities/business-case/business-case.service';
import { BusinessCase } from 'app/shared/model/business-case.model';

describe('Component Tests', () => {
  describe('BusinessCase Management Component', () => {
    let comp: BusinessCaseComponent;
    let fixture: ComponentFixture<BusinessCaseComponent>;
    let service: BusinessCaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BusinessCaseComponent],
        providers: []
      })
        .overrideTemplate(BusinessCaseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusinessCaseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusinessCaseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new BusinessCase('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.businessCases[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
