begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|changedetail
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
name|data
operator|.
name|ChangeDetail
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
name|common
operator|.
name|data
operator|.
name|ReviewResult
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
name|common
operator|.
name|errors
operator|.
name|NoSuchEntityException
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|server
operator|.
name|changedetail
operator|.
name|PublishDraft
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
name|PatchSetInfoNotAvailableException
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

begin_class
DECL|class|PublishAction
class|class
name|PublishAction
extends|extends
name|Handler
argument_list|<
name|ChangeDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (PatchSet.Id patchSetId)
name|PublishAction
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
function_decl|;
block|}
DECL|field|publishFactory
specifier|private
specifier|final
name|PublishDraft
operator|.
name|Factory
name|publishFactory
decl_stmt|;
DECL|field|changeDetailFactory
specifier|private
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
decl_stmt|;
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
annotation|@
name|Inject
DECL|method|PublishAction (final PublishDraft.Factory publishFactory, final ChangeDetailFactory.Factory changeDetailFactory, @Assisted final PatchSet.Id patchSetId)
name|PublishAction
parameter_list|(
specifier|final
name|PublishDraft
operator|.
name|Factory
name|publishFactory
parameter_list|,
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
block|{
name|this
operator|.
name|publishFactory
operator|=
name|publishFactory
expr_stmt|;
name|this
operator|.
name|changeDetailFactory
operator|=
name|changeDetailFactory
expr_stmt|;
name|this
operator|.
name|patchSetId
operator|=
name|patchSetId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ChangeDetail
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NoSuchEntityException
throws|,
name|IllegalStateException
throws|,
name|PatchSetInfoNotAvailableException
throws|,
name|NoSuchChangeException
block|{
specifier|final
name|ReviewResult
name|result
init|=
name|publishFactory
operator|.
name|create
argument_list|(
name|patchSetId
argument_list|)
operator|.
name|call
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot publish patchset"
argument_list|)
throw|;
block|}
return|return
name|changeDetailFactory
operator|.
name|create
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
block|}
end_class

end_unit

