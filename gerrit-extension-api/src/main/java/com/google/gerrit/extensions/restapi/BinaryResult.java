begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_comment
comment|/**  * Wrapper around a non-JSON result from a {@link RestView}.  *<p>  * Views may return this type to signal they want the server glue to write raw  * data to the client, instead of attempting automatic conversion to JSON. The  * create form is overloaded to handle plain text from a String, or binary data  * from a {@code byte[]} or {@code InputSteam}.  */
end_comment

begin_class
DECL|class|BinaryResult
specifier|public
specifier|abstract
class|class
name|BinaryResult
implements|implements
name|Closeable
block|{
comment|/** Default MIME type for unknown binary data. */
DECL|field|OCTET_STREAM
specifier|static
specifier|final
name|String
name|OCTET_STREAM
init|=
literal|"application/octet-stream"
decl_stmt|;
comment|/** Produce a UTF-8 encoded result from a string. */
DECL|method|create (String data)
specifier|public
specifier|static
name|BinaryResult
name|create
parameter_list|(
name|String
name|data
parameter_list|)
block|{
try|try
block|{
return|return
name|create
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"JVM does not support UTF-8"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** Produce an {@code application/octet-stream} result from a byte array. */
DECL|method|create (byte[] data)
specifier|public
specifier|static
name|BinaryResult
name|create
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
operator|new
name|Array
argument_list|(
name|data
argument_list|)
return|;
block|}
comment|/**    * Produce an {@code application/octet-stream} of unknown length by copying    * the InputStream until EOF. The server glue will automatically close this    * stream when copying is complete.    */
DECL|method|create (InputStream data)
specifier|public
specifier|static
name|BinaryResult
name|create
parameter_list|(
name|InputStream
name|data
parameter_list|)
block|{
return|return
operator|new
name|Stream
argument_list|(
name|data
argument_list|)
return|;
block|}
DECL|field|contentType
specifier|private
name|String
name|contentType
init|=
name|OCTET_STREAM
decl_stmt|;
DECL|field|characterEncoding
specifier|private
name|String
name|characterEncoding
decl_stmt|;
DECL|field|contentLength
specifier|private
name|long
name|contentLength
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|gzip
specifier|private
name|boolean
name|gzip
init|=
literal|true
decl_stmt|;
comment|/** @return the MIME type of the result, for HTTP clients. */
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
name|String
name|enc
init|=
name|getCharacterEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|enc
operator|!=
literal|null
condition|)
block|{
return|return
name|contentType
operator|+
literal|"; charset="
operator|+
name|enc
return|;
block|}
return|return
name|contentType
return|;
block|}
comment|/** Set the MIME type of the result, and return {@code this}. */
DECL|method|setContentType (String contentType)
specifier|public
name|BinaryResult
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
operator|!=
literal|null
condition|?
name|contentType
else|:
name|OCTET_STREAM
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Get the character encoding; null if not known. */
DECL|method|getCharacterEncoding ()
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
return|return
name|characterEncoding
return|;
block|}
comment|/** Set the character set used to encode text data and return {@code this}. */
DECL|method|setCharacterEncoding (String encoding)
specifier|public
name|BinaryResult
name|setCharacterEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|characterEncoding
operator|=
name|encoding
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** @return length in bytes of the result; -1 if not known. */
DECL|method|getContentLength ()
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|contentLength
return|;
block|}
comment|/** Set the content length of the result; -1 if not known. */
DECL|method|setContentLength (long len)
specifier|public
name|BinaryResult
name|setContentLength
parameter_list|(
name|long
name|len
parameter_list|)
block|{
name|this
operator|.
name|contentLength
operator|=
name|len
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** @return true if this result can be gzip compressed to clients. */
DECL|method|canGzip ()
specifier|public
name|boolean
name|canGzip
parameter_list|()
block|{
return|return
name|gzip
return|;
block|}
comment|/** Disable gzip compression for already compressed responses. */
DECL|method|disableGzip ()
specifier|public
name|BinaryResult
name|disableGzip
parameter_list|()
block|{
name|this
operator|.
name|gzip
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Write or copy the result onto the specified output stream.    *    * @param os stream to write result data onto. This stream will be closed by    *        the caller after this method returns.    * @throws IOException if the data cannot be produced, or the OutputStream    *         {@code os} throws any IOException during a write or flush call.    */
DECL|method|writeTo (OutputStream os)
specifier|public
specifier|abstract
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/** Close the result and release any resources it holds. */
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{   }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|getContentLength
argument_list|()
operator|>=
literal|0
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"BinaryResult[Content-Type: %s, Content-Length: %d]"
argument_list|,
name|getContentType
argument_list|()
argument_list|,
name|getContentLength
argument_list|()
argument_list|)
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"BinaryResult[Content-Type: %s, Content-Length: unknown]"
argument_list|,
name|getContentType
argument_list|()
argument_list|)
return|;
block|}
DECL|class|Array
specifier|private
specifier|static
class|class
name|Array
extends|extends
name|BinaryResult
block|{
DECL|field|data
specifier|private
specifier|final
name|byte
index|[]
name|data
decl_stmt|;
DECL|method|Array (byte[] data)
name|Array
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|setContentLength
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|writeTo (OutputStream os)
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|os
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Stream
specifier|private
specifier|static
class|class
name|Stream
extends|extends
name|BinaryResult
block|{
DECL|field|src
specifier|private
specifier|final
name|InputStream
name|src
decl_stmt|;
DECL|method|Stream (InputStream src)
name|Stream
parameter_list|(
name|InputStream
name|src
parameter_list|)
block|{
name|this
operator|.
name|src
operator|=
name|src
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|writeTo (OutputStream dst)
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|dst
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|tmp
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
literal|0
operator|<
operator|(
name|n
operator|=
name|src
operator|.
name|read
argument_list|(
name|tmp
argument_list|)
operator|)
condition|)
block|{
name|dst
operator|.
name|write
argument_list|(
name|tmp
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

