begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.config
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
name|config
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
name|DiffPreferencesInfo
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
name|ServerInfo
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
operator|.
name|TopMenu
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

begin_interface
DECL|interface|Server
specifier|public
interface|interface
name|Server
block|{
comment|/** @return Version of server. */
DECL|method|getVersion ()
name|String
name|getVersion
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|getInfo ()
name|ServerInfo
name|getInfo
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|getDefaultPreferences ()
name|GeneralPreferencesInfo
name|getDefaultPreferences
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|setDefaultPreferences (GeneralPreferencesInfo in)
name|GeneralPreferencesInfo
name|setDefaultPreferences
parameter_list|(
name|GeneralPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|getDefaultDiffPreferences ()
name|DiffPreferencesInfo
name|getDefaultDiffPreferences
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|setDefaultDiffPreferences (DiffPreferencesInfo in)
name|DiffPreferencesInfo
name|setDefaultDiffPreferences
parameter_list|(
name|DiffPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|getDefaultEditPreferences ()
name|EditPreferencesInfo
name|getDefaultEditPreferences
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|setDefaultEditPreferences (EditPreferencesInfo in)
name|EditPreferencesInfo
name|setDefaultEditPreferences
parameter_list|(
name|EditPreferencesInfo
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|checkConsistency (ConsistencyCheckInput in)
name|ConsistencyCheckInfo
name|checkConsistency
parameter_list|(
name|ConsistencyCheckInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|topMenus ()
name|List
argument_list|<
name|TopMenu
operator|.
name|MenuEntry
argument_list|>
name|topMenus
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|Server
block|{
annotation|@
name|Override
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
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
DECL|method|getInfo ()
specifier|public
name|ServerInfo
name|getInfo
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
DECL|method|getDefaultPreferences ()
specifier|public
name|GeneralPreferencesInfo
name|getDefaultPreferences
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
DECL|method|setDefaultPreferences (GeneralPreferencesInfo in)
specifier|public
name|GeneralPreferencesInfo
name|setDefaultPreferences
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
DECL|method|getDefaultDiffPreferences ()
specifier|public
name|DiffPreferencesInfo
name|getDefaultDiffPreferences
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
DECL|method|setDefaultDiffPreferences (DiffPreferencesInfo in)
specifier|public
name|DiffPreferencesInfo
name|setDefaultDiffPreferences
parameter_list|(
name|DiffPreferencesInfo
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
DECL|method|getDefaultEditPreferences ()
specifier|public
name|EditPreferencesInfo
name|getDefaultEditPreferences
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
DECL|method|setDefaultEditPreferences (EditPreferencesInfo in)
specifier|public
name|EditPreferencesInfo
name|setDefaultEditPreferences
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
DECL|method|checkConsistency (ConsistencyCheckInput in)
specifier|public
name|ConsistencyCheckInfo
name|checkConsistency
parameter_list|(
name|ConsistencyCheckInput
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
DECL|method|topMenus ()
specifier|public
name|List
argument_list|<
name|TopMenu
operator|.
name|MenuEntry
argument_list|>
name|topMenus
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

