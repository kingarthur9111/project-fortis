import { Route } from 'react-router'
import { AppPage } from './AppPage';
import { DashboardPage } from './DashboardPage';
import { FactsPage } from './FactsPage';
import { AdminPage } from './AdminPage';
import { NotFoundPage } from './NotFoundPage';
import React from 'react';

export const routes = (
  <Route path="/" component={AppPage} linkLabel="My App" icon="fa fa-share-alt-square fa">
    <Route path="site/:category(/share/:sharedViewState)" component={DashboardPage} />
    <Route path="site/:category/facts" component={FactsPage} />
    <Route path="site/:category/admin" component={AdminPage} />
    <Route path="*" component={NotFoundPage} />
  </Route>
);
