/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KotlinPmTestModule } from '../../../test.module';
import { MilestoneDeleteDialogComponent } from 'app/entities/milestone/milestone-delete-dialog.component';
import { MilestoneService } from 'app/entities/milestone/milestone.service';

describe('Component Tests', () => {
  describe('Milestone Management Delete Component', () => {
    let comp: MilestoneDeleteDialogComponent;
    let fixture: ComponentFixture<MilestoneDeleteDialogComponent>;
    let service: MilestoneService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KotlinPmTestModule],
        declarations: [MilestoneDeleteDialogComponent]
      })
        .overrideTemplate(MilestoneDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MilestoneDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MilestoneService);
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
