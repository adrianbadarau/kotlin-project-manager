import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBenefitType } from 'app/shared/model/benefit-type.model';

type EntityResponseType = HttpResponse<IBenefitType>;
type EntityArrayResponseType = HttpResponse<IBenefitType[]>;

@Injectable({ providedIn: 'root' })
export class BenefitTypeService {
  public resourceUrl = SERVER_API_URL + 'api/benefit-types';

  constructor(protected http: HttpClient) {}

  create(benefitType: IBenefitType): Observable<EntityResponseType> {
    return this.http.post<IBenefitType>(this.resourceUrl, benefitType, { observe: 'response' });
  }

  update(benefitType: IBenefitType): Observable<EntityResponseType> {
    return this.http.put<IBenefitType>(this.resourceUrl, benefitType, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBenefitType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBenefitType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
