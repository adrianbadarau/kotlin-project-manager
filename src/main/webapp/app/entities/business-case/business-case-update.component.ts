import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBusinessCase, BusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from './business-case.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-business-case-update',
  templateUrl: './business-case-update.component.html'
})
export class BusinessCaseUpdateComponent implements OnInit {
  isSaving: boolean;

  projects: IProject[];

  editForm = this.fb.group({
    id: [],
    summary: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected businessCaseService: BusinessCaseService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ businessCase }) => {
      this.updateForm(businessCase);
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(businessCase: IBusinessCase) {
    this.editForm.patchValue({
      id: businessCase.id,
      summary: businessCase.summary
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const businessCase = this.createFromForm();
    if (businessCase.id !== undefined) {
      this.subscribeToSaveResponse(this.businessCaseService.update(businessCase));
    } else {
      this.subscribeToSaveResponse(this.businessCaseService.create(businessCase));
    }
  }

  private createFromForm(): IBusinessCase {
    return {
      ...new BusinessCase(),
      id: this.editForm.get(['id']).value,
      summary: this.editForm.get(['summary']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessCase>>) {
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
}
