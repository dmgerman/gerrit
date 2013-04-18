begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|errors
operator|.
name|EmailException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountProjectWatch
operator|.
name|NotifyType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|ChangeMessage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Patch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetApproval
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|StarredChange
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|IdentifiedUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|ProjectWatch
operator|.
name|Watchers
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|PatchList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|PatchListEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|PatchListNotAvailableException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|PatchSetInfoNotAvailableException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|DiffFormatter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|internal
operator|.
name|JGitText
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|TemporaryBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/** Sends an email to one or more interested parties. */
end_comment

begin_class
DECL|class|ChangeEmail
specifier|public
specifier|abstract
class|class
name|ChangeEmail
extends|extends
name|NotificationEmail
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ChangeEmail
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|change
specifier|protected
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|patchSet
specifier|protected
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|patchSetInfo
specifier|protected
name|PatchSetInfo
name|patchSetInfo
decl_stmt|;
DECL|field|changeMessage
specifier|protected
name|ChangeMessage
name|changeMessage
decl_stmt|;
DECL|field|projectState
specifier|protected
name|ProjectState
name|projectState
decl_stmt|;
DECL|field|changeData
specifier|protected
name|ChangeData
name|changeData
decl_stmt|;
DECL|field|authors
specifier|protected
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|authors
decl_stmt|;
DECL|field|emailOnlyAuthors
specifier|protected
name|boolean
name|emailOnlyAuthors
decl_stmt|;
DECL|method|ChangeEmail (EmailArguments ea, Change c, String mc)
specifier|protected
name|ChangeEmail
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|Change
name|c
parameter_list|,
name|String
name|mc
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|mc
argument_list|,
name|c
operator|.
name|getProject
argument_list|()
argument_list|,
name|c
operator|.
name|getDest
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|=
name|c
expr_stmt|;
name|changeData
operator|=
operator|new
name|ChangeData
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|emailOnlyAuthors
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|setFrom (final Account.Id id)
specifier|public
name|void
name|setFrom
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|super
operator|.
name|setFrom
argument_list|(
name|id
argument_list|)
expr_stmt|;
comment|/** Is the from user in an email squelching group? */
specifier|final
name|IdentifiedUser
name|user
init|=
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|emailOnlyAuthors
operator|=
operator|!
name|user
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canEmailReviewers
argument_list|()
expr_stmt|;
block|}
DECL|method|setPatchSet (final PatchSet ps)
specifier|public
name|void
name|setPatchSet
parameter_list|(
specifier|final
name|PatchSet
name|ps
parameter_list|)
block|{
name|patchSet
operator|=
name|ps
expr_stmt|;
block|}
DECL|method|setPatchSet (final PatchSet ps, final PatchSetInfo psi)
specifier|public
name|void
name|setPatchSet
parameter_list|(
specifier|final
name|PatchSet
name|ps
parameter_list|,
specifier|final
name|PatchSetInfo
name|psi
parameter_list|)
block|{
name|patchSet
operator|=
name|ps
expr_stmt|;
name|patchSetInfo
operator|=
name|psi
expr_stmt|;
block|}
DECL|method|setChangeMessage (final ChangeMessage cm)
specifier|public
name|void
name|setChangeMessage
parameter_list|(
specifier|final
name|ChangeMessage
name|cm
parameter_list|)
block|{
name|changeMessage
operator|=
name|cm
expr_stmt|;
block|}
comment|/** Format the message body by calling {@link #appendText(String)}. */
DECL|method|format ()
specifier|protected
name|void
name|format
parameter_list|()
throws|throws
name|EmailException
block|{
name|formatChange
argument_list|()
expr_stmt|;
name|appendText
argument_list|(
name|velocifyFile
argument_list|(
literal|"ChangeFooter.vm"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|p
range|:
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
name|reviewers
operator|.
name|add
argument_list|(
name|p
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|TreeSet
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|who
range|:
name|reviewers
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|getNameEmailFor
argument_list|(
name|who
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|appendText
argument_list|(
literal|"Gerrit-Reviewer: "
operator|+
name|name
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{     }
name|formatFooter
argument_list|()
expr_stmt|;
block|}
comment|/** Format the message body by calling {@link #appendText(String)}. */
DECL|method|formatChange ()
specifier|protected
specifier|abstract
name|void
name|formatChange
parameter_list|()
throws|throws
name|EmailException
function_decl|;
comment|/** Format the message footer by calling {@link #appendText(String)}. */
DECL|method|formatFooter ()
specifier|protected
name|void
name|formatFooter
parameter_list|()
throws|throws
name|EmailException
block|{   }
comment|/** Setup the message headers and envelope (TO, CC, BCC). */
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
if|if
condition|(
name|args
operator|.
name|projectCache
operator|!=
literal|null
condition|)
block|{
name|projectState
operator|=
name|args
operator|.
name|projectCache
operator|.
name|get
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|projectState
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|patchSet
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|patchSet
operator|=
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
name|patchSet
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|patchSet
operator|!=
literal|null
operator|&&
name|patchSetInfo
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|patchSetInfo
operator|=
name|args
operator|.
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatchSetInfoNotAvailableException
name|err
parameter_list|)
block|{
name|patchSetInfo
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|authors
operator|=
name|getAuthors
argument_list|()
expr_stmt|;
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
if|if
condition|(
name|changeMessage
operator|!=
literal|null
operator|&&
name|changeMessage
operator|.
name|getWrittenOn
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setHeader
argument_list|(
literal|"Date"
argument_list|,
operator|new
name|Date
argument_list|(
name|changeMessage
operator|.
name|getWrittenOn
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setChangeSubjectHeader
argument_list|()
expr_stmt|;
name|setHeader
argument_list|(
literal|"X-Gerrit-Change-Id"
argument_list|,
literal|""
operator|+
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|setChangeUrlHeader
argument_list|()
expr_stmt|;
name|setCommitIdHeader
argument_list|()
expr_stmt|;
block|}
DECL|method|setChangeUrlHeader ()
specifier|private
name|void
name|setChangeUrlHeader
parameter_list|()
block|{
specifier|final
name|String
name|u
init|=
name|getChangeUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|u
operator|!=
literal|null
condition|)
block|{
name|setHeader
argument_list|(
literal|"X-Gerrit-ChangeURL"
argument_list|,
literal|"<"
operator|+
name|u
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setCommitIdHeader ()
specifier|private
name|void
name|setCommitIdHeader
parameter_list|()
block|{
if|if
condition|(
name|patchSet
operator|!=
literal|null
operator|&&
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|!=
literal|null
operator|&&
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|!=
literal|null
operator|&&
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|setHeader
argument_list|(
literal|"X-Gerrit-Commit"
argument_list|,
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setChangeSubjectHeader ()
specifier|private
name|void
name|setChangeSubjectHeader
parameter_list|()
throws|throws
name|EmailException
block|{
name|setHeader
argument_list|(
literal|"Subject"
argument_list|,
name|velocifyFile
argument_list|(
literal|"ChangeSubject.vm"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Get a link to the change; null if the server doesn't know its own address. */
DECL|method|getChangeUrl ()
specifier|public
name|String
name|getChangeUrl
parameter_list|()
block|{
if|if
condition|(
name|getGerritUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|getGerritUrl
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getChangeMessageThreadId ()
specifier|public
name|String
name|getChangeMessageThreadId
parameter_list|()
throws|throws
name|EmailException
block|{
return|return
name|velocify
argument_list|(
literal|"<gerrit.${change.createdOn.time}.$change.key.get()"
operator|+
literal|"@$email.gerritHost>"
argument_list|)
return|;
block|}
comment|/** Format the sender's "cover letter", {@link #getCoverLetter()}. */
DECL|method|formatCoverLetter ()
specifier|protected
name|void
name|formatCoverLetter
parameter_list|()
block|{
specifier|final
name|String
name|cover
init|=
name|getCoverLetter
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|cover
argument_list|)
condition|)
block|{
name|appendText
argument_list|(
name|cover
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Get the text of the "cover letter", from {@link ChangeMessage}. */
DECL|method|getCoverLetter ()
specifier|public
name|String
name|getCoverLetter
parameter_list|()
block|{
if|if
condition|(
name|changeMessage
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|txt
init|=
name|changeMessage
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|txt
operator|!=
literal|null
condition|)
block|{
return|return
name|txt
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
return|return
literal|""
return|;
block|}
comment|/** Format the change message and the affected file list. */
DECL|method|formatChangeDetail ()
specifier|protected
name|void
name|formatChangeDetail
parameter_list|()
block|{
name|appendText
argument_list|(
name|getChangeDetail
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Create the change message and the affected file list. */
DECL|method|getChangeDetail ()
specifier|public
name|String
name|getChangeDetail
parameter_list|()
block|{
try|try
block|{
name|StringBuilder
name|detail
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|patchSetInfo
operator|!=
literal|null
condition|)
block|{
name|detail
operator|.
name|append
argument_list|(
name|patchSetInfo
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|detail
operator|.
name|append
argument_list|(
name|change
operator|.
name|getSubject
argument_list|()
operator|.
name|trim
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|patchSet
operator|!=
literal|null
condition|)
block|{
name|detail
operator|.
name|append
argument_list|(
literal|"---\n"
argument_list|)
expr_stmt|;
name|PatchList
name|patchList
init|=
name|getPatchList
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchListEntry
name|p
range|:
name|patchList
operator|.
name|getPatches
argument_list|()
control|)
block|{
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getNewName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|detail
operator|.
name|append
argument_list|(
name|p
operator|.
name|getChangeType
argument_list|()
operator|.
name|getCode
argument_list|()
operator|+
literal|" "
operator|+
name|p
operator|.
name|getNewName
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|append
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|""
comment|//
operator|+
literal|"{0,choice,0#0 files|1#1 file|1<{0} files} changed, "
comment|//
operator|+
literal|"{1,choice,0#0 insertions|1#1 insertion|1<{1} insertions}(+), "
comment|//
operator|+
literal|"{2,choice,0#0 deletions|1#1 deletion|1<{2} deletions}(-)"
comment|//
operator|+
literal|"\n"
argument_list|,
name|patchList
operator|.
name|getPatches
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|,
comment|//
name|patchList
operator|.
name|getInsertions
argument_list|()
argument_list|,
comment|//
name|patchList
operator|.
name|getDeletions
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|detail
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|detail
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|err
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot format change detail"
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
literal|""
return|;
block|}
block|}
comment|/** Get the patch list corresponding to this patch set. */
DECL|method|getPatchList ()
specifier|protected
name|PatchList
name|getPatchList
parameter_list|()
throws|throws
name|PatchListNotAvailableException
block|{
if|if
condition|(
name|patchSet
operator|!=
literal|null
condition|)
block|{
return|return
name|args
operator|.
name|patchListCache
operator|.
name|get
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|)
return|;
block|}
throw|throw
operator|new
name|PatchListNotAvailableException
argument_list|(
literal|"no patchSet specified"
argument_list|)
throw|;
block|}
comment|/** Get the project entity the change is in; null if its been deleted. */
DECL|method|getProjectState ()
specifier|protected
name|ProjectState
name|getProjectState
parameter_list|()
block|{
return|return
name|projectState
return|;
block|}
comment|/** Get the groups which own the project. */
DECL|method|getProjectOwners ()
specifier|protected
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getProjectOwners
parameter_list|()
block|{
specifier|final
name|ProjectState
name|r
decl_stmt|;
name|r
operator|=
name|args
operator|.
name|projectCache
operator|.
name|get
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|r
operator|!=
literal|null
condition|?
name|r
operator|.
name|getOwners
argument_list|()
else|:
name|Collections
operator|.
expr|<
name|AccountGroup
operator|.
name|UUID
operator|>
name|emptySet
argument_list|()
return|;
block|}
comment|/** TO or CC all vested parties (change owner, patch set uploader, author). */
DECL|method|rcptToAuthors (final RecipientType rt)
specifier|protected
name|void
name|rcptToAuthors
parameter_list|(
specifier|final
name|RecipientType
name|rt
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Account
operator|.
name|Id
name|id
range|:
name|authors
control|)
block|{
name|add
argument_list|(
name|rt
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** BCC any user who has starred this change. */
DECL|method|bccStarredBy ()
specifier|protected
name|void
name|bccStarredBy
parameter_list|()
block|{
try|try
block|{
comment|// BCC anyone who has starred this change.
comment|//
for|for
control|(
name|StarredChange
name|w
range|:
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
operator|.
name|starredChanges
argument_list|()
operator|.
name|byChange
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
name|super
operator|.
name|add
argument_list|(
name|RecipientType
operator|.
name|BCC
argument_list|,
name|w
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
comment|// Just don't BCC everyone. Better to send a partial message to those
comment|// we already have queued up then to fail deliver entirely to people
comment|// who have a lower interest in the change.
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot BCC users that starred updated change"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getWatchers (NotifyType type)
specifier|protected
specifier|final
name|Watchers
name|getWatchers
parameter_list|(
name|NotifyType
name|type
parameter_list|)
throws|throws
name|OrmException
block|{
name|ProjectWatch
name|watch
init|=
operator|new
name|ProjectWatch
argument_list|(
name|args
argument_list|,
name|project
argument_list|,
name|projectState
argument_list|,
name|changeData
argument_list|)
decl_stmt|;
return|return
name|watch
operator|.
name|getWatchers
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/** Any user who has published comments on this change. */
DECL|method|ccAllApprovals ()
specifier|protected
name|void
name|ccAllApprovals
parameter_list|()
block|{
name|ccApprovals
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** Users who have non-zero approval codes on the change. */
DECL|method|ccExistingReviewers ()
specifier|protected
name|void
name|ccExistingReviewers
parameter_list|()
block|{
name|ccApprovals
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ccApprovals (final boolean includeZero)
specifier|private
name|void
name|ccApprovals
parameter_list|(
specifier|final
name|boolean
name|includeZero
parameter_list|)
block|{
try|try
block|{
comment|// CC anyone else who has posted an approval mark on this change
comment|//
for|for
control|(
name|PatchSetApproval
name|ap
range|:
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|includeZero
operator|&&
name|ap
operator|.
name|getValue
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|ap
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
if|if
condition|(
name|includeZero
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot CC users that commented on updated change"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot CC users that reviewed updated change"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|add (final RecipientType rt, final Account.Id to)
specifier|protected
name|void
name|add
parameter_list|(
specifier|final
name|RecipientType
name|rt
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|to
parameter_list|)
block|{
if|if
condition|(
operator|!
name|emailOnlyAuthors
operator|||
name|authors
operator|.
name|contains
argument_list|(
name|to
argument_list|)
condition|)
block|{
name|super
operator|.
name|add
argument_list|(
name|rt
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isVisibleTo (final Account.Id to)
specifier|protected
name|boolean
name|isVisibleTo
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|to
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|projectState
operator|==
literal|null
operator|||
name|projectState
operator|.
name|controlFor
argument_list|(
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|to
argument_list|)
argument_list|)
operator|.
name|controlFor
argument_list|(
name|change
argument_list|)
operator|.
name|isVisible
argument_list|(
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|/** Find all users who are authors of any part of this change. */
DECL|method|getAuthors ()
specifier|protected
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|getAuthors
parameter_list|()
block|{
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|authors
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
name|authors
operator|.
name|add
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|patchSet
operator|!=
literal|null
condition|)
block|{
name|authors
operator|.
name|add
argument_list|(
name|patchSet
operator|.
name|getUploader
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|patchSetInfo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|authors
operator|.
name|add
argument_list|(
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|patchSetInfo
operator|.
name|getCommitter
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|authors
operator|.
name|add
argument_list|(
name|patchSetInfo
operator|.
name|getCommitter
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|authors
return|;
block|}
annotation|@
name|Override
DECL|method|setupVelocityContext ()
specifier|protected
name|void
name|setupVelocityContext
parameter_list|()
block|{
name|super
operator|.
name|setupVelocityContext
argument_list|()
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"change"
argument_list|,
name|change
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"changeId"
argument_list|,
name|change
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"coverLetter"
argument_list|,
name|getCoverLetter
argument_list|()
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"fromName"
argument_list|,
name|getNameFor
argument_list|(
name|fromId
argument_list|)
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"patchSet"
argument_list|,
name|patchSet
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"patchSetInfo"
argument_list|,
name|patchSetInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|getIncludeDiff ()
specifier|public
name|boolean
name|getIncludeDiff
parameter_list|()
block|{
return|return
name|args
operator|.
name|settings
operator|.
name|includeDiff
return|;
block|}
comment|/** Show patch set as unified difference. */
DECL|method|getUnifiedDiff ()
specifier|public
name|String
name|getUnifiedDiff
parameter_list|()
block|{
name|PatchList
name|patchList
decl_stmt|;
try|try
block|{
name|patchList
operator|=
name|getPatchList
argument_list|()
expr_stmt|;
if|if
condition|(
name|patchList
operator|.
name|getOldId
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// Octopus merges are not well supported for diff output by Gerrit.
comment|// Currently these always have a null oldId in the PatchList.
return|return
literal|"[Octopus merge; cannot be formatted as a diff.]\n"
return|;
block|}
block|}
catch|catch
parameter_list|(
name|PatchListNotAvailableException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot format patch"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|""
return|;
block|}
name|TemporaryBuffer
operator|.
name|Heap
name|buf
init|=
operator|new
name|TemporaryBuffer
operator|.
name|Heap
argument_list|(
name|args
operator|.
name|settings
operator|.
name|maximumDiffSize
argument_list|)
decl_stmt|;
name|DiffFormatter
name|fmt
init|=
operator|new
name|DiffFormatter
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|Repository
name|git
decl_stmt|;
try|try
block|{
name|git
operator|=
name|args
operator|.
name|server
operator|.
name|openRepository
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot open repository to format patch"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|""
return|;
block|}
try|try
block|{
name|fmt
operator|.
name|setRepository
argument_list|(
name|git
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setDetectRenames
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|format
argument_list|(
name|patchList
operator|.
name|getOldId
argument_list|()
argument_list|,
name|patchList
operator|.
name|getNewId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|buf
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|JGitText
operator|.
name|get
argument_list|()
operator|.
name|inMemoryBufferLimitExceeded
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
name|log
operator|.
name|error
argument_list|(
literal|"Cannot format patch"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|""
return|;
block|}
finally|finally
block|{
name|fmt
operator|.
name|release
argument_list|()
expr_stmt|;
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

