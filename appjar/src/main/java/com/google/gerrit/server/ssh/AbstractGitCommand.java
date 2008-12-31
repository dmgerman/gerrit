begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|Account
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
name|client
operator|.
name|reviewdb
operator|.
name|Project
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
name|git
operator|.
name|InvalidRepositoryException
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
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
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

begin_class
DECL|class|AbstractGitCommand
specifier|abstract
class|class
name|AbstractGitCommand
extends|extends
name|AbstractCommand
block|{
DECL|field|repo
specifier|protected
name|Repository
name|repo
decl_stmt|;
DECL|field|proj
specifier|protected
name|Project
name|proj
decl_stmt|;
DECL|field|userAccount
specifier|protected
name|Account
name|userAccount
decl_stmt|;
DECL|field|db
specifier|protected
name|ReviewDb
name|db
decl_stmt|;
DECL|method|isGerrit ()
specifier|protected
name|boolean
name|isGerrit
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"gerrit-"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|run (final String[] args)
specifier|protected
specifier|final
name|void
name|run
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
throws|,
name|Failure
block|{
specifier|final
name|String
name|reqName
init|=
name|parseCommandLine
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|String
name|projectName
init|=
name|reqName
decl_stmt|;
if|if
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the trailing ".git" suffix, which we never keep
comment|// in our database, but clients might mistakenly provide anyway.
comment|//
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|projectName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the leading "/" if supplied by an absolute path.
comment|// We don't have a file system hierarchy, just a flat namespace in
comment|// the database's Project entities. We never encode these with a
comment|// leading '/' but users might accidentally include them in Git URLs.
comment|//
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|db
operator|=
name|openReviewDb
argument_list|()
expr_stmt|;
try|try
block|{
try|try
block|{
name|userAccount
operator|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: cannot query user database"
argument_list|)
throw|;
block|}
try|try
block|{
name|proj
operator|=
name|db
operator|.
name|projects
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: cannot query project database"
argument_list|)
throw|;
block|}
if|if
condition|(
name|proj
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: '"
operator|+
name|reqName
operator|+
literal|"': not a Gerrit project"
argument_list|)
throw|;
block|}
try|try
block|{
name|repo
operator|=
name|getRepositoryCache
argument_list|()
operator|.
name|get
argument_list|(
name|proj
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: '"
operator|+
name|reqName
operator|+
literal|"': not a git archive"
argument_list|)
throw|;
block|}
name|runImpl
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|closeDb
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|closeDb ()
specifier|protected
name|void
name|closeDb
parameter_list|()
block|{
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
name|db
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|runImpl ()
specifier|protected
specifier|abstract
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
function_decl|;
DECL|method|parseCommandLine (String[] args)
specifier|protected
specifier|abstract
name|String
name|parseCommandLine
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Failure
function_decl|;
block|}
end_class

end_unit

