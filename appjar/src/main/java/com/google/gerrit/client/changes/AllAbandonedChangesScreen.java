begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|SingleListChangeInfo
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
name|rpc
operator|.
name|GerritCallback
import|;
end_import

begin_class
DECL|class|AllAbandonedChangesScreen
specifier|public
class|class
name|AllAbandonedChangesScreen
extends|extends
name|AllSingleListScreen
block|{
DECL|method|AllAbandonedChangesScreen (final String positionToken)
specifier|public
name|AllAbandonedChangesScreen
parameter_list|(
specifier|final
name|String
name|positionToken
parameter_list|)
block|{
name|super
argument_list|(
name|Util
operator|.
name|C
operator|.
name|allAbandonedChanges
argument_list|()
argument_list|,
literal|"all,abandoned"
argument_list|,
name|positionToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadPrev ()
specifier|protected
name|void
name|loadPrev
parameter_list|()
block|{
name|Util
operator|.
name|LIST_SVC
operator|.
name|allClosedPrev
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
argument_list|,
name|pos
argument_list|,
name|pageSize
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|SingleListChangeInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|SingleListChangeInfo
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadNext ()
specifier|protected
name|void
name|loadNext
parameter_list|()
block|{
name|Util
operator|.
name|LIST_SVC
operator|.
name|allClosedNext
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
argument_list|,
name|pos
argument_list|,
name|pageSize
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|SingleListChangeInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|SingleListChangeInfo
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

