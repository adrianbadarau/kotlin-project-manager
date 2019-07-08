import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMilestone } from 'app/shared/model/milestone.model';

type EntityResponseType = HttpResponse<IMilestone>;
type EntityArrayResponseType = HttpResponse<IMilestone[]>;

@Injectable({ providedIn: 'root' })
export class MilestoneService {
  public resourceUrl = SERVER_API_URL + 'api/milestones';

  constructor(protected http: HttpClient) {}

  create(milestone: IMilestone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(milestone);
    return this.http
      .post<IMilestone>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(milestone: IMilestone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(milestone);
    return this.http
      .put<IMilestone>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IMilestone>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMilestone[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(milestone: IMilestone): IMilestone {
    const copy: IMilestone = Object.assign({}, milestone, {
      target: milestone.target != null && milestone.target.isValid() ? milestone.target.toJSON() : null,
      estimatedEndDate:
        milestone.estimatedEndDate != null && milestone.estimatedEndDate.isValid() ? milestone.estimatedEndDate.toJSON() : null,
      actualEndDate: milestone.actualEndDate != null && milestone.actualEndDate.isValid() ? milestone.actualEndDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.target = res.body.target != null ? moment(res.body.target) : null;
      res.body.estimatedEndDate = res.body.estimatedEndDate != null ? moment(res.body.estimatedEndDate) : null;
      res.body.actualEndDate = res.body.actualEndDate != null ? moment(res.body.actualEndDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((milestone: IMilestone) => {
        milestone.target = milestone.target != null ? moment(milestone.target) : null;
        milestone.estimatedEndDate = milestone.estimatedEndDate != null ? moment(milestone.estimatedEndDate) : null;
        milestone.actualEndDate = milestone.actualEndDate != null ? moment(milestone.actualEndDate) : null;
      });
    }
    return res;
  }
}
