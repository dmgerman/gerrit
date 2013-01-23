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
name|ResourceConflictException
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
name|VisibleGroups
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
name|util
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
DECL|field|visibleGroupsFactory
specifier|private
specifier|final
name|VisibleGroups
operator|.
name|Factory
name|visibleGroupsFactory
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
literal|"--type"
argument_list|,
name|usage
operator|=
literal|"type of group"
argument_list|)
DECL|field|groupType
specifier|private
name|AccountGroup
operator|.
name|Type
name|groupType
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
literal|"-m"
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
name|Inject
DECL|method|ListGroups (final VisibleGroups.Factory visibleGroupsFactory, final IdentifiedUser.GenericFactory userFactory, Provider<GetGroups> accountGetGroups)
specifier|protected
name|ListGroups
parameter_list|(
specifier|final
name|VisibleGroups
operator|.
name|Factory
name|visibleGroupsFactory
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|Provider
argument_list|<
name|GetGroups
argument_list|>
name|accountGetGroups
parameter_list|)
block|{
name|this
operator|.
name|visibleGroupsFactory
operator|=
name|visibleGroupsFactory
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
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|Exception
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
name|NoSuchGroupException
block|{
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groups
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|groups
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
else|else
block|{
name|VisibleGroups
name|visibleGroups
init|=
name|visibleGroupsFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|visibleGroups
operator|.
name|setOnlyVisibleToAll
argument_list|(
name|visibleToAll
argument_list|)
expr_stmt|;
name|visibleGroups
operator|.
name|setGroupType
argument_list|(
name|groupType
argument_list|)
expr_stmt|;
name|visibleGroups
operator|.
name|setMatch
argument_list|(
name|matchSubstring
argument_list|)
expr_stmt|;
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
name|groupList
operator|=
name|visibleGroups
operator|.
name|get
argument_list|(
name|projects
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|groupList
operator|=
name|visibleGroups
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|groups
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
for|for
control|(
name|AccountGroup
name|group
range|:
name|groupList
control|)
block|{
name|groups
operator|.
name|add
argument_list|(
operator|new
name|GroupInfo
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
return|return
name|groups
return|;
block|}
block|}
end_class

end_unit

