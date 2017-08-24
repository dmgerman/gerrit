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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|primitives
operator|.
name|Ints
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
name|index
operator|.
name|query
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|index
operator|.
name|query
operator|.
name|QueryBuilder
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
name|Project
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
name|account
operator|.
name|AccountField
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
DECL|class|AccountPredicates
specifier|public
class|class
name|AccountPredicates
block|{
DECL|method|hasActive (Predicate<AccountState> p)
specifier|public
specifier|static
name|boolean
name|hasActive
parameter_list|(
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|p
parameter_list|)
block|{
return|return
name|QueryBuilder
operator|.
name|find
argument_list|(
name|p
argument_list|,
name|AccountPredicate
operator|.
name|class
argument_list|,
name|AccountField
operator|.
name|ACTIVE
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|andActive (Predicate<AccountState> p)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|andActive
parameter_list|(
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|p
parameter_list|)
block|{
return|return
name|Predicate
operator|.
name|and
argument_list|(
name|p
argument_list|,
name|isActive
argument_list|()
argument_list|)
return|;
block|}
DECL|method|defaultPredicate (String query)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|defaultPredicate
parameter_list|(
name|String
name|query
parameter_list|)
block|{
comment|// Adapt the capacity of this list when adding more default predicates.
name|List
argument_list|<
name|Predicate
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|preds
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Integer
name|id
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|preds
operator|.
name|add
argument_list|(
name|id
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|preds
operator|.
name|add
argument_list|(
name|equalsName
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|preds
operator|.
name|add
argument_list|(
name|username
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
comment|// Adapt the capacity of the "predicates" list when adding more default
comment|// predicates.
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|preds
argument_list|)
return|;
block|}
DECL|method|id (Account.Id accountId)
specifier|public
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
specifier|public
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
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|method|preferredEmail (String email)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|preferredEmail
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
name|PREFERRED_EMAIL
argument_list|,
name|AccountQueryBuilder
operator|.
name|FIELD_PREFERRED_EMAIL
argument_list|,
name|email
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|method|preferredEmailExact (String email)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|preferredEmailExact
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
name|PREFERRED_EMAIL_EXACT
argument_list|,
name|AccountQueryBuilder
operator|.
name|FIELD_PREFERRED_EMAIL_EXACT
argument_list|,
name|email
argument_list|)
return|;
block|}
DECL|method|equalsName (String name)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|equalsName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|NAME_PART
argument_list|,
name|AccountQueryBuilder
operator|.
name|FIELD_NAME
argument_list|,
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|method|externalId (String externalId)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|externalId
parameter_list|(
name|String
name|externalId
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|EXTERNAL_ID
argument_list|,
name|externalId
argument_list|)
return|;
block|}
DECL|method|fullName (String fullName)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|fullName
parameter_list|(
name|String
name|fullName
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|FULL_NAME
argument_list|,
name|fullName
argument_list|)
return|;
block|}
DECL|method|isActive ()
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|isActive
parameter_list|()
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|ACTIVE
argument_list|,
literal|"1"
argument_list|)
return|;
block|}
DECL|method|isNotActive ()
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|isNotActive
parameter_list|()
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|ACTIVE
argument_list|,
literal|"0"
argument_list|)
return|;
block|}
DECL|method|username (String username)
specifier|public
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
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|method|watchedProject (Project.NameKey project)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|watchedProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
return|return
operator|new
name|AccountPredicate
argument_list|(
name|AccountField
operator|.
name|WATCHED_PROJECT
argument_list|,
name|project
operator|.
name|get
argument_list|()
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
block|{}
block|}
end_class

end_unit

