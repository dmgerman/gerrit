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

  var JSON_PREFIX = ')]}\'';

  Polymer({
    is: 'gr-rest-api-interface',

    fetchJSON: function(url, opt_cancelCondition, opt_params, opt_opts) {
      opt_opts = opt_opts || {};

      var fetchOptions = {
        credentials: (opt_opts.noCredentials ? undefined : 'same-origin'),
        headers: opt_opts.headers,
      };

      var urlWithParams = url;
      if (opt_params) {
        var params = [];
        for (var p in opt_params) {
          if (opt_params[p] == null) {
            params.push(encodeURIComponent(p));
            continue;
          }
          params.push(
            encodeURIComponent(p) + '=' +
            encodeURIComponent(opt_params[p]));
        }
        // Sorting the params leaves the order deterministic which is easier
        // to test.
        urlWithParams += '?' + params.sort().join('&');
      }

      return fetch(urlWithParams, fetchOptions).then(function(response) {
        if (opt_cancelCondition && opt_cancelCondition()) {
          response.body.cancel();
          return;
        }

        return response.text().then(function(text) {
          return JSON.parse(text.substring(JSON_PREFIX.length));
        });
      }).catch(function(err) {
        if (opt_opts.noCredentials) {
          throw err;
        } else {
          // This could be because of a 302 auth redirect. Retry the request.
          return this.fetchJSON(url, opt_cancelCondition, opt_params,
              Object.assign(opt_opts, {noCredentials: true}));
        }
      }.bind(this));
    },

    getAccountDetail: function() {
      return this.fetchJSON('/accounts/self/detail');
    },

    getDiff: function(changeNum, basePatchNum, patchNum, path,
        opt_cancelCondition) {
      var url = this._changeBaseURL(changeNum, patchNum) + '/files/' +
          encodeURIComponent(path) + '/diff';
      var params =  {
        context: 'ALL',
        intraline: null
      };
      if (basePatchNum != 'PARENT') {
        params.base = basePatchNum;
      }

      return this.fetchJSON(url, opt_cancelCondition, params);
    },

    _changeBaseURL: function(changeNum, patchNum) {
      var v = '/changes/' + changeNum;
      if (patchNum) {
        v += '/revisions/' + patchNum;
      }
      return v;
    },

  });
})();
