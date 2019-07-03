import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IField } from 'app/shared/model/field.model';

type EntityResponseType = HttpResponse<IField>;
type EntityArrayResponseType = HttpResponse<IField[]>;

@Injectable({ providedIn: 'root' })
export class FieldService {
  public resourceUrl = SERVER_API_URL + 'api/fields';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/fields';

  constructor(protected http: HttpClient) {}

  create(field: IField): Observable<EntityResponseType> {
    return this.http.post<IField>(this.resourceUrl, field, { observe: 'response' });
  }

  update(field: IField): Observable<EntityResponseType> {
    return this.http.put<IField>(this.resourceUrl, field, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IField>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IField[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IField[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
