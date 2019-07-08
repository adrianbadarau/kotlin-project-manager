/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { ProjectUpdateUpdateComponent } from 'app/entities/project-update/project-update-update.component';
import { ProjectUpdateService } from 'app/entities/project-update/project-update.service';
import { ProjectUpdate } from 'app/shared/model/project-update.model';

describe('Component Tests', () => {
  describe('ProjectUpdate Management Update Component', () => {
    let comp: ProjectUpdateUpdateComponent;
    let fixture: ComponentFixture<ProjectUpdateUpdateComponent>;
    let service: ProjectUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ProjectUpdateUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProjectUpdateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjectUpdateUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProjectUpdateService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProjectUpdate('123');
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
        const entity = new ProjectUpdate();
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
