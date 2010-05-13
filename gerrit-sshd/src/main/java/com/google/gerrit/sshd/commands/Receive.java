begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|server
operator|.
name|IdentifiedUser
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
name|ReceiveCommits
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
name|sshd
operator|.
name|AbstractGitCommand
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
name|sshd
operator|.
name|TransferConfig
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
name|transport
operator|.
name|ReceivePack
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|io
operator|.
name|InterruptedIOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Receives change upload over SSH using the Git receive-pack protocol. */
end_comment

begin_class
DECL|class|Receive
specifier|final
class|class
name|Receive
extends|extends
name|AbstractGitCommand
block|{
annotation|@
name|Inject
DECL|field|factory
specifier|private
name|ReceiveCommits
operator|.
name|Factory
name|factory
decl_stmt|;
annotation|@
name|Inject
DECL|field|currentUser
specifier|private
name|IdentifiedUser
name|currentUser
decl_stmt|;
annotation|@
name|Inject
DECL|field|identifiedUserFactory
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|config
specifier|private
name|TransferConfig
name|config
decl_stmt|;
DECL|field|reviewerId
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewerId
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ccId
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ccId
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--reviewer"
argument_list|,
name|aliases
operator|=
block|{
literal|"--re"
block|}
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"EMAIL"
argument_list|,
name|usage
operator|=
literal|"request reviewer for change(s)"
argument_list|)
DECL|method|addReviewer (final Account.Id id)
name|void
name|addReviewer
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|reviewerId
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--cc"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"EMAIL"
argument_list|,
name|usage
operator|=
literal|"CC user on change(s)"
argument_list|)
DECL|method|addCC (final Account.Id id)
name|void
name|addCC
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|ccId
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|runImpl ()
specifier|protected
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
block|{
specifier|final
name|ReceiveCommits
name|receive
init|=
name|factory
operator|.
name|create
argument_list|(
name|projectControl
argument_list|,
name|repo
argument_list|)
decl_stmt|;
name|ReceiveCommits
operator|.
name|Capable
name|r
init|=
name|receive
operator|.
name|canUpload
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|!=
name|ReceiveCommits
operator|.
name|Capable
operator|.
name|OK
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
literal|"\nfatal: "
operator|+
name|r
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|verifyProjectVisible
argument_list|(
literal|"reviewer"
argument_list|,
name|reviewerId
argument_list|)
expr_stmt|;
name|verifyProjectVisible
argument_list|(
literal|"CC"
argument_list|,
name|ccId
argument_list|)
expr_stmt|;
name|receive
operator|.
name|addReviewers
argument_list|(
name|reviewerId
argument_list|)
expr_stmt|;
name|receive
operator|.
name|addExtraCC
argument_list|(
name|ccId
argument_list|)
expr_stmt|;
specifier|final
name|ReceivePack
name|rp
init|=
name|receive
operator|.
name|getReceivePack
argument_list|()
decl_stmt|;
name|rp
operator|.
name|setRefLogIdent
argument_list|(
name|currentUser
operator|.
name|newRefLogIdent
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|rp
operator|.
name|receive
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedIOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|128
argument_list|,
literal|"fatal: client IO read/write timeout"
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
DECL|method|verifyProjectVisible (final String type, final Set<Account.Id> who)
specifier|private
name|void
name|verifyProjectVisible
parameter_list|(
specifier|final
name|String
name|type
parameter_list|,
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|who
parameter_list|)
throws|throws
name|UnloggedFailure
block|{
for|for
control|(
specifier|final
name|Account
operator|.
name|Id
name|id
range|:
name|who
control|)
block|{
specifier|final
name|IdentifiedUser
name|user
init|=
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projectControl
operator|.
name|forUser
argument_list|(
name|user
argument_list|)
operator|.
name|isVisible
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
name|type
operator|+
literal|" "
operator|+
name|user
operator|.
name|getAccount
argument_list|()
operator|.
name|getFullName
argument_list|()
operator|+
literal|" cannot access the project"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

