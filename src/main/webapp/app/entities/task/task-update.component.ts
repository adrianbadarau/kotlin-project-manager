import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITask, Task } from 'app/shared/model/task.model';
import { TaskService } from './task.service';
import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from 'app/entities/status';
import { IProjectUpdate } from 'app/shared/model/project-update.model';
import { ProjectUpdateService } from 'app/entities/project-update';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment';
import { IUser, UserService } from 'app/core';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
  isSaving: boolean;

  statuses: IStatus[];

  projectupdates: IProjectUpdate[];

  comments: IComment[];

  users: IUser[];

  teams: ITeam[];

  milestones: IMilestone[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    estimatedDate: [],
    details: [],
    status: [],
    projectUpdates: [],
    comments: [],
    users: [],
    teams: [],
    assignedTeam: [],
    milestone: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected taskService: TaskService,
    protected statusService: StatusService,
    protected projectUpdateService: ProjectUpdateService,
    protected commentService: CommentService,
    protected userService: UserService,
    protected teamService: TeamService,
    protected milestoneService: MilestoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ task }) => {
      this.updateForm(task);
    });
    this.statusService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStatus[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStatus[]>) => response.body)
      )
      .subscribe((res: IStatus[]) => (this.statuses = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.projectUpdateService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProjectUpdate[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProjectUpdate[]>) => response.body)
      )
      .subscribe((res: IProjectUpdate[]) => (this.projectupdates = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.commentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IComment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IComment[]>) => response.body)
      )
      .subscribe((res: IComment[]) => (this.comments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.teamService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(task: ITask) {
    this.editForm.patchValue({
      id: task.id,
      name: task.name,
      estimatedDate: task.estimatedDate != null ? task.estimatedDate.format(DATE_TIME_FORMAT) : null,
      details: task.details,
      status: task.status,
      projectUpdates: task.projectUpdates,
      comments: task.comments,
      users: task.users,
      teams: task.teams,
      assignedTeam: task.assignedTeam,
      milestone: task.milestone
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  private createFromForm(): ITask {
    return {
      ...new Task(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      estimatedDate:
        this.editForm.get(['estimatedDate']).value != null
          ? moment(this.editForm.get(['estimatedDate']).value, DATE_TIME_FORMAT)
          : undefined,
      details: this.editForm.get(['details']).value,
      status: this.editForm.get(['status']).value,
      projectUpdates: this.editForm.get(['projectUpdates']).value,
      comments: this.editForm.get(['comments']).value,
      users: this.editForm.get(['users']).value,
      teams: this.editForm.get(['teams']).value,
      assignedTeam: this.editForm.get(['assignedTeam']).value,
      milestone: this.editForm.get(['milestone']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>) {
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

  trackStatusById(index: number, item: IStatus) {
    return item.id;
  }

  trackProjectUpdateById(index: number, item: IProjectUpdate) {
    return item.id;
  }

  trackCommentById(index: number, item: IComment) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackTeamById(index: number, item: ITeam) {
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
