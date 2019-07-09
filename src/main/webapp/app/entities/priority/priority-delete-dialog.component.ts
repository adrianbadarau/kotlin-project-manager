import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPriority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';

@Component({
  selector: 'jhi-priority-delete-dialog',
  templateUrl: './priority-delete-dialog.component.html'
})
export class PriorityDeleteDialogComponent {
  priority: IPriority;

  constructor(protected priorityService: PriorityService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.priorityService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'priorityListModification',
        content: 'Deleted an priority'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-priority-delete-popup',
  template: ''
})
export class PriorityDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ priority }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PriorityDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.priority = priority;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/priority', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/priority', { outlets: { popup: null } }]);
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
