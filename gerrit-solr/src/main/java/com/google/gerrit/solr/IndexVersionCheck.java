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
DECL|package|com.google.gerrit.solr
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|solr
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
name|collect
operator|.
name|ImmutableMap
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
name|events
operator|.
name|LifecycleListener
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
name|SitePaths
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
name|index
operator|.
name|ChangeSchemas
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|IndexVersionCheck
class|class
name|IndexVersionCheck
implements|implements
name|LifecycleListener
block|{
DECL|field|SCHEMA_VERSIONS
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|SCHEMA_VERSIONS
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|SolrChangeIndex
operator|.
name|CHANGES_OPEN
argument_list|,
name|ChangeSchemas
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|,
name|SolrChangeIndex
operator|.
name|CHANGES_CLOSED
argument_list|,
name|ChangeSchemas
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|solrIndexConfig (SitePaths sitePaths)
specifier|public
specifier|static
name|File
name|solrIndexConfig
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|sitePaths
operator|.
name|index_dir
argument_list|,
literal|"gerrit_index.config"
argument_list|)
return|;
block|}
DECL|field|sitePaths
specifier|private
specifier|final
name|SitePaths
name|sitePaths
decl_stmt|;
annotation|@
name|Inject
DECL|method|IndexVersionCheck (SitePaths sitePaths)
name|IndexVersionCheck
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|this
operator|.
name|sitePaths
operator|=
name|sitePaths
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// TODO Query schema version from a special meta-document
name|File
name|file
init|=
name|solrIndexConfig
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
try|try
block|{
name|FileBasedConfig
name|cfg
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|file
argument_list|,
name|FS
operator|.
name|detect
argument_list|()
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|e
range|:
name|SCHEMA_VERSIONS
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|schemaVersion
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"schemaVersion"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemaVersion
operator|!=
name|e
operator|.
name|getValue
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"wrong index schema version for \"%s\": expected %d, found %d%s"
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|schemaVersion
argument_list|,
name|upgrade
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
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
literal|"unable to read "
operator|+
name|file
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
literal|"invalid config file "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|// Do nothing.
block|}
DECL|method|upgrade ()
specifier|private
specifier|final
name|String
name|upgrade
parameter_list|()
block|{
return|return
literal|"\nRun reindex to rebuild the index:\n"
operator|+
literal|"$ java -jar gerrit.war reindex -d "
operator|+
name|sitePaths
operator|.
name|site_path
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

