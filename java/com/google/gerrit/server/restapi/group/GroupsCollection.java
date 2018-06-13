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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|AcceptsCreate
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
name|IdString
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
name|NeedsParams
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
name|ResourceNotFoundException
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
name|RestCollection
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
name|RestView
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
name|UnprocessableEntityException
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
name|AnonymousUser
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
name|GroupBackends
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
name|GroupResource
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
name|InternalGroup
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
name|Optional
import|;
end_import

begin_class
DECL|class|GroupsCollection
specifier|public
class|class
name|GroupsCollection
implements|implements
name|RestCollection
argument_list|<
name|TopLevelResource
argument_list|,
name|GroupResource
argument_list|>
implements|,
name|AcceptsCreate
argument_list|<
name|TopLevelResource
argument_list|>
implements|,
name|NeedsParams
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|GroupResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListGroups
argument_list|>
name|list
decl_stmt|;
DECL|field|queryGroups
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryGroups
argument_list|>
name|queryGroups
decl_stmt|;
DECL|field|createGroup
specifier|private
specifier|final
name|CreateGroup
operator|.
name|Factory
name|createGroup
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|hasQuery2
specifier|private
name|boolean
name|hasQuery2
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsCollection ( DynamicMap<RestView<GroupResource>> views, Provider<ListGroups> list, Provider<QueryGroups> queryGroups, CreateGroup.Factory createGroup, GroupControl.Factory groupControlFactory, GroupBackend groupBackend, GroupCache groupCache, Provider<CurrentUser> self)
specifier|public
name|GroupsCollection
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|GroupResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|ListGroups
argument_list|>
name|list
parameter_list|,
name|Provider
argument_list|<
name|QueryGroups
argument_list|>
name|queryGroups
parameter_list|,
name|CreateGroup
operator|.
name|Factory
name|createGroup
parameter_list|,
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|queryGroups
operator|=
name|queryGroups
expr_stmt|;
name|this
operator|.
name|createGroup
operator|=
name|createGroup
expr_stmt|;
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setParams (ListMultimap<String, String> params)
specifier|public
name|void
name|setParams
parameter_list|(
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|BadRequestException
block|{
if|if
condition|(
name|params
operator|.
name|containsKey
argument_list|(
literal|"query"
argument_list|)
operator|&&
name|params
operator|.
name|containsKey
argument_list|(
literal|"query2"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"\"query\" and \"query2\" options are mutually exclusive"
argument_list|)
throw|;
block|}
comment|// The --query2 option is defined in QueryGroups
name|this
operator|.
name|hasQuery2
operator|=
name|params
operator|.
name|containsKey
argument_list|(
literal|"query2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|TopLevelResource
argument_list|>
name|list
parameter_list|()
throws|throws
name|ResourceNotFoundException
throws|,
name|AuthException
block|{
specifier|final
name|CurrentUser
name|user
init|=
name|self
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|instanceof
name|AnonymousUser
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
if|if
condition|(
name|hasQuery2
condition|)
block|{
return|return
name|queryGroups
operator|.
name|get
argument_list|()
return|;
block|}
return|return
name|list
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parse (TopLevelResource parent, IdString id)
specifier|public
name|GroupResource
name|parse
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
block|{
specifier|final
name|CurrentUser
name|user
init|=
name|self
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|instanceof
name|AnonymousUser
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|parseId
argument_list|(
name|id
operator|.
name|get
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
name|ResourceNotFoundException
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
name|GroupControl
name|ctl
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
name|ctl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
return|return
operator|new
name|GroupResource
argument_list|(
name|ctl
argument_list|)
return|;
block|}
comment|/**    * Parses a group ID from a request body and returns the group.    *    * @param id ID of the group, can be a group UUID, a group name or a legacy group ID    * @return the group    * @throws UnprocessableEntityException thrown if the group ID cannot be resolved or if the group    *     is not visible to the calling user    */
DECL|method|parse (String id)
specifier|public
name|GroupDescription
operator|.
name|Basic
name|parse
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|UnprocessableEntityException
block|{
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|parseId
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
operator|||
operator|!
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|group
argument_list|)
operator|.
name|isVisible
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Group Not Found: %s"
argument_list|,
name|id
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|group
return|;
block|}
comment|/**    * Parses a group ID from a request body and returns the group if it is a Gerrit internal group.    *    * @param id ID of the group, can be a group UUID, a group name or a legacy group ID    * @return the group    * @throws UnprocessableEntityException thrown if the group ID cannot be resolved, if the group is    *     not visible to the calling user or if it's an external group    */
DECL|method|parseInternal (String id)
specifier|public
name|GroupDescription
operator|.
name|Internal
name|parseInternal
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|UnprocessableEntityException
block|{
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|parse
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|instanceof
name|GroupDescription
operator|.
name|Internal
condition|)
block|{
return|return
operator|(
name|GroupDescription
operator|.
name|Internal
operator|)
name|group
return|;
block|}
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"External Group Not Allowed: %s"
argument_list|,
name|id
argument_list|)
argument_list|)
throw|;
block|}
comment|/**    * Parses a group ID and returns the group without making any permission check whether the current    * user can see the group.    *    * @param id ID of the group, can be a group UUID, a group name or a legacy group ID    * @return the group, null if no group is found for the given group ID    */
DECL|method|parseId (String id)
specifier|public
name|GroupDescription
operator|.
name|Basic
name|parseId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|AccountGroup
operator|.
name|UUID
name|uuid
init|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupBackend
operator|.
name|handles
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
name|GroupDescription
operator|.
name|Basic
name|d
init|=
name|groupBackend
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
comment|// Might be a numeric AccountGroup.Id. -> Internal group.
if|if
condition|(
name|id
operator|.
name|matches
argument_list|(
literal|"^[1-9][0-9]*$"
argument_list|)
condition|)
block|{
try|try
block|{
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|AccountGroup
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
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
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
operator|new
name|InternalGroupDescription
argument_list|(
name|group
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Ignored
block|}
block|}
comment|// Might be a group name, be nice and accept unique names.
name|GroupReference
name|ref
init|=
name|GroupBackends
operator|.
name|findExactSuggestion
argument_list|(
name|groupBackend
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|GroupDescription
operator|.
name|Basic
name|d
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
name|d
operator|!=
literal|null
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|create (TopLevelResource root, IdString name)
specifier|public
name|CreateGroup
name|create
parameter_list|(
name|TopLevelResource
name|root
parameter_list|,
name|IdString
name|name
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|createGroup
operator|.
name|create
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|GroupResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
block|}
end_class

end_unit

