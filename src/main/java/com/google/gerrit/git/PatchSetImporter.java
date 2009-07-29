begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|PatchScriptSettings
operator|.
name|Whitespace
operator|.
name|IGNORE_NONE
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountExternalId
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchSetAncestor
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|RevId
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|client
operator|.
name|reviewdb
operator|.
name|UserIdentity
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
name|GerritServer
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
name|DiffCache
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
name|DiffCacheContent
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
name|DiffCacheKey
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
name|client
operator|.
name|OrmException
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
name|client
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Commit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|CombinedFileHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|FileHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|HashMap
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
name|List
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Imports a {@link PatchSet} from a {@link Commit}. */
end_comment

begin_class
DECL|class|PatchSetImporter
specifier|public
class|class
name|PatchSetImporter
block|{
DECL|field|diffCache
specifier|private
specifier|final
name|DiffCache
name|diffCache
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|projectKey
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
decl_stmt|;
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|src
specifier|private
specifier|final
name|RevCommit
name|src
decl_stmt|;
DECL|field|dst
specifier|private
specifier|final
name|PatchSet
name|dst
decl_stmt|;
DECL|field|isNew
specifier|private
specifier|final
name|boolean
name|isNew
decl_stmt|;
DECL|field|txn
specifier|private
name|Transaction
name|txn
decl_stmt|;
DECL|field|gitpatch
specifier|private
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|Patch
name|gitpatch
decl_stmt|;
DECL|field|info
specifier|private
name|PatchSetInfo
name|info
decl_stmt|;
DECL|field|infoIsNew
specifier|private
name|boolean
name|infoIsNew
decl_stmt|;
DECL|field|patchExisting
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Patch
argument_list|>
name|patchExisting
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Patch
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|patchInsert
specifier|private
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|patchInsert
init|=
operator|new
name|ArrayList
argument_list|<
name|Patch
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|patchUpdate
specifier|private
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|patchUpdate
init|=
operator|new
name|ArrayList
argument_list|<
name|Patch
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ancestorExisting
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|PatchSetAncestor
argument_list|>
name|ancestorExisting
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|PatchSetAncestor
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ancestorInsert
specifier|private
specifier|final
name|List
argument_list|<
name|PatchSetAncestor
argument_list|>
name|ancestorInsert
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchSetAncestor
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ancestorUpdate
specifier|private
specifier|final
name|List
argument_list|<
name|PatchSetAncestor
argument_list|>
name|ancestorUpdate
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchSetAncestor
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|PatchSetImporter (final GerritServer gs, final DiffCache dc, final ReviewDb dstDb, final Project.NameKey proj, final Repository srcRepo, final RevCommit srcCommit, final PatchSet dstPatchSet, final boolean isNewPatchSet)
specifier|public
name|PatchSetImporter
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|DiffCache
name|dc
parameter_list|,
specifier|final
name|ReviewDb
name|dstDb
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
specifier|final
name|Repository
name|srcRepo
parameter_list|,
specifier|final
name|RevCommit
name|srcCommit
parameter_list|,
specifier|final
name|PatchSet
name|dstPatchSet
parameter_list|,
specifier|final
name|boolean
name|isNewPatchSet
parameter_list|)
block|{
name|diffCache
operator|=
name|dc
expr_stmt|;
name|db
operator|=
name|dstDb
expr_stmt|;
name|projectKey
operator|=
name|proj
expr_stmt|;
name|repo
operator|=
name|srcRepo
expr_stmt|;
name|src
operator|=
name|srcCommit
expr_stmt|;
name|dst
operator|=
name|dstPatchSet
expr_stmt|;
name|isNew
operator|=
name|isNewPatchSet
expr_stmt|;
block|}
DECL|method|setTransaction (final Transaction t)
specifier|public
name|void
name|setTransaction
parameter_list|(
specifier|final
name|Transaction
name|t
parameter_list|)
block|{
name|txn
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getPatchSetInfo ()
specifier|public
name|PatchSetInfo
name|getPatchSetInfo
parameter_list|()
block|{
return|return
name|info
return|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|gitpatch
operator|=
name|readGitPatch
argument_list|()
expr_stmt|;
name|dst
operator|.
name|setRevision
argument_list|(
name|toRevId
argument_list|(
name|src
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isNew
condition|)
block|{
comment|// If we aren't a new patch set then we need to load the existing
comment|// files so we can update or delete them if there are corrections.
comment|//
name|info
operator|=
name|db
operator|.
name|patchSetInfo
argument_list|()
operator|.
name|get
argument_list|(
name|dst
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Patch
name|p
range|:
name|db
operator|.
name|patches
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|dst
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
name|patchExisting
operator|.
name|put
argument_list|(
name|p
operator|.
name|getFileName
argument_list|()
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|PatchSetAncestor
name|a
range|:
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|ancestorsOf
argument_list|(
name|dst
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
name|ancestorExisting
operator|.
name|put
argument_list|(
name|a
operator|.
name|getPosition
argument_list|()
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
name|importInfo
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|FileHeader
name|fh
range|:
name|gitpatch
operator|.
name|getFiles
argument_list|()
control|)
block|{
name|importFile
argument_list|(
name|fh
argument_list|)
expr_stmt|;
block|}
specifier|final
name|boolean
name|auto
init|=
name|txn
operator|==
literal|null
decl_stmt|;
if|if
condition|(
name|auto
condition|)
block|{
name|txn
operator|=
name|db
operator|.
name|beginTransaction
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isNew
condition|)
block|{
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|dst
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|infoIsNew
condition|)
block|{
name|db
operator|.
name|patchSetInfo
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|info
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|db
operator|.
name|patchSetInfo
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|info
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patches
argument_list|()
operator|.
name|insert
argument_list|(
name|patchInsert
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|insert
argument_list|(
name|ancestorInsert
argument_list|,
name|txn
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isNew
condition|)
block|{
name|db
operator|.
name|patches
argument_list|()
operator|.
name|update
argument_list|(
name|patchUpdate
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|db
operator|.
name|patches
argument_list|()
operator|.
name|delete
argument_list|(
name|patchExisting
operator|.
name|values
argument_list|()
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|update
argument_list|(
name|ancestorUpdate
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|delete
argument_list|(
name|ancestorExisting
operator|.
name|values
argument_list|()
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|auto
condition|)
block|{
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
name|txn
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|importInfo ()
specifier|private
name|void
name|importInfo
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|info
operator|=
operator|new
name|PatchSetInfo
argument_list|(
name|dst
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|infoIsNew
operator|=
literal|true
expr_stmt|;
block|}
name|info
operator|.
name|setSubject
argument_list|(
name|src
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setMessage
argument_list|(
name|src
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setAuthor
argument_list|(
name|toUserIdentity
argument_list|(
name|src
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|setCommitter
argument_list|(
name|toUserIdentity
argument_list|(
name|src
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// FIXME: This code has to be moved to separate method when patchSetInfo
comment|// creation is removed
for|for
control|(
name|int
name|p
init|=
literal|0
init|;
name|p
operator|<
name|src
operator|.
name|getParentCount
argument_list|()
condition|;
name|p
operator|++
control|)
block|{
name|PatchSetAncestor
name|a
init|=
name|ancestorExisting
operator|.
name|remove
argument_list|(
name|p
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
name|a
operator|=
operator|new
name|PatchSetAncestor
argument_list|(
operator|new
name|PatchSetAncestor
operator|.
name|Id
argument_list|(
name|dst
operator|.
name|getId
argument_list|()
argument_list|,
name|p
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ancestorInsert
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ancestorUpdate
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
name|a
operator|.
name|setAncestorRevision
argument_list|(
name|toRevId
argument_list|(
name|src
operator|.
name|getParent
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|toUserIdentity (final PersonIdent who)
specifier|private
name|UserIdentity
name|toUserIdentity
parameter_list|(
specifier|final
name|PersonIdent
name|who
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|UserIdentity
name|u
init|=
operator|new
name|UserIdentity
argument_list|()
decl_stmt|;
name|u
operator|.
name|setName
argument_list|(
name|who
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setEmail
argument_list|(
name|who
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setDate
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|who
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|u
operator|.
name|setTimeZone
argument_list|(
name|who
operator|.
name|getTimeZoneOffset
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|u
operator|.
name|getEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// If only one account has access to this email address, select it
comment|// as the identity of the user.
comment|//
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|a
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
specifier|final
name|AccountExternalId
name|e
range|:
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byEmailAddress
argument_list|(
name|u
operator|.
name|getEmail
argument_list|()
argument_list|)
control|)
block|{
name|a
operator|.
name|add
argument_list|(
name|e
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|a
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|u
operator|.
name|setAccount
argument_list|(
name|a
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|u
return|;
block|}
DECL|method|importFile (final FileHeader fh)
specifier|private
name|void
name|importFile
parameter_list|(
specifier|final
name|FileHeader
name|fh
parameter_list|)
block|{
specifier|final
name|String
name|path
decl_stmt|;
if|if
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
operator|==
name|FileHeader
operator|.
name|ChangeType
operator|.
name|DELETE
condition|)
block|{
name|path
operator|=
name|fh
operator|.
name|getOldName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|fh
operator|.
name|getNewName
argument_list|()
expr_stmt|;
block|}
name|Patch
name|p
init|=
name|patchExisting
operator|.
name|remove
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
operator|new
name|Patch
argument_list|(
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|dst
operator|.
name|getId
argument_list|()
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|patchInsert
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|.
name|setSourceFileName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|patchUpdate
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
comment|// Convert the ChangeType
comment|//
if|if
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
operator|==
name|FileHeader
operator|.
name|ChangeType
operator|.
name|ADD
condition|)
block|{
name|p
operator|.
name|setChangeType
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|ADDED
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
operator|==
name|FileHeader
operator|.
name|ChangeType
operator|.
name|MODIFY
condition|)
block|{
name|p
operator|.
name|setChangeType
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|MODIFIED
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
operator|==
name|FileHeader
operator|.
name|ChangeType
operator|.
name|DELETE
condition|)
block|{
name|p
operator|.
name|setChangeType
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|DELETED
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
operator|==
name|FileHeader
operator|.
name|ChangeType
operator|.
name|RENAME
condition|)
block|{
name|p
operator|.
name|setChangeType
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|RENAMED
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSourceFileName
argument_list|(
name|fh
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
operator|==
name|FileHeader
operator|.
name|ChangeType
operator|.
name|COPY
condition|)
block|{
name|p
operator|.
name|setChangeType
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|COPIED
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSourceFileName
argument_list|(
name|fh
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Convert the PatchType
comment|//
if|if
condition|(
name|fh
operator|instanceof
name|CombinedFileHeader
condition|)
block|{
name|p
operator|.
name|setPatchType
argument_list|(
name|Patch
operator|.
name|PatchType
operator|.
name|N_WAY
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fh
operator|.
name|getPatchType
argument_list|()
operator|==
name|FileHeader
operator|.
name|PatchType
operator|.
name|GIT_BINARY
condition|)
block|{
name|p
operator|.
name|setPatchType
argument_list|(
name|Patch
operator|.
name|PatchType
operator|.
name|BINARY
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fh
operator|.
name|getPatchType
argument_list|()
operator|==
name|FileHeader
operator|.
name|PatchType
operator|.
name|BINARY
condition|)
block|{
name|p
operator|.
name|setPatchType
argument_list|(
name|Patch
operator|.
name|PatchType
operator|.
name|BINARY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getPatchType
argument_list|()
operator|!=
name|Patch
operator|.
name|PatchType
operator|.
name|BINARY
condition|)
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
name|fh
operator|.
name|getBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|ptr
init|=
name|fh
operator|.
name|getStartOffset
argument_list|()
init|;
name|ptr
operator|<
name|fh
operator|.
name|getEndOffset
argument_list|()
condition|;
name|ptr
operator|++
control|)
block|{
if|if
condition|(
name|buf
index|[
name|ptr
index|]
operator|==
literal|'\0'
condition|)
block|{
comment|// Its really binary, but Git couldn't see the nul early enough
comment|// to realize its binary, and instead produced the diff.
comment|//
comment|// Force it to be a binary; it really should have been that.
comment|//
name|p
operator|.
name|setPatchType
argument_list|(
name|Patch
operator|.
name|PatchType
operator|.
name|BINARY
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
DECL|method|toRevId (final RevCommit src)
specifier|private
specifier|static
name|RevId
name|toRevId
parameter_list|(
specifier|final
name|RevCommit
name|src
parameter_list|)
block|{
return|return
operator|new
name|RevId
argument_list|(
name|src
operator|.
name|getId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|readGitPatch ()
specifier|private
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|Patch
name|readGitPatch
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"git"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--git-dir=."
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"diff-tree"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"-M"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--full-index"
argument_list|)
expr_stmt|;
specifier|final
name|ObjectId
name|a
decl_stmt|,
name|b
decl_stmt|;
switch|switch
condition|(
name|src
operator|.
name|getParentCount
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
name|args
operator|.
name|add
argument_list|(
literal|"--unified=1"
argument_list|)
expr_stmt|;
name|a
operator|=
name|emptyTree
argument_list|()
expr_stmt|;
name|b
operator|=
name|src
operator|.
name|getId
argument_list|()
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|args
operator|.
name|add
argument_list|(
literal|"--unified=1"
argument_list|)
expr_stmt|;
name|a
operator|=
name|src
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
expr_stmt|;
name|b
operator|=
name|src
operator|.
name|getId
argument_list|()
expr_stmt|;
break|break;
default|default:
name|args
operator|.
name|add
argument_list|(
literal|"--cc"
argument_list|)
expr_stmt|;
name|a
operator|=
literal|null
expr_stmt|;
name|b
operator|=
name|src
operator|.
name|getId
argument_list|()
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
name|a
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|args
operator|.
name|add
argument_list|(
name|b
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Process
name|proc
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|args
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|args
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
literal|null
argument_list|,
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|Patch
name|p
decl_stmt|;
try|try
block|{
name|p
operator|=
operator|new
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|Patch
argument_list|()
expr_stmt|;
name|proc
operator|.
name|getOutputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|proc
operator|.
name|getErrorStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|p
operator|.
name|parse
argument_list|(
name|proc
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|proc
operator|.
name|getInputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|proc
operator|.
name|waitFor
argument_list|()
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"git diff-tree exited abnormally"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{       }
block|}
for|for
control|(
specifier|final
name|FileHeader
name|fh
range|:
name|p
operator|.
name|getFiles
argument_list|()
control|)
block|{
specifier|final
name|DiffCacheKey
name|k
decl_stmt|;
name|String
name|srcName
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|fh
operator|.
name|getChangeType
argument_list|()
condition|)
block|{
case|case
name|RENAME
case|:
case|case
name|COPY
case|:
name|srcName
operator|=
name|fh
operator|.
name|getOldName
argument_list|()
expr_stmt|;
break|break;
block|}
specifier|final
name|String
name|newName
init|=
name|fh
operator|.
name|getNewName
argument_list|()
decl_stmt|;
name|k
operator|=
operator|new
name|DiffCacheKey
argument_list|(
name|projectKey
argument_list|,
name|a
argument_list|,
name|b
argument_list|,
name|newName
argument_list|,
name|srcName
argument_list|,
name|IGNORE_NONE
argument_list|)
expr_stmt|;
name|diffCache
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|DiffCacheContent
operator|.
name|create
argument_list|(
name|fh
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|p
return|;
block|}
DECL|method|emptyTree ()
specifier|private
name|ObjectId
name|emptyTree
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ObjectWriter
argument_list|(
name|repo
argument_list|)
operator|.
name|writeCanonicalTree
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

