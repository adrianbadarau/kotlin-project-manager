/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { BenefitDetailComponent } from 'app/entities/benefit/benefit-detail.component';
import { Benefit } from 'app/shared/model/benefit.model';

describe('Component Tests', () => {
  describe('Benefit Management Detail Component', () => {
    let comp: BenefitDetailComponent;
    let fixture: ComponentFixture<BenefitDetailComponent>;
    const route = ({ data: of({ benefit: new Benefit('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BenefitDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BenefitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BenefitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.benefit).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
