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
DECL|package|com.google.gerrit.server.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|changes
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|Joiner
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
name|ImmutableList
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
name|api
operator|.
name|changes
operator|.
name|ChangeApi
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
name|api
operator|.
name|changes
operator|.
name|Changes
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
name|ListChangesOption
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
name|common
operator|.
name|ChangeInput
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
name|change
operator|.
name|CreateChange
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
name|git
operator|.
name|UpdateException
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
name|InvalidChangeOperationException
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
name|query
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
name|List
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ChangesImpl
class|class
name|ChangesImpl
implements|implements
name|Changes
block|{
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|changes
specifier|private
specifier|final
name|ChangesCollection
name|changes
decl_stmt|;
DECL|field|api
specifier|private
specifier|final
name|ChangeApiImpl
operator|.
name|Factory
name|api
decl_stmt|;
DECL|field|createChange
specifier|private
specifier|final
name|CreateChange
name|createChange
decl_stmt|;
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangesImpl (Provider<CurrentUser> user, ChangesCollection changes, ChangeApiImpl.Factory api, CreateChange createChange, Provider<QueryChanges> queryProvider)
name|ChangesImpl
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|ChangesCollection
name|changes
parameter_list|,
name|ChangeApiImpl
operator|.
name|Factory
name|api
parameter_list|,
name|CreateChange
name|createChange
parameter_list|,
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryProvider
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|changes
operator|=
name|changes
expr_stmt|;
name|this
operator|.
name|api
operator|=
name|api
expr_stmt|;
name|this
operator|.
name|createChange
operator|=
name|createChange
expr_stmt|;
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|id (int id)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|id
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|id (String project, String branch, String id)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|id
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|'~'
argument_list|)
operator|.
name|join
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|project
argument_list|)
argument_list|,
name|Url
operator|.
name|encode
argument_list|(
name|branch
argument_list|)
argument_list|,
name|Url
operator|.
name|encode
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|id (String id)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|api
operator|.
name|create
argument_list|(
name|changes
operator|.
name|parse
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|IdString
operator|.
name|fromUrl
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot parse change"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|create (ChangeInput in)
specifier|public
name|ChangeApi
name|create
parameter_list|(
name|ChangeInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|ChangeInfo
name|out
init|=
name|createChange
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|in
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
return|return
name|api
operator|.
name|create
argument_list|(
name|changes
operator|.
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|out
operator|.
name|_number
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
decl||
name|IOException
decl||
name|InvalidChangeOperationException
decl||
name|UpdateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot create change"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|query ()
specifier|public
name|QueryRequest
name|query
parameter_list|()
block|{
return|return
operator|new
name|QueryRequest
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|ChangesImpl
operator|.
name|this
operator|.
name|get
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|query (String query)
specifier|public
name|QueryRequest
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
return|return
name|query
argument_list|()
operator|.
name|withQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
DECL|method|get (final QueryRequest q)
specifier|private
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|get
parameter_list|(
specifier|final
name|QueryRequest
name|q
parameter_list|)
throws|throws
name|RestApiException
block|{
name|QueryChanges
name|qc
init|=
name|queryProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|q
operator|.
name|getQuery
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|qc
operator|.
name|addQuery
argument_list|(
name|q
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|qc
operator|.
name|setLimit
argument_list|(
name|q
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|qc
operator|.
name|setStart
argument_list|(
name|q
operator|.
name|getStart
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ListChangesOption
name|option
range|:
name|q
operator|.
name|getOptions
argument_list|()
control|)
block|{
name|qc
operator|.
name|addOption
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|CurrentUser
name|u
init|=
name|user
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|u
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|u
operator|.
name|asIdentifiedUser
argument_list|()
operator|.
name|clearStarredChanges
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|qc
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
comment|// Check type safety of result; the extension API should be safer than the
comment|// REST API in this case, since it's intended to be used in Java.
name|Object
name|first
init|=
name|checkNotNull
argument_list|(
name|result
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|first
operator|instanceof
name|ChangeInfo
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|infos
init|=
operator|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
operator|)
name|result
decl_stmt|;
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|infos
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
decl||
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot query changes"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

