// Copyright (C) 2016 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
(function() {
  'use strict';

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
        value: function() { return {}; },
      },
      viewState: Object,

      _results: Array,
      _groupTitles: {
        type: Array,
        value: [
          'Outgoing reviews',
          'Incoming reviews',
          'Recently closed',
        ],
      },

      /**
       * For showing a "loading..." string during ajax requests.
       */
      _loading: {
        type: Boolean,
        value: true,
      },
    },

    behaviors: [
      Gerrit.RESTClientBehavior,
    ],

    attached: function() {
      this.fire('title-change', {title: 'My Reviews'});
    },

    _computeQueryParams: function() {
      var options = this.listChangesOptionsToHex(
          this.ListChangesOption.LABELS,
          this.ListChangesOption.DETAILED_ACCOUNTS,
          this.ListChangesOption.REVIEWED
      );
      return {
        O: options,
        q: [
          'is:open owner:self',
          'is:open reviewer:self -owner:self',
          'is:closed (owner:self OR reviewer:self) -age:4w limit:10',
        ],
      };
    },
  });
})();
