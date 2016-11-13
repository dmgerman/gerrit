begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|extensions
operator|.
name|client
operator|.
name|ProjectWatchInfo
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
name|RestModifyView
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
name|WatchConfig
operator|.
name|NotifyType
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
name|WatchConfig
operator|.
name|ProjectWatchKey
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
name|ProjectsCollection
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PostWatchedProjects
specifier|public
class|class
name|PostWatchedProjects
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
argument_list|>
block|{
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
decl_stmt|;
DECL|field|getWatchedProjects
specifier|private
specifier|final
name|GetWatchedProjects
name|getWatchedProjects
decl_stmt|;
DECL|field|projectsCollection
specifier|private
specifier|final
name|ProjectsCollection
name|projectsCollection
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|watchConfig
specifier|private
specifier|final
name|WatchConfig
operator|.
name|Accessor
name|watchConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|PostWatchedProjects ( Provider<IdentifiedUser> self, GetWatchedProjects getWatchedProjects, ProjectsCollection projectsCollection, AccountCache accountCache, WatchConfig.Accessor watchConfig)
specifier|public
name|PostWatchedProjects
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
parameter_list|,
name|GetWatchedProjects
name|getWatchedProjects
parameter_list|,
name|ProjectsCollection
name|projectsCollection
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|WatchConfig
operator|.
name|Accessor
name|watchConfig
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|getWatchedProjects
operator|=
name|getWatchedProjects
expr_stmt|;
name|this
operator|.
name|projectsCollection
operator|=
name|projectsCollection
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|watchConfig
operator|=
name|watchConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, List<ProjectWatchInfo> input)
specifier|public
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|input
parameter_list|)
throws|throws
name|OrmException
throws|,
name|RestApiException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|&&
operator|!
name|self
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
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to edit project watches"
argument_list|)
throw|;
block|}
name|Account
operator|.
name|Id
name|accountId
init|=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|watchConfig
operator|.
name|upsertProjectWatches
argument_list|(
name|accountId
argument_list|,
name|asMap
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
name|accountCache
operator|.
name|evict
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
return|return
name|getWatchedProjects
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|)
return|;
block|}
DECL|method|asMap (List<ProjectWatchInfo> input)
specifier|private
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|asMap
parameter_list|(
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|input
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|UnprocessableEntityException
throws|,
name|IOException
block|{
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProjectWatchInfo
name|info
range|:
name|input
control|)
block|{
if|if
condition|(
name|info
operator|.
name|project
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"project name must be specified"
argument_list|)
throw|;
block|}
name|ProjectWatchKey
name|key
init|=
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|projectsCollection
operator|.
name|parse
argument_list|(
name|info
operator|.
name|project
argument_list|)
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|info
operator|.
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"duplicate entry for project "
operator|+
name|format
argument_list|(
name|info
operator|.
name|project
argument_list|,
name|info
operator|.
name|filter
argument_list|)
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|NotifyType
argument_list|>
name|notifyValues
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|toBoolean
argument_list|(
name|info
operator|.
name|notifyAbandonedChanges
argument_list|)
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|ABANDONED_CHANGES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toBoolean
argument_list|(
name|info
operator|.
name|notifyAllComments
argument_list|)
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toBoolean
argument_list|(
name|info
operator|.
name|notifyNewChanges
argument_list|)
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toBoolean
argument_list|(
name|info
operator|.
name|notifyNewPatchSets
argument_list|)
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toBoolean
argument_list|(
name|info
operator|.
name|notifySubmittedChanges
argument_list|)
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|SUBMITTED_CHANGES
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|notifyValues
argument_list|)
expr_stmt|;
block|}
return|return
name|m
return|;
block|}
DECL|method|toBoolean (Boolean b)
specifier|private
name|boolean
name|toBoolean
parameter_list|(
name|Boolean
name|b
parameter_list|)
block|{
return|return
name|b
operator|==
literal|null
condition|?
literal|false
else|:
name|b
return|;
block|}
DECL|method|format (String project, String filter)
specifier|private
specifier|static
name|String
name|format
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
return|return
name|project
operator|+
operator|(
name|filter
operator|!=
literal|null
operator|&&
operator|!
name|WatchConfig
operator|.
name|FILTER_ALL
operator|.
name|equals
argument_list|(
name|filter
argument_list|)
condition|?
literal|" and filter "
operator|+
name|filter
else|:
literal|""
operator|)
return|;
block|}
block|}
end_class

end_unit

