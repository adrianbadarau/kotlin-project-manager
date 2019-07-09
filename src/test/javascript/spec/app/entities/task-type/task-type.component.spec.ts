/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { TaskTypeComponent } from 'app/entities/task-type/task-type.component';
import { TaskTypeService } from 'app/entities/task-type/task-type.service';
import { TaskType } from 'app/shared/model/task-type.model';

describe('Component Tests', () => {
  describe('TaskType Management Component', () => {
    let comp: TaskTypeComponent;
    let fixture: ComponentFixture<TaskTypeComponent>;
    let service: TaskTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [TaskTypeComponent],
        providers: []
      })
        .overrideTemplate(TaskTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TaskType('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.taskTypes[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
