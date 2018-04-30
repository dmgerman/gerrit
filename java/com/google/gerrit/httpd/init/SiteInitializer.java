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
DECL|package|com.google.gerrit.httpd.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|init
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
name|flogger
operator|.
name|FluentLogger
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
name|pgm
operator|.
name|init
operator|.
name|BaseInit
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
name|pgm
operator|.
name|init
operator|.
name|PluginsDistribution
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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

begin_class
DECL|class|SiteInitializer
specifier|public
specifier|final
class|class
name|SiteInitializer
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|sitePath
specifier|private
specifier|final
name|String
name|sitePath
decl_stmt|;
DECL|field|initPath
specifier|private
specifier|final
name|String
name|initPath
decl_stmt|;
DECL|field|pluginsDistribution
specifier|private
specifier|final
name|PluginsDistribution
name|pluginsDistribution
decl_stmt|;
DECL|field|pluginsToInstall
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|pluginsToInstall
decl_stmt|;
DECL|method|SiteInitializer ( String sitePath, String initPath, PluginsDistribution pluginsDistribution, List<String> pluginsToInstall)
name|SiteInitializer
parameter_list|(
name|String
name|sitePath
parameter_list|,
name|String
name|initPath
parameter_list|,
name|PluginsDistribution
name|pluginsDistribution
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|pluginsToInstall
parameter_list|)
block|{
name|this
operator|.
name|sitePath
operator|=
name|sitePath
expr_stmt|;
name|this
operator|.
name|initPath
operator|=
name|initPath
expr_stmt|;
name|this
operator|.
name|pluginsDistribution
operator|=
name|pluginsDistribution
expr_stmt|;
name|this
operator|.
name|pluginsToInstall
operator|=
name|pluginsToInstall
expr_stmt|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|sitePath
operator|!=
literal|null
condition|)
block|{
name|Path
name|site
init|=
name|Paths
operator|.
name|get
argument_list|(
name|sitePath
argument_list|)
decl_stmt|;
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Initializing site at %s"
argument_list|,
name|site
operator|.
name|toRealPath
argument_list|()
operator|.
name|normalize
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|BaseInit
argument_list|(
name|site
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|pluginsDistribution
argument_list|,
name|pluginsToInstall
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
return|return;
block|}
try|try
init|(
name|Connection
name|conn
init|=
name|connectToDb
argument_list|()
init|)
block|{
name|Path
name|site
init|=
name|getSiteFromReviewDb
argument_list|(
name|conn
argument_list|)
decl_stmt|;
if|if
condition|(
name|site
operator|==
literal|null
operator|&&
name|initPath
operator|!=
literal|null
condition|)
block|{
name|site
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|initPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|site
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Initializing site at %s"
argument_list|,
name|site
operator|.
name|toRealPath
argument_list|()
operator|.
name|normalize
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|BaseInit
argument_list|(
name|site
argument_list|,
operator|new
name|ReviewDbDataSourceProvider
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|pluginsDistribution
argument_list|,
name|pluginsToInstall
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Site init failed"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|connectToDb ()
specifier|private
name|Connection
name|connectToDb
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
operator|new
name|ReviewDbDataSourceProvider
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getConnection
argument_list|()
return|;
block|}
DECL|method|getSiteFromReviewDb (Connection conn)
specifier|private
name|Path
name|getSiteFromReviewDb
parameter_list|(
name|Connection
name|conn
parameter_list|)
block|{
try|try
init|(
name|Statement
name|stmt
init|=
name|conn
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT site_path FROM system_config"
argument_list|)
init|)
block|{
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

