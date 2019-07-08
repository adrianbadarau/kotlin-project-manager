/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PmAppTestModule } from '../../../test.module';
import { DelivrableDetailComponent } from 'app/entities/delivrable/delivrable-detail.component';
import { Delivrable } from 'app/shared/model/delivrable.model';

describe('Component Tests', () => {
  describe('Delivrable Management Detail Component', () => {
    let comp: DelivrableDetailComponent;
    let fixture: ComponentFixture<DelivrableDetailComponent>;
    const route = ({ data: of({ delivrable: new Delivrable('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PmAppTestModule],
        declarations: [DelivrableDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DelivrableDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DelivrableDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.delivrable).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
