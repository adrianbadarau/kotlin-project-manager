/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { ProjectUpdateDetailComponent } from 'app/entities/project-update/project-update-detail.component';
import { ProjectUpdate } from 'app/shared/model/project-update.model';

describe('Component Tests', () => {
  describe('ProjectUpdate Management Detail Component', () => {
    let comp: ProjectUpdateDetailComponent;
    let fixture: ComponentFixture<ProjectUpdateDetailComponent>;
    const route = ({ data: of({ projectUpdate: new ProjectUpdate('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [ProjectUpdateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProjectUpdateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjectUpdateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.projectUpdate).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
