// Copyright (C) 2017 The Android Open Source Project
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
    is: 'gr-autocomplete-dropdown',

    /**
     * Fired when the dropdown is closed.
     *
     * @event dropdown-closed
     */

    /**
     * Fired when item is selected.
     *
     * @event item-selected
     */

    properties: {
      moveToRoot: Boolean,
      suggestions: {
        type: Array,
        observer: '_resetCursorStops',
      },
      _suggestionEls: {
        type: Array,
        observer: '_resetCursorIndex',
      },
    },

    behaviors: [
      Gerrit.KeyboardShortcutBehavior,
    ],

    keyBindings: {
      'up': '_handleUp',
      'down': '_handleDown',
      'enter': '_handleEnter',
      'esc': '_handleEscape',
      'tab': '_handleTab',
    },

    close: function() {
      if (this.moveToRoot) {
        Gerrit.getRootElement().removeChild(this);
      }
    },

    open: function() {
      if (this.moveToRoot) {
        Gerrit.getRootElement().appendChild(this);
      }
      this._resetCursorStops();
      this._resetCursorIndex();
    },

    setPosition: function(top, left) {
      this.style.top = top;
      this.style.left = left;
    },

    _handleUp(e) {
      if (!this.hidden) {
        e.preventDefault();
        e.stopPropagation();
        this.cursorUp();
      }
    },

    _handleDown(e) {
      if (!this.hidden) {
        e.preventDefault();
        e.stopPropagation();
        this.cursorDown();
      }
    },

    // TODO @beckysiegel make this work with shadow dom.
    cursorDown(e) {
      if (!this.hidden) {
        this.$.cursor.next();
      }
    },

    // TODO @beckysiegel make this work with shadow dom.
    cursorUp(e) {
      if (!this.hidden) {
        this.$.cursor.previous();
      }
    },

    _handleTab: function(e) {
      e.preventDefault();
      e.stopPropagation();
      this.fire('item-selected', {
        trigger: 'tab',
        selected: this.$.cursor.target,
      });
    },

    _handleEnter: function(e) {
      e.preventDefault();
      e.stopPropagation();
      this.fire('item-selected', {
        trigger: 'enter',
        selected: this.$.cursor.target,
      });
    },

    _handleEscape: function() {
      this._fireClose();
      this.close();
    },

    _handleTapItem: function(e) {
      e.preventDefault();
      e.stopPropagation();
      this.fire('item-selected', {
        trigger: 'tap',
        selected: e.target,
      });
    },

    _fireClose: function() {
      this.fire('dropdown-closed');
    },

    getCursorTarget: function() {
      return this.$.cursor.target;
    },

    _resetCursorStops: function() {
      Polymer.dom.flush();
      this._suggestionEls = this.$.suggestions.querySelectorAll('li');
    },

    _resetCursorIndex: function() {
      this.$.cursor.setCursorAtIndex(0);
    },
  });
})();
