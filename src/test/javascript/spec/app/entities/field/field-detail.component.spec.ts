/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KotlinPmTestModule } from '../../../test.module';
import { FieldDetailComponent } from 'app/entities/field/field-detail.component';
import { Field } from 'app/shared/model/field.model';

describe('Component Tests', () => {
  describe('Field Management Detail Component', () => {
    let comp: FieldDetailComponent;
    let fixture: ComponentFixture<FieldDetailComponent>;
    const route = ({ data: of({ field: new Field(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KotlinPmTestModule],
        declarations: [FieldDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FieldDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FieldDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.field).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
