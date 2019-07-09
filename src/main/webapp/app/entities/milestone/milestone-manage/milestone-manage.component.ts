import { Component, OnInit } from '@angular/core';
import { Milestone } from 'app/shared/model/milestone.model';

@Component({
  selector: 'jhi-milestone-manage',
  templateUrl: './milestone-manage.component.html',
  styleUrls: ['./milestone-manage.component.scss']
})
export class MilestoneManageComponent implements OnInit {
  milestone: Milestone;
  results: string[] = ['abadarau', 'testUser'];

  constructor() {}

  ngOnInit() {
    this.milestone = new Milestone();
  }

  onClickAssignToMe() {}

  search(event) {
    this.results = ['test', 'testAgain'];
  }
}
