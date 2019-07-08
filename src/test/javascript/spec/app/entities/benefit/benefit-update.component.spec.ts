/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { BenefitUpdateComponent } from 'app/entities/benefit/benefit-update.component';
import { BenefitService } from 'app/entities/benefit/benefit.service';
import { Benefit } from 'app/shared/model/benefit.model';

describe('Component Tests', () => {
  describe('Benefit Management Update Component', () => {
    let comp: BenefitUpdateComponent;
    let fixture: ComponentFixture<BenefitUpdateComponent>;
    let service: BenefitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BenefitUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BenefitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BenefitUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BenefitService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Benefit('123');
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
        const entity = new Benefit();
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
