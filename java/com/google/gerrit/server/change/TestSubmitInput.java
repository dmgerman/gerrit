begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|SubmitInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_comment
comment|/**  * Subclass of {@link SubmitInput} with special bits that may be flipped for testing purposes only.  */
end_comment

begin_class
annotation|@
name|VisibleForTesting
DECL|class|TestSubmitInput
specifier|public
class|class
name|TestSubmitInput
extends|extends
name|SubmitInput
block|{
DECL|field|failAfterRefUpdates
specifier|public
name|boolean
name|failAfterRefUpdates
decl_stmt|;
comment|/**    * For each change being submitted, an element is removed from this queue and, if the value is    * true, a bogus ref update is added to the batch, in order to generate a lock failure during    * execution.    */
DECL|field|generateLockFailures
specifier|public
name|Queue
argument_list|<
name|Boolean
argument_list|>
name|generateLockFailures
decl_stmt|;
block|}
end_class

end_unit

