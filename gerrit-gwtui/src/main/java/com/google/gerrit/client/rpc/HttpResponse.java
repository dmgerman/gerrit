begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|Response
import|;
end_import

begin_comment
comment|/** Wraps decoded server reply with HTTP headers. */
end_comment

begin_class
DECL|class|HttpResponse
specifier|public
class|class
name|HttpResponse
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|httpResponse
specifier|private
specifier|final
name|Response
name|httpResponse
decl_stmt|;
DECL|field|contentType
specifier|private
specifier|final
name|String
name|contentType
decl_stmt|;
DECL|field|result
specifier|private
specifier|final
name|T
name|result
decl_stmt|;
DECL|method|HttpResponse (Response httpResponse, String contentType, T result)
name|HttpResponse
parameter_list|(
name|Response
name|httpResponse
parameter_list|,
name|String
name|contentType
parameter_list|,
name|T
name|result
parameter_list|)
block|{
name|this
operator|.
name|httpResponse
operator|=
name|httpResponse
expr_stmt|;
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
comment|/** HTTP status code, always in the 2xx family. */
DECL|method|getStatusCode ()
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|httpResponse
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
comment|/**    * Content type supplied by the server.    *    * This helper simplifies the common {@code getHeader("Content-Type")} case.    */
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
comment|/** Lookup an arbitrary reply header. */
DECL|method|getHeader (String header)
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|header
parameter_list|)
block|{
if|if
condition|(
literal|"Content-Type"
operator|.
name|equals
argument_list|(
name|header
argument_list|)
condition|)
block|{
return|return
name|contentType
return|;
block|}
return|return
name|httpResponse
operator|.
name|getHeader
argument_list|(
name|header
argument_list|)
return|;
block|}
DECL|method|getResult ()
specifier|public
name|T
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

