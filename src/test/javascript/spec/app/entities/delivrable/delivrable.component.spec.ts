/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PmAppTestModule } from '../../../test.module';
import { DelivrableComponent } from 'app/entities/delivrable/delivrable.component';
import { DelivrableService } from 'app/entities/delivrable/delivrable.service';
import { Delivrable } from 'app/shared/model/delivrable.model';

describe('Component Tests', () => {
  describe('Delivrable Management Component', () => {
    let comp: DelivrableComponent;
    let fixture: ComponentFixture<DelivrableComponent>;
    let service: DelivrableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [DelivrableComponent],
        providers: []
      })
        .overrideTemplate(DelivrableComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DelivrableComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DelivrableService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Delivrable('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.delivrables[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
