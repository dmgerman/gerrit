begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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

begin_comment
comment|/** A single delta related to a specific patch-set of a change. */
end_comment

begin_class
DECL|class|AbstractChangeUpdate
specifier|public
specifier|abstract
class|class
name|AbstractChangeUpdate
extends|extends
name|VersionedMetaData
block|{
DECL|field|migration
specifier|protected
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|field|repoManager
specifier|protected
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|updateFactory
specifier|protected
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
decl_stmt|;
DECL|field|ctl
specifier|protected
specifier|final
name|ChangeControl
name|ctl
decl_stmt|;
DECL|field|serverIdent
specifier|protected
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|when
specifier|protected
specifier|final
name|Date
name|when
decl_stmt|;
DECL|field|psId
specifier|protected
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|method|AbstractChangeUpdate (NotesMigration migration, GitRepositoryManager repoManager, MetaDataUpdate.User updateFactory, ChangeControl ctl, PersonIdent serverIdent, Date when)
name|AbstractChangeUpdate
parameter_list|(
name|NotesMigration
name|migration
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|PersonIdent
name|serverIdent
parameter_list|,
name|Date
name|when
parameter_list|)
block|{
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|ctl
operator|=
name|ctl
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|when
operator|=
name|when
expr_stmt|;
block|}
DECL|method|getChangeNotes ()
specifier|public
name|ChangeNotes
name|getChangeNotes
parameter_list|()
block|{
return|return
name|ctl
operator|.
name|getNotes
argument_list|()
return|;
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
name|getId
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
DECL|method|load ()
specifier|private
name|void
name|load
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|migration
operator|.
name|write
argument_list|()
operator|&&
name|getRevision
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|getProjectName
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
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"use openUpdate()"
argument_list|)
throw|;
block|}
DECL|method|openUpdate ()
specifier|public
name|BatchMetaDataUpdate
name|openUpdate
parameter_list|()
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
name|load
argument_list|()
expr_stmt|;
name|MetaDataUpdate
name|md
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|getProjectName
argument_list|()
argument_list|,
name|getUser
argument_list|()
argument_list|)
decl_stmt|;
name|md
operator|.
name|setAllowEmpty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|openUpdate
argument_list|(
name|md
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
name|void
name|removeRef
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
comment|// Do nothing.
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"use commit()"
argument_list|)
throw|;
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
comment|//Do nothing; just reads the current revision.
block|}
DECL|method|newIdent (Account author, Date when)
specifier|protected
name|PersonIdent
name|newIdent
parameter_list|(
name|Account
name|author
parameter_list|,
name|Date
name|when
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
comment|/**    * @return the NameKey for the project where the update will be stored,    *    which is not necessarily the same as the change's project.    */
DECL|method|getProjectName ()
specifier|abstract
specifier|protected
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
function_decl|;
block|}
end_class

end_unit

