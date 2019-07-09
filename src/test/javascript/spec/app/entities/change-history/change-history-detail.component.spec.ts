/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { ChangeHistoryDetailComponent } from 'app/entities/change-history/change-history-detail.component';
import { ChangeHistory } from 'app/shared/model/change-history.model';

describe('Component Tests', () => {
  describe('ChangeHistory Management Detail Component', () => {
    let comp: ChangeHistoryDetailComponent;
    let fixture: ComponentFixture<ChangeHistoryDetailComponent>;
    const route = ({ data: of({ changeHistory: new ChangeHistory('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ChangeHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChangeHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChangeHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.changeHistory).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
