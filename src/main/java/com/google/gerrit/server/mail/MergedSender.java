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
name|data
operator|.
name|ApprovalType
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
name|data
operator|.
name|ApprovalTypes
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
name|ApprovalCategory
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
name|ApprovalCategoryValue
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
name|Branch
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
name|PatchSetApproval
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
name|project
operator|.
name|ProjectState
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Send notice about a change successfully merged. */
end_comment

begin_class
DECL|class|MergedSender
specifier|public
class|class
name|MergedSender
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
name|MergedSender
name|create
parameter_list|(
name|Change
name|change
parameter_list|)
function_decl|;
block|}
DECL|field|dest
specifier|private
name|Branch
operator|.
name|NameKey
name|dest
decl_stmt|;
annotation|@
name|Inject
DECL|field|approvalTypes
specifier|private
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
annotation|@
name|Inject
DECL|method|MergedSender (@ssisted Change c)
specifier|public
name|MergedSender
parameter_list|(
annotation|@
name|Assisted
name|Change
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|,
literal|"merged"
argument_list|)
expr_stmt|;
name|dest
operator|=
name|c
operator|.
name|getDest
argument_list|()
expr_stmt|;
block|}
DECL|method|setDest (final Branch.NameKey key)
specifier|public
name|void
name|setDest
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|key
parameter_list|)
block|{
name|dest
operator|=
name|key
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|ccAllApprovals
argument_list|()
expr_stmt|;
name|bccStarredBy
argument_list|()
expr_stmt|;
name|bccWatchesNotifyAllComments
argument_list|()
expr_stmt|;
name|bccWatchesNotifySubmittedChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format ()
specifier|protected
name|void
name|format
parameter_list|()
block|{
name|appendText
argument_list|(
literal|"Change "
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|patchSetInfo
operator|!=
literal|null
operator|&&
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|!=
literal|null
operator|&&
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|appendText
argument_list|(
literal|" by "
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|appendText
argument_list|(
literal|" submitted to "
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|dest
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|":\n\n"
argument_list|)
expr_stmt|;
name|formatChangeDetail
argument_list|()
expr_stmt|;
name|formatApprovals
argument_list|()
expr_stmt|;
block|}
DECL|method|formatApprovals ()
specifier|private
name|void
name|formatApprovals
parameter_list|()
block|{
if|if
condition|(
name|db
operator|!=
literal|null
operator|&&
name|patchSet
operator|!=
literal|null
condition|)
block|{
try|try
block|{
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
name|pos
init|=
operator|new
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
name|neg
init|=
operator|new
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|ca
range|:
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|ca
operator|.
name|getValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|insert
argument_list|(
name|pos
argument_list|,
name|ca
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ca
operator|.
name|getValue
argument_list|()
operator|<
literal|0
condition|)
block|{
name|insert
argument_list|(
name|neg
argument_list|,
name|ca
argument_list|)
expr_stmt|;
block|}
block|}
name|format
argument_list|(
literal|"Approvals"
argument_list|,
name|pos
argument_list|)
expr_stmt|;
name|format
argument_list|(
literal|"Objections"
argument_list|,
name|neg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
comment|// Don't list the approvals
block|}
block|}
block|}
DECL|method|format (final String type, final Map<Account.Id, Map<ApprovalCategory.Id, PatchSetApproval>> list)
specifier|private
name|void
name|format
parameter_list|(
specifier|final
name|String
name|type
parameter_list|,
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
name|list
parameter_list|)
block|{
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|appendText
argument_list|(
name|type
operator|+
literal|":\n"
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
name|ent
range|:
name|list
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|l
init|=
name|ent
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|appendText
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|getNameFor
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|ApprovalType
name|at
range|:
name|approvalTypes
operator|.
name|getApprovalTypes
argument_list|()
control|)
block|{
specifier|final
name|PatchSetApproval
name|ca
init|=
name|l
operator|.
name|get
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ca
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|appendText
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ApprovalCategoryValue
name|v
init|=
name|at
operator|.
name|getValue
argument_list|(
name|ca
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|appendText
argument_list|(
name|v
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendText
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
if|if
condition|(
name|ca
operator|.
name|getValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|appendText
argument_list|(
literal|"+"
argument_list|)
expr_stmt|;
block|}
name|appendText
argument_list|(
literal|""
operator|+
name|ca
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|insert ( final Map<Account.Id, Map<ApprovalCategory.Id, PatchSetApproval>> list, final PatchSetApproval ca)
specifier|private
name|void
name|insert
parameter_list|(
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
name|list
parameter_list|,
specifier|final
name|PatchSetApproval
name|ca
parameter_list|)
block|{
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|m
init|=
name|list
operator|.
name|get
argument_list|(
name|ca
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|()
expr_stmt|;
name|list
operator|.
name|put
argument_list|(
name|ca
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|ca
operator|.
name|getCategoryId
argument_list|()
argument_list|,
name|ca
argument_list|)
expr_stmt|;
block|}
DECL|method|bccWatchesNotifySubmittedChanges ()
specifier|private
name|void
name|bccWatchesNotifySubmittedChanges
parameter_list|()
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
name|ProjectState
name|ps
init|=
name|getProjectState
argument_list|()
decl_stmt|;
if|if
condition|(
name|ps
operator|!=
literal|null
condition|)
block|{
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
name|notifySubmittedChanges
argument_list|(
name|ps
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
control|)
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

