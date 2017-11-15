begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readVarInt32
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeVarInt32
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_class
DECL|class|ComparisonType
specifier|public
class|class
name|ComparisonType
block|{
comment|/** 1-based parent */
DECL|field|parentNum
specifier|private
specifier|final
name|Integer
name|parentNum
decl_stmt|;
DECL|field|autoMerge
specifier|private
specifier|final
name|boolean
name|autoMerge
decl_stmt|;
DECL|method|againstOtherPatchSet ()
specifier|public
specifier|static
name|ComparisonType
name|againstOtherPatchSet
parameter_list|()
block|{
return|return
operator|new
name|ComparisonType
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|againstParent (int parentNum)
specifier|public
specifier|static
name|ComparisonType
name|againstParent
parameter_list|(
name|int
name|parentNum
parameter_list|)
block|{
return|return
operator|new
name|ComparisonType
argument_list|(
name|parentNum
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|againstAutoMerge ()
specifier|public
specifier|static
name|ComparisonType
name|againstAutoMerge
parameter_list|()
block|{
return|return
operator|new
name|ComparisonType
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|ComparisonType (Integer parentNum, boolean autoMerge)
specifier|private
name|ComparisonType
parameter_list|(
name|Integer
name|parentNum
parameter_list|,
name|boolean
name|autoMerge
parameter_list|)
block|{
name|this
operator|.
name|parentNum
operator|=
name|parentNum
expr_stmt|;
name|this
operator|.
name|autoMerge
operator|=
name|autoMerge
expr_stmt|;
block|}
DECL|method|isAgainstParentOrAutoMerge ()
specifier|public
name|boolean
name|isAgainstParentOrAutoMerge
parameter_list|()
block|{
return|return
name|isAgainstParent
argument_list|()
operator|||
name|isAgainstAutoMerge
argument_list|()
return|;
block|}
DECL|method|isAgainstParent ()
specifier|public
name|boolean
name|isAgainstParent
parameter_list|()
block|{
return|return
name|parentNum
operator|!=
literal|null
return|;
block|}
DECL|method|isAgainstAutoMerge ()
specifier|public
name|boolean
name|isAgainstAutoMerge
parameter_list|()
block|{
return|return
name|autoMerge
return|;
block|}
DECL|method|getParentNum ()
specifier|public
name|int
name|getParentNum
parameter_list|()
block|{
name|checkNotNull
argument_list|(
name|parentNum
argument_list|)
expr_stmt|;
return|return
name|parentNum
return|;
block|}
DECL|method|writeTo (OutputStream out)
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|parentNum
operator|!=
literal|null
condition|?
name|parentNum
else|:
literal|0
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|autoMerge
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|readFrom (InputStream in)
specifier|static
name|ComparisonType
name|readFrom
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|p
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Integer
name|parentNum
init|=
name|p
operator|>
literal|0
condition|?
name|p
else|:
literal|null
decl_stmt|;
name|boolean
name|autoMerge
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
operator|!=
literal|0
decl_stmt|;
return|return
operator|new
name|ComparisonType
argument_list|(
name|parentNum
argument_list|,
name|autoMerge
argument_list|)
return|;
block|}
block|}
end_class

end_unit

