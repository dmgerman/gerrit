begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account.externalids
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
operator|.
name|externalids
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSetMultimap
operator|.
name|toImmutableSetMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ImmutableSetMultimap
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
name|SetMultimap
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
name|Account
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

begin_comment
comment|/** Cache value containing all external IDs. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|AllExternalIds
specifier|public
specifier|abstract
class|class
name|AllExternalIds
block|{
DECL|method|create (SetMultimap<Account.Id, ExternalId> byAccount)
specifier|static
name|AllExternalIds
name|create
parameter_list|(
name|SetMultimap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ExternalId
argument_list|>
name|byAccount
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AllExternalIds
argument_list|(
name|ImmutableSetMultimap
operator|.
name|copyOf
argument_list|(
name|byAccount
argument_list|)
argument_list|,
name|byEmailCopy
argument_list|(
name|byAccount
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (Collection<ExternalId> externalIds)
specifier|static
name|AllExternalIds
name|create
parameter_list|(
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AllExternalIds
argument_list|(
name|externalIds
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toImmutableSetMultimap
argument_list|(
name|e
lambda|->
name|e
operator|.
name|accountId
argument_list|()
argument_list|,
name|e
lambda|->
name|e
argument_list|)
argument_list|)
argument_list|,
name|byEmailCopy
argument_list|(
name|externalIds
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byEmailCopy ( Collection<ExternalId> externalIds)
specifier|private
specifier|static
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
name|byEmailCopy
parameter_list|(
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
return|return
name|externalIds
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|e
operator|.
name|email
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSetMultimap
argument_list|(
name|e
lambda|->
name|e
operator|.
name|email
argument_list|()
argument_list|,
name|e
lambda|->
name|e
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byAccount ()
specifier|public
specifier|abstract
name|ImmutableSetMultimap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ExternalId
argument_list|>
name|byAccount
parameter_list|()
function_decl|;
DECL|method|byEmail ()
specifier|public
specifier|abstract
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
name|byEmail
parameter_list|()
function_decl|;
block|}
end_class

end_unit

