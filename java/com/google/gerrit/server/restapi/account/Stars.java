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
DECL|package|com.google.gerrit.server.restapi.account
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
name|api
operator|.
name|changes
operator|.
name|StarsInput
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
name|ChangeInfo
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
name|ChildCollection
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
name|StarredChangesUtil
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
name|StarredChangesUtil
operator|.
name|IllegalLabelException
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
name|AccountResource
operator|.
name|Star
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
name|change
operator|.
name|ChangeResource
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
name|restapi
operator|.
name|change
operator|.
name|ChangesCollection
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
name|change
operator|.
name|QueryChanges
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
name|Collection
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
name|SortedSet
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|Stars
specifier|public
class|class
name|Stars
implements|implements
name|ChildCollection
argument_list|<
name|AccountResource
argument_list|,
name|AccountResource
operator|.
name|Star
argument_list|>
block|{
DECL|field|changes
specifier|private
specifier|final
name|ChangesCollection
name|changes
decl_stmt|;
DECL|field|listStarredChanges
specifier|private
specifier|final
name|ListStarredChanges
name|listStarredChanges
decl_stmt|;
DECL|field|starredChangesUtil
specifier|private
specifier|final
name|StarredChangesUtil
name|starredChangesUtil
decl_stmt|;
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|Star
argument_list|>
argument_list|>
name|views
decl_stmt|;
annotation|@
name|Inject
DECL|method|Stars ( ChangesCollection changes, ListStarredChanges listStarredChanges, StarredChangesUtil starredChangesUtil, DynamicMap<RestView<AccountResource.Star>> views)
name|Stars
parameter_list|(
name|ChangesCollection
name|changes
parameter_list|,
name|ListStarredChanges
name|listStarredChanges
parameter_list|,
name|StarredChangesUtil
name|starredChangesUtil
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|Star
argument_list|>
argument_list|>
name|views
parameter_list|)
block|{
name|this
operator|.
name|changes
operator|=
name|changes
expr_stmt|;
name|this
operator|.
name|listStarredChanges
operator|=
name|listStarredChanges
expr_stmt|;
name|this
operator|.
name|starredChangesUtil
operator|=
name|starredChangesUtil
expr_stmt|;
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (AccountResource parent, IdString id)
specifier|public
name|Star
name|parse
parameter_list|(
name|AccountResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|PermissionBackendException
throws|,
name|IOException
block|{
name|IdentifiedUser
name|user
init|=
name|parent
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|ChangeResource
name|change
init|=
name|changes
operator|.
name|parse
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|starredChangesUtil
operator|.
name|getLabels
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|AccountResource
operator|.
name|Star
argument_list|(
name|user
argument_list|,
name|change
argument_list|,
name|labels
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
name|Star
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|ListStarredChanges
name|list
parameter_list|()
block|{
return|return
name|listStarredChanges
return|;
block|}
annotation|@
name|Singleton
DECL|class|ListStarredChanges
specifier|public
specifier|static
class|class
name|ListStarredChanges
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|changes
specifier|private
specifier|final
name|ChangesCollection
name|changes
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListStarredChanges (Provider<CurrentUser> self, ChangesCollection changes)
name|ListStarredChanges
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|ChangesCollection
name|changes
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
name|changes
operator|=
name|changes
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|apply (AccountResource rsrc)
specifier|public
name|Response
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|hasSameAccountId
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to list stars of another account"
argument_list|)
throw|;
block|}
comment|// The type of the value in the response that is returned by QueryChanges depends on the
comment|// number of queries that is provided as input. If a single query is provided as input the
comment|// value type is {@code List<ChangeInfo>}, if multiple queries are provided as input the value
comment|// type is {@code List<List<ChangeInfo>>) (one {@code List<ChangeInfo>} as result to each
comment|// query). Since in this case we provide exactly one query ("has:stars") as input we know that
comment|// the value always has the type {@code List<ChangeInfo>} and hence we can safely cast the
comment|// value to this type.
name|QueryChanges
name|query
init|=
name|changes
operator|.
name|list
argument_list|()
decl_stmt|;
name|query
operator|.
name|addQuery
argument_list|(
literal|"has:stars"
argument_list|)
expr_stmt|;
name|Response
argument_list|<
name|?
argument_list|>
name|response
init|=
name|query
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|value
init|=
operator|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
operator|)
name|response
operator|.
name|value
argument_list|()
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|Get
specifier|public
specifier|static
class|class
name|Get
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
operator|.
name|Star
argument_list|>
block|{
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|starredChangesUtil
specifier|private
specifier|final
name|StarredChangesUtil
name|starredChangesUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|Get (Provider<CurrentUser> self, StarredChangesUtil starredChangesUtil)
name|Get
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|StarredChangesUtil
name|starredChangesUtil
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
name|starredChangesUtil
operator|=
name|starredChangesUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource.Star rsrc)
specifier|public
name|Response
argument_list|<
name|SortedSet
argument_list|<
name|String
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|AccountResource
operator|.
name|Star
name|rsrc
parameter_list|)
throws|throws
name|AuthException
block|{
if|if
condition|(
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|hasSameAccountId
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to get stars of another account"
argument_list|)
throw|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|starredChangesUtil
operator|.
name|getLabels
argument_list|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|Post
specifier|public
specifier|static
class|class
name|Post
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
operator|.
name|Star
argument_list|,
name|StarsInput
argument_list|>
block|{
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|starredChangesUtil
specifier|private
specifier|final
name|StarredChangesUtil
name|starredChangesUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|Post (Provider<CurrentUser> self, StarredChangesUtil starredChangesUtil)
name|Post
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|StarredChangesUtil
name|starredChangesUtil
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
name|starredChangesUtil
operator|=
name|starredChangesUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource.Star rsrc, StarsInput in)
specifier|public
name|Response
argument_list|<
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|AccountResource
operator|.
name|Star
name|rsrc
parameter_list|,
name|StarsInput
name|in
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
block|{
if|if
condition|(
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|hasSameAccountId
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to update stars of another account"
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|starredChangesUtil
operator|.
name|star
argument_list|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|in
operator|.
name|add
argument_list|,
name|in
operator|.
name|remove
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalLabelException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

