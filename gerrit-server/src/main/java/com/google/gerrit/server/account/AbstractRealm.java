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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|collect
operator|.
name|Sets
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
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|IdentifiedUser
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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

begin_comment
comment|/** Basic implementation of {@link Realm}.  */
end_comment

begin_class
DECL|class|AbstractRealm
specifier|public
specifier|abstract
class|class
name|AbstractRealm
implements|implements
name|Realm
block|{
annotation|@
name|Override
DECL|method|hasEmailAddress (IdentifiedUser user, String email)
specifier|public
name|boolean
name|hasEmailAddress
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|String
name|email
parameter_list|)
block|{
for|for
control|(
name|AccountExternalId
name|ext
range|:
name|user
operator|.
name|state
argument_list|()
operator|.
name|getExternalIds
argument_list|()
control|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|ext
operator|.
name|getEmailAddress
argument_list|()
argument_list|,
name|email
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getEmailAddresses (IdentifiedUser user)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEmailAddresses
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
init|=
name|user
operator|.
name|state
argument_list|()
operator|.
name|getExternalIds
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|emails
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|ids
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountExternalId
name|ext
range|:
name|ids
control|)
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|ext
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
condition|)
block|{
name|emails
operator|.
name|add
argument_list|(
name|ext
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|emails
return|;
block|}
block|}
end_class

end_unit

