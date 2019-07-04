import { Component, OnInit } from '@angular/core';
import { JhiMainComponent } from 'app/layouts';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { Title } from '@angular/platform-browser';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'jhi-main-custom',
  templateUrl: './main-custom.component.html',
  styles: []
})
export class MainCustomComponent extends JhiMainComponent implements OnInit {
  items: MenuItem[];

  constructor(titleService: Title, router: Router) {
    super(titleService, router);
  }

  ngOnInit() {
    super.ngOnInit();
    this.items = [
      {
        label: 'Dashboard',
        icon: 'pi pi-pw pi-sitemap',
        routerLink: ['/']
      },
      {
        label: 'Projects',
        icon: 'pi pi-fw pi-pencil',
        items: [
          {
            label: 'New',
            icon: 'pi pi-fw pi-plus',
            routerLink: ['project/new']
          },
          {
            label: 'View All',
            icon: 'pi pi-fw pi-list',
            routerLink: ['project']
          }
        ]
      },
      {
        label: 'Milestones',
        icon: 'pi pi-fw pi-pencil',
        items: [
          {
            label: 'New',
            icon: 'pi pi-fw pi-plus',
            routerLink: ['milestone/new']
          },
          {
            label: 'View All',
            icon: 'pi pi-fw pi-list',
            routerLink: ['milestone']
          }
        ]
      },
      {
        label: 'Tickets',
        icon: 'pi pi-fw pi-question',
        items: [
          {
            label: 'New',
            icon: 'pi pi-fw pi-plus',
            routerLink: ['task/new']
          },
          {
            label: 'View All',
            icon: 'pi pi-fw pi-list',
            routerLink: ['task']
          }
        ]
      },
      {
        label: 'Users&Teams',
        icon: 'pi pi-fw pi-cog',
        items: [
          {
            label: 'Teams',
            icon: 'pi pi-fw pi-users',
            items: [
              {
                label: 'New',
                icon: 'pi pi-fw pi-plus',
                routerLink: ['team/new']
              },
              {
                label: 'View All',
                icon: 'pi pi-fw pi-list',
                routerLink: ['team']
              }
            ]
          },
          {
            label: 'Users',
            icon: 'pi pi-fw pi-user',
            items: [
              {
                label: 'New',
                icon: 'pi pi-fw pi-plus',
                routerLink: ['admin/user-management/new']
              },
              {
                label: 'View All',
                icon: 'pi pi-fw pi-list',
                routerLink: ['admin/user-management']
              }
            ]
          }
        ]
      }
    ];
  }
}
