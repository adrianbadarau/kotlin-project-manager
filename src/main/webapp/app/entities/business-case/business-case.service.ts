import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBusinessCase } from 'app/shared/model/business-case.model';

type EntityResponseType = HttpResponse<IBusinessCase>;
type EntityArrayResponseType = HttpResponse<IBusinessCase[]>;

@Injectable({ providedIn: 'root' })
export class BusinessCaseService {
  public resourceUrl = SERVER_API_URL + 'api/business-cases';

  constructor(protected http: HttpClient) {}

  create(businessCase: IBusinessCase): Observable<EntityResponseType> {
    return this.http.post<IBusinessCase>(this.resourceUrl, businessCase, { observe: 'response' });
  }

  update(businessCase: IBusinessCase): Observable<EntityResponseType> {
    return this.http.put<IBusinessCase>(this.resourceUrl, businessCase, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBusinessCase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessCase[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
