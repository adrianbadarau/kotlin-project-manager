import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProjectUpdate } from 'app/shared/model/project-update.model';
import { ProjectUpdateService } from './project-update.service';

@Component({
  selector: 'jhi-project-update-delete-dialog',
  templateUrl: './project-update-delete-dialog.component.html'
})
export class ProjectUpdateDeleteDialogComponent {
  projectUpdate: IProjectUpdate;

  constructor(
    protected projectUpdateService: ProjectUpdateService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.projectUpdateService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'projectUpdateListModification',
        content: 'Deleted an projectUpdate'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-project-update-delete-popup',
  template: ''
})
export class ProjectUpdateDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ projectUpdate }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProjectUpdateDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.projectUpdate = projectUpdate;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/project-update', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/project-update', { outlets: { popup: null } }]);
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
