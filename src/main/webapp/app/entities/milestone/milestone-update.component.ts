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
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { IField } from 'app/shared/model/field.model';
import { FieldService } from 'app/entities/field';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-milestone-update',
  templateUrl: './milestone-update.component.html'
})
export class MilestoneUpdateComponent implements OnInit {
  milestone: IMilestone;
  isSaving: boolean;

  projects: IProject[];

  fields: IField[];

  teams: ITeam[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    estimatedEndDate: [],
    endDate: [],
    project: [],
    fields: [],
    teams: [],
    users: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected milestoneService: MilestoneService,
    protected projectService: ProjectService,
    protected fieldService: FieldService,
    protected teamService: TeamService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ milestone }) => {
      this.updateForm(milestone);
      this.milestone = milestone;
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.fieldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IField[]>) => mayBeOk.ok),
        map((response: HttpResponse<IField[]>) => response.body)
      )
      .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.teamService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(milestone: IMilestone) {
    this.editForm.patchValue({
      id: milestone.id,
      name: milestone.name,
      estimatedEndDate: milestone.estimatedEndDate != null ? milestone.estimatedEndDate.format(DATE_TIME_FORMAT) : null,
      endDate: milestone.endDate != null ? milestone.endDate.format(DATE_TIME_FORMAT) : null,
      project: milestone.project,
      fields: milestone.fields,
      teams: milestone.teams,
      users: milestone.users
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
    const entity = {
      ...new Milestone(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      estimatedEndDate:
        this.editForm.get(['estimatedEndDate']).value != null
          ? moment(this.editForm.get(['estimatedEndDate']).value, DATE_TIME_FORMAT)
          : undefined,
      endDate: this.editForm.get(['endDate']).value != null ? moment(this.editForm.get(['endDate']).value, DATE_TIME_FORMAT) : undefined,
      project: this.editForm.get(['project']).value,
      fields: this.editForm.get(['fields']).value,
      teams: this.editForm.get(['teams']).value,
      users: this.editForm.get(['users']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMilestone>>) {
    result.subscribe((res: HttpResponse<IMilestone>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackFieldById(index: number, item: IField) {
    return item.id;
  }

  trackTeamById(index: number, item: ITeam) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
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
