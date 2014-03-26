begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|AllUsersName
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
name|git
operator|.
name|GitRepositoryManager
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
name|errors
operator|.
name|RepositoryNotFoundException
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

begin_comment
comment|/** Creates the {@code All-Users} repository. */
end_comment

begin_class
DECL|class|AllUsersCreator
specifier|public
class|class
name|AllUsersCreator
block|{
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllUsersCreator (GitRepositoryManager mgr, AllUsersName allUsersName)
name|AllUsersCreator
parameter_list|(
name|GitRepositoryManager
name|mgr
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
name|this
operator|.
name|mgr
operator|=
name|mgr
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
block|}
DECL|method|create ()
specifier|public
name|void
name|create
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|Repository
name|git
init|=
literal|null
decl_stmt|;
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|createRepository
argument_list|(
name|allUsersName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|err
parameter_list|)
block|{
name|String
name|name
init|=
name|allUsersName
operator|.
name|get
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create repository "
operator|+
name|name
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|git
operator|!=
literal|null
condition|)
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

