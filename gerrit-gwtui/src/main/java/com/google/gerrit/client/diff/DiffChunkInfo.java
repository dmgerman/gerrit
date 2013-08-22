begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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

begin_comment
comment|/** Object recording the position of a diff chunk and whether it's an edit */
end_comment

begin_class
DECL|class|DiffChunkInfo
class|class
name|DiffChunkInfo
block|{
DECL|field|side
specifier|private
name|DisplaySide
name|side
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|end
specifier|private
name|int
name|end
decl_stmt|;
DECL|field|edit
specifier|private
name|boolean
name|edit
decl_stmt|;
DECL|method|DiffChunkInfo (DisplaySide side, int start, int end, boolean edit)
name|DiffChunkInfo
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
name|boolean
name|edit
parameter_list|)
block|{
name|this
operator|.
name|side
operator|=
name|side
expr_stmt|;
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
name|this
operator|.
name|end
operator|=
name|end
expr_stmt|;
name|this
operator|.
name|edit
operator|=
name|edit
expr_stmt|;
block|}
DECL|method|getSide ()
name|DisplaySide
name|getSide
parameter_list|()
block|{
return|return
name|side
return|;
block|}
DECL|method|getStart ()
name|int
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
DECL|method|getEnd ()
name|int
name|getEnd
parameter_list|()
block|{
return|return
name|end
return|;
block|}
DECL|method|isEdit ()
name|boolean
name|isEdit
parameter_list|()
block|{
return|return
name|edit
return|;
block|}
block|}
end_class

end_unit

