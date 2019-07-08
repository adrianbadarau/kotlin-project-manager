/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PmAppTestModule } from '../../../test.module';
import { BenefitTypeDeleteDialogComponent } from 'app/entities/benefit-type/benefit-type-delete-dialog.component';
import { BenefitTypeService } from 'app/entities/benefit-type/benefit-type.service';

describe('Component Tests', () => {
  describe('BenefitType Management Delete Component', () => {
    let comp: BenefitTypeDeleteDialogComponent;
    let fixture: ComponentFixture<BenefitTypeDeleteDialogComponent>;
    let service: BenefitTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [BenefitTypeDeleteDialogComponent]
      })
        .overrideTemplate(BenefitTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BenefitTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BenefitTypeService);
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
