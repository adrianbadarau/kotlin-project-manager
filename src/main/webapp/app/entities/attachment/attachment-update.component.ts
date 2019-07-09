import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAttachment, Attachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from './attachment.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';

@Component({
  selector: 'jhi-attachment-update',
  templateUrl: './attachment-update.component.html'
})
export class AttachmentUpdateComponent implements OnInit {
  isSaving: boolean;

  projects: IProject[];

  tasks: ITask[];

  milestones: IMilestone[];

  editForm = this.fb.group({
    id: [],
    filename: [null, [Validators.required]],
    projects: [],
    tasks: [],
    milestones: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected attachmentService: AttachmentService,
    protected projectService: ProjectService,
    protected taskService: TaskService,
    protected milestoneService: MilestoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.updateForm(attachment);
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.taskService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITask[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITask[]>) => response.body)
      )
      .subscribe((res: ITask[]) => (this.tasks = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(attachment: IAttachment) {
    this.editForm.patchValue({
      id: attachment.id,
      filename: attachment.filename,
      projects: attachment.projects,
      tasks: attachment.tasks,
      milestones: attachment.milestones
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const attachment = this.createFromForm();
    if (attachment.id !== undefined) {
      this.subscribeToSaveResponse(this.attachmentService.update(attachment));
    } else {
      this.subscribeToSaveResponse(this.attachmentService.create(attachment));
    }
  }

  private createFromForm(): IAttachment {
    return {
      ...new Attachment(),
      id: this.editForm.get(['id']).value,
      filename: this.editForm.get(['filename']).value,
      projects: this.editForm.get(['projects']).value,
      tasks: this.editForm.get(['tasks']).value,
      milestones: this.editForm.get(['milestones']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachment>>) {
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

  trackProjectById(index: number, item: IProject) {
    return item.id;
  }

  trackTaskById(index: number, item: ITask) {
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
