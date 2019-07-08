import { Component, OnInit } from '@angular/core';
import { Milestone } from 'app/shared/model/milestone.model';

@Component({
  selector: 'jhi-milestone-manage',
  templateUrl: './milestone-manage.component.html',
  styles: []
})
export class MilestoneManageComponent implements OnInit {
  milestone: Milestone = new Milestone();
  results: string[] = ['abadarau', 'testUser'];

  constructor() {}

  ngOnInit() {}

  onClickAssignToMe() {}

  search($event) {
    this.results = ['abadarau', 'testUser'];
  }
}
