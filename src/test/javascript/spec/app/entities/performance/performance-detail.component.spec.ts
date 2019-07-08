/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { PerformanceDetailComponent } from 'app/entities/performance/performance-detail.component';
import { Performance } from 'app/shared/model/performance.model';

describe('Component Tests', () => {
  describe('Performance Management Detail Component', () => {
    let comp: PerformanceDetailComponent;
    let fixture: ComponentFixture<PerformanceDetailComponent>;
    const route = ({ data: of({ performance: new Performance('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [PerformanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PerformanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PerformanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.performance).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
