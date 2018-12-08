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
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|Strings
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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

begin_class
DECL|class|GerritServerIdProvider
specifier|public
class|class
name|GerritServerIdProvider
implements|implements
name|Provider
argument_list|<
name|String
argument_list|>
block|{
DECL|field|SECTION
specifier|public
specifier|static
specifier|final
name|String
name|SECTION
init|=
literal|"gerrit"
decl_stmt|;
DECL|field|KEY
specifier|public
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"serverId"
decl_stmt|;
DECL|method|generate ()
specifier|public
specifier|static
name|String
name|generate
parameter_list|()
block|{
return|return
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritServerIdProvider (@erritServerConfig Config cfg, SitePaths sitePaths)
specifier|public
name|GerritServerIdProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|String
name|origId
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|SECTION
argument_list|,
literal|null
argument_list|,
name|KEY
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|origId
argument_list|)
condition|)
block|{
name|id
operator|=
name|origId
expr_stmt|;
return|return;
block|}
comment|// We're not generally supposed to do work in provider constructors, but this is a bit of a
comment|// special case because we really need to have the ID available by the time the dbInjector
comment|// is created. Fortunately, it's not much work, and it happens once.
name|id
operator|=
name|generate
argument_list|()
expr_stmt|;
name|Config
name|newCfg
init|=
name|readGerritConfig
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
name|newCfg
operator|.
name|setString
argument_list|(
name|SECTION
argument_list|,
literal|null
argument_list|,
name|KEY
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
argument_list|,
name|newCfg
operator|.
name|toText
argument_list|()
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|readGerritConfig (SitePaths sitePaths)
specifier|private
specifier|static
name|Config
name|readGerritConfig
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
comment|// Reread gerrit.config from disk before writing. We can't just use
comment|// cfg.toText(), as the @GerritServerConfig only has gerrit.config as a
comment|// fallback.
name|FileBasedConfig
name|cfg
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cfg
operator|.
name|getFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
operator|new
name|Config
argument_list|()
return|;
block|}
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
return|return
name|cfg
return|;
block|}
block|}
end_class

end_unit

