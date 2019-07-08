/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PmAppTestModule } from '../../../test.module';
import { ProjectUpdateDeleteDialogComponent } from 'app/entities/project-update/project-update-delete-dialog.component';
import { ProjectUpdateService } from 'app/entities/project-update/project-update.service';

describe('Component Tests', () => {
  describe('ProjectUpdate Management Delete Component', () => {
    let comp: ProjectUpdateDeleteDialogComponent;
    let fixture: ComponentFixture<ProjectUpdateDeleteDialogComponent>;
    let service: ProjectUpdateService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ProjectUpdateDeleteDialogComponent]
      })
        .overrideTemplate(ProjectUpdateDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjectUpdateDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProjectUpdateService);
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
