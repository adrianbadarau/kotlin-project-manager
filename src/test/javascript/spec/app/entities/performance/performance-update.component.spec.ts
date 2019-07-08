/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { PerformanceUpdateComponent } from 'app/entities/performance/performance-update.component';
import { PerformanceService } from 'app/entities/performance/performance.service';
import { Performance } from 'app/shared/model/performance.model';

describe('Component Tests', () => {
  describe('Performance Management Update Component', () => {
    let comp: PerformanceUpdateComponent;
    let fixture: ComponentFixture<PerformanceUpdateComponent>;
    let service: PerformanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [PerformanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PerformanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PerformanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PerformanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Performance('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Performance();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
