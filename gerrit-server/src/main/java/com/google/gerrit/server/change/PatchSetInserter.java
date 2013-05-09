begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|common
operator|.
name|ChangeHooks
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
name|reviewdb
operator|.
name|client
operator|.
name|ChangeMessage
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
name|PatchSet
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
name|server
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
name|ApprovalsUtil
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
name|ChangeUtil
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
name|config
operator|.
name|TrackingFooters
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
name|patch
operator|.
name|PatchSetInfoFactory
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
name|InvalidChangeOperationException
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
name|NoSuchChangeException
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
name|RefControl
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
name|AtomicUpdate
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|FooterLine
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
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
DECL|class|PatchSetInserter
specifier|public
class|class
name|PatchSetInserter
block|{
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|trackingFooters
specifier|private
specifier|final
name|TrackingFooters
name|trackingFooters
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|PatchSetInserter (final ChangeHooks hooks, final TrackingFooters trackingFooters, final ReviewDb db, final PatchSetInfoFactory patchSetInfoFactory, final IdentifiedUser user)
specifier|public
name|PatchSetInserter
parameter_list|(
specifier|final
name|ChangeHooks
name|hooks
parameter_list|,
specifier|final
name|TrackingFooters
name|trackingFooters
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|trackingFooters
operator|=
name|trackingFooters
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|insertPatchSet (Change change, final PatchSet patchSet, final RevCommit commit, RefControl refControl, String message, boolean copyLabels)
specifier|public
name|Change
name|insertPatchSet
parameter_list|(
name|Change
name|change
parameter_list|,
specifier|final
name|PatchSet
name|patchSet
parameter_list|,
specifier|final
name|RevCommit
name|commit
parameter_list|,
name|RefControl
name|refControl
parameter_list|,
name|String
name|message
parameter_list|,
name|boolean
name|copyLabels
parameter_list|)
throws|throws
name|OrmException
throws|,
name|InvalidChangeOperationException
throws|,
name|NoSuchChangeException
block|{
specifier|final
name|PatchSet
operator|.
name|Id
name|currentPatchSetId
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
if|if
condition|(
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|<=
name|currentPatchSetId
operator|.
name|get
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
literal|"New Patch Set ID ["
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"] is not greater than the current Patch Set ID ["
operator|+
name|currentPatchSetId
operator|.
name|get
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
name|db
operator|.
name|changes
argument_list|()
operator|.
name|beginTransaction
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Change %s is closed"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|ChangeUtil
operator|.
name|insertAncestors
argument_list|(
name|db
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|commit
argument_list|)
expr_stmt|;
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|patchSet
argument_list|)
argument_list|)
expr_stmt|;
name|Change
name|updatedChange
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|atomicUpdate
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|AtomicUpdate
argument_list|<
name|Change
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Change
name|update
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isClosed
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|change
operator|.
name|currentPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|currentPatchSetId
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|!=
name|Change
operator|.
name|Status
operator|.
name|DRAFT
condition|)
block|{
name|change
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
name|change
operator|.
name|setLastSha1MergeTested
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|change
operator|.
name|setCurrentPatchSet
argument_list|(
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|commit
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeUtil
operator|.
name|updated
argument_list|(
name|change
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|updatedChange
operator|!=
literal|null
condition|)
block|{
name|change
operator|=
name|updatedChange
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ChangeModifiedException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Change %s was modified"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|copyLabels
condition|)
block|{
name|ApprovalsUtil
operator|.
name|copyLabels
argument_list|(
name|db
argument_list|,
name|refControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
argument_list|,
name|currentPatchSetId
argument_list|,
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|List
argument_list|<
name|FooterLine
argument_list|>
name|footerLines
init|=
name|commit
operator|.
name|getFooterLines
argument_list|()
decl_stmt|;
name|ChangeUtil
operator|.
name|updateTrackingIds
argument_list|(
name|db
argument_list|,
name|change
argument_list|,
name|trackingFooters
argument_list|,
name|footerLines
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
specifier|final
name|ChangeMessage
name|cmsg
init|=
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
argument_list|)
argument_list|)
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|cmsg
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|cmsg
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|commit
argument_list|()
expr_stmt|;
name|hooks
operator|.
name|doPatchsetCreatedHook
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
return|return
name|change
return|;
block|}
DECL|class|ChangeModifiedException
specifier|public
class|class
name|ChangeModifiedException
extends|extends
name|InvalidChangeOperationException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|ChangeModifiedException (String msg)
specifier|public
name|ChangeModifiedException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

