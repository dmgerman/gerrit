begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util.git
package|package
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
name|git
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|util
operator|.
name|FS
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
name|util
operator|.
name|SystemReader
import|;
end_import

begin_class
DECL|class|DelegateSystemReader
specifier|public
class|class
name|DelegateSystemReader
extends|extends
name|SystemReader
block|{
DECL|field|delegate
specifier|private
specifier|final
name|SystemReader
name|delegate
decl_stmt|;
DECL|method|DelegateSystemReader (SystemReader delegate)
specifier|public
name|DelegateSystemReader
parameter_list|(
name|SystemReader
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getHostname
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getenv (String variable)
specifier|public
name|String
name|getenv
parameter_list|(
name|String
name|variable
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getenv
argument_list|(
name|variable
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getProperty (String key)
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|openUserConfig (Config parent, FS fs)
specifier|public
name|FileBasedConfig
name|openUserConfig
parameter_list|(
name|Config
name|parent
parameter_list|,
name|FS
name|fs
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|openUserConfig
argument_list|(
name|parent
argument_list|,
name|fs
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|openSystemConfig (Config parent, FS fs)
specifier|public
name|FileBasedConfig
name|openSystemConfig
parameter_list|(
name|Config
name|parent
parameter_list|,
name|FS
name|fs
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|openSystemConfig
argument_list|(
name|parent
argument_list|,
name|fs
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|openJGitConfig (Config parent, FS fs)
specifier|public
name|FileBasedConfig
name|openJGitConfig
parameter_list|(
name|Config
name|parent
parameter_list|,
name|FS
name|fs
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|openJGitConfig
argument_list|(
name|parent
argument_list|,
name|fs
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCurrentTime ()
specifier|public
name|long
name|getCurrentTime
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getCurrentTime
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTimezone (long when)
specifier|public
name|int
name|getTimezone
parameter_list|(
name|long
name|when
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getTimezone
argument_list|(
name|when
argument_list|)
return|;
block|}
block|}
end_class

end_unit

