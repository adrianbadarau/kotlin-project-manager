/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { MilestoneComponent } from 'app/entities/milestone/milestone.component';
import { MilestoneService } from 'app/entities/milestone/milestone.service';
import { Milestone } from 'app/shared/model/milestone.model';

describe('Component Tests', () => {
  describe('Milestone Management Component', () => {
    let comp: MilestoneComponent;
    let fixture: ComponentFixture<MilestoneComponent>;
    let service: MilestoneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [MilestoneComponent],
        providers: []
      })
        .overrideTemplate(MilestoneComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MilestoneComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MilestoneService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Milestone('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.milestones[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
