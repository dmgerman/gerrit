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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|Strings
operator|.
name|emptyToNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|AUTHORIZATION
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|BaseEncoding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_class
DECL|class|RemoteUserUtil
specifier|public
class|class
name|RemoteUserUtil
block|{
comment|/**    * Tries to get username from a request with following strategies:    *    *<ul>    *<li>ServletRequest#getRemoteUser    *<li>HTTP 'Authorization' header    *<li>Custom HTTP header    *</ul>    *    * @param req request to extract username from.    * @param loginHeader name of header which is used for extracting username.    * @return the extracted username or null.    */
DECL|method|getRemoteUser (HttpServletRequest req, String loginHeader)
specifier|public
specifier|static
name|String
name|getRemoteUser
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|loginHeader
parameter_list|)
block|{
if|if
condition|(
name|AUTHORIZATION
operator|.
name|equals
argument_list|(
name|loginHeader
argument_list|)
condition|)
block|{
name|String
name|user
init|=
name|emptyToNull
argument_list|(
name|req
operator|.
name|getRemoteUser
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
comment|// The container performed the authentication, and has the user
comment|// identity already decoded for us. Honor that as we have been
comment|// configured to honor HTTP authentication.
return|return
name|user
return|;
block|}
comment|// If the container didn't do the authentication we might
comment|// have done it in the front-end web server. Try to split
comment|// the identity out of the Authorization header and honor it.
name|String
name|auth
init|=
name|req
operator|.
name|getHeader
argument_list|(
name|AUTHORIZATION
argument_list|)
decl_stmt|;
return|return
name|extractUsername
argument_list|(
name|auth
argument_list|)
return|;
block|}
comment|// Nonstandard HTTP header. We have been told to trust this
comment|// header blindly as-is.
return|return
name|emptyToNull
argument_list|(
name|req
operator|.
name|getHeader
argument_list|(
name|loginHeader
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Extracts username from an HTTP Basic or Digest authentication header.    *    * @param auth header value which is used for extracting.    * @return username if available or null.    */
DECL|method|extractUsername (String auth)
specifier|public
specifier|static
name|String
name|extractUsername
parameter_list|(
name|String
name|auth
parameter_list|)
block|{
name|auth
operator|=
name|emptyToNull
argument_list|(
name|auth
argument_list|)
expr_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|auth
operator|.
name|startsWith
argument_list|(
literal|"Basic "
argument_list|)
condition|)
block|{
name|auth
operator|=
name|auth
operator|.
name|substring
argument_list|(
literal|"Basic "
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|auth
operator|=
operator|new
name|String
argument_list|(
name|BaseEncoding
operator|.
name|base64
argument_list|()
operator|.
name|decode
argument_list|(
name|auth
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
expr_stmt|;
specifier|final
name|int
name|c
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
return|return
name|c
operator|>
literal|0
condition|?
name|auth
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|auth
operator|.
name|startsWith
argument_list|(
literal|"Digest "
argument_list|)
condition|)
block|{
specifier|final
name|int
name|u
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|"username=\""
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|<=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|auth
operator|=
name|auth
operator|.
name|substring
argument_list|(
name|u
operator|+
literal|10
argument_list|)
expr_stmt|;
specifier|final
name|int
name|e
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|'"'
argument_list|)
decl_stmt|;
return|return
name|e
operator|>
literal|0
condition|?
name|auth
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|e
argument_list|)
else|:
literal|null
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

