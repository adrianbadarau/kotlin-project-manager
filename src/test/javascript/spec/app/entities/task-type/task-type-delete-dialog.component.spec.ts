/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PmAppTestModule } from '../../../test.module';
import { TaskTypeDeleteDialogComponent } from 'app/entities/task-type/task-type-delete-dialog.component';
import { TaskTypeService } from 'app/entities/task-type/task-type.service';

describe('Component Tests', () => {
  describe('TaskType Management Delete Component', () => {
    let comp: TaskTypeDeleteDialogComponent;
    let fixture: ComponentFixture<TaskTypeDeleteDialogComponent>;
    let service: TaskTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [TaskTypeDeleteDialogComponent]
      })
        .overrideTemplate(TaskTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaskTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskTypeService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('123');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('123');
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
