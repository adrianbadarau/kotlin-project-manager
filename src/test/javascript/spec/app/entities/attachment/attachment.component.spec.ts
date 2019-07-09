/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { AttachmentComponent } from 'app/entities/attachment/attachment.component';
import { AttachmentService } from 'app/entities/attachment/attachment.service';
import { Attachment } from 'app/shared/model/attachment.model';

describe('Component Tests', () => {
  describe('Attachment Management Component', () => {
    let comp: AttachmentComponent;
    let fixture: ComponentFixture<AttachmentComponent>;
    let service: AttachmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [AttachmentComponent],
        providers: []
      })
        .overrideTemplate(AttachmentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttachmentComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttachmentService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Attachment('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.attachments[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
