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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
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
name|Capable
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
name|GroupReference
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
operator|.
name|Action
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
name|AbstractAgreement
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
name|AccountAgreement
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
name|AccountGroupAgreement
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
name|ContributorAgreement
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
name|account
operator|.
name|GroupCache
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
name|CanonicalWebUrl
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
name|SchemaFactory
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
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
name|ProjectControl
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|field|userCache
specifier|private
specifier|final
name|Provider
argument_list|<
name|PerRequestProjectControlCache
argument_list|>
name|userCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|Factory (Provider<PerRequestProjectControlCache> uc)
name|Factory
parameter_list|(
name|Provider
argument_list|<
name|PerRequestProjectControlCache
argument_list|>
name|uc
parameter_list|)
block|{
name|userCache
operator|=
name|uc
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
return|return
name|userCache
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|(
name|nameKey
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
DECL|field|canonicalWebUrl
specifier|private
specifier|final
name|String
name|canonicalWebUrl
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
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
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|permissionFilter
specifier|private
specifier|final
name|PermissionCollection
operator|.
name|Factory
name|permissionFilter
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
DECL|method|ProjectControl (@itUploadPackGroups Set<AccountGroup.UUID> uploadGroups, @GitReceivePackGroups Set<AccountGroup.UUID> receiveGroups, final SchemaFactory<ReviewDb> schema, final GroupCache groupCache, final PermissionCollection.Factory permissionFilter, @CanonicalWebUrl @Nullable final String canonicalWebUrl, @Assisted CurrentUser who, @Assisted ProjectState ps)
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
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|PermissionCollection
operator|.
name|Factory
name|permissionFilter
parameter_list|,
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
specifier|final
name|String
name|canonicalWebUrl
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
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|permissionFilter
operator|=
name|permissionFilter
expr_stmt|;
name|this
operator|.
name|canonicalWebUrl
operator|=
name|canonicalWebUrl
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
specifier|public
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
name|state
operator|.
name|controlFor
argument_list|(
name|who
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
argument_list|<
name|String
argument_list|,
name|RefControl
argument_list|>
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
operator|.
name|getUserName
argument_list|()
argument_list|)
decl_stmt|;
name|ctl
operator|=
operator|new
name|RefControl
argument_list|(
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
name|state
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
name|Permission
operator|.
name|READ
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
name|Permission
operator|.
name|CREATE
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
name|Permission
operator|.
name|READ
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
name|user
operator|instanceof
name|ReplicationUser
operator|&&
operator|(
operator|(
name|ReplicationUser
operator|)
name|user
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
name|isDeclaredOwner
argument_list|()
operator|||
name|user
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
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
name|declaredOwner
operator|=
name|state
operator|.
name|isOwner
argument_list|(
name|user
operator|.
name|getEffectiveGroups
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|declaredOwner
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
name|Permission
operator|.
name|OWNER
argument_list|)
operator|||
name|user
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can upload to at least one reference */
DECL|method|canPushToAtLeastOneRef ()
specifier|public
name|Capable
name|canPushToAtLeastOneRef
parameter_list|()
block|{
if|if
condition|(
operator|!
name|canPerformOnAnyRef
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
operator|&&
operator|!
name|canPerformOnAnyRef
argument_list|(
name|Permission
operator|.
name|PUSH_TAG
argument_list|)
condition|)
block|{
name|String
name|pName
init|=
name|state
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
operator|new
name|Capable
argument_list|(
literal|"Upload denied for project '"
operator|+
name|pName
operator|+
literal|"'"
argument_list|)
return|;
block|}
name|Project
name|project
init|=
name|state
operator|.
name|getProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|project
operator|.
name|isUseContributorAgreements
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|verifyActiveContributorAgreement
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot query database for agreements"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
operator|new
name|Capable
argument_list|(
literal|"Cannot verify contribution agreement"
argument_list|)
return|;
block|}
block|}
return|return
name|Capable
operator|.
name|OK
return|;
block|}
DECL|method|getAllGroups ()
specifier|public
name|Set
argument_list|<
name|GroupReference
argument_list|>
name|getAllGroups
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|GroupReference
argument_list|>
name|all
init|=
operator|new
name|HashSet
argument_list|<
name|GroupReference
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|SectionMatcher
name|matcher
range|:
name|access
argument_list|()
control|)
block|{
specifier|final
name|AccessSection
name|section
init|=
name|matcher
operator|.
name|section
decl_stmt|;
for|for
control|(
specifier|final
name|Permission
name|permission
range|:
name|section
operator|.
name|getPermissions
argument_list|()
control|)
block|{
for|for
control|(
specifier|final
name|PermissionRule
name|rule
range|:
name|permission
operator|.
name|getRules
argument_list|()
control|)
block|{
name|all
operator|.
name|add
argument_list|(
name|rule
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|all
return|;
block|}
DECL|method|verifyActiveContributorAgreement ()
specifier|private
name|Capable
name|verifyActiveContributorAgreement
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
operator|(
name|user
operator|instanceof
name|IdentifiedUser
operator|)
condition|)
block|{
return|return
operator|new
name|Capable
argument_list|(
literal|"Must be logged in to verify Contributor Agreement"
argument_list|)
return|;
block|}
specifier|final
name|IdentifiedUser
name|iUser
init|=
operator|(
name|IdentifiedUser
operator|)
name|user
decl_stmt|;
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
name|AbstractAgreement
name|bestAgreement
init|=
literal|null
decl_stmt|;
name|ContributorAgreement
name|bestCla
init|=
literal|null
decl_stmt|;
try|try
block|{
name|OUTER
label|:
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|groupUUID
range|:
name|iUser
operator|.
name|getEffectiveGroups
argument_list|()
control|)
block|{
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupUUID
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
specifier|final
name|List
argument_list|<
name|AccountGroupAgreement
argument_list|>
name|temp
init|=
name|db
operator|.
name|accountGroupAgreements
argument_list|()
operator|.
name|byGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|temp
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|AccountGroupAgreement
name|a
range|:
name|temp
control|)
block|{
specifier|final
name|ContributorAgreement
name|cla
init|=
name|db
operator|.
name|contributorAgreements
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|getAgreementId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cla
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|bestAgreement
operator|=
name|a
expr_stmt|;
name|bestCla
operator|=
name|cla
expr_stmt|;
break|break
name|OUTER
break|;
block|}
block|}
if|if
condition|(
name|bestAgreement
operator|==
literal|null
condition|)
block|{
specifier|final
name|List
argument_list|<
name|AccountAgreement
argument_list|>
name|temp
init|=
name|db
operator|.
name|accountAgreements
argument_list|()
operator|.
name|byAccount
argument_list|(
name|iUser
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|temp
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|AccountAgreement
name|a
range|:
name|temp
control|)
block|{
specifier|final
name|ContributorAgreement
name|cla
init|=
name|db
operator|.
name|contributorAgreements
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|getAgreementId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cla
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|bestAgreement
operator|=
name|a
expr_stmt|;
name|bestCla
operator|=
name|cla
expr_stmt|;
break|break;
block|}
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|bestCla
operator|!=
literal|null
operator|&&
operator|!
name|bestCla
operator|.
name|isActive
argument_list|()
condition|)
block|{
specifier|final
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|bestCla
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" contributor agreement is expired.\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|canonicalWebUrl
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"\nPlease complete a new agreement"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|":\n\n  "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|canonicalWebUrl
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|PageLinks
operator|.
name|SETTINGS_AGREEMENTS
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Capable
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|bestCla
operator|!=
literal|null
operator|&&
name|bestCla
operator|.
name|isRequireContactInformation
argument_list|()
condition|)
block|{
name|boolean
name|fail
init|=
literal|false
decl_stmt|;
name|fail
operator||=
name|missing
argument_list|(
name|iUser
operator|.
name|getAccount
argument_list|()
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
name|fail
operator||=
name|missing
argument_list|(
name|iUser
operator|.
name|getAccount
argument_list|()
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
name|fail
operator||=
operator|!
name|iUser
operator|.
name|getAccount
argument_list|()
operator|.
name|isContactFiled
argument_list|()
expr_stmt|;
if|if
condition|(
name|fail
condition|)
block|{
specifier|final
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|bestCla
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" contributor agreement requires"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" current contact information.\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|canonicalWebUrl
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"\nPlease review your contact information"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|":\n\n  "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|canonicalWebUrl
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|PageLinks
operator|.
name|SETTINGS_CONTACT
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Capable
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|bestAgreement
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|bestAgreement
operator|.
name|getStatus
argument_list|()
condition|)
block|{
case|case
name|VERIFIED
case|:
return|return
name|Capable
operator|.
name|OK
return|;
case|case
name|REJECTED
case|:
return|return
operator|new
name|Capable
argument_list|(
name|bestCla
operator|.
name|getShortName
argument_list|()
operator|+
literal|" contributor agreement was rejected."
operator|+
literal|"\n       (rejected on "
operator|+
name|bestAgreement
operator|.
name|getReviewedOn
argument_list|()
operator|+
literal|")\n"
argument_list|)
return|;
case|case
name|NEW
case|:
return|return
operator|new
name|Capable
argument_list|(
name|bestCla
operator|.
name|getShortName
argument_list|()
operator|+
literal|" contributor agreement is still pending review.\n"
argument_list|)
return|;
block|}
block|}
specifier|final
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" A Contributor Agreement must be completed before uploading"
argument_list|)
expr_stmt|;
if|if
condition|(
name|canonicalWebUrl
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|":\n\n  "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|canonicalWebUrl
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|PageLinks
operator|.
name|SETTINGS_AGREEMENTS
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Capable
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|missing (final String value)
specifier|private
specifier|static
name|boolean
name|missing
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
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
name|section
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
else|else
block|{
break|break;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|canPerformOnAllRefs (String permission)
specifier|private
name|boolean
name|canPerformOnAllRefs
parameter_list|(
name|String
name|permission
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
name|permission
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
argument_list|<
name|String
argument_list|>
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
name|section
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
name|boolean
name|match
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
if|if
condition|(
name|AccountGroup
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
DECL|method|canRunUploadPack ()
specifier|public
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
DECL|method|canRunReceivePack ()
specifier|public
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
block|}
end_class

end_unit

