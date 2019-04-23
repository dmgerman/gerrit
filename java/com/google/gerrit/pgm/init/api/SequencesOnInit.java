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
DECL|package|com.google.gerrit.pgm.init.api
package|package
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
name|api
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
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|RepoSequence
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
name|notedb
operator|.
name|Sequences
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

begin_class
annotation|@
name|Singleton
DECL|class|SequencesOnInit
specifier|public
class|class
name|SequencesOnInit
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
name|AllUsersNameOnInitProvider
name|allUsersName
decl_stmt|;
annotation|@
name|Inject
DECL|method|SequencesOnInit (GitRepositoryManagerOnInit repoManager, AllUsersNameOnInitProvider allUsersName)
name|SequencesOnInit
parameter_list|(
name|GitRepositoryManagerOnInit
name|repoManager
parameter_list|,
name|AllUsersNameOnInitProvider
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
DECL|method|nextAccountId ()
specifier|public
name|int
name|nextAccountId
parameter_list|()
block|{
name|RepoSequence
name|accountSeq
init|=
operator|new
name|RepoSequence
argument_list|(
name|repoManager
argument_list|,
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|Project
operator|.
name|nameKey
argument_list|(
name|allUsersName
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|Sequences
operator|.
name|NAME_ACCOUNTS
argument_list|,
parameter_list|()
lambda|->
name|Sequences
operator|.
name|FIRST_ACCOUNT_ID
argument_list|,
literal|1
argument_list|)
decl_stmt|;
return|return
name|accountSeq
operator|.
name|next
argument_list|()
return|;
block|}
block|}
end_class

end_unit

