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
DECL|package|org.spearce.jgit.lib
package|package
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
package|;
end_package

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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_class
DECL|class|ObjectIdSerialization
specifier|public
class|class
name|ObjectIdSerialization
block|{
DECL|method|write (final ObjectOutputStream out, final AnyObjectId id)
specifier|public
specifier|static
name|void
name|write
parameter_list|(
specifier|final
name|ObjectOutputStream
name|out
parameter_list|,
specifier|final
name|AnyObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|writeBoolean
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|id
operator|.
name|w1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|id
operator|.
name|w2
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|id
operator|.
name|w3
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|id
operator|.
name|w4
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|id
operator|.
name|w5
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|writeBoolean
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|read (final ObjectInputStream in)
specifier|public
specifier|static
name|ObjectId
name|read
parameter_list|(
specifier|final
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|in
operator|.
name|readBoolean
argument_list|()
condition|)
block|{
specifier|final
name|int
name|w1
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
specifier|final
name|int
name|w2
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
specifier|final
name|int
name|w3
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
specifier|final
name|int
name|w4
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
specifier|final
name|int
name|w5
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
return|return
operator|new
name|ObjectId
argument_list|(
name|w1
argument_list|,
name|w2
argument_list|,
name|w3
argument_list|,
name|w4
argument_list|,
name|w5
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|ObjectIdSerialization ()
specifier|private
name|ObjectIdSerialization
parameter_list|()
block|{   }
block|}
end_class

end_unit

