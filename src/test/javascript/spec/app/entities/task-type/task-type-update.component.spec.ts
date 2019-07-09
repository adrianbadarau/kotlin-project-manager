/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { TaskTypeUpdateComponent } from 'app/entities/task-type/task-type-update.component';
import { TaskTypeService } from 'app/entities/task-type/task-type.service';
import { TaskType } from 'app/shared/model/task-type.model';

describe('Component Tests', () => {
  describe('TaskType Management Update Component', () => {
    let comp: TaskTypeUpdateComponent;
    let fixture: ComponentFixture<TaskTypeUpdateComponent>;
    let service: TaskTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [TaskTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TaskTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaskType('123');
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
        const entity = new TaskType();
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
