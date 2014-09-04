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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
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
name|base
operator|.
name|Strings
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
name|Lists
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
name|common
operator|.
name|data
operator|.
name|GroupDescriptions
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
name|common
operator|.
name|groups
operator|.
name|ListGroupsOption
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
name|RestReadView
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
name|TopLevelResource
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
name|Url
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
name|OutputFormat
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
name|AccountResource
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
name|GetGroups
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
name|account
operator|.
name|GroupComparator
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
name|GroupControl
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
name|GroupJson
operator|.
name|GroupInfo
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
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|EnumSet
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
name|Locale
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
comment|/** List groups visible to the calling user. */
end_comment

begin_class
DECL|class|ListGroups
specifier|public
class|class
name|ListGroups
implements|implements
name|RestReadView
argument_list|<
name|TopLevelResource
argument_list|>
block|{
DECL|field|groupCache
specifier|protected
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
DECL|field|genericGroupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|GenericFactory
name|genericGroupControlFactory
decl_stmt|;
DECL|field|identifiedUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|accountGetGroups
specifier|private
specifier|final
name|Provider
argument_list|<
name|GetGroups
argument_list|>
name|accountGetGroups
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|GroupJson
name|json
decl_stmt|;
DECL|field|options
specifier|private
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|options
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|usage
operator|=
literal|"projects for which the groups should be listed"
argument_list|)
DECL|field|projects
specifier|private
specifier|final
name|List
argument_list|<
name|ProjectControl
argument_list|>
name|projects
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectControl
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--visible-to-all"
argument_list|,
name|usage
operator|=
literal|"to list only groups that are visible to all registered users"
argument_list|)
DECL|field|visibleToAll
specifier|private
name|boolean
name|visibleToAll
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--user"
argument_list|,
name|aliases
operator|=
block|{
literal|"-u"
block|}
argument_list|,
name|usage
operator|=
literal|"user for which the groups should be listed"
argument_list|)
DECL|field|user
specifier|private
name|Account
operator|.
name|Id
name|user
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--owned"
argument_list|,
name|usage
operator|=
literal|"to list only groups that are owned by the specified user"
operator|+
literal|" or by the calling user if no user was specifed"
argument_list|)
DECL|field|owned
specifier|private
name|boolean
name|owned
decl_stmt|;
DECL|field|groupsToInspect
specifier|private
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupsToInspect
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-q"
argument_list|,
name|usage
operator|=
literal|"group to inspect"
argument_list|)
DECL|method|addGroup (final AccountGroup.UUID id)
name|void
name|addGroup
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|)
block|{
name|groupsToInspect
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--limit"
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"maximum number of groups to list"
argument_list|)
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--start"
argument_list|,
name|aliases
operator|=
block|{
literal|"-S"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"number of groups to skip"
argument_list|)
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--match"
argument_list|,
name|aliases
operator|=
block|{
literal|"-m"
block|}
argument_list|,
name|metaVar
operator|=
literal|"MATCH"
argument_list|,
name|usage
operator|=
literal|"match group substring"
argument_list|)
DECL|field|matchSubstring
specifier|private
name|String
name|matchSubstring
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|usage
operator|=
literal|"Output options per group"
argument_list|)
DECL|method|addOption (ListGroupsOption o)
specifier|public
name|void
name|addOption
parameter_list|(
name|ListGroupsOption
name|o
parameter_list|)
block|{
name|options
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-O"
argument_list|,
name|usage
operator|=
literal|"Output option flags, in hex"
argument_list|)
DECL|method|setOptionFlagsHex (String hex)
name|void
name|setOptionFlagsHex
parameter_list|(
name|String
name|hex
parameter_list|)
block|{
name|options
operator|.
name|addAll
argument_list|(
name|ListGroupsOption
operator|.
name|fromBits
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|hex
argument_list|,
literal|16
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|ListGroups (final GroupCache groupCache, final GroupControl.Factory groupControlFactory, final GroupControl.GenericFactory genericGroupControlFactory, final Provider<IdentifiedUser> identifiedUser, final IdentifiedUser.GenericFactory userFactory, final Provider<GetGroups> accountGetGroups, GroupJson json)
specifier|protected
name|ListGroups
parameter_list|(
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
name|GroupControl
operator|.
name|GenericFactory
name|genericGroupControlFactory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|GetGroups
argument_list|>
name|accountGetGroups
parameter_list|,
name|GroupJson
name|json
parameter_list|)
block|{
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
name|genericGroupControlFactory
operator|=
name|genericGroupControlFactory
expr_stmt|;
name|this
operator|.
name|identifiedUser
operator|=
name|identifiedUser
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|accountGetGroups
operator|=
name|accountGetGroups
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListGroupsOption
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|Account
operator|.
name|Id
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getProjects ()
specifier|public
name|List
argument_list|<
name|ProjectControl
argument_list|>
name|getProjects
parameter_list|()
block|{
return|return
name|projects
return|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource resource)
specifier|public
name|Object
name|apply
parameter_list|(
name|TopLevelResource
name|resource
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
name|output
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|GroupInfo
name|info
range|:
name|get
argument_list|()
control|)
block|{
name|output
operator|.
name|put
argument_list|(
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|info
operator|.
name|name
argument_list|,
literal|"Group "
operator|+
name|Url
operator|.
name|decode
argument_list|(
name|info
operator|.
name|id
argument_list|)
argument_list|)
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|info
operator|.
name|name
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|OutputFormat
operator|.
name|JSON
operator|.
name|newGson
argument_list|()
operator|.
name|toJsonTree
argument_list|(
name|output
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|get ()
specifier|public
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groupInfos
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|owned
condition|)
block|{
name|groupInfos
operator|=
name|getGroupsOwnedBy
argument_list|(
name|userFactory
operator|.
name|create
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|groupInfos
operator|=
name|accountGetGroups
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
operator|new
name|AccountResource
argument_list|(
name|userFactory
operator|.
name|create
argument_list|(
name|user
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|owned
condition|)
block|{
name|groupInfos
operator|=
name|getGroupsOwnedBy
argument_list|(
name|identifiedUser
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|groupList
decl_stmt|;
if|if
condition|(
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|AccountGroup
argument_list|>
name|groups
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
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
operator|!=
literal|null
condition|)
block|{
name|groups
operator|.
name|put
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|groupList
operator|=
name|filterGroups
argument_list|(
name|groups
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|groupList
operator|=
name|filterGroups
argument_list|(
name|groupCache
operator|.
name|all
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|groupInfos
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|found
init|=
literal|0
decl_stmt|;
name|int
name|foundIndex
init|=
literal|0
decl_stmt|;
for|for
control|(
name|AccountGroup
name|group
range|:
name|groupList
control|)
block|{
if|if
condition|(
name|foundIndex
operator|++
operator|<
name|start
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|limit
operator|>
literal|0
operator|&&
operator|++
name|found
operator|>
name|limit
condition|)
block|{
break|break;
block|}
name|groupInfos
operator|.
name|add
argument_list|(
name|json
operator|.
name|addOptions
argument_list|(
name|options
argument_list|)
operator|.
name|format
argument_list|(
name|GroupDescriptions
operator|.
name|forAccountGroup
argument_list|(
name|group
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|groupInfos
return|;
block|}
DECL|method|getGroupsOwnedBy (IdentifiedUser user)
specifier|private
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|getGroupsOwnedBy
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groups
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|int
name|found
init|=
literal|0
decl_stmt|;
name|int
name|foundIndex
init|=
literal|0
decl_stmt|;
for|for
control|(
name|AccountGroup
name|g
range|:
name|filterGroups
argument_list|(
name|groupCache
operator|.
name|all
argument_list|()
argument_list|)
control|)
block|{
name|GroupControl
name|ctl
init|=
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|g
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|genericGroupControlFactory
operator|.
name|controlFor
argument_list|(
name|user
argument_list|,
name|g
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
operator|.
name|isOwner
argument_list|()
condition|)
block|{
if|if
condition|(
name|foundIndex
operator|++
operator|<
name|start
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|limit
operator|>
literal|0
operator|&&
operator|++
name|found
operator|>
name|limit
condition|)
block|{
break|break;
block|}
name|groups
operator|.
name|add
argument_list|(
name|json
operator|.
name|addOptions
argument_list|(
name|options
argument_list|)
operator|.
name|format
argument_list|(
name|ctl
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
continue|continue;
block|}
block|}
return|return
name|groups
return|;
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
name|Lists
operator|.
name|newArrayList
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
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|matchSubstring
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|group
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
name|matchSubstring
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
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
name|visibleToAll
operator|&&
operator|!
name|group
operator|.
name|isVisibleToAll
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|groupsToInspect
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|groupsToInspect
operator|.
name|contains
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
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
name|Collections
operator|.
name|sort
argument_list|(
name|filteredGroups
argument_list|,
operator|new
name|GroupComparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|filteredGroups
return|;
block|}
block|}
end_class

end_unit

