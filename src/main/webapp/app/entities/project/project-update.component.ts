import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IProject, Project } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { IBusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from 'app/entities/business-case';
import { IField } from 'app/shared/model/field.model';
import { FieldService } from 'app/entities/field';
import { IPerformance } from 'app/shared/model/performance.model';
import { PerformanceService } from 'app/entities/performance';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit {
  isSaving: boolean;

  businesscases: IBusinessCase[];

  fields: IField[];

  performances: IPerformance[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    objective: [null, [Validators.required, Validators.minLength(10)]],
    target: [null, [Validators.required]],
    budget: [],
    risk: [null, [Validators.required]],
    benefitMesurement: [],
    businessCase: [],
    fields: [],
    performances: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected projectService: ProjectService,
    protected businessCaseService: BusinessCaseService,
    protected fieldService: FieldService,
    protected performanceService: PerformanceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ project }) => {
      this.updateForm(project);
    });
    this.businessCaseService
      .query({ filter: 'project-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IBusinessCase[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBusinessCase[]>) => response.body)
      )
      .subscribe(
        (res: IBusinessCase[]) => {
          if (!this.editForm.get('businessCase').value || !this.editForm.get('businessCase').value.id) {
            this.businesscases = res;
          } else {
            this.businessCaseService
              .find(this.editForm.get('businessCase').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IBusinessCase>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IBusinessCase>) => subResponse.body)
              )
              .subscribe(
                (subRes: IBusinessCase) => (this.businesscases = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.fieldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IField[]>) => mayBeOk.ok),
        map((response: HttpResponse<IField[]>) => response.body)
      )
      .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.performanceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPerformance[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPerformance[]>) => response.body)
      )
      .subscribe((res: IPerformance[]) => (this.performances = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(project: IProject) {
    this.editForm.patchValue({
      id: project.id,
      name: project.name,
      objective: project.objective,
      target: project.target != null ? project.target.format(DATE_TIME_FORMAT) : null,
      budget: project.budget,
      risk: project.risk,
      benefitMesurement: project.benefitMesurement,
      businessCase: project.businessCase,
      fields: project.fields,
      performances: project.performances
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const project = this.createFromForm();
    if (project.id !== undefined) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  private createFromForm(): IProject {
    return {
      ...new Project(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      objective: this.editForm.get(['objective']).value,
      target: this.editForm.get(['target']).value != null ? moment(this.editForm.get(['target']).value, DATE_TIME_FORMAT) : undefined,
      budget: this.editForm.get(['budget']).value,
      risk: this.editForm.get(['risk']).value,
      benefitMesurement: this.editForm.get(['benefitMesurement']).value,
      businessCase: this.editForm.get(['businessCase']).value,
      fields: this.editForm.get(['fields']).value,
      performances: this.editForm.get(['performances']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
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

  trackBusinessCaseById(index: number, item: IBusinessCase) {
    return item.id;
  }

  trackFieldById(index: number, item: IField) {
    return item.id;
  }

  trackPerformanceById(index: number, item: IPerformance) {
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
