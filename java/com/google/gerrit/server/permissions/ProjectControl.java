begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|AccessSection
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
name|Permission
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
name|PermissionRule
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
name|conditions
operator|.
name|BooleanCondition
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
name|account
operator|.
name|GroupMembership
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
name|config
operator|.
name|GitReceivePackGroups
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
name|config
operator|.
name|GitUploadPackGroups
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
name|group
operator|.
name|SystemGroupBackend
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
name|permissions
operator|.
name|PermissionBackend
operator|.
name|ForChange
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
operator|.
name|ForProject
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
operator|.
name|ForRef
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
operator|.
name|RefFilterOptions
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
name|project
operator|.
name|SectionMatcher
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|EnumSet
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
name|Repository
import|;
end_import

begin_comment
comment|/** Access control management for a user accessing a project's data. */
end_comment

begin_class
DECL|class|ProjectControl
class|class
name|ProjectControl
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (CurrentUser who, ProjectState ps)
name|ProjectControl
name|create
parameter_list|(
name|CurrentUser
name|who
parameter_list|,
name|ProjectState
name|ps
parameter_list|)
function_decl|;
block|}
DECL|field|uploadGroups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|uploadGroups
decl_stmt|;
DECL|field|receiveGroups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|receiveGroups
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|state
specifier|private
specifier|final
name|ProjectState
name|state
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|permissionFilter
specifier|private
specifier|final
name|PermissionCollection
operator|.
name|Factory
name|permissionFilter
decl_stmt|;
DECL|field|refFilterFactory
specifier|private
specifier|final
name|DefaultRefFilter
operator|.
name|Factory
name|refFilterFactory
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|allSections
specifier|private
name|List
argument_list|<
name|SectionMatcher
argument_list|>
name|allSections
decl_stmt|;
DECL|field|refControls
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RefControl
argument_list|>
name|refControls
decl_stmt|;
DECL|field|declaredOwner
specifier|private
name|Boolean
name|declaredOwner
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectControl ( @itUploadPackGroups Set<AccountGroup.UUID> uploadGroups, @GitReceivePackGroups Set<AccountGroup.UUID> receiveGroups, PermissionCollection.Factory permissionFilter, ChangeControl.Factory changeControlFactory, PermissionBackend permissionBackend, DefaultRefFilter.Factory refFilterFactory, IdentifiedUser.GenericFactory identifiedUserFactory, @Assisted CurrentUser who, @Assisted ProjectState ps)
name|ProjectControl
parameter_list|(
annotation|@
name|GitUploadPackGroups
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|uploadGroups
parameter_list|,
annotation|@
name|GitReceivePackGroups
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|receiveGroups
parameter_list|,
name|PermissionCollection
operator|.
name|Factory
name|permissionFilter
parameter_list|,
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|DefaultRefFilter
operator|.
name|Factory
name|refFilterFactory
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
annotation|@
name|Assisted
name|CurrentUser
name|who
parameter_list|,
annotation|@
name|Assisted
name|ProjectState
name|ps
parameter_list|)
block|{
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|uploadGroups
operator|=
name|uploadGroups
expr_stmt|;
name|this
operator|.
name|receiveGroups
operator|=
name|receiveGroups
expr_stmt|;
name|this
operator|.
name|permissionFilter
operator|=
name|permissionFilter
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|refFilterFactory
operator|=
name|refFilterFactory
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|user
operator|=
name|who
expr_stmt|;
name|state
operator|=
name|ps
expr_stmt|;
block|}
DECL|method|forUser (CurrentUser who)
name|ProjectControl
name|forUser
parameter_list|(
name|CurrentUser
name|who
parameter_list|)
block|{
name|ProjectControl
name|r
init|=
operator|new
name|ProjectControl
argument_list|(
name|uploadGroups
argument_list|,
name|receiveGroups
argument_list|,
name|permissionFilter
argument_list|,
name|changeControlFactory
argument_list|,
name|permissionBackend
argument_list|,
name|refFilterFactory
argument_list|,
name|identifiedUserFactory
argument_list|,
name|who
argument_list|,
name|state
argument_list|)
decl_stmt|;
comment|// Not per-user, and reusing saves lookup time.
name|r
operator|.
name|allSections
operator|=
name|allSections
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|asForProject ()
name|ForProject
name|asForProject
parameter_list|()
block|{
return|return
operator|new
name|ForProjectImpl
argument_list|()
return|;
block|}
DECL|method|controlFor (ReviewDb db, Change change)
name|ChangeControl
name|controlFor
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
name|change
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|changeControlFactory
operator|.
name|create
argument_list|(
name|controlForRef
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
argument_list|,
name|db
argument_list|,
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|controlFor (ChangeNotes notes)
name|ChangeControl
name|controlFor
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|changeControlFactory
operator|.
name|create
argument_list|(
name|controlForRef
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
argument_list|,
name|notes
argument_list|)
return|;
block|}
DECL|method|controlForRef (Branch.NameKey ref)
name|RefControl
name|controlForRef
parameter_list|(
name|Branch
operator|.
name|NameKey
name|ref
parameter_list|)
block|{
return|return
name|controlForRef
argument_list|(
name|ref
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|controlForRef (String refName)
specifier|public
name|RefControl
name|controlForRef
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
if|if
condition|(
name|refControls
operator|==
literal|null
condition|)
block|{
name|refControls
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|RefControl
name|ctl
init|=
name|refControls
operator|.
name|get
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctl
operator|==
literal|null
condition|)
block|{
name|PermissionCollection
name|relevant
init|=
name|permissionFilter
operator|.
name|filter
argument_list|(
name|access
argument_list|()
argument_list|,
name|refName
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|ctl
operator|=
operator|new
name|RefControl
argument_list|(
name|identifiedUserFactory
argument_list|,
name|this
argument_list|,
name|refName
argument_list|,
name|relevant
argument_list|)
expr_stmt|;
name|refControls
operator|.
name|put
argument_list|(
name|refName
argument_list|,
name|ctl
argument_list|)
expr_stmt|;
block|}
return|return
name|ctl
return|;
block|}
DECL|method|getUser ()
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getProjectState ()
name|ProjectState
name|getProjectState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|getProject ()
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|state
operator|.
name|getProject
argument_list|()
return|;
block|}
comment|/** Is this user a project owner? */
DECL|method|isOwner ()
name|boolean
name|isOwner
parameter_list|()
block|{
return|return
operator|(
name|isDeclaredOwner
argument_list|()
operator|&&
name|controlForRef
argument_list|(
literal|"refs/*"
argument_list|)
operator|.
name|canPerform
argument_list|(
name|Permission
operator|.
name|OWNER
argument_list|)
operator|)
operator|||
name|isAdmin
argument_list|()
return|;
block|}
comment|/**    * @return {@code Capable.OK} if the user can upload to at least one reference. Does not check    *     Contributor Agreements.    */
DECL|method|canPushToAtLeastOneRef ()
name|boolean
name|canPushToAtLeastOneRef
parameter_list|()
block|{
return|return
name|canPerformOnAnyRef
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
operator|||
name|canPerformOnAnyRef
argument_list|(
name|Permission
operator|.
name|CREATE_TAG
argument_list|)
operator|||
name|isOwner
argument_list|()
return|;
block|}
DECL|method|isAdmin ()
name|boolean
name|isAdmin
parameter_list|()
block|{
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
decl||
name|PermissionBackendException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|match (PermissionRule rule, boolean isChangeOwner)
name|boolean
name|match
parameter_list|(
name|PermissionRule
name|rule
parameter_list|,
name|boolean
name|isChangeOwner
parameter_list|)
block|{
return|return
name|match
argument_list|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
argument_list|,
name|isChangeOwner
argument_list|)
return|;
block|}
DECL|method|allRefsAreVisible (Set<String> ignore)
name|boolean
name|allRefsAreVisible
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|ignore
parameter_list|)
block|{
return|return
name|user
operator|.
name|isInternalUser
argument_list|()
operator|||
name|canPerformOnAllRefs
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|ignore
argument_list|)
return|;
block|}
comment|/** Can the user run upload pack? */
DECL|method|canRunUploadPack ()
specifier|private
name|boolean
name|canRunUploadPack
parameter_list|()
block|{
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|group
range|:
name|uploadGroups
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** Can the user run receive pack? */
DECL|method|canRunReceivePack ()
specifier|private
name|boolean
name|canRunReceivePack
parameter_list|()
block|{
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|group
range|:
name|receiveGroups
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|canAddRefs ()
specifier|private
name|boolean
name|canAddRefs
parameter_list|()
block|{
return|return
operator|(
name|canPerformOnAnyRef
argument_list|(
name|Permission
operator|.
name|CREATE
argument_list|)
operator|||
name|isAdmin
argument_list|()
operator|)
return|;
block|}
DECL|method|canCreateChanges ()
specifier|private
name|boolean
name|canCreateChanges
parameter_list|()
block|{
for|for
control|(
name|SectionMatcher
name|matcher
range|:
name|access
argument_list|()
control|)
block|{
name|AccessSection
name|section
init|=
name|matcher
operator|.
name|getSection
argument_list|()
decl_stmt|;
if|if
condition|(
name|section
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"refs/for/"
argument_list|)
condition|)
block|{
name|Permission
name|permission
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
decl_stmt|;
if|if
condition|(
name|permission
operator|!=
literal|null
operator|&&
name|controlForRef
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|canPerform
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|isDeclaredOwner ()
specifier|private
name|boolean
name|isDeclaredOwner
parameter_list|()
block|{
if|if
condition|(
name|declaredOwner
operator|==
literal|null
condition|)
block|{
name|GroupMembership
name|effectiveGroups
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
name|declaredOwner
operator|=
name|effectiveGroups
operator|.
name|containsAnyOf
argument_list|(
name|state
operator|.
name|getAllOwners
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|declaredOwner
return|;
block|}
DECL|method|canPerformOnAnyRef (String permissionName)
specifier|private
name|boolean
name|canPerformOnAnyRef
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
for|for
control|(
name|SectionMatcher
name|matcher
range|:
name|access
argument_list|()
control|)
block|{
name|AccessSection
name|section
init|=
name|matcher
operator|.
name|getSection
argument_list|()
decl_stmt|;
name|Permission
name|permission
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|)
decl_stmt|;
if|if
condition|(
name|permission
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|PermissionRule
name|rule
range|:
name|permission
operator|.
name|getRules
argument_list|()
control|)
block|{
if|if
condition|(
name|rule
operator|.
name|isBlock
argument_list|()
operator|||
name|rule
operator|.
name|isDeny
argument_list|()
operator|||
operator|!
name|match
argument_list|(
name|rule
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// Being in a group that was granted this permission is only an
comment|// approximation.  There might be overrides and doNotInherit
comment|// that would render this to be false.
comment|//
if|if
condition|(
name|controlForRef
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|canPerform
argument_list|(
name|permissionName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
break|break;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|canPerformOnAllRefs (String permission, Set<String> ignore)
specifier|private
name|boolean
name|canPerformOnAllRefs
parameter_list|(
name|String
name|permission
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|ignore
parameter_list|)
block|{
name|boolean
name|canPerform
init|=
literal|false
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|patterns
init|=
name|allRefPatterns
argument_list|(
name|permission
argument_list|)
decl_stmt|;
if|if
condition|(
name|patterns
operator|.
name|contains
argument_list|(
name|AccessSection
operator|.
name|ALL
argument_list|)
condition|)
block|{
comment|// Only possible if granted on the pattern that
comment|// matches every possible reference.  Check all
comment|// patterns also have the permission.
comment|//
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
if|if
condition|(
name|controlForRef
argument_list|(
name|pattern
argument_list|)
operator|.
name|canPerform
argument_list|(
name|permission
argument_list|)
condition|)
block|{
name|canPerform
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ignore
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
continue|continue;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
name|canPerform
return|;
block|}
DECL|method|allRefPatterns (String permissionName)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|allRefPatterns
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|all
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SectionMatcher
name|matcher
range|:
name|access
argument_list|()
control|)
block|{
name|AccessSection
name|section
init|=
name|matcher
operator|.
name|getSection
argument_list|()
decl_stmt|;
name|Permission
name|permission
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|)
decl_stmt|;
if|if
condition|(
name|permission
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|add
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|all
return|;
block|}
DECL|method|access ()
specifier|private
name|List
argument_list|<
name|SectionMatcher
argument_list|>
name|access
parameter_list|()
block|{
if|if
condition|(
name|allSections
operator|==
literal|null
condition|)
block|{
name|allSections
operator|=
name|state
operator|.
name|getAllSections
argument_list|()
expr_stmt|;
block|}
return|return
name|allSections
return|;
block|}
DECL|method|match (PermissionRule rule)
specifier|private
name|boolean
name|match
parameter_list|(
name|PermissionRule
name|rule
parameter_list|)
block|{
return|return
name|match
argument_list|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
argument_list|)
return|;
block|}
DECL|method|match (AccountGroup.UUID uuid)
specifier|private
name|boolean
name|match
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|match
argument_list|(
name|uuid
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|match (AccountGroup.UUID uuid, boolean isChangeOwner)
specifier|private
name|boolean
name|match
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
name|boolean
name|isChangeOwner
parameter_list|)
block|{
if|if
condition|(
name|SystemGroupBackend
operator|.
name|PROJECT_OWNERS
operator|.
name|equals
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
return|return
name|isDeclaredOwner
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|SystemGroupBackend
operator|.
name|CHANGE_OWNER
operator|.
name|equals
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
return|return
name|isChangeOwner
return|;
block|}
else|else
block|{
return|return
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|uuid
argument_list|)
return|;
block|}
block|}
DECL|class|ForProjectImpl
specifier|private
class|class
name|ForProjectImpl
extends|extends
name|ForProject
block|{
DECL|field|refFilter
specifier|private
name|DefaultRefFilter
name|refFilter
decl_stmt|;
DECL|field|resourcePath
specifier|private
name|String
name|resourcePath
decl_stmt|;
annotation|@
name|Override
DECL|method|resourcePath ()
specifier|public
name|String
name|resourcePath
parameter_list|()
block|{
if|if
condition|(
name|resourcePath
operator|==
literal|null
condition|)
block|{
name|resourcePath
operator|=
literal|"/projects/"
operator|+
name|getProjectState
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|resourcePath
return|;
block|}
annotation|@
name|Override
DECL|method|ref (String ref)
specifier|public
name|ForRef
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|controlForRef
argument_list|(
name|ref
argument_list|)
operator|.
name|asForRef
argument_list|()
operator|.
name|database
argument_list|(
name|db
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|change (ChangeData cd)
specifier|public
name|ForChange
name|change
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
try|try
block|{
name|checkProject
argument_list|(
name|cd
operator|.
name|change
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|change
argument_list|(
name|cd
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
return|return
name|FailedPermissionBackend
operator|.
name|change
argument_list|(
literal|"unavailable"
argument_list|,
name|e
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|change (ChangeNotes notes)
specifier|public
name|ForChange
name|change
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
name|checkProject
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|change
argument_list|(
name|notes
argument_list|)
return|;
block|}
DECL|method|checkProject (Change change)
specifier|private
name|void
name|checkProject
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|project
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|,
literal|"expected change in project %s, not %s"
argument_list|,
name|project
argument_list|,
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|check (ProjectPermission perm)
specifier|public
name|void
name|check
parameter_list|(
name|ProjectPermission
name|perm
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
operator|!
name|can
argument_list|(
name|perm
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|perm
operator|.
name|describeForException
argument_list|()
operator|+
literal|" not permitted"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|test (Collection<ProjectPermission> permSet)
specifier|public
name|Set
argument_list|<
name|ProjectPermission
argument_list|>
name|test
parameter_list|(
name|Collection
argument_list|<
name|ProjectPermission
argument_list|>
name|permSet
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|EnumSet
argument_list|<
name|ProjectPermission
argument_list|>
name|ok
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ProjectPermission
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|ProjectPermission
name|perm
range|:
name|permSet
control|)
block|{
if|if
condition|(
name|can
argument_list|(
name|perm
argument_list|)
condition|)
block|{
name|ok
operator|.
name|add
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ok
return|;
block|}
annotation|@
name|Override
DECL|method|testCond (ProjectPermission perm)
specifier|public
name|BooleanCondition
name|testCond
parameter_list|(
name|ProjectPermission
name|perm
parameter_list|)
block|{
return|return
operator|new
name|PermissionBackendCondition
operator|.
name|ForProject
argument_list|(
name|this
argument_list|,
name|perm
argument_list|,
name|getUser
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|filter (Map<String, Ref> refs, Repository repo, RefFilterOptions opts)
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
name|Repository
name|repo
parameter_list|,
name|RefFilterOptions
name|opts
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
if|if
condition|(
name|refFilter
operator|==
literal|null
condition|)
block|{
name|refFilter
operator|=
name|refFilterFactory
operator|.
name|create
argument_list|(
name|ProjectControl
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|refFilter
operator|.
name|filter
argument_list|(
name|refs
argument_list|,
name|repo
argument_list|,
name|opts
argument_list|)
return|;
block|}
DECL|method|can (ProjectPermission perm)
specifier|private
name|boolean
name|can
parameter_list|(
name|ProjectPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
switch|switch
condition|(
name|perm
condition|)
block|{
case|case
name|ACCESS
case|:
return|return
name|user
operator|.
name|isInternalUser
argument_list|()
operator|||
name|isOwner
argument_list|()
operator|||
name|canPerformOnAnyRef
argument_list|(
name|Permission
operator|.
name|READ
argument_list|)
return|;
case|case
name|READ
case|:
return|return
name|allRefsAreVisible
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
return|;
case|case
name|CREATE_REF
case|:
return|return
name|canAddRefs
argument_list|()
return|;
case|case
name|CREATE_CHANGE
case|:
return|return
name|canCreateChanges
argument_list|()
return|;
case|case
name|RUN_RECEIVE_PACK
case|:
return|return
name|canRunReceivePack
argument_list|()
return|;
case|case
name|RUN_UPLOAD_PACK
case|:
return|return
name|canRunUploadPack
argument_list|()
return|;
case|case
name|PUSH_AT_LEAST_ONE_REF
case|:
return|return
name|canPushToAtLeastOneRef
argument_list|()
return|;
case|case
name|READ_CONFIG
case|:
return|return
name|controlForRef
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|isVisible
argument_list|()
return|;
case|case
name|BAN_COMMIT
case|:
case|case
name|READ_REFLOG
case|:
case|case
name|WRITE_CONFIG
case|:
return|return
name|isOwner
argument_list|()
return|;
block|}
throw|throw
operator|new
name|PermissionBackendException
argument_list|(
name|perm
operator|+
literal|" unsupported"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

