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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
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
name|Branch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_interface
DECL|interface|MergeQueue
specifier|public
interface|interface
name|MergeQueue
block|{
DECL|method|merge (Branch.NameKey branch)
name|void
name|merge
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
function_decl|;
DECL|method|schedule (Branch.NameKey branch)
name|void
name|schedule
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
function_decl|;
DECL|method|recheckAfter (Branch.NameKey branch, long delay, TimeUnit delayUnit)
name|void
name|recheckAfter
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|delayUnit
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

