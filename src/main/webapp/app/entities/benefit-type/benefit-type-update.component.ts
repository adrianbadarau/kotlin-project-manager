import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IBenefitType, BenefitType } from 'app/shared/model/benefit-type.model';
import { BenefitTypeService } from './benefit-type.service';

@Component({
  selector: 'jhi-benefit-type-update',
  templateUrl: './benefit-type-update.component.html'
})
export class BenefitTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected benefitTypeService: BenefitTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ benefitType }) => {
      this.updateForm(benefitType);
    });
  }

  updateForm(benefitType: IBenefitType) {
    this.editForm.patchValue({
      id: benefitType.id,
      name: benefitType.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const benefitType = this.createFromForm();
    if (benefitType.id !== undefined) {
      this.subscribeToSaveResponse(this.benefitTypeService.update(benefitType));
    } else {
      this.subscribeToSaveResponse(this.benefitTypeService.create(benefitType));
    }
  }

  private createFromForm(): IBenefitType {
    return {
      ...new BenefitType(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBenefitType>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
