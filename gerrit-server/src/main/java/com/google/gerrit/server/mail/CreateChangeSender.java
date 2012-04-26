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
name|AccountProjectWatch
operator|.
name|NotifyType
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
name|config
operator|.
name|AnonymousCowardName
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
name|gwtorm
operator|.
name|server
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

begin_comment
comment|/** Notify interested parties of a brand new change. */
end_comment

begin_class
DECL|class|CreateChangeSender
specifier|public
class|class
name|CreateChangeSender
extends|extends
name|NewChangeSender
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CreateChangeSender
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (Change change)
specifier|public
name|CreateChangeSender
name|create
parameter_list|(
name|Change
name|change
parameter_list|)
function_decl|;
block|}
annotation|@
name|Inject
DECL|method|CreateChangeSender (EmailArguments ea, @AnonymousCowardName String anonymousCowardName, SshInfo sshInfo, @Assisted Change c)
specifier|public
name|CreateChangeSender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
name|SshInfo
name|sshInfo
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
name|anonymousCowardName
argument_list|,
name|sshInfo
argument_list|,
name|c
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
try|try
block|{
comment|// BCC anyone who has interest in this project's changes
comment|// Try to mark interested owners with a TO and not a BCC line.
comment|//
name|Watchers
name|matching
init|=
name|getWatches
argument_list|(
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|)
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|user
range|:
name|matching
operator|.
name|accounts
control|)
block|{
if|if
condition|(
name|isOwnerOfProjectOrBranch
argument_list|(
name|user
argument_list|)
condition|)
block|{
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|add
argument_list|(
name|RecipientType
operator|.
name|BCC
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Address
name|addr
range|:
name|matching
operator|.
name|emails
control|)
block|{
name|add
argument_list|(
name|RecipientType
operator|.
name|BCC
argument_list|,
name|addr
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
comment|// Just don't CC everyone. Better to send a partial message to those
comment|// we already have queued up then to fail deliver entirely to people
comment|// who have a lower interest in the change.
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot BCC watchers for new change"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isOwnerOfProjectOrBranch (Account.Id user)
specifier|private
name|boolean
name|isOwnerOfProjectOrBranch
parameter_list|(
name|Account
operator|.
name|Id
name|user
parameter_list|)
block|{
return|return
name|projectState
operator|!=
literal|null
operator|&&
name|change
operator|!=
literal|null
operator|&&
name|projectState
operator|.
name|controlFor
argument_list|(
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|user
argument_list|)
argument_list|)
operator|.
name|controlForRef
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
operator|.
name|isOwner
argument_list|()
return|;
block|}
block|}
end_class

end_unit

