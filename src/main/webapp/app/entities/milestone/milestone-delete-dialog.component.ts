import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';

@Component({
  selector: 'jhi-milestone-delete-dialog',
  templateUrl: './milestone-delete-dialog.component.html'
})
export class MilestoneDeleteDialogComponent {
  milestone: IMilestone;

  constructor(protected milestoneService: MilestoneService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.milestoneService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'milestoneListModification',
        content: 'Deleted an milestone'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-milestone-delete-popup',
  template: ''
})
export class MilestoneDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ milestone }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MilestoneDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.milestone = milestone;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/milestone', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/milestone', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
