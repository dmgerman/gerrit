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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|entities
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Specialization of {@link BatchUpdateOp} for creating changes.  *  *<p>A typical {@code BatchUpdateOp} operates on a change that has been read from a transaction;  * this type, by contrast, is responsible for creating the change from scratch.  *  *<p>Ops of this type must be used via {@link BatchUpdate#insertChange(InsertChangeOp)}. They may  * be mixed with other {@link BatchUpdateOp}s for the same change, in which case the insert op runs  * first.  */
end_comment

begin_interface
DECL|interface|InsertChangeOp
specifier|public
interface|interface
name|InsertChangeOp
extends|extends
name|BatchUpdateOp
block|{
DECL|method|createChange (Context ctx)
name|Change
name|createChange
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

