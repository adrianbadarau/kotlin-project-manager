import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDelivrable, Delivrable } from 'app/shared/model/delivrable.model';
import { DelivrableService } from './delivrable.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-delivrable-update',
  templateUrl: './delivrable-update.component.html'
})
export class DelivrableUpdateComponent implements OnInit {
  isSaving: boolean;

  projects: IProject[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    target: [null, [Validators.required]],
    project: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected delivrableService: DelivrableService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ delivrable }) => {
      this.updateForm(delivrable);
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(delivrable: IDelivrable) {
    this.editForm.patchValue({
      id: delivrable.id,
      name: delivrable.name,
      description: delivrable.description,
      target: delivrable.target != null ? delivrable.target.format(DATE_TIME_FORMAT) : null,
      project: delivrable.project
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const delivrable = this.createFromForm();
    if (delivrable.id !== undefined) {
      this.subscribeToSaveResponse(this.delivrableService.update(delivrable));
    } else {
      this.subscribeToSaveResponse(this.delivrableService.create(delivrable));
    }
  }

  private createFromForm(): IDelivrable {
    return {
      ...new Delivrable(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      target: this.editForm.get(['target']).value != null ? moment(this.editForm.get(['target']).value, DATE_TIME_FORMAT) : undefined,
      project: this.editForm.get(['project']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDelivrable>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProjectById(index: number, item: IProject) {
    return item.id;
  }
}
