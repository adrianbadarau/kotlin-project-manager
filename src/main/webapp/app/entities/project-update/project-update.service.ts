import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProjectUpdate } from 'app/shared/model/project-update.model';

type EntityResponseType = HttpResponse<IProjectUpdate>;
type EntityArrayResponseType = HttpResponse<IProjectUpdate[]>;

@Injectable({ providedIn: 'root' })
export class ProjectUpdateService {
  public resourceUrl = SERVER_API_URL + 'api/project-updates';

  constructor(protected http: HttpClient) {}

  create(projectUpdate: IProjectUpdate): Observable<EntityResponseType> {
    return this.http.post<IProjectUpdate>(this.resourceUrl, projectUpdate, { observe: 'response' });
  }

  update(projectUpdate: IProjectUpdate): Observable<EntityResponseType> {
    return this.http.put<IProjectUpdate>(this.resourceUrl, projectUpdate, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IProjectUpdate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProjectUpdate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
