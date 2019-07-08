import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBenefit, Benefit } from 'app/shared/model/benefit.model';
import { BenefitService } from './benefit.service';
import { IBenefitType } from 'app/shared/model/benefit-type.model';
import { BenefitTypeService } from 'app/entities/benefit-type';
import { IField } from 'app/shared/model/field.model';
import { FieldService } from 'app/entities/field';
import { IBusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from 'app/entities/business-case';

@Component({
  selector: 'jhi-benefit-update',
  templateUrl: './benefit-update.component.html'
})
export class BenefitUpdateComponent implements OnInit {
  isSaving: boolean;

  benefittypes: IBenefitType[];

  fields: IField[];

  businesscases: IBusinessCase[];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    type: [],
    fields: [],
    businessCase: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected benefitService: BenefitService,
    protected benefitTypeService: BenefitTypeService,
    protected fieldService: FieldService,
    protected businessCaseService: BusinessCaseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ benefit }) => {
      this.updateForm(benefit);
    });
    this.benefitTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBenefitType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBenefitType[]>) => response.body)
      )
      .subscribe((res: IBenefitType[]) => (this.benefittypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.fieldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IField[]>) => mayBeOk.ok),
        map((response: HttpResponse<IField[]>) => response.body)
      )
      .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.businessCaseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBusinessCase[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBusinessCase[]>) => response.body)
      )
      .subscribe((res: IBusinessCase[]) => (this.businesscases = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(benefit: IBenefit) {
    this.editForm.patchValue({
      id: benefit.id,
      description: benefit.description,
      type: benefit.type,
      fields: benefit.fields,
      businessCase: benefit.businessCase
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const benefit = this.createFromForm();
    if (benefit.id !== undefined) {
      this.subscribeToSaveResponse(this.benefitService.update(benefit));
    } else {
      this.subscribeToSaveResponse(this.benefitService.create(benefit));
    }
  }

  private createFromForm(): IBenefit {
    return {
      ...new Benefit(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value,
      type: this.editForm.get(['type']).value,
      fields: this.editForm.get(['fields']).value,
      businessCase: this.editForm.get(['businessCase']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBenefit>>) {
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

  trackBenefitTypeById(index: number, item: IBenefitType) {
    return item.id;
  }

  trackFieldById(index: number, item: IField) {
    return item.id;
  }

  trackBusinessCaseById(index: number, item: IBusinessCase) {
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
