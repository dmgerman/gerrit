begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
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
name|exceptions
operator|.
name|StorageException
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

begin_comment
comment|/**  * Exception indicating that the change has received too many updates. Further actions apart from  * {@code abandon} or {@code submit} are blocked.  */
end_comment

begin_class
DECL|class|TooManyUpdatesException
specifier|public
class|class
name|TooManyUpdatesException
extends|extends
name|StorageException
block|{
annotation|@
name|VisibleForTesting
DECL|method|message (Change.Id id, int maxUpdates)
specifier|public
specifier|static
name|String
name|message
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|int
name|maxUpdates
parameter_list|)
block|{
return|return
literal|"Change "
operator|+
name|id
operator|+
literal|" may not exceed "
operator|+
name|maxUpdates
operator|+
literal|" updates. It may still be abandoned or submitted. To continue working on this "
operator|+
literal|"change, recreate it with a new Change-Id, then abandon this one."
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|TooManyUpdatesException (Change.Id id, int maxUpdates)
name|TooManyUpdatesException
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|int
name|maxUpdates
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|(
name|id
argument_list|,
name|maxUpdates
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

