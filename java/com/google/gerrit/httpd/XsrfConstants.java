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

begin_comment
comment|/** XSRF Constants. */
end_comment

begin_class
DECL|class|XsrfConstants
specifier|public
class|class
name|XsrfConstants
block|{
comment|/**    * Name of the cookie in which the XSRF token is sent from the server to the client during host    * page bootstrapping.    */
DECL|field|XSRF_COOKIE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|XSRF_COOKIE_NAME
init|=
literal|"XSRF_TOKEN"
decl_stmt|;
comment|/**    * Name of the HTTP header in which the client must send the XSRF token to the server on each    * request.    */
DECL|field|XSRF_HEADER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|XSRF_HEADER_NAME
init|=
literal|"X-Gerrit-Auth"
decl_stmt|;
block|}
end_class

end_unit

