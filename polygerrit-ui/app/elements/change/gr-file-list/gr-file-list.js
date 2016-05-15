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

  var COMMIT_MESSAGE_PATH = '/COMMIT_MSG';

  Polymer({
    is: 'gr-file-list',

    properties: {
      patchRange: Object,
      patchNum: String,
      changeNum: String,
      comments: Object,
      drafts: Object,
      revisions: Object,
      projectConfig: Object,
      topMargin: Number,
      selectedIndex: {
        type: Number,
        notify: true,
      },
      keyEventTarget: {
        type: Object,
        value: function() { return document.body; },
      },

      _files: Array,
      _loggedIn: {
        type: Boolean,
        value: false,
      },
      _reviewed: {
        type: Array,
        value: function() { return []; },
      },
      _diffPrefs: Object,
      _userPrefs: Object,
      _showInlineDiffs: Boolean,
    },

    behaviors: [
      Gerrit.KeyboardShortcutBehavior,
    ],

    reload: function() {
      if (!this.changeNum || !this.patchRange.patchNum) {
        return Promise.resolve();
      }

      var promises = [];
      var _this = this;

      promises.push(this._getFiles().then(function(files) {
        _this._files = files;
      }));
      promises.push(this._getLoggedIn().then(function(loggedIn) {
        return _this._loggedIn = loggedIn;
      }).then(function(loggedIn) {
        if (!loggedIn) { return; }

        return _this._getReviewedFiles().then(function(reviewed) {
          _this._reviewed = reviewed;
        });
      }));

      promises.push(this._getDiffPreferences().then(function(prefs) {
        this._diffPrefs = prefs;
      }.bind(this)));

      promises.push(this._getPreferences().then(function(prefs) {
        this._userPrefs = prefs;
      }.bind(this)));
    },

    _getDiffPreferences: function() {
      return this.$.restAPI.getDiffPreferences();
    },

    _getPreferences: function() {
      return this.$.restAPI.getPreferences();
    },

    _computePatchSets: function(revisions) {
      var patchNums = [];
      for (var commit in revisions) {
        patchNums.push(revisions[commit]._number);
      }
      return patchNums.sort(function(a, b) { return a - b; });
    },

    _computePatchSetDisabled: function(patchNum, currentPatchNum) {
      return parseInt(patchNum, 10) >= parseInt(currentPatchNum, 10);
    },

    _computePatchSetSelected: function(patchNum, basePatchNum) {
      return parseInt(patchNum, 10) === parseInt(basePatchNum, 10);
    },

    _handlePatchChange: function(e) {
      this.set('patchRange.basePatchNum', Polymer.dom(e).rootTarget.value);
      page.show('/c/' + encodeURIComponent(this.changeNum) + '/' +
          encodeURIComponent(this._patchRangeStr(this.patchRange)));
    },

    _forEachDiff: function(fn) {
      var diffs = Polymer.dom(this.root).querySelectorAll('gr-diff');
      for (var i = 0; i < diffs.length; i++) {
        fn(diffs[i]);
      }
    },

    _expandAllDiffs: function() {
      this._showInlineDiffs = true;
      this._forEachDiff(function(diff) {
        diff.hidden = false;
        diff.reload();
      });
    },

    _collapseAllDiffs: function() {
      this._showInlineDiffs = false;
      this._forEachDiff(function(diff) {
        diff.hidden = true;
      });
    },

    _computeCommentsString: function(comments, patchNum, path) {
      return this._computeCountString(comments, patchNum, path, 'comment');
    },

    _computeDraftsString: function(drafts, patchNum, path) {
      return this._computeCountString(drafts, patchNum, path, 'draft');
    },

    _computeCountString: function(comments, patchNum, path, noun) {
      if (!comments) { return ''; }

      var patchComments = (comments[path] || []).filter(function(c) {
        return parseInt(c.patch_set, 10) === parseInt(patchNum, 10);
      });
      var num = patchComments.length;
      if (num === 0) { return ''; }
      return num + ' ' + noun + (num > 1 ? 's' : '');
    },

    _computeReviewed: function(file, _reviewed) {
      return _reviewed.indexOf(file.__path) !== -1;
    },

    _handleReviewedChange: function(e) {
      var path = Polymer.dom(e).rootTarget.getAttribute('data-path');
      var index = this._reviewed.indexOf(path);
      var reviewed = index !== -1;
      if (reviewed) {
        this.splice('_reviewed', index, 1);
      } else {
        this.push('_reviewed', path);
      }

      this._saveReviewedState(path, !reviewed).catch(function(err) {
        alert('Couldn’t change file review status. Check the console ' +
            'and contact the PolyGerrit team for assistance.');
        throw err;
      }.bind(this));
    },

    _saveReviewedState: function(path, reviewed) {
      return this.$.restAPI.saveFileReviewed(this.changeNum,
          this.patchRange.patchNum, path, reviewed);
    },

    _getLoggedIn: function() {
      return this.$.restAPI.getLoggedIn();
    },

    _getReviewedFiles: function() {
      return this.$.restAPI.getReviewedFiles(this.changeNum,
          this.patchRange.patchNum);
    },

    _getFiles: function() {
      return this.$.restAPI.getChangeFilesAsSpeciallySortedArray(
          this.changeNum, this.patchRange);
    },

    _handleKey: function(e) {
      if (this.shouldSupressKeyboardShortcut(e)) { return; }

      switch (e.keyCode) {
        case 73:  // 'i'
          if (!e.shiftKey) { return; }
          e.preventDefault();
          this._toggleInlineDiffs();
          break;
        case 74:  // 'j'
          e.preventDefault();
          this.selectedIndex =
              Math.min(this._files.length - 1, this.selectedIndex + 1);
          this._scrollToSelectedFile();
          break;
        case 75:  // 'k'
          e.preventDefault();
          this.selectedIndex = Math.max(0, this.selectedIndex - 1);
          this._scrollToSelectedFile();
          break;
        case 219:  // '['
          e.preventDefault();
          this._openSelectedFile(this._files.length - 1);
          break;
        case 221:  // ']'
          e.preventDefault();
          this._openSelectedFile(0);
          break;
        case 13:  // <enter>
        case 79:  // 'o'
          e.preventDefault();
          this._openSelectedFile();
          break;
      }
    },

    _toggleInlineDiffs: function() {
      if (this._showInlineDiffs) {
        this._collapseAllDiffs();
      } else {
        this._expandAllDiffs();
      }
    },

    _openSelectedFile: function(opt_index) {
      if (opt_index != null) {
        this.selectedIndex = opt_index;
      }
      page.show(this._computeDiffURL(this.changeNum, this.patchRange,
          this._files[this.selectedIndex].__path));
    },

    _scrollToSelectedFile: function() {
      var el = this.$$('.row[selected]');
      var top = 0;
      for (var node = el; node; node = node.offsetParent) {
        top += node.offsetTop;
      }
      if (this._showInlineDiffs) {
        window.scrollTo(0, top - this.topMargin);
      } else {
        window.scrollTo(0, top - document.body.clientHeight / 2);
      }
    },

    _computeFileSelected: function(index, selectedIndex) {
      return index === selectedIndex;
    },

    _computeFileStatus: function(status) {
      return status || 'M';
    },

    _computeDiffURL: function(changeNum, patchRange, path) {
      return '/c/' +
          encodeURIComponent(changeNum) +
          '/' +
          encodeURIComponent(this._patchRangeStr(patchRange)) +
          '/' +
          path;
    },

    _patchRangeStr: function(patchRange) {
      return patchRange.basePatchNum !== 'PARENT' ?
          patchRange.basePatchNum + '..' + patchRange.patchNum :
          patchRange.patchNum + '';
    },

    _computeFileDisplayName: function(path) {
      return path === COMMIT_MESSAGE_PATH ? 'Commit message' : path;
    },

    _computeClass: function(baseClass, path) {
      var classes = [baseClass];
      if (path === COMMIT_MESSAGE_PATH) {
        classes.push('invisible');
      }
      return classes.join(' ');
    },
  });
})();
