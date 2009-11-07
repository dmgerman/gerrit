begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|AccountSecurity
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|AccountService
import|;
end_import

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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|JsonUtil
import|;
end_import

begin_class
DECL|class|Util
specifier|public
class|class
name|Util
block|{
DECL|field|C
specifier|public
specifier|static
specifier|final
name|AccountConstants
name|C
init|=
name|GWT
operator|.
name|create
argument_list|(
name|AccountConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|M
specifier|public
specifier|static
specifier|final
name|AccountMessages
name|M
init|=
name|GWT
operator|.
name|create
argument_list|(
name|AccountMessages
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ACCOUNT_SVC
specifier|public
specifier|static
specifier|final
name|AccountService
name|ACCOUNT_SVC
decl_stmt|;
DECL|field|ACCOUNT_SEC
specifier|public
specifier|static
specifier|final
name|AccountSecurity
name|ACCOUNT_SEC
decl_stmt|;
static|static
block|{
name|ACCOUNT_SVC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|AccountService
operator|.
name|class
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|bind
argument_list|(
name|ACCOUNT_SVC
argument_list|,
literal|"rpc/AccountService"
argument_list|)
expr_stmt|;
name|ACCOUNT_SEC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|AccountSecurity
operator|.
name|class
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|bind
argument_list|(
name|ACCOUNT_SEC
argument_list|,
literal|"rpc/AccountSecurity"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

