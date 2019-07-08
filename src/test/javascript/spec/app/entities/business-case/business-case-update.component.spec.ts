/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { BusinessCaseUpdateComponent } from 'app/entities/business-case/business-case-update.component';
import { BusinessCaseService } from 'app/entities/business-case/business-case.service';
import { BusinessCase } from 'app/shared/model/business-case.model';

describe('Component Tests', () => {
  describe('BusinessCase Management Update Component', () => {
    let comp: BusinessCaseUpdateComponent;
    let fixture: ComponentFixture<BusinessCaseUpdateComponent>;
    let service: BusinessCaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BusinessCaseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BusinessCaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusinessCaseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusinessCaseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BusinessCase('123');
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
        const entity = new BusinessCase();
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
