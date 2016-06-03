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
DECL|package|com.google.gerrit.server.api.config
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
name|common
operator|.
name|Version
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
name|config
operator|.
name|Server
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
name|server
operator|.
name|config
operator|.
name|ConfigResource
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
name|GetDiffPreferences
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
name|SetDiffPreferences
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
name|Singleton
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
DECL|class|ServerImpl
specifier|public
class|class
name|ServerImpl
implements|implements
name|Server
block|{
DECL|field|getDiffPreferences
specifier|private
specifier|final
name|GetDiffPreferences
name|getDiffPreferences
decl_stmt|;
DECL|field|setDiffPreferences
specifier|private
specifier|final
name|SetDiffPreferences
name|setDiffPreferences
decl_stmt|;
annotation|@
name|Inject
DECL|method|ServerImpl (GetDiffPreferences getDiffPreferences, SetDiffPreferences setDiffPreferences)
name|ServerImpl
parameter_list|(
name|GetDiffPreferences
name|getDiffPreferences
parameter_list|,
name|SetDiffPreferences
name|setDiffPreferences
parameter_list|)
block|{
name|this
operator|.
name|getDiffPreferences
operator|=
name|getDiffPreferences
expr_stmt|;
name|this
operator|.
name|setDiffPreferences
operator|=
name|setDiffPreferences
expr_stmt|;
block|}
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
return|return
name|Version
operator|.
name|getVersion
argument_list|()
return|;
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
try|try
block|{
return|return
name|getDiffPreferences
operator|.
name|apply
argument_list|(
operator|new
name|ConfigResource
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot get default diff preferences"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
try|try
block|{
return|return
name|setDiffPreferences
operator|.
name|apply
argument_list|(
operator|new
name|ConfigResource
argument_list|()
argument_list|,
name|in
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot set default diff preferences"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

