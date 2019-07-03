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
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/milestones';

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

  find(id: number): Observable<EntityResponseType> {
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

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMilestone[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(milestone: IMilestone): IMilestone {
    const copy: IMilestone = Object.assign({}, milestone, {
      estimatedEndDate:
        milestone.estimatedEndDate != null && milestone.estimatedEndDate.isValid() ? milestone.estimatedEndDate.toJSON() : null,
      endDate: milestone.endDate != null && milestone.endDate.isValid() ? milestone.endDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.estimatedEndDate = res.body.estimatedEndDate != null ? moment(res.body.estimatedEndDate) : null;
      res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((milestone: IMilestone) => {
        milestone.estimatedEndDate = milestone.estimatedEndDate != null ? moment(milestone.estimatedEndDate) : null;
        milestone.endDate = milestone.endDate != null ? moment(milestone.endDate) : null;
      });
    }
    return res;
  }
}
