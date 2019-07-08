/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { BenefitTypeUpdateComponent } from 'app/entities/benefit-type/benefit-type-update.component';
import { BenefitTypeService } from 'app/entities/benefit-type/benefit-type.service';
import { BenefitType } from 'app/shared/model/benefit-type.model';

describe('Component Tests', () => {
  describe('BenefitType Management Update Component', () => {
    let comp: BenefitTypeUpdateComponent;
    let fixture: ComponentFixture<BenefitTypeUpdateComponent>;
    let service: BenefitTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BenefitTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BenefitTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BenefitTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BenefitTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BenefitType('123');
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
        const entity = new BenefitType();
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
