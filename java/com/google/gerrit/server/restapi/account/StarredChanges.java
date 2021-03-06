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
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|DuplicateKeyException
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
name|StorageException
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
name|RestCollectionCreateView
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
name|StarredChangesUtil
operator|.
name|MutuallyExclusiveLabelsException
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

begin_class
annotation|@
name|Singleton
DECL|class|StarredChanges
specifier|public
class|class
name|StarredChanges
implements|implements
name|ChildCollection
argument_list|<
name|AccountResource
argument_list|,
name|AccountResource
operator|.
name|StarredChange
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|changes
specifier|private
specifier|final
name|ChangesCollection
name|changes
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
name|StarredChange
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|starredChangesUtil
specifier|private
specifier|final
name|StarredChangesUtil
name|starredChangesUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|StarredChanges ( ChangesCollection changes, DynamicMap<RestView<AccountResource.StarredChange>> views, StarredChangesUtil starredChangesUtil)
name|StarredChanges
parameter_list|(
name|ChangesCollection
name|changes
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|StarredChange
argument_list|>
argument_list|>
name|views
parameter_list|,
name|StarredChangesUtil
name|starredChangesUtil
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
name|views
operator|=
name|views
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
DECL|method|parse (AccountResource parent, IdString id)
specifier|public
name|AccountResource
operator|.
name|StarredChange
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
if|if
condition|(
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
operator|.
name|contains
argument_list|(
name|StarredChangesUtil
operator|.
name|DEFAULT_LABEL
argument_list|)
condition|)
block|{
return|return
operator|new
name|AccountResource
operator|.
name|StarredChange
argument_list|(
name|user
argument_list|,
name|change
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|StarredChange
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
name|RestView
argument_list|<
name|AccountResource
argument_list|>
name|list
parameter_list|()
throws|throws
name|ResourceNotFoundException
block|{
return|return
operator|(
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
operator|)
name|self
lambda|->
block|{
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
literal|"starredby:"
operator|+
name|self
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|query
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
return|;
block|}
return|;
block|}
annotation|@
name|Singleton
DECL|class|Create
specifier|public
specifier|static
class|class
name|Create
implements|implements
name|RestCollectionCreateView
argument_list|<
name|AccountResource
argument_list|,
name|AccountResource
operator|.
name|StarredChange
argument_list|,
name|EmptyInput
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
DECL|field|starredChangesUtil
specifier|private
specifier|final
name|StarredChangesUtil
name|starredChangesUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|Create ( Provider<CurrentUser> self, ChangesCollection changes, StarredChangesUtil starredChangesUtil)
name|Create
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|ChangesCollection
name|changes
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
name|changes
operator|=
name|changes
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
DECL|method|apply (AccountResource rsrc, IdString id, EmptyInput in)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|IdString
name|id
parameter_list|,
name|EmptyInput
name|in
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
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
literal|"not allowed to add starred change"
argument_list|)
throw|;
block|}
name|ChangeResource
name|change
decl_stmt|;
try|try
block|{
name|change
operator|=
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
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"change %s not found"
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|StorageException
decl||
name|PermissionBackendException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"cannot resolve change"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"internal server error"
argument_list|)
throw|;
block|}
try|try
block|{
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
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|StarredChangesUtil
operator|.
name|DEFAULT_LABELS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MutuallyExclusiveLabelsException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
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
catch|catch
parameter_list|(
name|DuplicateKeyException
name|e
parameter_list|)
block|{
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|Put
specifier|public
specifier|static
class|class
name|Put
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
operator|.
name|StarredChange
argument_list|,
name|EmptyInput
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
annotation|@
name|Inject
DECL|method|Put (Provider<CurrentUser> self)
name|Put
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource.StarredChange rsrc, EmptyInput in)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|AccountResource
operator|.
name|StarredChange
name|rsrc
parameter_list|,
name|EmptyInput
name|in
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
literal|"not allowed update starred changes"
argument_list|)
throw|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|Delete
specifier|public
specifier|static
class|class
name|Delete
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
operator|.
name|StarredChange
argument_list|,
name|EmptyInput
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
DECL|method|Delete (Provider<CurrentUser> self, StarredChangesUtil starredChangesUtil)
name|Delete
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
DECL|method|apply (AccountResource.StarredChange rsrc, EmptyInput in)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|AccountResource
operator|.
name|StarredChange
name|rsrc
parameter_list|,
name|EmptyInput
name|in
parameter_list|)
throws|throws
name|AuthException
throws|,
name|IOException
throws|,
name|IllegalLabelException
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
literal|"not allowed remove starred change"
argument_list|)
throw|;
block|}
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
literal|null
argument_list|,
name|StarredChangesUtil
operator|.
name|DEFAULT_LABELS
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
DECL|class|EmptyInput
specifier|public
specifier|static
class|class
name|EmptyInput
block|{}
block|}
end_class

end_unit

