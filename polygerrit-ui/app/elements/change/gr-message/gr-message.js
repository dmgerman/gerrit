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

  const CI_LABELS = ['Trybot-Ready', 'Tryjob-Request', 'Commit-Queue'];
  const PATCH_SET_PREFIX_PATTERN = /^Patch Set \d+: /;
  const LABEL_TITLE_SCORE_PATTERN = /([A-Za-z0-9-]+)([+-]\d+)/;

  Polymer({
    is: 'gr-message',

    /**
     * Fired when this message's permalink is tapped.
     *
     * @event scroll-to
     */

    /**
     * Fired when this message's reply link is tapped.
     *
     * @event reply
     */

    listeners: {
      tap: '_handleTap',
    },

    properties: {
      changeNum: Number,
      message: Object,
      author: {
        type: Object,
        computed: '_computeAuthor(message)',
      },
      comments: {
        type: Object,
        observer: '_commentsChanged',
      },
      config: Object,
      hideAutomated: {
        type: Boolean,
        value: false,
      },
      hidden: {
        type: Boolean,
        computed: '_computeIsHidden(hideAutomated, isAutomated)',
        reflectToAttribute: true,
      },
      isAutomated: {
        type: Boolean,
        computed: '_computeIsAutomated(message)',
      },
      showAvatar: {
        type: Boolean,
        computed: '_computeShowAvatar(author, config)',
      },
      showOnBehalfOf: {
        type: Boolean,
        computed: '_computeShowOnBehalfOf(message)',
      },
      showReplyButton: {
        type: Boolean,
        computed: '_computeShowReplyButton(message, _loggedIn)',
      },
      projectName: {
        type: String,
        observer: '_projectNameChanged',
      },
      _commentLinks: Object,
      // Computed property needed to trigger Polymer value observing.
      _expanded: {
        type: Object,
        computed: '_computeExpanded(message.expanded)',
      },
      _loggedIn: {
        type: Boolean,
        value: false,
      },
    },

    behaviors: [
      Gerrit.AnonymousNameBehavior,
    ],

    observers: [
      '_updateExpandedClass(message.expanded)',
    ],

    ready() {
      this.$.restAPI.getConfig().then(config => {
        this.config = config;
      });
      this.$.restAPI.getLoggedIn().then(loggedIn => {
        this._loggedIn = loggedIn;
      });
    },

    _updateExpandedClass(expanded) {
      if (expanded) {
        this.classList.add('expanded');
      } else {
        this.classList.remove('expanded');
      }
    },

    _computeAuthor(message) {
      return message.author || message.updated_by;
    },

    _computeShowAvatar(author, config) {
      return !!(author && config && config.plugin && config.plugin.has_avatars);
    },

    _computeShowOnBehalfOf(message) {
      const author = message.author || message.updated_by;
      return !!(author && message.real_author &&
          author._account_id != message.real_author._account_id);
    },

    _computeShowReplyButton(message, loggedIn) {
      return !!message.message && loggedIn;
    },

    _computeExpanded(expanded) {
      return expanded;
    },

    /**
     * If there is no value set on the message object as to whether _expanded
     * should be true or not, then _expanded is set to true if there are
     * inline comments (otherwise false).
     */
    _commentsChanged(value) {
      if (this.message && this.message.expanded === undefined) {
        this.set('message.expanded', Object.keys(value || {}).length > 0);
      }
    },

    _handleTap(e) {
      if (this.message.expanded) { return; }
      e.stopPropagation();
      this.set('message.expanded', true);
    },

    _handleAuthorTap(e) {
      if (!this.message.expanded) { return; }
      e.stopPropagation();
      this.set('message.expanded', false);
    },

    _computeIsAutomated(message) {
      return !!(message.reviewer ||
          message.type === 'REVIEWER_UPDATE' ||
          (message.tag && message.tag.startsWith('autogenerated')));
    },

    _computeIsHidden(hideAutomated, isAutomated) {
      return hideAutomated && isAutomated;
    },

    _computeIsReviewerUpdate(event) {
      return event.type === 'REVIEWER_UPDATE';
    },

    _isMessagePositive(message) {
      if (!message.message) { return null; }
      const line = message.message.split('\n', 1)[0];
      const patchSetPrefix = PATCH_SET_PREFIX_PATTERN;
      if (!line.match(patchSetPrefix)) { return null; }
      const scoresRaw = line.split(patchSetPrefix)[1];
      if (!scoresRaw) { return null; }
      const scores = scoresRaw.split(' ');
      if (!scores.length) { return null; }
      const {min, max} = scores
          .map(s => s.match(LABEL_TITLE_SCORE_PATTERN))
          .filter(ms => ms && ms.length === 3)
          .filter(([, label]) => !CI_LABELS.includes(label))
          .map(([, , score]) => score)
          .map(s => parseInt(s, 10))
          .reduce(({min, max}, s) =>
              ({min: (s < min ? s : min), max: (s > max ? s : max)}),
              {min: 0, max: 0});
      if (max - min === 0) {
        return 0;
      } else {
        return (max + min) > 0 ? 1 : -1;
      }
    },

    _computeClass(expanded, showAvatar, message) {
      const classes = [];
      classes.push(expanded ? 'expanded' : 'collapsed');
      classes.push(showAvatar ? 'showAvatar' : 'hideAvatar');
      const scoreQuality = this._isMessagePositive(message);
      if (scoreQuality === 1) {
        classes.push('positiveVote');
      } else if (scoreQuality === -1) {
        classes.push('negativeVote');
      }
      return classes.join(' ');
    },

    _computeMessageHash(message) {
      return '#message-' + message.id;
    },

    _handleLinkTap(e) {
      e.preventDefault();

      this.fire('scroll-to', {message: this.message}, {bubbles: false});

      const hash = this._computeMessageHash(this.message);
      // Don't add the hash to the window history if it's already there.
      // Otherwise you mess up expected back button behavior.
      if (window.location.hash == hash) { return; }
      // Change the URL but don’t trigger a nav event. Otherwise it will
      // reload the page.
      page.show(window.location.pathname + hash, null, false);
    },

    _handleReplyTap(e) {
      e.preventDefault();
      this.fire('reply', {message: this.message});
    },

    _authorOrAnon(author) {
      if (author && author.name) {
        return author.name;
      } else if (author && author.email) {
        return author.email;
      }

      return this.getAnonymousName(this.config);
    },

    _projectNameChanged(name) {
      this.$.restAPI.getProjectConfig(name).then(config => {
        this._commentLinks = config.commentlinks;
      });
    },
  });
})();
