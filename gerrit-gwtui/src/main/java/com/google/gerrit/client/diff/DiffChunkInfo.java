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

begin_comment
comment|/** Object recording the position of a diff chunk and whether it's an edit */
end_comment

begin_class
DECL|class|DiffChunkInfo
class|class
name|DiffChunkInfo
implements|implements
name|Comparable
argument_list|<
name|DiffChunkInfo
argument_list|>
block|{
DECL|field|side
specifier|final
name|DisplaySide
name|side
decl_stmt|;
DECL|field|start
specifier|final
name|int
name|start
decl_stmt|;
DECL|field|end
specifier|final
name|int
name|end
decl_stmt|;
DECL|field|edit
specifier|final
name|boolean
name|edit
decl_stmt|;
DECL|field|startOnOther
specifier|private
specifier|final
name|int
name|startOnOther
decl_stmt|;
DECL|method|DiffChunkInfo (DisplaySide side, int start, int startOnOther, int end, boolean edit)
name|DiffChunkInfo
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|startOnOther
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
name|startOnOther
operator|=
name|startOnOther
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
comment|/**    * Chunks are ordered by their starting line. If it's a deletion, use its    * corresponding line on the revision side for comparison. In the edit case,    * put the deletion chunk right before the insertion chunk. This placement    * guarantees well-ordering.    */
annotation|@
name|Override
DECL|method|compareTo (DiffChunkInfo o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|DiffChunkInfo
name|o
parameter_list|)
block|{
if|if
condition|(
name|side
operator|==
name|o
operator|.
name|side
condition|)
block|{
return|return
name|start
operator|-
name|o
operator|.
name|start
return|;
block|}
elseif|else
if|if
condition|(
name|side
operator|==
name|DisplaySide
operator|.
name|A
condition|)
block|{
name|int
name|comp
init|=
name|startOnOther
operator|-
name|o
operator|.
name|start
decl_stmt|;
return|return
name|comp
operator|==
literal|0
condition|?
operator|-
literal|1
else|:
name|comp
return|;
block|}
else|else
block|{
name|int
name|comp
init|=
name|start
operator|-
name|o
operator|.
name|startOnOther
decl_stmt|;
return|return
name|comp
operator|==
literal|0
condition|?
literal|1
else|:
name|comp
return|;
block|}
block|}
block|}
end_class

end_unit

