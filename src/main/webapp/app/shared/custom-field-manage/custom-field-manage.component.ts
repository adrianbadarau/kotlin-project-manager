import { Component, Input, OnInit } from '@angular/core';
import { Field, IField } from 'app/shared/model/field.model';

@Component({
  selector: 'jhi-custom-field-manage',
  templateUrl: './custom-field-manage.component.html',
  styles: []
})
export class CustomFieldManageComponent implements OnInit {
  @Input() existingFields: IField[] = [];
  newField: boolean;
  field: IField = new Field();
  selectedField: IField = new Field();
  cols: any[];
  displayDialog: boolean;

  constructor() {}

  ngOnInit() {
    this.cols = [{ field: 'name', header: 'Name' }, { field: 'data', header: 'Value' }];
  }

  showDialogToAdd() {
    this.newField = true;
    this.field = new Field();
    this.displayDialog = true;
  }

  save() {
    if (this.newField) {
      this.existingFields.push(this.field);
    } else {
      this.existingFields[this.existingFields.indexOf(this.selectedField)] = this.field;
    }
    this.field = null;
    this.displayDialog = false;
  }

  delete() {
    const index = this.existingFields.indexOf(this.selectedField);
    this.existingFields = this.existingFields.filter((val, i) => i != index);
    this.field = null;
    this.displayDialog = false;
  }

  onRowSelect(event) {
    this.newField = false;
    this.field = this.cloneField(event.data);
    this.displayDialog = true;
  }

  private cloneField(c: IField): IField {
    const field = {};
    for (let prop in c) {
      field[prop] = c[prop];
    }
    return field;
  }
}
