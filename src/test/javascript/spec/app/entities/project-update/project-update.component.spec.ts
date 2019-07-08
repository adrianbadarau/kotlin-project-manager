/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { ProjectUpdateComponent } from 'app/entities/project-update/project-update.component';
import { ProjectUpdateService } from 'app/entities/project-update/project-update.service';
import { ProjectUpdate } from 'app/shared/model/project-update.model';

describe('Component Tests', () => {
  describe('ProjectUpdate Management Component', () => {
    let comp: ProjectUpdateComponent;
    let fixture: ComponentFixture<ProjectUpdateComponent>;
    let service: ProjectUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ProjectUpdateComponent],
        providers: []
      })
        .overrideTemplate(ProjectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjectUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProjectUpdateService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProjectUpdate('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.projectUpdates[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
