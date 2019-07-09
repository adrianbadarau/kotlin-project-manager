/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PmAppTestModule } from '../../../test.module';
import { PriorityDeleteDialogComponent } from 'app/entities/priority/priority-delete-dialog.component';
import { PriorityService } from 'app/entities/priority/priority.service';

describe('Component Tests', () => {
  describe('Priority Management Delete Component', () => {
    let comp: PriorityDeleteDialogComponent;
    let fixture: ComponentFixture<PriorityDeleteDialogComponent>;
    let service: PriorityService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [PriorityDeleteDialogComponent]
      })
        .overrideTemplate(PriorityDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PriorityDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PriorityService);
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
