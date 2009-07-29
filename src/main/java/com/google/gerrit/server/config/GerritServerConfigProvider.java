begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|ProvisionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileBasedConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|WindowCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|WindowCacheConfig
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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

begin_comment
comment|/** Provides {@link Config} annotated with {@link GerritServerConfig}. */
end_comment

begin_class
DECL|class|GerritServerConfigProvider
specifier|public
class|class
name|GerritServerConfigProvider
implements|implements
name|Provider
argument_list|<
name|Config
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GerritServerConfigProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sitePath
specifier|private
specifier|final
name|File
name|sitePath
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritServerConfigProvider (@itePath final File path)
name|GerritServerConfigProvider
parameter_list|(
annotation|@
name|SitePath
specifier|final
name|File
name|path
parameter_list|)
block|{
name|sitePath
operator|=
name|path
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Config
name|get
parameter_list|()
block|{
specifier|final
name|File
name|cfgPath
init|=
operator|new
name|File
argument_list|(
name|sitePath
argument_list|,
literal|"gerrit.config"
argument_list|)
decl_stmt|;
specifier|final
name|FileBasedConfig
name|cfg
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|cfgPath
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
name|log
operator|.
name|info
argument_list|(
literal|"No "
operator|+
name|cfgPath
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"; assuming defaults"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
try|try
block|{
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|WindowCacheConfig
name|c
init|=
operator|new
name|WindowCacheConfig
argument_list|()
decl_stmt|;
name|c
operator|.
name|fromConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|WindowCache
operator|.
name|reconfigure
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
block|}
end_class

end_unit

