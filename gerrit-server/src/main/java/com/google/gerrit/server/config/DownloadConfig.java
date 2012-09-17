begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
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
name|reviewdb
operator|.
name|client
operator|.
name|SystemConfig
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
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_comment
comment|/** Download protocol from {@code gerrit.config}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|DownloadConfig
specifier|public
class|class
name|DownloadConfig
block|{
DECL|field|downloadSchemes
specifier|private
specifier|final
name|Set
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
decl_stmt|;
DECL|field|downloadCommands
specifier|private
specifier|final
name|Set
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
decl_stmt|;
annotation|@
name|Inject
DECL|method|DownloadConfig (@erritServerConfig final Config cfg, final SystemConfig s)
name|DownloadConfig
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|,
specifier|final
name|SystemConfig
name|s
parameter_list|)
block|{
name|List
argument_list|<
name|DownloadScheme
argument_list|>
name|allSchemes
init|=
name|ConfigUtil
operator|.
name|getEnumList
argument_list|(
name|cfg
argument_list|,
literal|"download"
argument_list|,
literal|null
argument_list|,
literal|"scheme"
argument_list|,
name|DownloadScheme
operator|.
name|DEFAULT_DOWNLOADS
argument_list|)
decl_stmt|;
name|downloadSchemes
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|DownloadScheme
argument_list|>
argument_list|(
name|allSchemes
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DownloadCommand
argument_list|>
name|allCommands
init|=
name|ConfigUtil
operator|.
name|getEnumList
argument_list|(
name|cfg
argument_list|,
literal|"download"
argument_list|,
literal|null
argument_list|,
literal|"command"
argument_list|,
name|DownloadCommand
operator|.
name|DEFAULT_DOWNLOADS
argument_list|)
decl_stmt|;
name|downloadCommands
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|DownloadCommand
argument_list|>
argument_list|(
name|allCommands
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Scheme used to download. */
DECL|method|getDownloadSchemes ()
specifier|public
name|Set
argument_list|<
name|DownloadScheme
argument_list|>
name|getDownloadSchemes
parameter_list|()
block|{
return|return
name|downloadSchemes
return|;
block|}
comment|/** Command used to download. */
DECL|method|getDownloadCommands ()
specifier|public
name|Set
argument_list|<
name|DownloadCommand
argument_list|>
name|getDownloadCommands
parameter_list|()
block|{
return|return
name|downloadCommands
return|;
block|}
block|}
end_class

end_unit

