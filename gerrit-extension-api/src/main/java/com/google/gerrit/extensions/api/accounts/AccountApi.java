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
DECL|package|com.google.gerrit.extensions.api.accounts
package|package
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
name|EditPreferencesInfo
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
name|GeneralPreferencesInfo
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
name|NotImplementedException
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

begin_interface
DECL|interface|AccountApi
specifier|public
interface|interface
name|AccountApi
block|{
DECL|method|get ()
name|AccountInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|getAvatarUrl (int size)
name|String
name|getAvatarUrl
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|getPreferences ()
name|GeneralPreferencesInfo
name|getPreferences
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|setPreferences (GeneralPreferencesInfo in)
name|GeneralPreferencesInfo
name|setPreferences
parameter_list|(
name|GeneralPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|getEditPreferences ()
name|EditPreferencesInfo
name|getEditPreferences
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|setEditPreferences (EditPreferencesInfo in)
name|EditPreferencesInfo
name|setEditPreferences
parameter_list|(
name|EditPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|starChange (String id)
name|void
name|starChange
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|unstarChange (String id)
name|void
name|unstarChange
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|addEmail (EmailInput input)
name|void
name|addEmail
parameter_list|(
name|EmailInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|listGpgKeys ()
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
function_decl|;
DECL|method|putGpgKeys (List<String> add, List<String> remove)
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
name|remove
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|gpgKey (String id)
name|GpgKeyApi
name|gpgKey
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility    * when adding new methods to the interface.    **/
DECL|class|NotImplemented
specifier|public
class|class
name|NotImplemented
implements|implements
name|AccountApi
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|AccountInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getAvatarUrl (int size)
specifier|public
name|String
name|getAvatarUrl
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getPreferences ()
specifier|public
name|GeneralPreferencesInfo
name|getPreferences
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setPreferences (GeneralPreferencesInfo in)
specifier|public
name|GeneralPreferencesInfo
name|setPreferences
parameter_list|(
name|GeneralPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getEditPreferences ()
specifier|public
name|EditPreferencesInfo
name|getEditPreferences
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setEditPreferences (EditPreferencesInfo in)
specifier|public
name|EditPreferencesInfo
name|setEditPreferences
parameter_list|(
name|EditPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|putGpgKeys (List<String> add, List<String> remove)
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
name|remove
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit

