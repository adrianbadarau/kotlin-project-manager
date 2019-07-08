import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDelivrable } from 'app/shared/model/delivrable.model';

type EntityResponseType = HttpResponse<IDelivrable>;
type EntityArrayResponseType = HttpResponse<IDelivrable[]>;

@Injectable({ providedIn: 'root' })
export class DelivrableService {
  public resourceUrl = SERVER_API_URL + 'api/delivrables';

  constructor(protected http: HttpClient) {}

  create(delivrable: IDelivrable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delivrable);
    return this.http
      .post<IDelivrable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(delivrable: IDelivrable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delivrable);
    return this.http
      .put<IDelivrable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IDelivrable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDelivrable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(delivrable: IDelivrable): IDelivrable {
    const copy: IDelivrable = Object.assign({}, delivrable, {
      target: delivrable.target != null && delivrable.target.isValid() ? delivrable.target.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.target = res.body.target != null ? moment(res.body.target) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((delivrable: IDelivrable) => {
        delivrable.target = delivrable.target != null ? moment(delivrable.target) : null;
      });
    }
    return res;
  }
}
