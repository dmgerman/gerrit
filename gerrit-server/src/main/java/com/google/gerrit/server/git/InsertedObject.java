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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|ByteString
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectInserter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectLoader
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|InsertedObject
specifier|public
specifier|abstract
class|class
name|InsertedObject
block|{
DECL|method|create (int type, InputStream in)
specifier|static
name|InsertedObject
name|create
parameter_list|(
name|int
name|type
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|create
argument_list|(
name|type
argument_list|,
name|ByteString
operator|.
name|readFrom
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (int type, ByteString bs)
specifier|static
name|InsertedObject
name|create
parameter_list|(
name|int
name|type
parameter_list|,
name|ByteString
name|bs
parameter_list|)
block|{
name|ObjectId
name|id
decl_stmt|;
try|try
init|(
name|ObjectInserter
operator|.
name|Formatter
name|fmt
init|=
operator|new
name|ObjectInserter
operator|.
name|Formatter
argument_list|()
init|)
block|{
name|id
operator|=
name|fmt
operator|.
name|idFor
argument_list|(
name|type
argument_list|,
name|bs
operator|.
name|size
argument_list|()
argument_list|,
name|bs
operator|.
name|newInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|AutoValue_InsertedObject
argument_list|(
name|id
argument_list|,
name|type
argument_list|,
name|bs
argument_list|)
return|;
block|}
DECL|method|create (int type, byte[] src, int off, int len)
specifier|static
name|InsertedObject
name|create
parameter_list|(
name|int
name|type
parameter_list|,
name|byte
index|[]
name|src
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|type
argument_list|,
name|ByteString
operator|.
name|copyFrom
argument_list|(
name|src
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
argument_list|)
return|;
block|}
DECL|method|id ()
specifier|public
specifier|abstract
name|ObjectId
name|id
parameter_list|()
function_decl|;
DECL|method|type ()
specifier|public
specifier|abstract
name|int
name|type
parameter_list|()
function_decl|;
DECL|method|data ()
specifier|public
specifier|abstract
name|ByteString
name|data
parameter_list|()
function_decl|;
DECL|method|newLoader ()
name|ObjectLoader
name|newLoader
parameter_list|()
block|{
return|return
operator|new
name|ObjectLoader
operator|.
name|SmallObject
argument_list|(
name|type
argument_list|()
argument_list|,
name|data
argument_list|()
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

