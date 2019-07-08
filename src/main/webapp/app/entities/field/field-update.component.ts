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
import { IBusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from 'app/entities/business-case';
import { IBenefit } from 'app/shared/model/benefit.model';
import { BenefitService } from 'app/entities/benefit';
import { IDelivrable } from 'app/shared/model/delivrable.model';
import { DelivrableService } from 'app/entities/delivrable';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';

@Component({
  selector: 'jhi-field-update',
  templateUrl: './field-update.component.html'
})
export class FieldUpdateComponent implements OnInit {
  isSaving: boolean;

  projects: IProject[];

  businesscases: IBusinessCase[];

  benefits: IBenefit[];

  delivrables: IDelivrable[];

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
    protected businessCaseService: BusinessCaseService,
    protected benefitService: BenefitService,
    protected delivrableService: DelivrableService,
    protected milestoneService: MilestoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ field }) => {
      this.updateForm(field);
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.businessCaseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBusinessCase[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBusinessCase[]>) => response.body)
      )
      .subscribe((res: IBusinessCase[]) => (this.businesscases = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.benefitService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBenefit[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBenefit[]>) => response.body)
      )
      .subscribe((res: IBenefit[]) => (this.benefits = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.delivrableService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDelivrable[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDelivrable[]>) => response.body)
      )
      .subscribe((res: IDelivrable[]) => (this.delivrables = res), (res: HttpErrorResponse) => this.onError(res.message));
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
    return {
      ...new Field(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      data: this.editForm.get(['data']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IField>>) {
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

  trackBusinessCaseById(index: number, item: IBusinessCase) {
    return item.id;
  }

  trackBenefitById(index: number, item: IBenefit) {
    return item.id;
  }

  trackDelivrableById(index: number, item: IDelivrable) {
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
