begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|common
operator|.
name|FetchInfo
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
name|config
operator|.
name|DownloadCommand
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
name|config
operator|.
name|DownloadScheme
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
name|registration
operator|.
name|Extension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/** Populates the {@link FetchInfo} which is serialized to JSON afterwards. */
end_comment

begin_class
DECL|class|DownloadCommandsJson
specifier|public
class|class
name|DownloadCommandsJson
block|{
DECL|method|DownloadCommandsJson ()
specifier|private
name|DownloadCommandsJson
parameter_list|()
block|{}
comment|/**    * Populates the provided {@link FetchInfo} by calling all {@link DownloadCommand} extensions.    * Will mutate {@link FetchInfo#commands}.    */
DECL|method|populateFetchMap ( DownloadScheme scheme, DynamicMap<DownloadCommand> commands, String projectName, String refName, FetchInfo fetchInfo)
specifier|public
specifier|static
name|void
name|populateFetchMap
parameter_list|(
name|DownloadScheme
name|scheme
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadCommand
argument_list|>
name|commands
parameter_list|,
name|String
name|projectName
parameter_list|,
name|String
name|refName
parameter_list|,
name|FetchInfo
name|fetchInfo
parameter_list|)
block|{
for|for
control|(
name|Extension
argument_list|<
name|DownloadCommand
argument_list|>
name|ext
range|:
name|commands
control|)
block|{
name|String
name|commandName
init|=
name|ext
operator|.
name|getExportName
argument_list|()
decl_stmt|;
name|DownloadCommand
name|command
init|=
name|ext
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|c
init|=
name|command
operator|.
name|getCommand
argument_list|(
name|scheme
argument_list|,
name|projectName
argument_list|,
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|fetchInfo
operator|.
name|commands
operator|==
literal|null
condition|)
block|{
name|fetchInfo
operator|.
name|commands
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|fetchInfo
operator|.
name|commands
operator|.
name|put
argument_list|(
name|commandName
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

