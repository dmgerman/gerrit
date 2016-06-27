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
DECL|package|com.google.gerrit.server.query.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|account
operator|.
name|AccountState
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
name|index
operator|.
name|FieldDef
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
name|index
operator|.
name|IndexPredicate
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
name|index
operator|.
name|account
operator|.
name|AccountField
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
name|query
operator|.
name|Predicate
import|;
end_import

begin_class
DECL|class|AccountPredicates
specifier|public
class|class
name|AccountPredicates
block|{
DECL|method|id (Account.Id accountId)
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|id
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|ID
argument_list|,
name|AccountQueryBuilder
operator|.
name|FIELD_ACCOUNT
argument_list|,
name|accountId
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|email (String email)
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|email
parameter_list|(
name|String
name|email
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|EMAIL
argument_list|,
name|AccountQueryBuilder
operator|.
name|FIELD_EMAIL
argument_list|,
name|email
argument_list|)
return|;
block|}
DECL|method|username (String username)
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|USERNAME
argument_list|,
name|AccountQueryBuilder
operator|.
name|FIELD_USERNAME
argument_list|,
name|username
argument_list|)
return|;
block|}
DECL|class|AccountPredicate
specifier|static
class|class
name|AccountPredicate
extends|extends
name|IndexPredicate
argument_list|<
name|AccountState
argument_list|>
block|{
DECL|method|AccountPredicate (FieldDef<AccountState, ?> def, String value)
name|AccountPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|AccountState
argument_list|,
name|?
argument_list|>
name|def
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|AccountPredicate (FieldDef<AccountState, ?> def, String name, String value)
name|AccountPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|AccountState
argument_list|,
name|?
argument_list|>
name|def
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|AccountPredicates ()
specifier|private
name|AccountPredicates
parameter_list|()
block|{   }
block|}
end_class

end_unit

