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
DECL|package|com.google.gerrit.server.restapi.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|group
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|toList
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
name|MoreObjects
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
name|Streams
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
name|GroupDescription
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
name|entities
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
name|entities
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
name|exceptions
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
name|extensions
operator|.
name|client
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
name|client
operator|.
name|ListOption
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
name|common
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
name|extensions
operator|.
name|restapi
operator|.
name|BadRequestException
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
name|Response
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
name|RestApiException
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
name|GroupBackend
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
name|GroupResolver
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
name|InternalGroupDescription
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
name|db
operator|.
name|Groups
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
name|restapi
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|errors
operator|.
name|ConfigInvalidException
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
DECL|field|GROUP_COMPARATOR
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|GROUP_COMPARATOR
init|=
name|Comparator
operator|.
name|comparing
argument_list|(
name|GroupDescription
operator|.
name|Basic
operator|::
name|getName
argument_list|)
decl_stmt|;
DECL|field|groupCache
specifier|protected
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|projects
specifier|private
specifier|final
name|List
argument_list|<
name|ProjectState
argument_list|>
name|projects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|groupsToInspect
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupsToInspect
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
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
name|GetGroups
name|accountGetGroups
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|GroupJson
name|json
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
DECL|field|groupResolver
specifier|private
specifier|final
name|GroupResolver
name|groupResolver
decl_stmt|;
DECL|field|options
specifier|private
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListGroupsOption
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|visibleToAll
specifier|private
name|boolean
name|visibleToAll
decl_stmt|;
DECL|field|user
specifier|private
name|Account
operator|.
name|Id
name|user
decl_stmt|;
DECL|field|owned
specifier|private
name|boolean
name|owned
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|matchSubstring
specifier|private
name|String
name|matchSubstring
decl_stmt|;
DECL|field|matchRegex
specifier|private
name|String
name|matchRegex
decl_stmt|;
DECL|field|suggest
specifier|private
name|String
name|suggest
decl_stmt|;
DECL|field|ownedBy
specifier|private
name|String
name|ownedBy
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
DECL|method|addProject (ProjectState project)
specifier|public
name|void
name|addProject
parameter_list|(
name|ProjectState
name|project
parameter_list|)
block|{
name|projects
operator|.
name|add
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setVisibleToAll (boolean visibleToAll)
specifier|public
name|void
name|setVisibleToAll
parameter_list|(
name|boolean
name|visibleToAll
parameter_list|)
block|{
name|this
operator|.
name|visibleToAll
operator|=
name|visibleToAll
expr_stmt|;
block|}
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
DECL|method|setUser (Account.Id user)
specifier|public
name|void
name|setUser
parameter_list|(
name|Account
operator|.
name|Id
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--owned"
argument_list|,
name|usage
operator|=
literal|"to list only groups that are owned by the"
operator|+
literal|" specified user or by the calling user if no user was specifed"
argument_list|)
DECL|method|setOwned (boolean owned)
specifier|public
name|void
name|setOwned
parameter_list|(
name|boolean
name|owned
parameter_list|)
block|{
name|this
operator|.
name|owned
operator|=
name|owned
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--group"
argument_list|,
name|aliases
operator|=
block|{
literal|"-g"
block|}
argument_list|,
name|usage
operator|=
literal|"group to inspect"
argument_list|)
DECL|method|addGroup (AccountGroup.UUID uuid)
specifier|public
name|void
name|addGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|groupsToInspect
operator|.
name|add
argument_list|(
name|uuid
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
DECL|method|setLimit (int limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
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
DECL|method|setStart (int start)
specifier|public
name|void
name|setStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
block|}
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
DECL|method|setMatchSubstring (String matchSubstring)
specifier|public
name|void
name|setMatchSubstring
parameter_list|(
name|String
name|matchSubstring
parameter_list|)
block|{
name|this
operator|.
name|matchSubstring
operator|=
name|matchSubstring
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--regex"
argument_list|,
name|aliases
operator|=
block|{
literal|"-r"
block|}
argument_list|,
name|metaVar
operator|=
literal|"REGEX"
argument_list|,
name|usage
operator|=
literal|"match group regex"
argument_list|)
DECL|method|setMatchRegex (String matchRegex)
specifier|public
name|void
name|setMatchRegex
parameter_list|(
name|String
name|matchRegex
parameter_list|)
block|{
name|this
operator|.
name|matchRegex
operator|=
name|matchRegex
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--suggest"
argument_list|,
name|aliases
operator|=
block|{
literal|"-s"
block|}
argument_list|,
name|usage
operator|=
literal|"to get a suggestion of groups"
argument_list|)
DECL|method|setSuggest (String suggest)
specifier|public
name|void
name|setSuggest
parameter_list|(
name|String
name|suggest
parameter_list|)
block|{
name|this
operator|.
name|suggest
operator|=
name|suggest
expr_stmt|;
block|}
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
name|ListOption
operator|.
name|fromBits
argument_list|(
name|ListGroupsOption
operator|.
name|class
argument_list|,
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
name|Option
argument_list|(
name|name
operator|=
literal|"--owned-by"
argument_list|,
name|usage
operator|=
literal|"list groups owned by the given group uuid"
argument_list|)
DECL|method|setOwnedBy (String ownedBy)
specifier|public
name|void
name|setOwnedBy
parameter_list|(
name|String
name|ownedBy
parameter_list|)
block|{
name|this
operator|.
name|ownedBy
operator|=
name|ownedBy
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|ListGroups ( final GroupCache groupCache, final GroupControl.Factory groupControlFactory, final GroupControl.GenericFactory genericGroupControlFactory, final Provider<IdentifiedUser> identifiedUser, final IdentifiedUser.GenericFactory userFactory, final GetGroups accountGetGroups, final GroupResolver groupResolver, GroupJson json, GroupBackend groupBackend, Groups groups)
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
name|GetGroups
name|accountGetGroups
parameter_list|,
specifier|final
name|GroupResolver
name|groupResolver
parameter_list|,
name|GroupJson
name|json
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|Groups
name|groups
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
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|groupResolver
operator|=
name|groupResolver
expr_stmt|;
block|}
DECL|method|setOptions (EnumSet<ListGroupsOption> options)
specifier|public
name|void
name|setOptions
parameter_list|(
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
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
name|ProjectState
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
name|Response
argument_list|<
name|SortedMap
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|resource
parameter_list|)
throws|throws
name|Exception
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
name|output
init|=
operator|new
name|TreeMap
argument_list|<>
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
name|MoreObjects
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
name|Response
operator|.
name|ok
argument_list|(
name|output
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
name|Exception
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|suggest
argument_list|)
condition|)
block|{
return|return
name|suggestGroups
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|matchSubstring
argument_list|)
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|matchRegex
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Specify one of m/r"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ownedBy
operator|!=
literal|null
condition|)
block|{
return|return
name|getGroupsOwnedBy
argument_list|(
name|ownedBy
argument_list|)
return|;
block|}
if|if
condition|(
name|owned
condition|)
block|{
return|return
name|getGroupsOwnedBy
argument_list|(
name|user
operator|!=
literal|null
condition|?
name|userFactory
operator|.
name|create
argument_list|(
name|user
argument_list|)
else|:
name|identifiedUser
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
return|return
name|accountGetGroups
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
operator|.
name|value
argument_list|()
return|;
block|}
return|return
name|getAllGroups
argument_list|()
return|;
block|}
DECL|method|getAllGroups ()
specifier|private
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|getAllGroups
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
name|Pattern
name|pattern
init|=
name|getRegexPattern
argument_list|()
decl_stmt|;
name|Stream
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|existingGroups
init|=
name|getAllExistingGroups
argument_list|()
operator|.
name|filter
argument_list|(
name|group
lambda|->
name|isRelevant
argument_list|(
name|pattern
argument_list|,
name|group
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|loadGroup
argument_list|)
operator|.
name|flatMap
argument_list|(
name|Streams
operator|::
name|stream
argument_list|)
operator|.
name|filter
argument_list|(
name|this
operator|::
name|isVisible
argument_list|)
operator|.
name|sorted
argument_list|(
name|GROUP_COMPARATOR
argument_list|)
operator|.
name|skip
argument_list|(
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
name|existingGroups
operator|=
name|existingGroups
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|relevantGroups
init|=
name|existingGroups
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groupInfos
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|relevantGroups
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupDescription
operator|.
name|Internal
name|group
range|:
name|relevantGroups
control|)
block|{
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
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|groupInfos
return|;
block|}
DECL|method|getAllExistingGroups ()
specifier|private
name|Stream
argument_list|<
name|GroupReference
argument_list|>
name|getAllExistingGroups
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|projects
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ProjectState
operator|::
name|getAllGroups
argument_list|)
operator|.
name|flatMap
argument_list|(
name|Collection
operator|::
name|stream
argument_list|)
operator|.
name|distinct
argument_list|()
return|;
block|}
return|return
name|groups
operator|.
name|getAllGroupReferences
argument_list|()
return|;
block|}
DECL|method|suggestGroups ()
specifier|private
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|suggestGroups
parameter_list|()
throws|throws
name|BadRequestException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|conflictingSuggestParameters
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"You should only have no more than one --project and -n with --suggest"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|GroupReference
argument_list|>
name|groupRefs
init|=
name|groupBackend
operator|.
name|suggest
argument_list|(
name|suggest
argument_list|,
name|projects
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|limit
argument_list|(
name|limit
operator|<=
literal|0
condition|?
literal|10
else|:
name|Math
operator|.
name|min
argument_list|(
name|limit
argument_list|,
literal|10
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groupInfos
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|groupRefs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|ref
range|:
name|groupRefs
control|)
block|{
name|GroupDescription
operator|.
name|Basic
name|desc
init|=
name|groupBackend
operator|.
name|get
argument_list|(
name|ref
operator|.
name|getUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
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
name|desc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|groupInfos
return|;
block|}
DECL|method|conflictingSuggestParameters ()
specifier|private
name|boolean
name|conflictingSuggestParameters
parameter_list|()
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|suggest
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|projects
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|visibleToAll
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|owned
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|ownedBy
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|start
operator|!=
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|groupsToInspect
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
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
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|matchRegex
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|filterGroupsOwnedBy (Predicate<GroupDescription.Internal> filter)
specifier|private
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|filterGroupsOwnedBy
parameter_list|(
name|Predicate
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|filter
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
name|Pattern
name|pattern
init|=
name|getRegexPattern
argument_list|()
decl_stmt|;
name|Stream
argument_list|<
name|?
extends|extends
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|foundGroups
init|=
name|groups
operator|.
name|getAllGroupReferences
argument_list|()
operator|.
name|filter
argument_list|(
name|group
lambda|->
name|isRelevant
argument_list|(
name|pattern
argument_list|,
name|group
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|loadGroup
argument_list|)
operator|.
name|flatMap
argument_list|(
name|Streams
operator|::
name|stream
argument_list|)
operator|.
name|filter
argument_list|(
name|this
operator|::
name|isVisible
argument_list|)
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
operator|.
name|sorted
argument_list|(
name|GROUP_COMPARATOR
argument_list|)
operator|.
name|skip
argument_list|(
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
name|foundGroups
operator|=
name|foundGroups
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|ownedGroups
init|=
name|foundGroups
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groupInfos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|ownedGroups
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupDescription
operator|.
name|Internal
name|group
range|:
name|ownedGroups
control|)
block|{
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
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|groupInfos
return|;
block|}
DECL|method|loadGroup (GroupReference groupReference)
specifier|private
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|loadGroup
parameter_list|(
name|GroupReference
name|groupReference
parameter_list|)
block|{
return|return
name|groupCache
operator|.
name|get
argument_list|(
name|groupReference
operator|.
name|getUUID
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|InternalGroupDescription
operator|::
operator|new
argument_list|)
return|;
block|}
DECL|method|getGroupsOwnedBy (String id)
specifier|private
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|getGroupsOwnedBy
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
name|String
name|uuid
init|=
name|groupResolver
operator|.
name|parse
argument_list|(
name|id
argument_list|)
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|filterGroupsOwnedBy
argument_list|(
name|group
lambda|->
name|group
operator|.
name|getOwnerGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|uuid
argument_list|)
argument_list|)
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
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
return|return
name|filterGroupsOwnedBy
argument_list|(
name|group
lambda|->
name|isOwner
argument_list|(
name|user
argument_list|,
name|group
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isOwner (CurrentUser user, GroupDescription.Internal group)
specifier|private
name|boolean
name|isOwner
parameter_list|(
name|CurrentUser
name|user
parameter_list|,
name|GroupDescription
operator|.
name|Internal
name|group
parameter_list|)
block|{
try|try
block|{
return|return
name|genericGroupControlFactory
operator|.
name|controlFor
argument_list|(
name|user
argument_list|,
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
operator|.
name|isOwner
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|getRegexPattern ()
specifier|private
name|Pattern
name|getRegexPattern
parameter_list|()
block|{
return|return
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|matchRegex
argument_list|)
condition|?
literal|null
else|:
name|Pattern
operator|.
name|compile
argument_list|(
name|matchRegex
argument_list|)
return|;
block|}
DECL|method|isRelevant (Pattern pattern, GroupReference group)
specifier|private
name|boolean
name|isRelevant
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|GroupReference
name|group
parameter_list|)
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
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|pattern
operator|.
name|matcher
argument_list|(
name|group
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
name|groupsToInspect
operator|.
name|isEmpty
argument_list|()
operator|||
name|groupsToInspect
operator|.
name|contains
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isVisible (GroupDescription.Internal group)
specifier|private
name|boolean
name|isVisible
parameter_list|(
name|GroupDescription
operator|.
name|Internal
name|group
parameter_list|)
block|{
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
return|return
literal|false
return|;
block|}
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
return|return
name|c
operator|.
name|isVisible
argument_list|()
return|;
block|}
block|}
end_class

end_unit

