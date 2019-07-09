/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { ChangeHistoryComponent } from 'app/entities/change-history/change-history.component';
import { ChangeHistoryService } from 'app/entities/change-history/change-history.service';
import { ChangeHistory } from 'app/shared/model/change-history.model';

describe('Component Tests', () => {
  describe('ChangeHistory Management Component', () => {
    let comp: ChangeHistoryComponent;
    let fixture: ComponentFixture<ChangeHistoryComponent>;
    let service: ChangeHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ChangeHistoryComponent],
        providers: []
      })
        .overrideTemplate(ChangeHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChangeHistoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChangeHistoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ChangeHistory('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.changeHistories[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
