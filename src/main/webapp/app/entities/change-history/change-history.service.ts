import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IChangeHistory } from 'app/shared/model/change-history.model';

type EntityResponseType = HttpResponse<IChangeHistory>;
type EntityArrayResponseType = HttpResponse<IChangeHistory[]>;

@Injectable({ providedIn: 'root' })
export class ChangeHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/change-histories';

  constructor(protected http: HttpClient) {}

  create(changeHistory: IChangeHistory): Observable<EntityResponseType> {
    return this.http.post<IChangeHistory>(this.resourceUrl, changeHistory, { observe: 'response' });
  }

  update(changeHistory: IChangeHistory): Observable<EntityResponseType> {
    return this.http.put<IChangeHistory>(this.resourceUrl, changeHistory, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IChangeHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChangeHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
