/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PmAppTestModule } from '../../../test.module';
import { AttachmentDeleteDialogComponent } from 'app/entities/attachment/attachment-delete-dialog.component';
import { AttachmentService } from 'app/entities/attachment/attachment.service';

describe('Component Tests', () => {
  describe('Attachment Management Delete Component', () => {
    let comp: AttachmentDeleteDialogComponent;
    let fixture: ComponentFixture<AttachmentDeleteDialogComponent>;
    let service: AttachmentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [AttachmentDeleteDialogComponent]
      })
        .overrideTemplate(AttachmentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttachmentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttachmentService);
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
