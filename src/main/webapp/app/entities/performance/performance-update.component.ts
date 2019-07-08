import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPerformance, Performance } from 'app/shared/model/performance.model';
import { PerformanceService } from './performance.service';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-performance-update',
  templateUrl: './performance-update.component.html'
})
export class PerformanceUpdateComponent implements OnInit {
  isSaving: boolean;

  milestones: IMilestone[];

  projects: IProject[];

  editForm = this.fb.group({
    id: [],
    timelinePerformance: [],
    riskRegister: [],
    mitigationPlan: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected performanceService: PerformanceService,
    protected milestoneService: MilestoneService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ performance }) => {
      this.updateForm(performance);
    });
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(performance: IPerformance) {
    this.editForm.patchValue({
      id: performance.id,
      timelinePerformance: performance.timelinePerformance,
      riskRegister: performance.riskRegister,
      mitigationPlan: performance.mitigationPlan
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const performance = this.createFromForm();
    if (performance.id !== undefined) {
      this.subscribeToSaveResponse(this.performanceService.update(performance));
    } else {
      this.subscribeToSaveResponse(this.performanceService.create(performance));
    }
  }

  private createFromForm(): IPerformance {
    return {
      ...new Performance(),
      id: this.editForm.get(['id']).value,
      timelinePerformance: this.editForm.get(['timelinePerformance']).value,
      riskRegister: this.editForm.get(['riskRegister']).value,
      mitigationPlan: this.editForm.get(['mitigationPlan']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerformance>>) {
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

  trackMilestoneById(index: number, item: IMilestone) {
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
