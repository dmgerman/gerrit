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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountGroup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|DefaultRealm
specifier|public
specifier|final
class|class
name|DefaultRealm
implements|implements
name|Realm
block|{
DECL|field|emailExpander
specifier|private
specifier|final
name|EmailExpander
name|emailExpander
decl_stmt|;
DECL|field|byEmail
specifier|private
specifier|final
name|AccountByEmailCache
name|byEmail
decl_stmt|;
annotation|@
name|Inject
DECL|method|DefaultRealm (final EmailExpander emailExpander, final AccountByEmailCache byEmail)
name|DefaultRealm
parameter_list|(
specifier|final
name|EmailExpander
name|emailExpander
parameter_list|,
specifier|final
name|AccountByEmailCache
name|byEmail
parameter_list|)
block|{
name|this
operator|.
name|emailExpander
operator|=
name|emailExpander
expr_stmt|;
name|this
operator|.
name|byEmail
operator|=
name|byEmail
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|allowsEdit (final Account.FieldName field)
specifier|public
name|boolean
name|allowsEdit
parameter_list|(
specifier|final
name|Account
operator|.
name|FieldName
name|field
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|authenticate (final AuthRequest who)
specifier|public
name|AuthRequest
name|authenticate
parameter_list|(
specifier|final
name|AuthRequest
name|who
parameter_list|)
block|{
if|if
condition|(
name|who
operator|.
name|getEmailAddress
argument_list|()
operator|==
literal|null
operator|&&
name|who
operator|.
name|getLocalUser
argument_list|()
operator|!=
literal|null
operator|&&
name|emailExpander
operator|.
name|canExpand
argument_list|(
name|who
operator|.
name|getLocalUser
argument_list|()
argument_list|)
condition|)
block|{
name|who
operator|.
name|setEmailAddress
argument_list|(
name|emailExpander
operator|.
name|expand
argument_list|(
name|who
operator|.
name|getLocalUser
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|who
return|;
block|}
annotation|@
name|Override
DECL|method|onCreateAccount (final AuthRequest who, final Account account)
specifier|public
name|void
name|onCreateAccount
parameter_list|(
specifier|final
name|AuthRequest
name|who
parameter_list|,
specifier|final
name|Account
name|account
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|groups (final AccountState who)
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groups
parameter_list|(
specifier|final
name|AccountState
name|who
parameter_list|)
block|{
return|return
name|who
operator|.
name|getInternalGroups
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lookup (final String accountName)
specifier|public
name|Account
operator|.
name|Id
name|lookup
parameter_list|(
specifier|final
name|String
name|accountName
parameter_list|)
block|{
if|if
condition|(
name|emailExpander
operator|.
name|canExpand
argument_list|(
name|accountName
argument_list|)
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|c
init|=
name|byEmail
operator|.
name|get
argument_list|(
name|emailExpander
operator|.
name|expand
argument_list|(
name|accountName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|1
operator|==
name|c
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|c
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|lookupGroups (String name)
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|ExternalNameKey
argument_list|>
name|lookupGroups
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

