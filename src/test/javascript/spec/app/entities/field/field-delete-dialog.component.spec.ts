/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KotlinPmTestModule } from '../../../test.module';
import { FieldDeleteDialogComponent } from 'app/entities/field/field-delete-dialog.component';
import { FieldService } from 'app/entities/field/field.service';

describe('Component Tests', () => {
  describe('Field Management Delete Component', () => {
    let comp: FieldDeleteDialogComponent;
    let fixture: ComponentFixture<FieldDeleteDialogComponent>;
    let service: FieldService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KotlinPmTestModule],
        declarations: [FieldDeleteDialogComponent]
      })
        .overrideTemplate(FieldDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FieldDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FieldService);
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
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
