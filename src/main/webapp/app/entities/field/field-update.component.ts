import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IField, Field } from 'app/shared/model/field.model';
import { FieldService } from './field.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';

@Component({
  selector: 'jhi-field-update',
  templateUrl: './field-update.component.html'
})
export class FieldUpdateComponent implements OnInit {
  field: IField;
  isSaving: boolean;

  projects: IProject[];

  milestones: IMilestone[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    data: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected fieldService: FieldService,
    protected projectService: ProjectService,
    protected milestoneService: MilestoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ field }) => {
      this.updateForm(field);
      this.field = field;
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(field: IField) {
    this.editForm.patchValue({
      id: field.id,
      name: field.name,
      data: field.data
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const field = this.createFromForm();
    if (field.id !== undefined) {
      this.subscribeToSaveResponse(this.fieldService.update(field));
    } else {
      this.subscribeToSaveResponse(this.fieldService.create(field));
    }
  }

  private createFromForm(): IField {
    const entity = {
      ...new Field(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      data: this.editForm.get(['data']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IField>>) {
    result.subscribe((res: HttpResponse<IField>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackMilestoneById(index: number, item: IMilestone) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
