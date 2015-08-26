begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.api.accounts
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
name|accounts
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
name|common
operator|.
name|errors
operator|.
name|EmailException
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
name|accounts
operator|.
name|AccountApi
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
name|accounts
operator|.
name|EmailInput
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
name|accounts
operator|.
name|GpgKeyApi
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
name|AccountInfo
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
name|GpgKeyInfo
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
name|server
operator|.
name|GpgException
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
name|AccountLoader
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
name|CreateEmail
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
name|StarredChanges
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
name|assistedinject
operator|.
name|Assisted
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

begin_class
DECL|class|AccountApiImpl
specifier|public
class|class
name|AccountApiImpl
implements|implements
name|AccountApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (AccountResource account)
name|AccountApiImpl
name|create
parameter_list|(
name|AccountResource
name|account
parameter_list|)
function_decl|;
block|}
DECL|field|account
specifier|private
specifier|final
name|AccountResource
name|account
decl_stmt|;
DECL|field|changes
specifier|private
specifier|final
name|ChangesCollection
name|changes
decl_stmt|;
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
decl_stmt|;
DECL|field|starredChangesCreate
specifier|private
specifier|final
name|StarredChanges
operator|.
name|Create
name|starredChangesCreate
decl_stmt|;
DECL|field|starredChangesDelete
specifier|private
specifier|final
name|StarredChanges
operator|.
name|Delete
name|starredChangesDelete
decl_stmt|;
DECL|field|createEmailFactory
specifier|private
specifier|final
name|CreateEmail
operator|.
name|Factory
name|createEmailFactory
decl_stmt|;
DECL|field|gpgApiAdapter
specifier|private
specifier|final
name|GpgApiAdapter
name|gpgApiAdapter
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountApiImpl (AccountLoader.Factory ailf, ChangesCollection changes, StarredChanges.Create starredChangesCreate, StarredChanges.Delete starredChangesDelete, CreateEmail.Factory createEmailFactory, GpgApiAdapter gpgApiAdapter, @Assisted AccountResource account)
name|AccountApiImpl
parameter_list|(
name|AccountLoader
operator|.
name|Factory
name|ailf
parameter_list|,
name|ChangesCollection
name|changes
parameter_list|,
name|StarredChanges
operator|.
name|Create
name|starredChangesCreate
parameter_list|,
name|StarredChanges
operator|.
name|Delete
name|starredChangesDelete
parameter_list|,
name|CreateEmail
operator|.
name|Factory
name|createEmailFactory
parameter_list|,
name|GpgApiAdapter
name|gpgApiAdapter
parameter_list|,
annotation|@
name|Assisted
name|AccountResource
name|account
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
name|this
operator|.
name|accountLoaderFactory
operator|=
name|ailf
expr_stmt|;
name|this
operator|.
name|changes
operator|=
name|changes
expr_stmt|;
name|this
operator|.
name|starredChangesCreate
operator|=
name|starredChangesCreate
expr_stmt|;
name|this
operator|.
name|starredChangesDelete
operator|=
name|starredChangesDelete
expr_stmt|;
name|this
operator|.
name|createEmailFactory
operator|=
name|createEmailFactory
expr_stmt|;
name|this
operator|.
name|gpgApiAdapter
operator|=
name|gpgApiAdapter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
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
name|AccountInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
name|AccountLoader
name|accountLoader
init|=
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|AccountInfo
name|ai
init|=
name|accountLoader
operator|.
name|get
argument_list|(
name|account
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|accountLoader
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|ai
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
DECL|method|starChange (String id)
specifier|public
name|void
name|starChange
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|ChangeResource
name|rsrc
init|=
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
decl_stmt|;
name|starredChangesCreate
operator|.
name|setChange
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
name|starredChangesCreate
operator|.
name|apply
argument_list|(
name|account
argument_list|,
operator|new
name|StarredChanges
operator|.
name|EmptyInput
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Cannot star change"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|unstarChange (String id)
specifier|public
name|void
name|unstarChange
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|ChangeResource
name|rsrc
init|=
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
decl_stmt|;
name|AccountResource
operator|.
name|StarredChange
name|starredChange
init|=
operator|new
name|AccountResource
operator|.
name|StarredChange
argument_list|(
name|account
operator|.
name|getUser
argument_list|()
argument_list|,
name|rsrc
argument_list|)
decl_stmt|;
name|starredChangesDelete
operator|.
name|apply
argument_list|(
name|starredChange
argument_list|,
operator|new
name|StarredChanges
operator|.
name|EmptyInput
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Cannot unstar change"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|addEmail (EmailInput input)
specifier|public
name|void
name|addEmail
parameter_list|(
name|EmailInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
name|AccountResource
operator|.
name|Email
name|rsrc
init|=
operator|new
name|AccountResource
operator|.
name|Email
argument_list|(
name|account
operator|.
name|getUser
argument_list|()
argument_list|,
name|input
operator|.
name|email
argument_list|)
decl_stmt|;
try|try
block|{
name|createEmailFactory
operator|.
name|create
argument_list|(
name|input
operator|.
name|email
argument_list|)
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EmailException
decl||
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot add email"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|listGpgKeys ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|GpgKeyInfo
argument_list|>
name|listGpgKeys
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|gpgApiAdapter
operator|.
name|listGpgKeys
argument_list|(
name|account
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|GpgException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot list GPG keys"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|putGpgKeys (List<String> add, List<String> delete)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|GpgKeyInfo
argument_list|>
name|putGpgKeys
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|add
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|delete
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|gpgApiAdapter
operator|.
name|putGpgKeys
argument_list|(
name|account
argument_list|,
name|add
argument_list|,
name|delete
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|GpgException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot add GPG key"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|gpgKey (String id)
specifier|public
name|GpgKeyApi
name|gpgKey
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
name|gpgApiAdapter
operator|.
name|gpgKey
argument_list|(
name|account
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|GpgException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot get PGP key"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

