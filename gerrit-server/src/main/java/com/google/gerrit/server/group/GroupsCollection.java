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
name|collect
operator|.
name|Iterables
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
name|AcceptsPost
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
name|account
operator|.
name|PerformCreateGroup
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
name|GerritServerConfig
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
name|Collection
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
name|Config
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
name|AcceptsPost
argument_list|<
name|TopLevelResource
argument_list|>
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
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|performCreateGroupFactory
specifier|private
specifier|final
name|PerformCreateGroup
operator|.
name|Factory
name|performCreateGroupFactory
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsCollection (final DynamicMap<RestView<GroupResource>> views, final Provider<ListGroups> list, final GroupControl.Factory groupControlFactory, final GroupBackend groupBackend, final Provider<CurrentUser> self, final PerformCreateGroup.Factory performCreateGroupFactory, final GroupCache groupCache, @GerritServerConfig final Config cfg)
name|GroupsCollection
parameter_list|(
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|GroupResource
argument_list|>
argument_list|>
name|views
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|ListGroups
argument_list|>
name|list
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
specifier|final
name|GroupBackend
name|groupBackend
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
specifier|final
name|PerformCreateGroup
operator|.
name|Factory
name|performCreateGroupFactory
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
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
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|performCreateGroupFactory
operator|=
name|performCreateGroupFactory
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|cfg
operator|=
name|cfg
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
operator|instanceof
name|IdentifiedUser
operator|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
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
DECL|method|parse (TopLevelResource parent, String id)
specifier|public
name|GroupResource
name|parse
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|Exception
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
operator|instanceof
name|IdentifiedUser
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
return|return
name|parse
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|parse (String urlId)
name|GroupResource
name|parse
parameter_list|(
name|String
name|urlId
parameter_list|)
throws|throws
name|ResourceNotFoundException
block|{
name|String
name|id
init|=
name|Url
operator|.
name|decode
argument_list|(
name|urlId
argument_list|)
decl_stmt|;
try|try
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
return|return
name|check
argument_list|(
name|urlId
argument_list|,
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|uuid
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|noSuchGroup
parameter_list|)
block|{     }
comment|// Might be a legacy AccountGroup.Id.
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
name|legacyId
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
return|return
name|check
argument_list|(
name|urlId
argument_list|,
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|legacyId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|invalidId
parameter_list|)
block|{       }
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{       }
block|}
comment|// Might be a group name. Try to be nice and accept unique names.
name|Collection
argument_list|<
name|GroupReference
argument_list|>
name|suggestions
init|=
name|groupBackend
operator|.
name|suggest
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|suggestions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
try|try
block|{
name|GroupReference
name|ref
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|suggestions
argument_list|)
decl_stmt|;
return|return
name|check
argument_list|(
name|urlId
argument_list|,
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|ref
operator|.
name|getUUID
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{       }
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|urlId
argument_list|)
throw|;
block|}
DECL|method|check (String urlId, GroupControl ctl)
specifier|private
name|GroupResource
name|check
parameter_list|(
name|String
name|urlId
parameter_list|,
name|GroupControl
name|ctl
parameter_list|)
throws|throws
name|ResourceNotFoundException
block|{
if|if
condition|(
name|ctl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
return|return
operator|new
name|GroupResource
argument_list|(
name|ctl
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|urlId
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|post (TopLevelResource parent)
specifier|public
name|CreateGroup
name|post
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|)
block|{
return|return
operator|new
name|CreateGroup
argument_list|(
name|performCreateGroupFactory
argument_list|,
name|groupCache
argument_list|,
name|self
argument_list|,
name|cfg
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

