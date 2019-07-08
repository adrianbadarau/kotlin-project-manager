import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IMilestone, Milestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from 'app/entities/status';
import { IUser, UserService } from 'app/core';
import { IField } from 'app/shared/model/field.model';
import { FieldService } from 'app/entities/field';
import { IProjectUpdate } from 'app/shared/model/project-update.model';
import { ProjectUpdateService } from 'app/entities/project-update';
import { IPerformance } from 'app/shared/model/performance.model';
import { PerformanceService } from 'app/entities/performance';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment';
import { IDelivrable } from 'app/shared/model/delivrable.model';
import { DelivrableService } from 'app/entities/delivrable';

@Component({
  selector: 'jhi-milestone-update',
  templateUrl: './milestone-update.component.html'
})
export class MilestoneUpdateComponent implements OnInit {
  isSaving: boolean;

  statuses: IStatus[];

  users: IUser[];

  fields: IField[];

  projectupdates: IProjectUpdate[];

  performances: IPerformance[];

  comments: IComment[];

  delivrables: IDelivrable[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    target: [null, [Validators.required]],
    description: [],
    workstream: [],
    code: [],
    track: [],
    estimatedEndDate: [],
    actualEndDate: [],
    result: [],
    status: [],
    owner: [],
    fields: [],
    projectUpdates: [],
    performances: [],
    comments: [],
    delivrable: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected milestoneService: MilestoneService,
    protected statusService: StatusService,
    protected userService: UserService,
    protected fieldService: FieldService,
    protected projectUpdateService: ProjectUpdateService,
    protected performanceService: PerformanceService,
    protected commentService: CommentService,
    protected delivrableService: DelivrableService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ milestone }) => {
      this.updateForm(milestone);
    });
    this.statusService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStatus[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStatus[]>) => response.body)
      )
      .subscribe((res: IStatus[]) => (this.statuses = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.fieldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IField[]>) => mayBeOk.ok),
        map((response: HttpResponse<IField[]>) => response.body)
      )
      .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.projectUpdateService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProjectUpdate[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProjectUpdate[]>) => response.body)
      )
      .subscribe((res: IProjectUpdate[]) => (this.projectupdates = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.performanceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPerformance[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPerformance[]>) => response.body)
      )
      .subscribe((res: IPerformance[]) => (this.performances = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.commentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IComment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IComment[]>) => response.body)
      )
      .subscribe((res: IComment[]) => (this.comments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.delivrableService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDelivrable[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDelivrable[]>) => response.body)
      )
      .subscribe((res: IDelivrable[]) => (this.delivrables = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(milestone: IMilestone) {
    this.editForm.patchValue({
      id: milestone.id,
      name: milestone.name,
      target: milestone.target != null ? milestone.target.format(DATE_TIME_FORMAT) : null,
      description: milestone.description,
      workstream: milestone.workstream,
      code: milestone.code,
      track: milestone.track,
      estimatedEndDate: milestone.estimatedEndDate != null ? milestone.estimatedEndDate.format(DATE_TIME_FORMAT) : null,
      actualEndDate: milestone.actualEndDate != null ? milestone.actualEndDate.format(DATE_TIME_FORMAT) : null,
      result: milestone.result,
      status: milestone.status,
      owner: milestone.owner,
      fields: milestone.fields,
      projectUpdates: milestone.projectUpdates,
      performances: milestone.performances,
      comments: milestone.comments,
      delivrable: milestone.delivrable
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const milestone = this.createFromForm();
    if (milestone.id !== undefined) {
      this.subscribeToSaveResponse(this.milestoneService.update(milestone));
    } else {
      this.subscribeToSaveResponse(this.milestoneService.create(milestone));
    }
  }

  private createFromForm(): IMilestone {
    return {
      ...new Milestone(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      target: this.editForm.get(['target']).value != null ? moment(this.editForm.get(['target']).value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description']).value,
      workstream: this.editForm.get(['workstream']).value,
      code: this.editForm.get(['code']).value,
      track: this.editForm.get(['track']).value,
      estimatedEndDate:
        this.editForm.get(['estimatedEndDate']).value != null
          ? moment(this.editForm.get(['estimatedEndDate']).value, DATE_TIME_FORMAT)
          : undefined,
      actualEndDate:
        this.editForm.get(['actualEndDate']).value != null
          ? moment(this.editForm.get(['actualEndDate']).value, DATE_TIME_FORMAT)
          : undefined,
      result: this.editForm.get(['result']).value,
      status: this.editForm.get(['status']).value,
      owner: this.editForm.get(['owner']).value,
      fields: this.editForm.get(['fields']).value,
      projectUpdates: this.editForm.get(['projectUpdates']).value,
      performances: this.editForm.get(['performances']).value,
      comments: this.editForm.get(['comments']).value,
      delivrable: this.editForm.get(['delivrable']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMilestone>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackFieldById(index: number, item: IField) {
    return item.id;
  }

  trackProjectUpdateById(index: number, item: IProjectUpdate) {
    return item.id;
  }

  trackPerformanceById(index: number, item: IPerformance) {
    return item.id;
  }

  trackCommentById(index: number, item: IComment) {
    return item.id;
  }

  trackDelivrableById(index: number, item: IDelivrable) {
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
