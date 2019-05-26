begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|entities
operator|.
name|Account
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
name|extensions
operator|.
name|client
operator|.
name|AccountFieldName
import|;
end_import

begin_comment
comment|/** Fake implementation of {@link Realm} that does not communicate. */
end_comment

begin_class
DECL|class|FakeRealm
specifier|public
class|class
name|FakeRealm
extends|extends
name|AbstractRealm
block|{
annotation|@
name|Override
DECL|method|allowsEdit (AccountFieldName field)
specifier|public
name|boolean
name|allowsEdit
parameter_list|(
name|AccountFieldName
name|field
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|authenticate (AuthRequest who)
specifier|public
name|AuthRequest
name|authenticate
parameter_list|(
name|AuthRequest
name|who
parameter_list|)
block|{
return|return
name|who
return|;
block|}
annotation|@
name|Override
DECL|method|onCreateAccount (AuthRequest who, Account account)
specifier|public
name|void
name|onCreateAccount
parameter_list|(
name|AuthRequest
name|who
parameter_list|,
name|Account
name|account
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|lookup (String accountName)
specifier|public
name|Account
operator|.
name|Id
name|lookup
parameter_list|(
name|String
name|accountName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

