/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { BusinessCaseDetailComponent } from 'app/entities/business-case/business-case-detail.component';
import { BusinessCase } from 'app/shared/model/business-case.model';

describe('Component Tests', () => {
  describe('BusinessCase Management Detail Component', () => {
    let comp: BusinessCaseDetailComponent;
    let fixture: ComponentFixture<BusinessCaseDetailComponent>;
    const route = ({ data: of({ businessCase: new BusinessCase('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BusinessCaseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BusinessCaseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusinessCaseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.businessCase).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
