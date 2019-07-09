import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITaskType } from 'app/shared/model/task-type.model';

type EntityResponseType = HttpResponse<ITaskType>;
type EntityArrayResponseType = HttpResponse<ITaskType[]>;

@Injectable({ providedIn: 'root' })
export class TaskTypeService {
  public resourceUrl = SERVER_API_URL + 'api/task-types';

  constructor(protected http: HttpClient) {}

  create(taskType: ITaskType): Observable<EntityResponseType> {
    return this.http.post<ITaskType>(this.resourceUrl, taskType, { observe: 'response' });
  }

  update(taskType: ITaskType): Observable<EntityResponseType> {
    return this.http.put<ITaskType>(this.resourceUrl, taskType, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ITaskType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaskType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
