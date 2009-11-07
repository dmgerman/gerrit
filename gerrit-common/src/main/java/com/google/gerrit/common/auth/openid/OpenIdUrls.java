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
DECL|package|com.google.gerrit.common.auth.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|auth
operator|.
name|openid
package|;
end_package

begin_class
DECL|class|OpenIdUrls
specifier|public
class|class
name|OpenIdUrls
block|{
DECL|field|OPENID_IDENTIFIER
specifier|public
specifier|static
specifier|final
name|String
name|OPENID_IDENTIFIER
init|=
literal|"openid_identifier"
decl_stmt|;
DECL|field|LASTID_COOKIE
specifier|public
specifier|static
specifier|final
name|String
name|LASTID_COOKIE
init|=
literal|"gerrit.last_openid"
decl_stmt|;
DECL|field|URL_YAHOO
specifier|public
specifier|static
specifier|final
name|String
name|URL_YAHOO
init|=
literal|"https://me.yahoo.com"
decl_stmt|;
DECL|field|URL_GOOGLE
specifier|public
specifier|static
specifier|final
name|String
name|URL_GOOGLE
init|=
literal|"https://www.google.com/accounts/o8/id"
decl_stmt|;
block|}
end_class

end_unit

