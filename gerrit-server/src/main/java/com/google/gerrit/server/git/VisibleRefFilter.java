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
operator|.
name|REFS_CACHE_AUTOMERGE
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_CHANGES
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_CONFIG
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_USERS_SELF
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toMap
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
name|Nullable
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|Branch
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
name|reviewdb
operator|.
name|server
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
name|server
operator|.
name|CurrentUser
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
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|ChangeNotes
operator|.
name|Factory
operator|.
name|ChangeNotesResult
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|GlobalPermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|ProjectPermission
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
name|permissions
operator|.
name|RefPermission
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
name|ProjectControl
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|Constants
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
name|lib
operator|.
name|RefDatabase
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
name|lib
operator|.
name|SymbolicRef
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
name|RevWalk
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
name|AbstractAdvertiseRefsHook
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

begin_class
DECL|class|VisibleRefFilter
specifier|public
class|class
name|VisibleRefFilter
extends|extends
name|AbstractAdvertiseRefsHook
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
name|VisibleRefFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (ProjectState projectState, Repository git)
name|VisibleRefFilter
name|create
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|Repository
name|git
parameter_list|)
function_decl|;
block|}
DECL|field|tagCache
specifier|private
specifier|final
name|TagCache
name|tagCache
decl_stmt|;
DECL|field|changeNotesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
decl_stmt|;
DECL|field|changeCache
annotation|@
name|Nullable
specifier|private
specifier|final
name|SearchingChangeCacheImpl
name|changeCache
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|perm
specifier|private
specifier|final
name|PermissionBackend
operator|.
name|ForProject
name|perm
decl_stmt|;
DECL|field|projectState
specifier|private
specifier|final
name|ProjectState
name|projectState
decl_stmt|;
DECL|field|git
specifier|private
specifier|final
name|Repository
name|git
decl_stmt|;
DECL|field|projectCtl
specifier|private
name|ProjectControl
name|projectCtl
decl_stmt|;
DECL|field|showMetadata
specifier|private
name|boolean
name|showMetadata
init|=
literal|true
decl_stmt|;
DECL|field|userEditPrefix
specifier|private
name|String
name|userEditPrefix
decl_stmt|;
DECL|field|visibleChanges
specifier|private
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Branch
operator|.
name|NameKey
argument_list|>
name|visibleChanges
decl_stmt|;
annotation|@
name|Inject
DECL|method|VisibleRefFilter ( TagCache tagCache, ChangeNotes.Factory changeNotesFactory, @Nullable SearchingChangeCacheImpl changeCache, Provider<ReviewDb> db, Provider<CurrentUser> user, PermissionBackend permissionBackend, @Assisted ProjectState projectState, @Assisted Repository git)
name|VisibleRefFilter
parameter_list|(
name|TagCache
name|tagCache
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
parameter_list|,
annotation|@
name|Nullable
name|SearchingChangeCacheImpl
name|changeCache
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
annotation|@
name|Assisted
name|ProjectState
name|projectState
parameter_list|,
annotation|@
name|Assisted
name|Repository
name|git
parameter_list|)
block|{
name|this
operator|.
name|tagCache
operator|=
name|tagCache
expr_stmt|;
name|this
operator|.
name|changeNotesFactory
operator|=
name|changeNotesFactory
expr_stmt|;
name|this
operator|.
name|changeCache
operator|=
name|changeCache
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|perm
operator|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|database
argument_list|(
name|db
argument_list|)
operator|.
name|project
argument_list|(
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectState
operator|=
name|projectState
expr_stmt|;
name|this
operator|.
name|git
operator|=
name|git
expr_stmt|;
block|}
comment|/** Show change references. Default is {@code true}. */
DECL|method|setShowMetadata (boolean show)
specifier|public
name|VisibleRefFilter
name|setShowMetadata
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
name|showMetadata
operator|=
name|show
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|filter (Map<String, Ref> refs, boolean filterTagsSeparately)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|filter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
parameter_list|,
name|boolean
name|filterTagsSeparately
parameter_list|)
block|{
if|if
condition|(
name|projectState
operator|.
name|isAllUsers
argument_list|()
condition|)
block|{
name|refs
operator|=
name|addUsersSelfSymref
argument_list|(
name|refs
argument_list|)
expr_stmt|;
block|}
name|PermissionBackend
operator|.
name|WithUser
name|withUser
init|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|PermissionBackend
operator|.
name|ForProject
name|forProject
init|=
name|withUser
operator|.
name|project
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projectState
operator|.
name|isAllUsers
argument_list|()
condition|)
block|{
if|if
condition|(
name|checkProjectPermission
argument_list|(
name|forProject
argument_list|,
name|ProjectPermission
operator|.
name|READ
argument_list|)
condition|)
block|{
return|return
name|refs
return|;
block|}
elseif|else
if|if
condition|(
name|checkProjectPermission
argument_list|(
name|forProject
argument_list|,
name|ProjectPermission
operator|.
name|READ_NO_CONFIG
argument_list|)
condition|)
block|{
return|return
name|fastHideRefsMetaConfig
argument_list|(
name|refs
argument_list|)
return|;
block|}
block|}
name|Account
operator|.
name|Id
name|userId
decl_stmt|;
name|boolean
name|viewMetadata
decl_stmt|;
if|if
condition|(
name|user
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|viewMetadata
operator|=
name|withUser
operator|.
name|testOrFalse
argument_list|(
name|GlobalPermission
operator|.
name|ACCESS_DATABASE
argument_list|)
expr_stmt|;
name|IdentifiedUser
name|u
init|=
name|user
operator|.
name|get
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
decl_stmt|;
name|userId
operator|=
name|u
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
name|userEditPrefix
operator|=
name|RefNames
operator|.
name|refsEditPrefix
argument_list|(
name|userId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|userId
operator|=
literal|null
expr_stmt|;
name|viewMetadata
operator|=
literal|false
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Ref
argument_list|>
name|deferredTags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|projectCtl
operator|=
name|projectState
operator|.
name|controlFor
argument_list|(
name|user
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|refs
operator|.
name|values
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|ref
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|REFS_CACHE_AUTOMERGE
argument_list|)
operator|||
operator|(
operator|!
name|showMetadata
operator|&&
name|isMetadata
argument_list|(
name|name
argument_list|)
operator|)
condition|)
block|{
continue|continue;
block|}
elseif|else
if|if
condition|(
name|RefNames
operator|.
name|isRefsEdit
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// Edits are visible only to the owning user, if change is visible.
if|if
condition|(
name|viewMetadata
operator|||
name|visibleEdit
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|(
name|changeId
operator|=
name|Change
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|name
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
comment|// Change ref is visible only if the change is visible.
if|if
condition|(
name|viewMetadata
operator|||
name|visible
argument_list|(
name|changeId
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|(
name|accountId
operator|=
name|Account
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|name
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
comment|// Account ref is visible only to corresponding account.
if|if
condition|(
name|viewMetadata
operator|||
operator|(
name|accountId
operator|.
name|equals
argument_list|(
name|userId
argument_list|)
operator|&&
name|canReadRef
argument_list|(
name|name
argument_list|)
operator|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isTag
argument_list|(
name|ref
argument_list|)
condition|)
block|{
comment|// If its a tag, consider it later.
if|if
condition|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|deferredTags
operator|.
name|add
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_SEQUENCES
argument_list|)
condition|)
block|{
comment|// Sequences are internal database implementation details.
if|if
condition|(
name|viewMetadata
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|projectState
operator|.
name|isAllUsers
argument_list|()
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|RefNames
operator|.
name|REFS_EXTERNAL_IDS
argument_list|)
condition|)
block|{
comment|// The notes branch with the external IDs of all users must not be exposed to normal users.
if|if
condition|(
name|viewMetadata
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|canReadRef
argument_list|(
name|ref
operator|.
name|getLeaf
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// Use the leaf to lookup the control data. If the reference is
comment|// symbolic we want the control around the final target. If its
comment|// not symbolic then getLeaf() is a no-op returning ref itself.
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If we have tags that were deferred, we need to do a revision walk
comment|// to identify what tags we can actually reach, and what we cannot.
comment|//
if|if
condition|(
operator|!
name|deferredTags
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|(
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
operator|||
name|filterTagsSeparately
operator|)
condition|)
block|{
name|TagMatcher
name|tags
init|=
name|tagCache
operator|.
name|get
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|matcher
argument_list|(
name|tagCache
argument_list|,
name|git
argument_list|,
name|filterTagsSeparately
condition|?
name|filter
argument_list|(
name|git
operator|.
name|getAllRefs
argument_list|()
argument_list|)
operator|.
name|values
argument_list|()
else|:
name|result
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Ref
name|tag
range|:
name|deferredTags
control|)
block|{
if|if
condition|(
name|tags
operator|.
name|isReachable
argument_list|(
name|tag
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|tag
operator|.
name|getName
argument_list|()
argument_list|,
name|tag
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|fastHideRefsMetaConfig (Map<String, Ref> refs)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|fastHideRefsMetaConfig
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
parameter_list|)
block|{
if|if
condition|(
name|refs
operator|.
name|containsKey
argument_list|(
name|REFS_CONFIG
argument_list|)
operator|&&
operator|!
name|canReadRef
argument_list|(
name|REFS_CONFIG
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|r
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|refs
argument_list|)
decl_stmt|;
name|r
operator|.
name|remove
argument_list|(
name|REFS_CONFIG
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
return|return
name|refs
return|;
block|}
DECL|method|addUsersSelfSymref (Map<String, Ref> refs)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|addUsersSelfSymref
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
parameter_list|)
block|{
if|if
condition|(
name|user
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|Ref
name|r
init|=
name|refs
operator|.
name|get
argument_list|(
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|user
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|SymbolicRef
name|s
init|=
operator|new
name|SymbolicRef
argument_list|(
name|REFS_USERS_SELF
argument_list|,
name|r
argument_list|)
decl_stmt|;
name|refs
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|refs
argument_list|)
expr_stmt|;
name|refs
operator|.
name|put
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|refs
return|;
block|}
annotation|@
name|Override
DECL|method|getAdvertisedRefs (Repository repository, RevWalk revWalk)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|getAdvertisedRefs
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|RevWalk
name|revWalk
parameter_list|)
throws|throws
name|ServiceMayNotContinueException
block|{
try|try
block|{
return|return
name|filter
argument_list|(
name|repository
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|RefDatabase
operator|.
name|ALL
argument_list|)
argument_list|)
return|;
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
DECL|method|filter (Map<String, Ref> refs)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|filter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
parameter_list|)
block|{
return|return
name|filter
argument_list|(
name|refs
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|visible (Change.Id changeId)
specifier|private
name|boolean
name|visible
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
if|if
condition|(
name|visibleChanges
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|changeCache
operator|==
literal|null
condition|)
block|{
name|visibleChanges
operator|=
name|visibleChangesByScan
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|visibleChanges
operator|=
name|visibleChangesBySearch
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|visibleChanges
operator|.
name|containsKey
argument_list|(
name|changeId
argument_list|)
return|;
block|}
DECL|method|visibleEdit (String name)
specifier|private
name|boolean
name|visibleEdit
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|Change
operator|.
name|Id
operator|.
name|fromEditRefPart
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// Initialize if it wasn't yet
if|if
condition|(
name|visibleChanges
operator|==
literal|null
condition|)
block|{
name|visible
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|userEditPrefix
operator|!=
literal|null
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|userEditPrefix
argument_list|)
operator|&&
name|visible
argument_list|(
name|id
argument_list|)
operator|)
operator|||
operator|(
name|visibleChanges
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
operator|&&
name|projectCtl
operator|.
name|controlForRef
argument_list|(
name|visibleChanges
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEditVisible
argument_list|()
operator|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|visibleChangesBySearch ()
specifier|private
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Branch
operator|.
name|NameKey
argument_list|>
name|visibleChangesBySearch
parameter_list|()
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|projectState
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Branch
operator|.
name|NameKey
argument_list|>
name|visibleChanges
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changeCache
operator|.
name|getChangeData
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|project
argument_list|)
control|)
block|{
name|ChangeNotes
name|notes
init|=
name|changeNotesFactory
operator|.
name|createFromIndexedChange
argument_list|(
name|cd
operator|.
name|change
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|perm
operator|.
name|indexedChange
argument_list|(
name|cd
argument_list|,
name|notes
argument_list|)
operator|.
name|test
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
condition|)
block|{
name|visibleChanges
operator|.
name|put
argument_list|(
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|visibleChanges
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
decl||
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot load changes for project "
operator|+
name|project
operator|+
literal|", assuming no changes are visible"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
DECL|method|visibleChangesByScan ()
specifier|private
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Branch
operator|.
name|NameKey
argument_list|>
name|visibleChangesByScan
parameter_list|()
block|{
name|Project
operator|.
name|NameKey
name|p
init|=
name|projectState
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|Stream
argument_list|<
name|ChangeNotesResult
argument_list|>
name|s
decl_stmt|;
try|try
block|{
name|s
operator|=
name|changeNotesFactory
operator|.
name|scan
argument_list|(
name|git
argument_list|,
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|p
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
literal|"Cannot load changes for project "
operator|+
name|p
operator|+
literal|", assuming no changes are visible"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
return|return
name|s
operator|.
name|map
argument_list|(
name|r
lambda|->
name|toNotes
argument_list|(
name|p
argument_list|,
name|r
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|n
lambda|->
name|n
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|n
lambda|->
name|n
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Nullable
DECL|method|toNotes (Project.NameKey p, ChangeNotesResult r)
specifier|private
name|ChangeNotes
name|toNotes
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
name|ChangeNotesResult
name|r
parameter_list|)
block|{
if|if
condition|(
name|r
operator|.
name|error
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to load change "
operator|+
name|r
operator|.
name|id
argument_list|()
operator|+
literal|" in "
operator|+
name|p
argument_list|,
name|r
operator|.
name|error
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
if|if
condition|(
name|perm
operator|.
name|change
argument_list|(
name|r
operator|.
name|notes
argument_list|()
argument_list|)
operator|.
name|test
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
condition|)
block|{
return|return
name|r
operator|.
name|notes
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to check permission for "
operator|+
name|r
operator|.
name|id
argument_list|()
operator|+
literal|" in "
operator|+
name|p
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|isMetadata (String name)
specifier|private
name|boolean
name|isMetadata
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
name|REFS_CHANGES
argument_list|)
operator|||
name|RefNames
operator|.
name|isRefsEdit
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|isTag (Ref ref)
specifier|private
specifier|static
name|boolean
name|isTag
parameter_list|(
name|Ref
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|getLeaf
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_TAGS
argument_list|)
return|;
block|}
DECL|method|canReadRef (String ref)
specifier|private
name|boolean
name|canReadRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
try|try
block|{
name|perm
operator|.
name|ref
argument_list|(
name|ref
argument_list|)
operator|.
name|check
argument_list|(
name|RefPermission
operator|.
name|READ
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"unable to check permissions"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|checkProjectPermission ( PermissionBackend.ForProject forProject, ProjectPermission perm)
specifier|private
name|boolean
name|checkProjectPermission
parameter_list|(
name|PermissionBackend
operator|.
name|ForProject
name|forProject
parameter_list|,
name|ProjectPermission
name|perm
parameter_list|)
block|{
try|try
block|{
name|forProject
operator|.
name|check
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Can't check permission for user %s on project %s"
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|projectState
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

