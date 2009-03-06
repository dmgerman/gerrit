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
DECL|package|com.google.gerrit.client.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|openid
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
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_class
DECL|class|OpenIdUtil
specifier|public
class|class
name|OpenIdUtil
block|{
DECL|field|C
specifier|public
specifier|static
specifier|final
name|LoginConstants
name|C
decl_stmt|;
DECL|field|M
specifier|public
specifier|static
specifier|final
name|LoginMessages
name|M
decl_stmt|;
DECL|field|SVC
specifier|public
specifier|static
specifier|final
name|OpenIdService
name|SVC
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
DECL|field|P_SIGNIN_MODE
specifier|public
specifier|static
specifier|final
name|String
name|P_SIGNIN_MODE
init|=
literal|"gerrit.signin_mode"
decl_stmt|;
DECL|field|P_SIGNIN_CB
specifier|public
specifier|static
specifier|final
name|String
name|P_SIGNIN_CB
init|=
literal|"gerrit.signin_cb"
decl_stmt|;
DECL|field|P_DISCOVERY_CB
specifier|public
specifier|static
specifier|final
name|String
name|P_DISCOVERY_CB
init|=
literal|"gerrit.discovery_cb"
decl_stmt|;
DECL|field|P_REMEMBERID
specifier|public
specifier|static
specifier|final
name|String
name|P_REMEMBERID
init|=
literal|"gerrit.rememberid"
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
static|static
block|{
if|if
condition|(
name|GWT
operator|.
name|isClient
argument_list|()
condition|)
block|{
name|C
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|LoginConstants
operator|.
name|class
argument_list|)
expr_stmt|;
name|M
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|LoginMessages
operator|.
name|class
argument_list|)
expr_stmt|;
name|SVC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|OpenIdService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|C
operator|=
literal|null
expr_stmt|;
name|M
operator|=
literal|null
expr_stmt|;
name|SVC
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

