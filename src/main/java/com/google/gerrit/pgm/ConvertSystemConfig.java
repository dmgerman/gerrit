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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|GerritServer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcSchema
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
name|RepositoryConfig
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
name|FileNotFoundException
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

begin_comment
comment|/** Export system_config from schema version 11 to gerrit.config file. */
end_comment

begin_class
DECL|class|ConvertSystemConfig
specifier|public
class|class
name|ConvertSystemConfig
block|{
DECL|method|main (final String[] argv)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|argv
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
throws|,
name|IOException
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|GerritServer
operator|.
name|createDatabase
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|Statement
name|s
init|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ResultSet
name|r
init|=
name|s
operator|.
name|executeQuery
argument_list|(
literal|"SELECT * FROM system_config"
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|next
argument_list|()
condition|)
block|{
specifier|final
name|File
name|sitePath
init|=
operator|new
name|File
argument_list|(
name|r
operator|.
name|getString
argument_list|(
literal|"site_path"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|File
name|file
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
name|RepositoryConfig
name|config
init|=
operator|new
name|RepositoryConfig
argument_list|(
literal|null
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|String
name|action
decl_stmt|;
try|try
block|{
name|config
operator|.
name|load
argument_list|()
expr_stmt|;
name|action
operator|=
literal|"Updated"
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|noFile
parameter_list|)
block|{
name|action
operator|=
literal|"Created"
expr_stmt|;
block|}
name|export
argument_list|(
name|config
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|config
operator|.
name|save
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|action
operator|+
literal|" "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|export (RepositoryConfig config, ResultSet rs)
specifier|private
specifier|static
name|void
name|export
parameter_list|(
name|RepositoryConfig
name|config
parameter_list|,
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|sshd
argument_list|(
name|config
argument_list|,
name|rs
argument_list|)
expr_stmt|;
block|}
DECL|method|sshd (RepositoryConfig config, ResultSet rs)
specifier|private
specifier|static
name|void
name|sshd
parameter_list|(
name|RepositoryConfig
name|config
parameter_list|,
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|int
name|port
init|=
name|rs
operator|.
name|getInt
argument_list|(
literal|"sshd_port"
argument_list|)
decl_stmt|;
if|if
condition|(
name|port
operator|==
literal|29418
condition|)
block|{
name|config
operator|.
name|unsetString
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"listenaddress"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|setString
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"listenaddress"
argument_list|,
literal|"*:"
operator|+
name|port
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copy (RepositoryConfig config, String section, String key, ResultSet rs, String colName)
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
name|RepositoryConfig
name|config
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|key
parameter_list|,
name|ResultSet
name|rs
parameter_list|,
name|String
name|colName
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|value
init|=
name|rs
operator|.
name|getString
argument_list|(
name|colName
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|setString
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|unsetString
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

