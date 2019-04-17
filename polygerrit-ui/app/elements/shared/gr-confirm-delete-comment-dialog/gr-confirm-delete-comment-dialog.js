/**
 * @license
 * Copyright (C) 2017 The Android Open Source Project
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

  Polymer({
    is: 'gr-confirm-delete-comment-dialog',
    _legacyUndefinedCheck: true,

    /**
     * Fired when the confirm button is pressed.
     *
     * @event confirm
     */

    /**
     * Fired when the cancel button is pressed.
     *
     * @event cancel
     */

    properties: {
      message: String,
    },

    resetFocus() {
      this.$.messageInput.textarea.focus();
    },

    _handleConfirmTap(e) {
      e.preventDefault();
      this.fire('confirm', {reason: this.message}, {bubbles: false});
    },

    _handleCancelTap(e) {
      e.preventDefault();
      this.fire('cancel', null, {bubbles: false});
    },
  });
})();
