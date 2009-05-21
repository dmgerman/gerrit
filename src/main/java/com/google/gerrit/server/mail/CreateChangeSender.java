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
name|AccountGroupMember
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
name|AccountProjectWatch
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

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
operator|.
name|RecipientType
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
DECL|method|CreateChangeSender (GerritServer gs, Change c)
specifier|public
name|CreateChangeSender
parameter_list|(
name|GerritServer
name|gs
parameter_list|,
name|Change
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|gs
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
name|MessagingException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|bccWatchers
argument_list|()
expr_stmt|;
block|}
DECL|method|bccWatchers ()
specifier|private
name|void
name|bccWatchers
parameter_list|()
throws|throws
name|MessagingException
block|{
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// BCC anyone else who has interest in this project's changes
comment|//
specifier|final
name|Project
name|project
init|=
name|getProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
comment|// Try to mark interested owners with a TO and not a BCC line.
comment|//
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|owners
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
for|for
control|(
name|AccountGroupMember
name|m
range|:
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|byGroup
argument_list|(
name|project
operator|.
name|getOwnerGroupId
argument_list|()
argument_list|)
control|)
block|{
name|owners
operator|.
name|add
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// BCC anyone who has interest in this project's changes
comment|//
for|for
control|(
name|AccountProjectWatch
name|w
range|:
name|db
operator|.
name|accountProjectWatches
argument_list|()
operator|.
name|notifyNewChanges
argument_list|(
name|project
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|owners
operator|.
name|contains
argument_list|(
name|w
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|w
operator|.
name|getAccountId
argument_list|()
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
name|w
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
block|}
block|}
block|}
end_class

end_unit

