begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|ChangeNoteUtil
operator|.
name|FOOTER_LABEL
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|ChangeNoteUtil
operator|.
name|FOOTER_PATCH_SET
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|ChangeNoteUtil
operator|.
name|GERRIT_PLACEHOLDER_HOST
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|common
operator|.
name|data
operator|.
name|LabelType
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
name|common
operator|.
name|data
operator|.
name|LabelTypes
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
name|Project
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
name|GerritPersonIdent
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
name|account
operator|.
name|AccountCache
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
name|git
operator|.
name|GitRepositoryManager
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
name|git
operator|.
name|MetaDataUpdate
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
name|git
operator|.
name|VersionedMetaData
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
name|ChangeControl
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
name|ProjectCache
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
name|util
operator|.
name|LabelVote
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|AssistedInject
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
name|errors
operator|.
name|ConfigInvalidException
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
name|CommitBuilder
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
name|ObjectId
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
name|PersonIdent
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
name|revwalk
operator|.
name|FooterKey
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
name|revwalk
operator|.
name|RevCommit
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A single delta to apply atomically to a change.  *<p>  * This delta becomes a single commit on the notes branch, so there are  * limitations on the set of modifications that can be handled in a single  * update. In particular, there is a single author and timestamp for each  * update.  *<p>  * This class is not thread-safe.  */
end_comment

