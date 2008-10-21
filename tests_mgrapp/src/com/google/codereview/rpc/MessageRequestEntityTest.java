begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.codereview.rpc
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|SubmitChange
operator|.
name|SubmitChangeResponse
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
name|Message
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DeflaterOutputStream
import|;
end_import

begin_class
DECL|class|MessageRequestEntityTest
specifier|public
class|class
name|MessageRequestEntityTest
extends|extends
name|TestCase
block|{
DECL|method|testCompressedEntity ()
specifier|public
name|void
name|testCompressedEntity
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|msg
init|=
name|buildMessage
argument_list|()
decl_stmt|;
specifier|final
name|byte
index|[]
name|bin
init|=
name|compress
argument_list|(
name|msg
argument_list|)
decl_stmt|;
specifier|final
name|String
name|contentType
init|=
literal|"application/x-google-protobuf"
operator|+
literal|"; name=codereview.internal.SubmitChangeResponse"
operator|+
literal|"; compress=deflate"
decl_stmt|;
specifier|final
name|MessageRequestEntity
name|mre
init|=
operator|new
name|MessageRequestEntity
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mre
operator|.
name|isRepeatable
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bin
operator|.
name|length
argument_list|,
name|mre
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|contentType
argument_list|,
name|mre
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|ByteArrayOutputStream
name|compressed
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|mre
operator|.
name|writeRequest
argument_list|(
name|compressed
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|bin
argument_list|,
name|compressed
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildMessage ()
specifier|private
specifier|static
name|Message
name|buildMessage
parameter_list|()
block|{
specifier|final
name|SubmitChangeResponse
operator|.
name|Builder
name|r
init|=
name|SubmitChangeResponse
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|setStatusCode
argument_list|(
name|SubmitChangeResponse
operator|.
name|CodeType
operator|.
name|PATCHSET_EXISTS
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|compress (final Message m)
specifier|private
specifier|static
name|byte
index|[]
name|compress
parameter_list|(
specifier|final
name|Message
name|m
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|DeflaterOutputStream
name|dos
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|m
operator|.
name|writeTo
argument_list|(
name|dos
argument_list|)
expr_stmt|;
name|dos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

