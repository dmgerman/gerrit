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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|common
operator|.
name|CollectionsUtil
operator|.
name|*
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
name|ApprovalCategory
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
name|RefRight
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
name|ReplicationUser
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

begin_comment
comment|/** Access control management for a user accessing a project's data. */
end_comment

begin_class
DECL|class|ProjectControl
specifier|public
class|class
name|ProjectControl
block|{
DECL|field|VISIBLE
specifier|public
specifier|static
specifier|final
name|int
name|VISIBLE
init|=
literal|1
operator|<<
literal|0
decl_stmt|;
DECL|field|OWNER
specifier|public
specifier|static
specifier|final
name|int
name|OWNER
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
DECL|class|GenericFactory
specifier|public
specifier|static
class|class
name|GenericFactory
block|{
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|GenericFactory (final ProjectCache pc)
name|GenericFactory
parameter_list|(
specifier|final
name|ProjectCache
name|pc
parameter_list|)
block|{
name|projectCache
operator|=
name|pc
expr_stmt|;
block|}
DECL|method|controlFor (Project.NameKey nameKey, CurrentUser user)
specifier|public
name|ProjectControl
name|controlFor
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
specifier|final
name|ProjectState
name|p
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|nameKey
argument_list|)
throw|;
block|}
return|return
name|p
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
return|;
block|}
block|}
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
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
annotation|@
name|Inject
DECL|method|Factory (final ProjectCache pc, final Provider<CurrentUser> cu)
name|Factory
parameter_list|(
specifier|final
name|ProjectCache
name|pc
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|cu
parameter_list|)
block|{
name|projectCache
operator|=
name|pc
expr_stmt|;
name|user
operator|=
name|cu
expr_stmt|;
block|}
DECL|method|controlFor (final Project.NameKey nameKey)
specifier|public
name|ProjectControl
name|controlFor
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
specifier|final
name|ProjectState
name|p
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|nameKey
argument_list|)
throw|;
block|}
return|return
name|p
operator|.
name|controlFor
argument_list|(
name|user
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|validateFor (final Project.NameKey nameKey)
specifier|public
name|ProjectControl
name|validateFor
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
return|return
name|validateFor
argument_list|(
name|nameKey
argument_list|,
name|VISIBLE
argument_list|)
return|;
block|}
DECL|method|ownerFor (final Project.NameKey nameKey)
specifier|public
name|ProjectControl
name|ownerFor
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
return|return
name|validateFor
argument_list|(
name|nameKey
argument_list|,
name|OWNER
argument_list|)
return|;
block|}
DECL|method|validateFor (final Project.NameKey nameKey, final int need)
specifier|public
name|ProjectControl
name|validateFor
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|,
specifier|final
name|int
name|need
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
specifier|final
name|ProjectControl
name|c
init|=
name|controlFor
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|need
operator|&
name|VISIBLE
operator|)
operator|==
name|VISIBLE
operator|&&
name|c
operator|.
name|isVisible
argument_list|()
condition|)
block|{
return|return
name|c
return|;
block|}
if|if
condition|(
operator|(
name|need
operator|&
name|OWNER
operator|)
operator|==
name|OWNER
operator|&&
name|c
operator|.
name|isOwner
argument_list|()
condition|)
block|{
return|return
name|c
return|;
block|}
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|nameKey
argument_list|)
throw|;
block|}
block|}
DECL|interface|AssistedFactory
interface|interface
name|AssistedFactory
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
DECL|field|refControlFactory
specifier|private
specifier|final
name|RefControl
operator|.
name|Factory
name|refControlFactory
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
annotation|@
name|Inject
DECL|method|ProjectControl (@itUploadPackGroups Set<AccountGroup.UUID> uploadGroups, @GitReceivePackGroups Set<AccountGroup.UUID> receiveGroups, final RefControl.Factory refControlFactory, @Assisted CurrentUser who, @Assisted ProjectState ps)
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
specifier|final
name|RefControl
operator|.
name|Factory
name|refControlFactory
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
name|refControlFactory
operator|=
name|refControlFactory
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
DECL|method|forAnonymousUser ()
specifier|public
name|ProjectControl
name|forAnonymousUser
parameter_list|()
block|{
return|return
name|state
operator|.
name|controlForAnonymousUser
argument_list|()
return|;
block|}
DECL|method|forUser (final CurrentUser who)
specifier|public
name|ProjectControl
name|forUser
parameter_list|(
specifier|final
name|CurrentUser
name|who
parameter_list|)
block|{
return|return
name|state
operator|.
name|controlFor
argument_list|(
name|who
argument_list|)
return|;
block|}
DECL|method|controlFor (final Change change)
specifier|public
name|ChangeControl
name|controlFor
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
block|{
return|return
operator|new
name|ChangeControl
argument_list|(
name|controlForRef
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
argument_list|,
name|change
argument_list|)
return|;
block|}
DECL|method|controlForRef (Branch.NameKey ref)
specifier|public
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
return|return
name|refControlFactory
operator|.
name|create
argument_list|(
name|this
argument_list|,
name|refName
argument_list|)
return|;
block|}
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getProjectState ()
specifier|public
name|ProjectState
name|getProjectState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|getProjectState
argument_list|()
operator|.
name|getProject
argument_list|()
return|;
block|}
comment|/** Can this user see this project exists? */
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|visibleForReplication
argument_list|()
operator|||
name|canPerformOnAnyRef
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
return|;
block|}
DECL|method|canAddRefs ()
specifier|public
name|boolean
name|canAddRefs
parameter_list|()
block|{
return|return
operator|(
name|canPerformOnAnyRef
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_HEAD
argument_list|,
name|ApprovalCategory
operator|.
name|PUSH_HEAD_CREATE
argument_list|)
operator|||
name|isOwnerAnyRef
argument_list|()
operator|)
return|;
block|}
comment|/** Can this user see all the refs in this projects? */
DECL|method|allRefsAreVisible ()
specifier|public
name|boolean
name|allRefsAreVisible
parameter_list|()
block|{
return|return
name|visibleForReplication
argument_list|()
operator|||
name|canPerformOnAllRefs
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
return|;
block|}
comment|/** Is this project completely visible for replication? */
DECL|method|visibleForReplication ()
name|boolean
name|visibleForReplication
parameter_list|()
block|{
return|return
name|getCurrentUser
argument_list|()
operator|instanceof
name|ReplicationUser
operator|&&
operator|(
operator|(
name|ReplicationUser
operator|)
name|getCurrentUser
argument_list|()
operator|)
operator|.
name|isEverythingVisible
argument_list|()
return|;
block|}
comment|/** Is this user a project owner? Ownership does not imply {@link #isVisible()} */
DECL|method|isOwner ()
specifier|public
name|boolean
name|isOwner
parameter_list|()
block|{
return|return
name|controlForRef
argument_list|(
name|RefRight
operator|.
name|ALL
argument_list|)
operator|.
name|isOwner
argument_list|()
operator|||
name|getCurrentUser
argument_list|()
operator|.
name|isAdministrator
argument_list|()
return|;
block|}
comment|/** Does this user have ownership on at least one reference name? */
DECL|method|isOwnerAnyRef ()
specifier|public
name|boolean
name|isOwnerAnyRef
parameter_list|()
block|{
return|return
name|canPerformOnAnyRef
argument_list|(
name|ApprovalCategory
operator|.
name|OWN
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
operator|||
name|getCurrentUser
argument_list|()
operator|.
name|isAdministrator
argument_list|()
return|;
block|}
comment|/** @return true if the user can upload to at least one reference */
DECL|method|canPushToAtLeastOneRef ()
specifier|public
name|boolean
name|canPushToAtLeastOneRef
parameter_list|()
block|{
return|return
name|canPerformOnAnyRef
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|,
operator|(
name|short
operator|)
literal|2
argument_list|)
operator|||
name|canPerformOnAnyRef
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_HEAD
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
operator|||
name|canPerformOnAnyRef
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_TAG
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
return|;
block|}
comment|// TODO (anatol.pomazau): Try to merge this method with similar RefRightsForPattern#canPerform
DECL|method|canPerformOnAnyRef (ApprovalCategory.Id actionId, short requireValue)
specifier|private
name|boolean
name|canPerformOnAnyRef
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|,
name|short
name|requireValue
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groups
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|RefRight
name|pr
range|:
name|state
operator|.
name|getAllRights
argument_list|(
name|actionId
argument_list|,
literal|true
argument_list|)
control|)
block|{
if|if
condition|(
name|groups
operator|.
name|contains
argument_list|(
name|pr
operator|.
name|getAccountGroupUUID
argument_list|()
argument_list|)
operator|&&
name|pr
operator|.
name|getMaxValue
argument_list|()
operator|>=
name|requireValue
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
DECL|method|canPerformOnAllRefs (ApprovalCategory.Id actionId, short requireValue)
specifier|private
name|boolean
name|canPerformOnAllRefs
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|,
name|short
name|requireValue
parameter_list|)
block|{
name|boolean
name|canPerform
init|=
literal|false
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|patterns
init|=
name|allRefPatterns
argument_list|(
name|actionId
argument_list|)
decl_stmt|;
if|if
condition|(
name|patterns
operator|.
name|contains
argument_list|(
name|RefRight
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
specifier|final
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
name|actionId
argument_list|,
name|requireValue
argument_list|)
condition|)
block|{
name|canPerform
operator|=
literal|true
expr_stmt|;
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
DECL|method|allRefPatterns (ApprovalCategory.Id actionId)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|allRefPatterns
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|all
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|RefRight
name|pr
range|:
name|state
operator|.
name|getAllRights
argument_list|(
name|actionId
argument_list|,
literal|true
argument_list|)
control|)
block|{
name|all
operator|.
name|add
argument_list|(
name|pr
operator|.
name|getRefPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|all
return|;
block|}
DECL|method|canRunUploadPack ()
specifier|public
name|boolean
name|canRunUploadPack
parameter_list|()
block|{
return|return
name|isAnyIncludedIn
argument_list|(
name|uploadGroups
argument_list|,
name|user
operator|.
name|getEffectiveGroups
argument_list|()
argument_list|)
return|;
block|}
DECL|method|canRunReceivePack ()
specifier|public
name|boolean
name|canRunReceivePack
parameter_list|()
block|{
return|return
name|isAnyIncludedIn
argument_list|(
name|receiveGroups
argument_list|,
name|user
operator|.
name|getEffectiveGroups
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