begin_class
DECL|class|ChangeUpdate
specifier|public
class|class
name|ChangeUpdate
extends|extends
name|VersionedMetaData
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (ChangeControl ctl)
name|ChangeUpdate
name|create
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|)
function_decl|;
DECL|method|create (ChangeControl ctl, Date when)
name|ChangeUpdate
name|create
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|,
name|Date
name|when
parameter_list|)
function_decl|;
block|}
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
decl_stmt|;
DECL|field|labelTypes
specifier|private
specifier|final
name|LabelTypes
name|labelTypes
decl_stmt|;
DECL|field|ctl
specifier|private
specifier|final
name|ChangeControl
name|ctl
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|when
specifier|private
specifier|final
name|Date
name|when
decl_stmt|;
DECL|field|approvals
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|approvals
decl_stmt|;
DECL|field|reviewers
specifier|private
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ReviewerState
argument_list|>
name|reviewers
decl_stmt|;
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
DECL|field|psId
specifier|private
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ChangeUpdate ( @erritPersonIdent PersonIdent serverIdent, GitRepositoryManager repoManager, NotesMigration migration, AccountCache accountCache, MetaDataUpdate.User updateFactory, ProjectCache projectCache, @Assisted ChangeControl ctl)
name|ChangeUpdate
parameter_list|(
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
annotation|@
name|Assisted
name|ChangeControl
name|ctl
parameter_list|)
block|{
name|this
argument_list|(
name|serverIdent
argument_list|,
name|repoManager
argument_list|,
name|migration
argument_list|,
name|accountCache
argument_list|,
name|updateFactory
argument_list|,
name|projectCache
argument_list|,
name|ctl
argument_list|,
name|serverIdent
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|ChangeUpdate ( @erritPersonIdent PersonIdent serverIdent, GitRepositoryManager repoManager, NotesMigration migration, AccountCache accountCache, MetaDataUpdate.User updateFactory, ProjectCache projectCache, @Assisted ChangeControl ctl, @Assisted Date when)
name|ChangeUpdate
parameter_list|(
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
annotation|@
name|Assisted
name|ChangeControl
name|ctl
parameter_list|,
annotation|@
name|Assisted
name|Date
name|when
parameter_list|)
block|{
name|this
argument_list|(
name|serverIdent
argument_list|,
name|repoManager
argument_list|,
name|migration
argument_list|,
name|accountCache
argument_list|,
name|updateFactory
argument_list|,
name|projectCache
operator|.
name|get
argument_list|(
name|getProjectName
argument_list|(
name|ctl
argument_list|)
argument_list|)
operator|.
name|getLabelTypes
argument_list|()
argument_list|,
name|ctl
argument_list|,
name|when
argument_list|)
expr_stmt|;
block|}
DECL|method|getProjectName (ChangeControl ctl)
specifier|private
specifier|static
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|)
block|{
return|return
name|ctl
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|ChangeUpdate ( PersonIdent serverIdent, GitRepositoryManager repoManager, NotesMigration migration, AccountCache accountCache, MetaDataUpdate.User updateFactory, LabelTypes labelTypes, ChangeControl ctl, Date when)
name|ChangeUpdate
parameter_list|(
name|PersonIdent
name|serverIdent
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|Date
name|when
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|labelTypes
operator|=
name|labelTypes
expr_stmt|;
name|this
operator|.
name|ctl
operator|=
name|ctl
expr_stmt|;
name|this
operator|.
name|when
operator|=
name|when
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|approvals
operator|=
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|labelTypes
operator|.
name|nameComparator
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|reviewers
operator|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
expr_stmt|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|ctl
operator|.
name|getChange
argument_list|()
return|;
block|}
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
operator|(
name|IdentifiedUser
operator|)
name|ctl
operator|.
name|getCurrentUser
argument_list|()
return|;
block|}
DECL|method|getWhen ()
specifier|public
name|Date
name|getWhen
parameter_list|()
block|{
return|return
name|when
return|;
block|}
DECL|method|putApproval (String label, short value)
specifier|public
name|void
name|putApproval
parameter_list|(
name|String
name|label
parameter_list|,
name|short
name|value
parameter_list|)
block|{
name|approvals
operator|.
name|put
argument_list|(
name|label
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|setPatchSetId (PatchSet.Id psId)
specifier|public
name|void
name|setPatchSetId
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|psId
operator|==
literal|null
operator|||
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|equals
argument_list|(
name|getChange
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|psId
operator|=
name|psId
expr_stmt|;
block|}
DECL|method|putReviewer (Account.Id reviewer, ReviewerState type)
specifier|public
name|void
name|putReviewer
parameter_list|(
name|Account
operator|.
name|Id
name|reviewer
parameter_list|,
name|ReviewerState
name|type
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|type
operator|!=
name|ReviewerState
operator|.
name|REMOVED
argument_list|,
literal|"invalid ReviewerType"
argument_list|)
expr_stmt|;
name|reviewers
operator|.
name|put
argument_list|(
name|reviewer
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|removeReviewer (Account.Id reviewer)
specifier|public
name|void
name|removeReviewer
parameter_list|(
name|Account
operator|.
name|Id
name|reviewer
parameter_list|)
block|{
name|reviewers
operator|.
name|put
argument_list|(
name|reviewer
argument_list|,
name|ReviewerState
operator|.
name|REMOVED
argument_list|)
expr_stmt|;
block|}
DECL|method|commit ()
specifier|public
name|RevCommit
name|commit
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|commit
argument_list|(
name|checkNotNull
argument_list|(
name|updateFactory
argument_list|,
literal|"MetaDataUpdate.Factory"
argument_list|)
operator|.
name|create
argument_list|(
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|getUser
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|commit (MetaDataUpdate md)
specifier|public
name|RevCommit
name|commit
parameter_list|(
name|MetaDataUpdate
name|md
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|write
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|load
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|md
operator|.
name|setAllowEmpty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CommitBuilder
name|cb
init|=
name|md
operator|.
name|getCommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|newIdent
argument_list|(
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|serverIdent
argument_list|,
name|when
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|commit
argument_list|(
name|md
argument_list|)
return|;
block|}
DECL|method|newIdent (Account author)
specifier|public
name|PersonIdent
name|newIdent
parameter_list|(
name|Account
name|author
parameter_list|)
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|author
operator|.
name|getFullName
argument_list|()
argument_list|,
name|author
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"@"
operator|+
name|GERRIT_PLACEHOLDER_HOST
argument_list|,
name|when
argument_list|,
name|serverIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|openUpdate (MetaDataUpdate update)
specifier|public
name|BatchMetaDataUpdate
name|openUpdate
parameter_list|(
name|MetaDataUpdate
name|update
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|migration
operator|.
name|write
argument_list|()
condition|)
block|{
return|return
name|super
operator|.
name|openUpdate
argument_list|(
name|update
argument_list|)
return|;
block|}
return|return
operator|new
name|BatchMetaDataUpdate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|CommitBuilder
name|commit
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|VersionedMetaData
name|config
parameter_list|,
name|CommitBuilder
name|commit
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
specifier|public
name|RevCommit
name|createRef
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|RevCommit
name|commit
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|RevCommit
name|commitAt
parameter_list|(
name|ObjectId
name|revision
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// Do nothing.
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|protected
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|ChangeNoteUtil
operator|.
name|changeRefName
argument_list|(
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onSave (CommitBuilder commit)
specifier|protected
name|boolean
name|onSave
parameter_list|(
name|CommitBuilder
name|commit
parameter_list|)
block|{
if|if
condition|(
name|approvals
operator|.
name|isEmpty
argument_list|()
operator|&&
name|reviewers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|ps
init|=
name|psId
operator|!=
literal|null
condition|?
name|psId
operator|.
name|get
argument_list|()
else|:
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
name|subject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"Update patch set "
argument_list|)
operator|.
name|append
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
name|addFooter
argument_list|(
name|msg
argument_list|,
name|FOOTER_PATCH_SET
argument_list|,
name|ps
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ReviewerState
argument_list|>
name|e
range|:
name|reviewers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Account
name|account
init|=
name|accountCache
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|PersonIdent
name|ident
init|=
name|newIdent
argument_list|(
name|account
argument_list|)
decl_stmt|;
name|addFooter
argument_list|(
name|msg
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getFooterKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|ident
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|ident
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|e
range|:
name|approvals
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|LabelType
name|lt
init|=
name|labelTypes
operator|.
name|byLabel
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lt
operator|!=
literal|null
condition|)
block|{
name|addFooter
argument_list|(
name|msg
argument_list|,
name|FOOTER_LABEL
argument_list|,
operator|new
name|LabelVote
argument_list|(
name|lt
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|formatWithEquals
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|commit
operator|.
name|setMessage
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|addFooter (StringBuilder sb, FooterKey footer)
specifier|private
specifier|static
name|StringBuilder
name|addFooter
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|FooterKey
name|footer
parameter_list|)
block|{
return|return
name|sb
operator|.
name|append
argument_list|(
name|footer
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
return|;
block|}
DECL|method|addFooter (StringBuilder sb, FooterKey footer, Object value)
specifier|private
specifier|static
name|void
name|addFooter
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|FooterKey
name|footer
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|addFooter
argument_list|(
name|sb
argument_list|,
name|footer
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
comment|// Do nothing; just reads current revision.
block|}
block|}
end_class

end_unit

