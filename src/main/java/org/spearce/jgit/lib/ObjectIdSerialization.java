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
name|readFixInt32
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
name|writeFixInt32
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
DECL|class|ObjectIdSerialization
specifier|public
class|class
name|ObjectIdSerialization
block|{
DECL|method|writeCanBeNull (final OutputStream out, final AnyObjectId id)
specifier|public
specifier|static
name|void
name|writeCanBeNull
parameter_list|(
specifier|final
name|OutputStream
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
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeNotNull (final OutputStream out, final AnyObjectId id)
specifier|public
specifier|static
name|void
name|writeNotNull
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|,
specifier|final
name|AnyObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|writeFixInt32
argument_list|(
name|out
argument_list|,
name|id
operator|.
name|w1
argument_list|)
expr_stmt|;
name|writeFixInt32
argument_list|(
name|out
argument_list|,
name|id
operator|.
name|w2
argument_list|)
expr_stmt|;
name|writeFixInt32
argument_list|(
name|out
argument_list|,
name|id
operator|.
name|w3
argument_list|)
expr_stmt|;
name|writeFixInt32
argument_list|(
name|out
argument_list|,
name|id
operator|.
name|w4
argument_list|)
expr_stmt|;
name|writeFixInt32
argument_list|(
name|out
argument_list|,
name|id
operator|.
name|w5
argument_list|)
expr_stmt|;
block|}
DECL|method|readCanBeNull (final InputStream in)
specifier|public
specifier|static
name|ObjectId
name|readCanBeNull
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
switch|switch
condition|(
name|readVarInt32
argument_list|(
name|in
argument_list|)
condition|)
block|{
case|case
literal|0
case|:
return|return
literal|null
return|;
case|case
literal|1
case|:
return|return
name|readNotNull
argument_list|(
name|in
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid flag before ObjectId"
argument_list|)
throw|;
block|}
block|}
DECL|method|readNotNull (final InputStream in)
specifier|public
specifier|static
name|ObjectId
name|readNotNull
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|int
name|w1
init|=
name|readFixInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|w2
init|=
name|readFixInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|w3
init|=
name|readFixInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|w4
init|=
name|readFixInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|w5
init|=
name|readFixInt32
argument_list|(
name|in
argument_list|)
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
DECL|method|ObjectIdSerialization ()
specifier|private
name|ObjectIdSerialization
parameter_list|()
block|{   }
block|}
end_class

end_unit

