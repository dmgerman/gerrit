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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_enum
DECL|enum|GitBasicAuthPolicy
specifier|public
enum|enum
name|GitBasicAuthPolicy
block|{
comment|/** Only the HTTP password is accepted when doing Git over HTTP and REST API requests. */
DECL|enumConstant|HTTP
name|HTTP
block|,
comment|/** Only the LDAP password is allowed when doing Git over HTTP and REST API requests. */
DECL|enumConstant|LDAP
name|LDAP
block|,
comment|/**    * The password in the request is first checked against the HTTP password and, if it does not    * match, it is then validated against the LDAP password.    */
DECL|enumConstant|HTTP_LDAP
name|HTTP_LDAP
block|,
comment|/** Only the `OAUTH` authentication is allowed when doing Git over HTTP and REST API requests. */
DECL|enumConstant|OAUTH
name|OAUTH
block|}
end_enum

end_unit

