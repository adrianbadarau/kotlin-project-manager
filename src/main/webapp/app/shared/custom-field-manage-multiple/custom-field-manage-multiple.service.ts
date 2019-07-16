import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class CustomFieldManageMultipleService {
  public resourceUrl = `${SERVER_API_URL}api/excel-reader`;

  constructor(private httpService: HttpClient) {}

  async uploadFile(file) {
    const formData = new FormData();
    formData.append('template', file);
    return await this.httpService.post(this.resourceUrl + '/read-custom-table-template', formData).toPromise();
  }
}
