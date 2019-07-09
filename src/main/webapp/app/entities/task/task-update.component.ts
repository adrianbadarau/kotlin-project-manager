import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITask, Task } from 'app/shared/model/task.model';
import { TaskService } from './task.service';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';
import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from 'app/entities/status';
import { ITaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from 'app/entities/task-type';
import { IPriority } from 'app/shared/model/priority.model';
import { PriorityService } from 'app/entities/priority';
import { IUser, UserService } from 'app/core';
import { IAttachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from 'app/entities/attachment';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
  isSaving: boolean;

  milestones: IMilestone[];

  statuses: IStatus[];

  tasktypes: ITaskType[];

  priorities: IPriority[];

  users: IUser[];

  tasks: ITask[];

  attachments: IAttachment[];

  comments: IComment[];

  teams: ITeam[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    estimatedTime: [],
    spentTime: [],
    milestone: [],
    status: [],
    taskType: [],
    priority: [],
    assignee: [],
    parent: [],
    users: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected taskService: TaskService,
    protected milestoneService: MilestoneService,
    protected statusService: StatusService,
    protected taskTypeService: TaskTypeService,
    protected priorityService: PriorityService,
    protected userService: UserService,
    protected attachmentService: AttachmentService,
    protected commentService: CommentService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ task }) => {
      this.updateForm(task);
    });
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.statusService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStatus[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStatus[]>) => response.body)
      )
      .subscribe((res: IStatus[]) => (this.statuses = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.taskTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITaskType[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITaskType[]>) => response.body)
      )
      .subscribe((res: ITaskType[]) => (this.tasktypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.priorityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPriority[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPriority[]>) => response.body)
      )
      .subscribe((res: IPriority[]) => (this.priorities = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.taskService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITask[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITask[]>) => response.body)
      )
      .subscribe((res: ITask[]) => (this.tasks = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.attachmentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAttachment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAttachment[]>) => response.body)
      )
      .subscribe((res: IAttachment[]) => (this.attachments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.commentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IComment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IComment[]>) => response.body)
      )
      .subscribe((res: IComment[]) => (this.comments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.teamService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(task: ITask) {
    this.editForm.patchValue({
      id: task.id,
      name: task.name,
      description: task.description,
      estimatedTime: task.estimatedTime,
      spentTime: task.spentTime,
      milestone: task.milestone,
      status: task.status,
      taskType: task.taskType,
      priority: task.priority,
      assignee: task.assignee,
      parent: task.parent,
      users: task.users
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
      description: this.editForm.get(['description']).value,
      estimatedTime: this.editForm.get(['estimatedTime']).value,
      spentTime: this.editForm.get(['spentTime']).value,
      milestone: this.editForm.get(['milestone']).value,
      status: this.editForm.get(['status']).value,
      taskType: this.editForm.get(['taskType']).value,
      priority: this.editForm.get(['priority']).value,
      assignee: this.editForm.get(['assignee']).value,
      parent: this.editForm.get(['parent']).value,
      users: this.editForm.get(['users']).value
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

  trackMilestoneById(index: number, item: IMilestone) {
    return item.id;
  }

  trackStatusById(index: number, item: IStatus) {
    return item.id;
  }

  trackTaskTypeById(index: number, item: ITaskType) {
    return item.id;
  }

  trackPriorityById(index: number, item: IPriority) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackTaskById(index: number, item: ITask) {
    return item.id;
  }

  trackAttachmentById(index: number, item: IAttachment) {
    return item.id;
  }

  trackCommentById(index: number, item: IComment) {
    return item.id;
  }

  trackTeamById(index: number, item: ITeam) {
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
