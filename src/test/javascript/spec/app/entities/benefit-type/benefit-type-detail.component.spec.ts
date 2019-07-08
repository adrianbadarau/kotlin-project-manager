/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { BenefitTypeDetailComponent } from 'app/entities/benefit-type/benefit-type-detail.component';
import { BenefitType } from 'app/shared/model/benefit-type.model';

describe('Component Tests', () => {
  describe('BenefitType Management Detail Component', () => {
    let comp: BenefitTypeDetailComponent;
    let fixture: ComponentFixture<BenefitTypeDetailComponent>;
    const route = ({ data: of({ benefitType: new BenefitType('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BenefitTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BenefitTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BenefitTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.benefitType).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
