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
name|client
operator|.
name|reviewdb
operator|.
name|AccountExternalId
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
name|client
operator|.
name|reviewdb
operator|.
name|TrustedExternalId
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
name|client
operator|.
name|rpc
operator|.
name|Common
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ExternalIdDetail
specifier|public
class|class
name|ExternalIdDetail
block|{
DECL|field|ids
specifier|protected
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
decl_stmt|;
DECL|field|trusted
specifier|protected
name|List
argument_list|<
name|TrustedExternalId
argument_list|>
name|trusted
decl_stmt|;
DECL|method|ExternalIdDetail ()
specifier|protected
name|ExternalIdDetail
parameter_list|()
block|{   }
DECL|method|ExternalIdDetail (final List<AccountExternalId> myIds, final List<TrustedExternalId> siteTrusts)
specifier|public
name|ExternalIdDetail
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|myIds
parameter_list|,
specifier|final
name|List
argument_list|<
name|TrustedExternalId
argument_list|>
name|siteTrusts
parameter_list|)
block|{
name|ids
operator|=
name|myIds
expr_stmt|;
name|trusted
operator|=
name|siteTrusts
expr_stmt|;
block|}
DECL|method|getIds ()
specifier|public
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|getIds
parameter_list|()
block|{
return|return
name|ids
return|;
block|}
DECL|method|isTrusted (final AccountExternalId id)
specifier|public
name|boolean
name|isTrusted
parameter_list|(
specifier|final
name|AccountExternalId
name|id
parameter_list|)
block|{
switch|switch
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getLoginType
argument_list|()
condition|)
block|{
case|case
name|HTTP
case|:
return|return
literal|true
return|;
case|case
name|OPENID
case|:
default|default:
return|return
name|TrustedExternalId
operator|.
name|isTrusted
argument_list|(
name|id
argument_list|,
name|trusted
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

