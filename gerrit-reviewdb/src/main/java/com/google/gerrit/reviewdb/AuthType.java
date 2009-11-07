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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

begin_enum
DECL|enum|AuthType
specifier|public
enum|enum
name|AuthType
block|{
comment|/** Login relies upon the OpenID standard: {@link "http://openid.net/"} */
DECL|enumConstant|OPENID
name|OPENID
block|,
comment|/**    * Login relies upon the container/web server security.    *<p>    * The container or web server must populate an HTTP header with a unique name    * for the current user. Gerrit will implicitly trust the value of this header    * to supply the unique identity.    */
DECL|enumConstant|HTTP
name|HTTP
block|,
comment|/**    * Login relies upon the container/web server security, but also uses LDAP.    *<p>    * Like {@link #HTTP}, the container or web server must populate an HTTP    * header with a unique name for the current user. Gerrit will implicitly    * trust the value of this header to supply the unique identity.    *<p>    * In addition to trusting the HTTP headers, Gerrit will obtain basic user    * registration (name and email) from LDAP, and some group memberships.    */
DECL|enumConstant|HTTP_LDAP
name|HTTP_LDAP
block|,
comment|/**    * Login collects username and password through a web form, and binds to LDAP.    *<p>    * Unlike {@link #HTTP_LDAP}, Gerrit presents a sign-in dialog to the user and    * makes the connection to the LDAP server on their behalf.    */
DECL|enumConstant|LDAP
name|LDAP
block|,
comment|/** Development mode to enable becoming anyone you want. */
DECL|enumConstant|DEVELOPMENT_BECOME_ANY_ACCOUNT
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
block|; }
end_enum

end_unit

