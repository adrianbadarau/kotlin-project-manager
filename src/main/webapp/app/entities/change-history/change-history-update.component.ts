import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IChangeHistory, ChangeHistory } from 'app/shared/model/change-history.model';
import { ChangeHistoryService } from './change-history.service';

@Component({
  selector: 'jhi-change-history-update',
  templateUrl: './change-history-update.component.html'
})
export class ChangeHistoryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    subject: [null, [Validators.required]],
    fromValue: [],
    toValue: [null, [Validators.required]]
  });

  constructor(protected changeHistoryService: ChangeHistoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ changeHistory }) => {
      this.updateForm(changeHistory);
    });
  }

  updateForm(changeHistory: IChangeHistory) {
    this.editForm.patchValue({
      id: changeHistory.id,
      subject: changeHistory.subject,
      fromValue: changeHistory.fromValue,
      toValue: changeHistory.toValue
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const changeHistory = this.createFromForm();
    if (changeHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.changeHistoryService.update(changeHistory));
    } else {
      this.subscribeToSaveResponse(this.changeHistoryService.create(changeHistory));
    }
  }

  private createFromForm(): IChangeHistory {
    return {
      ...new ChangeHistory(),
      id: this.editForm.get(['id']).value,
      subject: this.editForm.get(['subject']).value,
      fromValue: this.editForm.get(['fromValue']).value,
      toValue: this.editForm.get(['toValue']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChangeHistory>>) {
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
