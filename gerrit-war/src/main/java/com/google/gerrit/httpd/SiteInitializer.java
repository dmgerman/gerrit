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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|pgm
operator|.
name|BaseInit
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

begin_class
DECL|class|SiteInitializer
specifier|public
specifier|final
class|class
name|SiteInitializer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SiteInitializer
operator|.
name|class
argument_list|)
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
DECL|method|SiteInitializer (String sitePath, String initPath)
name|SiteInitializer
parameter_list|(
name|String
name|sitePath
parameter_list|,
name|String
name|initPath
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
name|File
name|site
init|=
operator|new
name|File
argument_list|(
name|sitePath
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Initializing site at %s"
argument_list|,
name|site
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
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
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
return|return;
block|}
name|Connection
name|conn
init|=
name|connectToDb
argument_list|()
decl_stmt|;
try|try
block|{
name|File
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
operator|new
name|File
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
name|LOG
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Initializing site at %s"
argument_list|,
name|site
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
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
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Site init failed"
argument_list|,
name|e
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
name|File
name|getSiteFromReviewDb
parameter_list|(
name|Connection
name|conn
parameter_list|)
block|{
try|try
block|{
name|Statement
name|stmt
init|=
name|conn
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|ResultSet
name|rs
init|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT site_path FROM system_config"
argument_list|)
decl_stmt|;
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
return|return
operator|new
name|File
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
finally|finally
block|{
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
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
block|}
block|}
end_class

end_unit

