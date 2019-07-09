/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { ChangeHistoryUpdateComponent } from 'app/entities/change-history/change-history-update.component';
import { ChangeHistoryService } from 'app/entities/change-history/change-history.service';
import { ChangeHistory } from 'app/shared/model/change-history.model';

describe('Component Tests', () => {
  describe('ChangeHistory Management Update Component', () => {
    let comp: ChangeHistoryUpdateComponent;
    let fixture: ComponentFixture<ChangeHistoryUpdateComponent>;
    let service: ChangeHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ChangeHistoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChangeHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChangeHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChangeHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChangeHistory('123');
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
        const entity = new ChangeHistory();
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
