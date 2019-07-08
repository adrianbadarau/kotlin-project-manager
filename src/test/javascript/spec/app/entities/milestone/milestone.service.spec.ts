/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { MilestoneService } from 'app/entities/milestone/milestone.service';
import { IMilestone, Milestone } from 'app/shared/model/milestone.model';

describe('Service Tests', () => {
  describe('Milestone Service', () => {
    let injector: TestBed;
    let service: MilestoneService;
    let httpMock: HttpTestingController;
    let elemDefault: IMilestone;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(MilestoneService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Milestone(
        'ID',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            target: currentDate.format(DATE_TIME_FORMAT),
            estimatedEndDate: currentDate.format(DATE_TIME_FORMAT),
            actualEndDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find('123')
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Milestone', async () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            target: currentDate.format(DATE_TIME_FORMAT),
            estimatedEndDate: currentDate.format(DATE_TIME_FORMAT),
            actualEndDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            target: currentDate,
            estimatedEndDate: currentDate,
            actualEndDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Milestone(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Milestone', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            target: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            workstream: 'BBBBBB',
            code: 'BBBBBB',
            track: 'BBBBBB',
            estimatedEndDate: currentDate.format(DATE_TIME_FORMAT),
            actualEndDate: currentDate.format(DATE_TIME_FORMAT),
            result: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            target: currentDate,
            estimatedEndDate: currentDate,
            actualEndDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Milestone', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            target: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            workstream: 'BBBBBB',
            code: 'BBBBBB',
            track: 'BBBBBB',
            estimatedEndDate: currentDate.format(DATE_TIME_FORMAT),
            actualEndDate: currentDate.format(DATE_TIME_FORMAT),
            result: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            target: currentDate,
            estimatedEndDate: currentDate,
            actualEndDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Milestone', async () => {
        const rxPromise = service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
