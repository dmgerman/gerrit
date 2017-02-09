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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefDatabase
operator|.
name|ALL
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ImmutableSet
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
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|index
operator|.
name|change
operator|.
name|ChangeField
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|MagicBranch
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|Collections
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
name|Ref
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
name|transport
operator|.
name|AdvertiseRefsHook
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
name|transport
operator|.
name|BaseReceivePack
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
name|transport
operator|.
name|ServiceMayNotContinueException
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
name|transport
operator|.
name|UploadPack
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

begin_comment
comment|/** Exposes only the non refs/changes/ reference names. */
end_comment

begin_class
DECL|class|ReceiveCommitsAdvertiseRefsHook
specifier|public
class|class
name|ReceiveCommitsAdvertiseRefsHook
implements|implements
name|AdvertiseRefsHook
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
name|ReceiveCommitsAdvertiseRefsHook
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|AutoValue
DECL|class|Result
specifier|public
specifier|abstract
specifier|static
class|class
name|Result
block|{
DECL|method|allRefs ()
specifier|public
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|allRefs
parameter_list|()
function_decl|;
DECL|method|additionalHaves ()
specifier|public
specifier|abstract
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|additionalHaves
parameter_list|()
function_decl|;
block|}
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
DECL|field|projectName
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
DECL|method|ReceiveCommitsAdvertiseRefsHook ( Provider<InternalChangeQuery> queryProvider, Project.NameKey projectName)
specifier|public
name|ReceiveCommitsAdvertiseRefsHook
parameter_list|(
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
block|{
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|projectName
operator|=
name|projectName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|advertiseRefs (UploadPack us)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|UploadPack
name|us
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ReceiveCommitsAdvertiseRefsHook cannot be used for UploadPack"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|advertiseRefs (BaseReceivePack rp)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|BaseReceivePack
name|rp
parameter_list|)
throws|throws
name|ServiceMayNotContinueException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|oldRefs
init|=
name|rp
operator|.
name|getAdvertisedRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldRefs
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|oldRefs
operator|=
name|rp
operator|.
name|getRepository
argument_list|()
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|ALL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServiceMayNotContinueException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ServiceMayNotContinueException
name|ex
init|=
operator|new
name|ServiceMayNotContinueException
argument_list|()
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
name|Result
name|r
init|=
name|advertiseRefs
argument_list|(
name|oldRefs
argument_list|)
decl_stmt|;
name|rp
operator|.
name|setAdvertisedRefs
argument_list|(
name|r
operator|.
name|allRefs
argument_list|()
argument_list|,
name|r
operator|.
name|additionalHaves
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|advertiseRefs (Map<String, Ref> oldRefs)
specifier|public
name|Result
name|advertiseRefs
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|oldRefs
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|r
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|oldRefs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|allPatchSets
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|oldRefs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|e
range|:
name|oldRefs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|skip
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|r
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
condition|)
block|{
name|allPatchSets
operator|.
name|add
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AutoValue_ReceiveCommitsAdvertiseRefsHook_Result
argument_list|(
name|r
argument_list|,
name|advertiseOpenChanges
argument_list|(
name|allPatchSets
argument_list|)
argument_list|)
return|;
block|}
DECL|field|OPEN_CHANGES_FIELDS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|OPEN_CHANGES_FIELDS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
comment|// Required for ChangeIsVisibleToPrdicate.
name|ChangeField
operator|.
name|CHANGE
operator|.
name|getName
argument_list|()
argument_list|,
name|ChangeField
operator|.
name|REVIEWER
operator|.
name|getName
argument_list|()
argument_list|,
comment|// Required during advertiseOpenChanges.
name|ChangeField
operator|.
name|PATCH_SET
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|advertiseOpenChanges (Set<ObjectId> allPatchSets)
specifier|private
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|advertiseOpenChanges
parameter_list|(
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|allPatchSets
parameter_list|)
block|{
comment|// Advertise some recent open changes, in case a commit is based on one.
name|int
name|limit
init|=
literal|32
decl_stmt|;
try|try
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|limit
argument_list|)
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|setRequestedFields
argument_list|(
name|OPEN_CHANGES_FIELDS
argument_list|)
operator|.
name|enforceVisibility
argument_list|(
literal|true
argument_list|)
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
operator|.
name|byProjectOpen
argument_list|(
name|projectName
argument_list|)
control|)
block|{
name|PatchSet
name|ps
init|=
name|cd
operator|.
name|currentPatchSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|ps
operator|!=
literal|null
condition|)
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
comment|// Ensure we actually observed a patch set ref pointing to this
comment|// object, in case the database is out of sync with the repo and the
comment|// object doesn't actually exist.
if|if
condition|(
name|allPatchSets
operator|.
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|r
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot list open changes of "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
DECL|method|skip (String name)
specifier|private
specifier|static
name|boolean
name|skip
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_CACHE_AUTOMERGE
argument_list|)
operator|||
name|MagicBranch
operator|.
name|isMagicBranch
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

