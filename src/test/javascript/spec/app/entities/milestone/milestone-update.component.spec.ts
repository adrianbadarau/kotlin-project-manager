/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { KotlinPmTestModule } from '../../../test.module';
import { MilestoneUpdateComponent } from 'app/entities/milestone/milestone-update.component';
import { MilestoneService } from 'app/entities/milestone/milestone.service';
import { Milestone } from 'app/shared/model/milestone.model';

describe('Component Tests', () => {
  describe('Milestone Management Update Component', () => {
    let comp: MilestoneUpdateComponent;
    let fixture: ComponentFixture<MilestoneUpdateComponent>;
    let service: MilestoneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KotlinPmTestModule],
        declarations: [MilestoneUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MilestoneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MilestoneUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MilestoneService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Milestone(123);
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
        const entity = new Milestone();
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
