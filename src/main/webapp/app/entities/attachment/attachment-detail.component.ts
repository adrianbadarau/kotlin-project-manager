import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttachment } from 'app/shared/model/attachment.model';

@Component({
  selector: 'jhi-attachment-detail',
  templateUrl: './attachment-detail.component.html'
})
export class AttachmentDetailComponent implements OnInit {
  attachment: IAttachment;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.attachment = attachment;
    });
  }

  previousState() {
    window.history.back();
  }
}
