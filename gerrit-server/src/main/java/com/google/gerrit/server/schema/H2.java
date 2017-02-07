begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
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
name|inject
operator|.
name|Inject
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
name|Path
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

begin_class
DECL|class|H2
class|class
name|H2
extends|extends
name|BaseDataSourceType
block|{
DECL|field|cfg
specifier|protected
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
annotation|@
name|Inject
DECL|method|H2 (final SitePaths site, @GerritServerConfig final Config cfg)
name|H2
parameter_list|(
specifier|final
name|SitePaths
name|site
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
name|super
argument_list|(
literal|"org.h2.Driver"
argument_list|)
expr_stmt|;
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
name|String
name|database
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"database"
argument_list|,
literal|null
argument_list|,
literal|"database"
argument_list|)
decl_stmt|;
if|if
condition|(
name|database
operator|==
literal|null
operator|||
name|database
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|database
operator|=
literal|"db/ReviewDB"
expr_stmt|;
block|}
return|return
name|appendUrlOptions
argument_list|(
name|cfg
argument_list|,
name|createUrl
argument_list|(
name|site
operator|.
name|resolve
argument_list|(
name|database
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createUrl (Path path)
specifier|public
specifier|static
name|String
name|createUrl
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"jdbc:h2:"
argument_list|)
operator|.
name|append
argument_list|(
name|path
operator|.
name|toUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|appendUrlOptions (Config cfg, String url)
specifier|public
specifier|static
name|String
name|appendUrlOptions
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|long
name|h2CacheSize
init|=
name|cfg
operator|.
name|getLong
argument_list|(
literal|"database"
argument_list|,
literal|"h2"
argument_list|,
literal|"cacheSize"
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|boolean
name|h2AutoServer
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"database"
argument_list|,
literal|"h2"
argument_list|,
literal|"autoServer"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|StringBuilder
name|urlBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|h2CacheSize
operator|>=
literal|0
condition|)
block|{
comment|// H2 CACHE_SIZE is always given in KB
name|urlBuilder
operator|.
name|append
argument_list|(
literal|";CACHE_SIZE="
argument_list|)
operator|.
name|append
argument_list|(
name|h2CacheSize
operator|/
literal|1024
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|h2AutoServer
condition|)
block|{
name|urlBuilder
operator|.
name|append
argument_list|(
literal|";AUTO_SERVER=TRUE"
argument_list|)
expr_stmt|;
block|}
return|return
name|urlBuilder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

