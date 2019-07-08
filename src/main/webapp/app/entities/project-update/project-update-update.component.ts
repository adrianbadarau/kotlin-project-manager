import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IProjectUpdate, ProjectUpdate } from 'app/shared/model/project-update.model';
import { ProjectUpdateService } from './project-update.service';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task';

@Component({
  selector: 'jhi-project-update-update',
  templateUrl: './project-update-update.component.html'
})
export class ProjectUpdateUpdateComponent implements OnInit {
  isSaving: boolean;

  milestones: IMilestone[];

  tasks: ITask[];

  editForm = this.fb.group({
    id: [],
    keyMilestoneUpdate: [],
    comments: [],
    taskPlan: [],
    risk: [],
    supportNeaded: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected projectUpdateService: ProjectUpdateService,
    protected milestoneService: MilestoneService,
    protected taskService: TaskService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ projectUpdate }) => {
      this.updateForm(projectUpdate);
    });
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.taskService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITask[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITask[]>) => response.body)
      )
      .subscribe((res: ITask[]) => (this.tasks = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(projectUpdate: IProjectUpdate) {
    this.editForm.patchValue({
      id: projectUpdate.id,
      keyMilestoneUpdate: projectUpdate.keyMilestoneUpdate,
      comments: projectUpdate.comments,
      taskPlan: projectUpdate.taskPlan,
      risk: projectUpdate.risk,
      supportNeaded: projectUpdate.supportNeaded
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const projectUpdate = this.createFromForm();
    if (projectUpdate.id !== undefined) {
      this.subscribeToSaveResponse(this.projectUpdateService.update(projectUpdate));
    } else {
      this.subscribeToSaveResponse(this.projectUpdateService.create(projectUpdate));
    }
  }

  private createFromForm(): IProjectUpdate {
    return {
      ...new ProjectUpdate(),
      id: this.editForm.get(['id']).value,
      keyMilestoneUpdate: this.editForm.get(['keyMilestoneUpdate']).value,
      comments: this.editForm.get(['comments']).value,
      taskPlan: this.editForm.get(['taskPlan']).value,
      risk: this.editForm.get(['risk']).value,
      supportNeaded: this.editForm.get(['supportNeaded']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectUpdate>>) {
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

  trackTaskById(index: number, item: ITask) {
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
