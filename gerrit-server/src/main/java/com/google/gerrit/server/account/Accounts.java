begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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

begin_comment
comment|/** Class to access accounts. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|Accounts
specifier|public
class|class
name|Accounts
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
annotation|@
name|Inject
DECL|method|Accounts (GitRepositoryManager repoManager, AllUsersName allUsersName)
name|Accounts
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
block|}
comment|/**    * Returns the first n account IDs.    *    * @param n the number of account IDs that should be returned    * @return first n account IDs    */
DECL|method|firstNIds (int n)
specifier|public
name|List
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|firstNIds
parameter_list|(
name|int
name|n
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readUserRefs
argument_list|()
operator|.
name|sorted
argument_list|(
name|comparing
argument_list|(
name|id
lambda|->
name|id
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|limit
argument_list|(
name|n
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Checks if any account exists.    *    * @return {@code true} if at least one account exists, otherwise {@code false}    */
DECL|method|hasAnyAccount ()
specifier|public
name|boolean
name|hasAnyAccount
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
return|return
name|hasAnyAccount
argument_list|(
name|repo
argument_list|)
return|;
block|}
block|}
DECL|method|hasAnyAccount (Repository repo)
specifier|public
specifier|static
name|boolean
name|hasAnyAccount
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readUserRefs
argument_list|(
name|repo
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|isPresent
argument_list|()
return|;
block|}
DECL|method|readUserRefs ()
specifier|private
name|Stream
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|readUserRefs
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
return|return
name|readUserRefs
argument_list|(
name|repo
argument_list|)
return|;
block|}
block|}
DECL|method|readUserRefs (Repository repo)
specifier|private
specifier|static
name|Stream
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|readUserRefs
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|RefNames
operator|.
name|REFS_USERS
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|r
lambda|->
name|Account
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
return|;
block|}
block|}
end_class

end_unit

