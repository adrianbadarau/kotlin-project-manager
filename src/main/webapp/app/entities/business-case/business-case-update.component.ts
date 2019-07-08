import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBusinessCase, BusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from './business-case.service';
import { IField } from 'app/shared/model/field.model';
import { FieldService } from 'app/entities/field';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-business-case-update',
  templateUrl: './business-case-update.component.html'
})
export class BusinessCaseUpdateComponent implements OnInit {
  isSaving: boolean;

  fields: IField[];

  projects: IProject[];

  editForm = this.fb.group({
    id: [],
    summary: [],
    fields: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected businessCaseService: BusinessCaseService,
    protected fieldService: FieldService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ businessCase }) => {
      this.updateForm(businessCase);
    });
    this.fieldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IField[]>) => mayBeOk.ok),
        map((response: HttpResponse<IField[]>) => response.body)
      )
      .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(businessCase: IBusinessCase) {
    this.editForm.patchValue({
      id: businessCase.id,
      summary: businessCase.summary,
      fields: businessCase.fields
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const businessCase = this.createFromForm();
    if (businessCase.id !== undefined) {
      this.subscribeToSaveResponse(this.businessCaseService.update(businessCase));
    } else {
      this.subscribeToSaveResponse(this.businessCaseService.create(businessCase));
    }
  }

  private createFromForm(): IBusinessCase {
    return {
      ...new BusinessCase(),
      id: this.editForm.get(['id']).value,
      summary: this.editForm.get(['summary']).value,
      fields: this.editForm.get(['fields']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessCase>>) {
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

  trackFieldById(index: number, item: IField) {
    return item.id;
  }

  trackProjectById(index: number, item: IProject) {
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
