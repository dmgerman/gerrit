begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
comment|// limitations under the License
end_comment

begin_package
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|data
operator|.
name|GroupDetail
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
name|GroupList
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
name|errors
operator|.
name|NoSuchGroupException
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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

begin_class
DECL|class|VisibleGroups
specifier|public
class|class
name|VisibleGroups
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|VisibleGroups
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|identifiedUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|groupDetailFactory
specifier|private
specifier|final
name|GroupDetailFactory
operator|.
name|Factory
name|groupDetailFactory
decl_stmt|;
DECL|field|onlyVisibleToAll
specifier|private
name|boolean
name|onlyVisibleToAll
decl_stmt|;
DECL|field|groupType
specifier|private
name|AccountGroup
operator|.
name|Type
name|groupType
decl_stmt|;
annotation|@
name|Inject
DECL|method|VisibleGroups (final Provider<IdentifiedUser> currentUser, final GroupCache groupCache, final GroupControl.Factory groupControlFactory, final GroupDetailFactory.Factory groupDetailFactory)
name|VisibleGroups
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
specifier|final
name|GroupDetailFactory
operator|.
name|Factory
name|groupDetailFactory
parameter_list|)
block|{
name|this
operator|.
name|identifiedUser
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|groupDetailFactory
operator|=
name|groupDetailFactory
expr_stmt|;
block|}
DECL|method|setOnlyVisibleToAll (final boolean onlyVisibleToAll)
specifier|public
name|void
name|setOnlyVisibleToAll
parameter_list|(
specifier|final
name|boolean
name|onlyVisibleToAll
parameter_list|)
block|{
name|this
operator|.
name|onlyVisibleToAll
operator|=
name|onlyVisibleToAll
expr_stmt|;
block|}
DECL|method|setGroupType (final AccountGroup.Type groupType)
specifier|public
name|void
name|setGroupType
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Type
name|groupType
parameter_list|)
block|{
name|this
operator|.
name|groupType
operator|=
name|groupType
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|GroupList
name|get
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
specifier|final
name|Iterable
argument_list|<
name|AccountGroup
argument_list|>
name|groups
init|=
name|groupCache
operator|.
name|all
argument_list|()
decl_stmt|;
return|return
name|createGroupList
argument_list|(
name|filterGroups
argument_list|(
name|groups
argument_list|)
argument_list|)
return|;
block|}
DECL|method|get (final Collection<ProjectControl> projects)
specifier|public
name|GroupList
name|get
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|ProjectControl
argument_list|>
name|projects
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
argument_list|>
name|groups
init|=
operator|new
name|TreeSet
argument_list|<
name|AccountGroup
argument_list|>
argument_list|(
operator|new
name|GroupComparator
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectControl
name|projectControl
range|:
name|projects
control|)
block|{
specifier|final
name|Set
argument_list|<
name|GroupReference
argument_list|>
name|groupsRefs
init|=
name|projectControl
operator|.
name|getAllGroups
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|GroupReference
name|groupRef
range|:
name|groupsRefs
control|)
block|{
specifier|final
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupRef
operator|.
name|getUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupRef
operator|.
name|getUUID
argument_list|()
argument_list|)
throw|;
block|}
name|groups
operator|.
name|add
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|createGroupList
argument_list|(
name|filterGroups
argument_list|(
name|groups
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns visible list of known groups for the user. Depending on the group    * membership realms supported, this may only return a subset of the effective    * groups.    * @See GroupMembership#getKnownGroups()    */
DECL|method|get (final IdentifiedUser user)
specifier|public
name|GroupList
name|get
parameter_list|(
specifier|final
name|IdentifiedUser
name|user
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
if|if
condition|(
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|||
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|effective
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|getKnownGroups
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|AccountGroup
argument_list|>
name|groups
init|=
operator|new
name|TreeSet
argument_list|<
name|AccountGroup
argument_list|>
argument_list|(
operator|new
name|GroupComparator
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|groupId
range|:
name|effective
control|)
block|{
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|groups
operator|.
name|add
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|createGroupList
argument_list|(
name|filterGroups
argument_list|(
name|groups
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
literal|"Groups of user '"
operator|+
name|user
operator|.
name|getAccountId
argument_list|()
operator|+
literal|"' are not visible."
argument_list|)
throw|;
block|}
block|}
DECL|method|filterGroups (final Iterable<AccountGroup> groups)
specifier|private
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|filterGroups
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|AccountGroup
argument_list|>
name|groups
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|filteredGroups
init|=
operator|new
name|LinkedList
argument_list|<
name|AccountGroup
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|isAdmin
init|=
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
name|group
range|:
name|groups
control|)
block|{
if|if
condition|(
operator|!
name|isAdmin
condition|)
block|{
specifier|final
name|GroupControl
name|c
init|=
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|group
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|c
operator|.
name|isVisible
argument_list|()
condition|)
block|{
continue|continue;
block|}
block|}
if|if
condition|(
operator|(
name|onlyVisibleToAll
operator|&&
operator|!
name|group
operator|.
name|isVisibleToAll
argument_list|()
operator|)
operator|||
operator|(
name|groupType
operator|!=
literal|null
operator|&&
operator|!
name|groupType
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getType
argument_list|()
argument_list|)
operator|)
condition|)
block|{
continue|continue;
block|}
name|filteredGroups
operator|.
name|add
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
return|return
name|filteredGroups
return|;
block|}
DECL|method|createGroupList (final List<AccountGroup> groups)
specifier|private
name|GroupList
name|createGroupList
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|groups
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
specifier|final
name|List
argument_list|<
name|GroupDetail
argument_list|>
name|groupDetailList
init|=
operator|new
name|ArrayList
argument_list|<
name|GroupDetail
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
name|group
range|:
name|groups
control|)
block|{
name|groupDetailList
operator|.
name|add
argument_list|(
name|groupDetailFactory
operator|.
name|create
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|GroupList
argument_list|(
name|groupDetailList
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canCreateGroup
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

