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
import { IUser, UserService } from 'app/core';
import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from 'app/entities/status';
import { IField } from 'app/shared/model/field.model';
import { FieldService } from 'app/entities/field';
import { IAttachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from 'app/entities/attachment';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit {
  project: IProject;
  isSaving: boolean;

  users: IUser[];

  statuses: IStatus[];

  fields: IField[];

  attachments: IAttachment[];

  comments: IComment[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    code: [null, [Validators.required]],
    description: [],
    estimatedEndDate: [],
    owner: [],
    status: [],
    fields: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected projectService: ProjectService,
    protected userService: UserService,
    protected statusService: StatusService,
    protected fieldService: FieldService,
    protected attachmentService: AttachmentService,
    protected commentService: CommentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ project }) => {
      this.updateForm(project);
      this.project = project;
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.statusService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStatus[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStatus[]>) => response.body)
      )
      .subscribe((res: IStatus[]) => (this.statuses = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.fieldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IField[]>) => mayBeOk.ok),
        map((response: HttpResponse<IField[]>) => response.body)
      )
      .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
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
  }

  updateForm(project: IProject) {
    this.editForm.patchValue({
      id: project.id,
      name: project.name,
      code: project.code,
      description: project.description,
      estimatedEndDate: project.estimatedEndDate != null ? project.estimatedEndDate.format(DATE_TIME_FORMAT) : null,
      owner: project.owner,
      status: project.status,
      fields: project.fields
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
    const entity = {
      ...new Project(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      code: this.editForm.get(['code']).value,
      description: this.editForm.get(['description']).value,
      estimatedEndDate:
        this.editForm.get(['estimatedEndDate']).value != null
          ? moment(this.editForm.get(['estimatedEndDate']).value, DATE_TIME_FORMAT)
          : undefined,
      owner: this.editForm.get(['owner']).value,
      status: this.editForm.get(['status']).value,
      fields: this.editForm.get(['fields']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
    result.subscribe((res: HttpResponse<IProject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackStatusById(index: number, item: IStatus) {
    return item.id;
  }

  trackFieldById(index: number, item: IField) {
    return item.id;
  }

  trackAttachmentById(index: number, item: IAttachment) {
    return item.id;
  }

  trackCommentById(index: number, item: IComment) {
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
