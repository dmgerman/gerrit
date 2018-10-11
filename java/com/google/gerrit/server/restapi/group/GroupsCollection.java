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
name|GroupResource
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
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|groupResolver
specifier|private
specifier|final
name|GroupResolver
name|groupResolver
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
DECL|method|GroupsCollection ( DynamicMap<RestView<GroupResource>> views, Provider<ListGroups> list, Provider<QueryGroups> queryGroups, GroupControl.Factory groupControlFactory, GroupResolver groupResolver, Provider<CurrentUser> self)
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
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
name|GroupResolver
name|groupResolver
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
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|groupResolver
operator|=
name|groupResolver
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
name|groupResolver
operator|.
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

