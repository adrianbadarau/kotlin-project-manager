/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { PerformanceComponent } from 'app/entities/performance/performance.component';
import { PerformanceService } from 'app/entities/performance/performance.service';
import { Performance } from 'app/shared/model/performance.model';

describe('Component Tests', () => {
  describe('Performance Management Component', () => {
    let comp: PerformanceComponent;
    let fixture: ComponentFixture<PerformanceComponent>;
    let service: PerformanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [PerformanceComponent],
        providers: []
      })
        .overrideTemplate(PerformanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PerformanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PerformanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Performance('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.performances[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
