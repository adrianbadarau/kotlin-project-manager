/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PmAppTestModule } from '../../../test.module';
import { BusinessCaseDeleteDialogComponent } from 'app/entities/business-case/business-case-delete-dialog.component';
import { BusinessCaseService } from 'app/entities/business-case/business-case.service';

describe('Component Tests', () => {
  describe('BusinessCase Management Delete Component', () => {
    let comp: BusinessCaseDeleteDialogComponent;
    let fixture: ComponentFixture<BusinessCaseDeleteDialogComponent>;
    let service: BusinessCaseService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BusinessCaseDeleteDialogComponent]
      })
        .overrideTemplate(BusinessCaseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusinessCaseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusinessCaseService);
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
