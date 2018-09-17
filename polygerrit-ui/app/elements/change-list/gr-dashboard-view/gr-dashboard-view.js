/**
 * @license
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function() {
  'use strict';

  const PROJECT_PLACEHOLDER_PATTERN = /\$\{project\}/g;
  const USER_PLACEHOLDER_PATTERN = /\$\{user\}/g;

  // NOTE: These queries are tested in Java. Any changes made to definitions
  // here require corresponding changes to:
  // javatests/com/google/gerrit/server/query/change/AbstractQueryChangesTest.java
  const DEFAULT_SECTIONS = [
    {
      // Changes with unpublished draft comments. This section is omitted when
      // viewing other users, so we don't need to filter anything out.
      name: 'Has unpublished drafts',
      query: 'has:draft',
      selfOnly: true,
      hideIfEmpty: true,
    },
    {
      // WIP open changes owned by viewing user. This section is omitted when
      // viewing other users, so we don't need to filter anything out.
      name: 'Work in progress',
      query: 'is:open owner:${user} is:wip',
      selfOnly: true,
      hideIfEmpty: true,
    },
    {
      // Non-WIP open changes owned by viewed user. Filter out changes ignored
      // by the viewing user.
      name: 'Outgoing reviews',
      query: 'is:open owner:${user} -is:wip -is:ignored',
    },
    {
      // Non-WIP open changes not owned by the viewed user, that the viewed user
      // is associated with (as either a reviewer or the assignee). Changes
      // ignored by the viewing user are filtered out.
      name: 'Incoming reviews',
      query: 'is:open -owner:${user} -is:wip -is:ignored ' +
          '(reviewer:${user} OR assignee:${user})',
    },
    {
      // Open changes the viewed user is CCed on. Changes ignored by the viewing
      // user are filtered out.
      name: 'CCed on',
      query: 'is:open -is:ignored cc:${user}',
    },
    {
      name: 'Recently closed',
      // Closed changes where viewed user is owner, reviewer, or assignee.
      // Changes ignored by the viewing user are filtered out, and so are WIP
      // changes not owned by the viewing user (the one instance of
      // 'owner:self' is intentional and implements this logic).
      query: 'is:closed -is:ignored (-is:wip OR owner:self) ' +
          '(owner:${user} OR reviewer:${user} OR assignee:${user} ' +
          'OR cc:${user})',
      suffixForDashboard: '-age:4w limit:10',
    },
  ];

  Polymer({
    is: 'gr-dashboard-view',

    /**
     * Fired when the title of the page should change.
     *
     * @event title-change
     */

    properties: {
      account: {
        type: Object,
        value: null,
      },
      preferences: Object,
      /** @type {{ selectedChangeIndex: number }} */
      viewState: Object,

      /** @type {{ user: string }} */
      params: {
        type: Object,
      },

      _results: Array,
      _sectionMetadata: {
        type: Array,
        value() { return DEFAULT_SECTIONS; },
      },

      /**
       * For showing a "loading..." string during ajax requests.
       */
      _loading: {
        type: Boolean,
        value: true,
      },
    },

    observers: [
      '_paramsChanged(params.*)',
    ],

    behaviors: [
      Gerrit.RESTClientBehavior,
    ],

    get options() {
      return this.listChangesOptionsToHex(
          this.ListChangesOption.LABELS,
          this.ListChangesOption.DETAILED_ACCOUNTS,
          this.ListChangesOption.REVIEWED
      );
    },

    attached() {
      this._loadPreferences();
    },

    _loadPreferences() {
      return this.$.restAPI.getLoggedIn().then(loggedIn => {
        if (loggedIn) {
          this.$.restAPI.getPreferences().then(preferences => {
            this.preferences = preferences;
          });
        } else {
          this.preferences = {};
        }
      });
    },

    _getProjectDashboard(project, dashboard) {
      const errFn = response => {
        this.fire('page-error', {response});
      };
      return this.$.restAPI.getDashboard(
          project, dashboard, errFn).then(response => {
            if (!response) {
              return;
            }
            return {
              title: response.title,
              sections: response.sections.map(section => {
                const suffix = response.foreach ? ' ' + response.foreach : '';
                return {
                  name: section.name,
                  query:
                      section.query.replace(
                          PROJECT_PLACEHOLDER_PATTERN, project) + suffix,
                };
              }),
            };
          });
    },

    _getUserDashboard(user, sections, title) {
      sections = sections
        .filter(section => (user === 'self' || !section.selfOnly))
        .map(section => Object.assign({}, section, {
          name: section.name,
          query: section.query.replace(USER_PLACEHOLDER_PATTERN, user),
        }));
      return Promise.resolve({title, sections});
    },

    _computeTitle(user) {
      if (!user || user === 'self') {
        return 'My Reviews';
      }
      return 'Dashboard for ' + user;
    },

    _isViewActive(params) {
      return params.view === Gerrit.Nav.View.DASHBOARD;
    },

    _paramsChanged(paramsChangeRecord) {
      const params = paramsChangeRecord.base;

      if (!this._isViewActive(params)) {
        return Promise.resolve();
      }

      const user = params.user || 'self';

      // NOTE: This method may be called before attachment. Fire title-change
      // in an async so that attachment to the DOM can take place first.
      const title = params.title || this._computeTitle(user);
      this.async(() => this.fire('title-change', {title}));
      return this._reload();
    },

    /**
     * Reloads the element.
     *
     * @return {Promise<!Object>}
     */
    _reload() {
      this._loading = true;
      const {project, dashboard, title, user, sections} = this.params;
      const dashboardPromise = project ?
          this._getProjectDashboard(project, dashboard) :
          this._getUserDashboard(
              user || 'self',
              sections || DEFAULT_SECTIONS,
              title || this._computeTitle(user));

      return dashboardPromise.then(this._fetchDashboardChanges.bind(this))
          .then(() => {
            this.$.reporting.dashboardDisplayed();
          }).catch(err => {
            console.warn(err);
          }).then(() => { this._loading = false; });
    },

    /**
     * Fetches the changes for each dashboard section and sets this._results
     * with the response.
     *
     * @param {!Object} res
     * @return {Promise}
     */
    _fetchDashboardChanges(res) {
      if (!res) { return Promise.resolve(); }
      const queries = res.sections.map(section => {
        if (section.suffixForDashboard) {
          return section.query + ' ' + section.suffixForDashboard;
        }
        return section.query;
      });

      return this.$.restAPI.getChanges(null, queries, null, this.options)
          .then(changes => {
            this._results = changes.map((results, i) => ({
              sectionName: res.sections[i].name,
              query: res.sections[i].query,
              results,
            })).filter((section, i) => !res.sections[i].hideIfEmpty ||
                section.results.length);
          });
    },

    _computeUserHeaderClass(userParam) {
      return userParam === 'self' ? 'hide' : '';
    },

    _handleToggleStar(e) {
      this.$.restAPI.saveChangeStarred(e.detail.change._number,
          e.detail.starred);
    },

    _handleToggleReviewed(e) {
      this.$.restAPI.saveChangeReviewed(e.detail.change._number,
          e.detail.reviewed);
    },
  });
})();
