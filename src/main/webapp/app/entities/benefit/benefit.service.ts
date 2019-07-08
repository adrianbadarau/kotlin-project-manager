import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBenefit } from 'app/shared/model/benefit.model';

type EntityResponseType = HttpResponse<IBenefit>;
type EntityArrayResponseType = HttpResponse<IBenefit[]>;

@Injectable({ providedIn: 'root' })
export class BenefitService {
  public resourceUrl = SERVER_API_URL + 'api/benefits';

  constructor(protected http: HttpClient) {}

  create(benefit: IBenefit): Observable<EntityResponseType> {
    return this.http.post<IBenefit>(this.resourceUrl, benefit, { observe: 'response' });
  }

  update(benefit: IBenefit): Observable<EntityResponseType> {
    return this.http.put<IBenefit>(this.resourceUrl, benefit, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBenefit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBenefit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
