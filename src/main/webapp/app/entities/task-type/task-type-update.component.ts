import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ITaskType, TaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from './task-type.service';

@Component({
  selector: 'jhi-task-type-update',
  templateUrl: './task-type-update.component.html'
})
export class TaskTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected taskTypeService: TaskTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ taskType }) => {
      this.updateForm(taskType);
    });
  }

  updateForm(taskType: ITaskType) {
    this.editForm.patchValue({
      id: taskType.id,
      name: taskType.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const taskType = this.createFromForm();
    if (taskType.id !== undefined) {
      this.subscribeToSaveResponse(this.taskTypeService.update(taskType));
    } else {
      this.subscribeToSaveResponse(this.taskTypeService.create(taskType));
    }
  }

  private createFromForm(): ITaskType {
    return {
      ...new TaskType(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskType>>) {
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
