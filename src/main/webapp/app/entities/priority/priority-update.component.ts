import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPriority, Priority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';

@Component({
  selector: 'jhi-priority-update',
  templateUrl: './priority-update.component.html'
})
export class PriorityUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected priorityService: PriorityService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ priority }) => {
      this.updateForm(priority);
    });
  }

  updateForm(priority: IPriority) {
    this.editForm.patchValue({
      id: priority.id,
      name: priority.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const priority = this.createFromForm();
    if (priority.id !== undefined) {
      this.subscribeToSaveResponse(this.priorityService.update(priority));
    } else {
      this.subscribeToSaveResponse(this.priorityService.create(priority));
    }
  }

  private createFromForm(): IPriority {
    return {
      ...new Priority(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriority>>) {
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
