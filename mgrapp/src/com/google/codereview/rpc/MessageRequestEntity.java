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
name|protobuf
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|RequestEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|NullProgressMonitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|TemporaryBuffer
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
name|OutputStream
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

begin_comment
comment|/**  * Formats a protocol buffer Message into an HTTP request body.  *<p>  * The compressed binary serialized form is always used.  */
end_comment

begin_class
DECL|class|MessageRequestEntity
specifier|public
class|class
name|MessageRequestEntity
implements|implements
name|RequestEntity
block|{
comment|/** Content-Type for a protocol buffer. */
DECL|field|TYPE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"application/x-google-protobuf"
decl_stmt|;
DECL|field|msg
specifier|private
specifier|final
name|Message
name|msg
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|temp
specifier|private
specifier|final
name|TemporaryBuffer
name|temp
decl_stmt|;
comment|/**    * Create a new request for a single message.    *     * @param m the message to serialize and transmit.    * @throws IOException the message could not be compressed into temporary    *         storage. The local file system may be full, or the message is    *         unable to compress itself.    */
DECL|method|MessageRequestEntity (final Message m)
specifier|public
name|MessageRequestEntity
parameter_list|(
specifier|final
name|Message
name|m
parameter_list|)
throws|throws
name|IOException
block|{
name|msg
operator|=
name|m
expr_stmt|;
name|name
operator|=
name|msg
operator|.
name|getDescriptorForType
argument_list|()
operator|.
name|getFullName
argument_list|()
expr_stmt|;
name|temp
operator|=
operator|new
name|TemporaryBuffer
argument_list|()
expr_stmt|;
specifier|final
name|DeflaterOutputStream
name|dos
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|temp
argument_list|)
decl_stmt|;
try|try
block|{
name|msg
operator|.
name|writeTo
argument_list|(
name|dos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|dos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getContentLength ()
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
operator|(
name|int
operator|)
name|temp
operator|.
name|length
argument_list|()
return|;
block|}
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|TYPE
operator|+
literal|"; name="
operator|+
name|name
operator|+
literal|"; compress=deflate"
return|;
block|}
DECL|method|isRepeatable ()
specifier|public
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|writeRequest (final OutputStream out)
specifier|public
name|void
name|writeRequest
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|temp
operator|.
name|writeTo
argument_list|(
name|out
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|temp
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

