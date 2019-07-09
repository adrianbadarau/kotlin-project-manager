import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITeam, Team } from 'app/shared/model/team.model';
import { TeamService } from './team.service';
import { IUser, UserService } from 'app/core';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';

@Component({
  selector: 'jhi-team-update',
  templateUrl: './team-update.component.html'
})
export class TeamUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  tasks: ITask[];

  milestones: IMilestone[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    users: [],
    tasks: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected teamService: TeamService,
    protected userService: UserService,
    protected taskService: TaskService,
    protected milestoneService: MilestoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ team }) => {
      this.updateForm(team);
    });
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
    this.milestoneService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMilestone[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMilestone[]>) => response.body)
      )
      .subscribe((res: IMilestone[]) => (this.milestones = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(team: ITeam) {
    this.editForm.patchValue({
      id: team.id,
      name: team.name,
      users: team.users,
      tasks: team.tasks
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const team = this.createFromForm();
    if (team.id !== undefined) {
      this.subscribeToSaveResponse(this.teamService.update(team));
    } else {
      this.subscribeToSaveResponse(this.teamService.create(team));
    }
  }

  private createFromForm(): ITeam {
    return {
      ...new Team(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      users: this.editForm.get(['users']).value,
      tasks: this.editForm.get(['tasks']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>) {
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

  trackUserById(index: number, item: IUser) {
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
