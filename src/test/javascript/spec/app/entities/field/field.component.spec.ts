/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { FieldComponent } from 'app/entities/field/field.component';
import { FieldService } from 'app/entities/field/field.service';
import { Field } from 'app/shared/model/field.model';

describe('Component Tests', () => {
  describe('Field Management Component', () => {
    let comp: FieldComponent;
    let fixture: ComponentFixture<FieldComponent>;
    let service: FieldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [FieldComponent],
        providers: []
      })
        .overrideTemplate(FieldComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FieldComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FieldService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Field('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fields[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
