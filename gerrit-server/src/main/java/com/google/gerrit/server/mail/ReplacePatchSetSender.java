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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|reviewdb
operator|.
name|Change
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
name|ssh
operator|.
name|SshInfo
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|HostKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Iterator
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
name|Set
import|;
end_import

begin_comment
comment|/** Send notice of new patch sets for reviewers. */
end_comment

begin_class
DECL|class|ReplacePatchSetSender
specifier|public
class|class
name|ReplacePatchSetSender
extends|extends
name|ReplyToChangeSender
block|{
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (Change change)
specifier|public
name|ReplacePatchSetSender
name|create
parameter_list|(
name|Change
name|change
parameter_list|)
function_decl|;
block|}
DECL|field|reviewers
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
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
DECL|field|extraCC
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|extraCC
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
DECL|field|sshInfo
specifier|private
specifier|final
name|SshInfo
name|sshInfo
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReplacePatchSetSender (EmailArguments ea, SshInfo si, @Assisted Change c)
specifier|public
name|ReplacePatchSetSender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|SshInfo
name|si
parameter_list|,
annotation|@
name|Assisted
name|Change
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|c
argument_list|,
literal|"newpatchset"
argument_list|)
expr_stmt|;
name|sshInfo
operator|=
name|si
expr_stmt|;
block|}
DECL|method|addReviewers (final Collection<Account.Id> cc)
specifier|public
name|void
name|addReviewers
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|cc
parameter_list|)
block|{
name|reviewers
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
DECL|method|addExtraCC (final Collection<Account.Id> cc)
specifier|public
name|void
name|addExtraCC
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|cc
parameter_list|)
block|{
name|extraCC
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
if|if
condition|(
name|fromId
operator|!=
literal|null
condition|)
block|{
comment|// Don't call yourself a reviewer of your own patch set.
comment|//
name|reviewers
operator|.
name|remove
argument_list|(
name|fromId
argument_list|)
expr_stmt|;
block|}
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|reviewers
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|extraCC
argument_list|)
expr_stmt|;
name|rcptToAuthors
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|formatChange ()
specifier|protected
name|void
name|formatChange
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|velocifyFile
argument_list|(
literal|"ReplacePatchSet.vm"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getReviewerNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getReviewerNames
parameter_list|()
block|{
if|if
condition|(
name|reviewers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|id
range|:
name|reviewers
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|getNameFor
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
DECL|method|getSshHost ()
specifier|public
name|String
name|getSshHost
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|HostKey
argument_list|>
name|hostKeys
init|=
name|sshInfo
operator|.
name|getHostKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|hostKeys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
name|host
init|=
name|hostKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|startsWith
argument_list|(
literal|"*:"
argument_list|)
condition|)
block|{
return|return
name|getGerritHost
argument_list|()
operator|+
name|host
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
name|host
return|;
block|}
block|}
end_class

end_unit

