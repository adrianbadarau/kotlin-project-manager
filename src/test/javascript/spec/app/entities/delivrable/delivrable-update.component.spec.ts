/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { DelivrableUpdateComponent } from 'app/entities/delivrable/delivrable-update.component';
import { DelivrableService } from 'app/entities/delivrable/delivrable.service';
import { Delivrable } from 'app/shared/model/delivrable.model';

describe('Component Tests', () => {
  describe('Delivrable Management Update Component', () => {
    let comp: DelivrableUpdateComponent;
    let fixture: ComponentFixture<DelivrableUpdateComponent>;
    let service: DelivrableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [DelivrableUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DelivrableUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DelivrableUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DelivrableService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Delivrable('123');
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
        const entity = new Delivrable();
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
