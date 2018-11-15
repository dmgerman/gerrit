begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
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
name|server
operator|.
name|ChangeAccess
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
name|ChangeMessageAccess
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
name|PatchLineCommentAccess
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
name|PatchSetAccess
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
name|PatchSetApprovalAccess
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
name|reviewdb
operator|.
name|server
operator|.
name|SchemaVersionAccess
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
name|Access
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
name|StatementExecutor
import|;
end_import

begin_comment
comment|/** ReviewDb that is disabled for testing. */
end_comment

begin_class
DECL|class|DisabledReviewDb
specifier|public
class|class
name|DisabledReviewDb
implements|implements
name|ReviewDb
block|{
DECL|class|Disabled
specifier|public
specifier|static
class|class
name|Disabled
extends|extends
name|RuntimeException
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
DECL|method|Disabled ()
specifier|private
name|Disabled
parameter_list|()
block|{
name|super
argument_list|(
literal|"ReviewDb is disabled for this test"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|commit ()
specifier|public
name|void
name|commit
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|rollback ()
specifier|public
name|void
name|rollback
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|updateSchema (StatementExecutor e)
specifier|public
name|void
name|updateSchema
parameter_list|(
name|StatementExecutor
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|pruneSchema (StatementExecutor e)
specifier|public
name|void
name|pruneSchema
parameter_list|(
name|StatementExecutor
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|allRelations ()
specifier|public
name|Access
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
index|[]
name|allRelations
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|schemaVersion ()
specifier|public
name|SchemaVersionAccess
name|schemaVersion
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|changes ()
specifier|public
name|ChangeAccess
name|changes
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|patchSetApprovals ()
specifier|public
name|PatchSetApprovalAccess
name|patchSetApprovals
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|changeMessages ()
specifier|public
name|ChangeMessageAccess
name|changeMessages
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|patchSets ()
specifier|public
name|PatchSetAccess
name|patchSets
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|patchComments ()
specifier|public
name|PatchLineCommentAccess
name|patchComments
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|nextAccountId ()
specifier|public
name|int
name|nextAccountId
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|nextAccountGroupId ()
specifier|public
name|int
name|nextAccountGroupId
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|nextChangeId ()
specifier|public
name|int
name|nextChangeId
parameter_list|()
block|{
throw|throw
operator|new
name|Disabled
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

