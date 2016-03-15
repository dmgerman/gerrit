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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

begin_class
DECL|class|UnifiedDiffChunkInfo
specifier|public
class|class
name|UnifiedDiffChunkInfo
extends|extends
name|DiffChunkInfo
block|{
DECL|field|cmLine
specifier|private
name|int
name|cmLine
decl_stmt|;
DECL|method|UnifiedDiffChunkInfo (DisplaySide side, int start, int end, int cmLine, boolean edit)
name|UnifiedDiffChunkInfo
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|int
name|cmLine
parameter_list|,
name|boolean
name|edit
parameter_list|)
block|{
name|super
argument_list|(
name|side
argument_list|,
name|start
argument_list|,
name|end
argument_list|,
name|edit
argument_list|)
expr_stmt|;
name|this
operator|.
name|cmLine
operator|=
name|cmLine
expr_stmt|;
block|}
DECL|method|getCmLine ()
name|int
name|getCmLine
parameter_list|()
block|{
return|return
name|cmLine
return|;
block|}
block|}
end_class

end_unit

