import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from './task-type.service';

@Component({
  selector: 'jhi-task-type-delete-dialog',
  templateUrl: './task-type-delete-dialog.component.html'
})
export class TaskTypeDeleteDialogComponent {
  taskType: ITaskType;

  constructor(protected taskTypeService: TaskTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.taskTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'taskTypeListModification',
        content: 'Deleted an taskType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-task-type-delete-popup',
  template: ''
})
export class TaskTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ taskType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TaskTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.taskType = taskType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/task-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/task-type', { outlets: { popup: null } }]);
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
