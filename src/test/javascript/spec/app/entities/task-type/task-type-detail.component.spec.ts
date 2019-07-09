/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { TaskTypeDetailComponent } from 'app/entities/task-type/task-type-detail.component';
import { TaskType } from 'app/shared/model/task-type.model';

describe('Component Tests', () => {
  describe('TaskType Management Detail Component', () => {
    let comp: TaskTypeDetailComponent;
    let fixture: ComponentFixture<TaskTypeDetailComponent>;
    const route = ({ data: of({ taskType: new TaskType('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [TaskTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TaskTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaskTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taskType).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
