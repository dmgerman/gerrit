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
DECL|package|com.google.gerrit.server.index.account
package|package
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
name|index
operator|.
name|Index
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
name|IndexDefinition
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
name|query
operator|.
name|account
operator|.
name|AccountPredicates
import|;
end_import

begin_comment
comment|/**  * Index for Gerrit accounts. This class is mainly used for typing the generic parent class that  * contains actual implementations.  */
end_comment

begin_interface
DECL|interface|AccountIndex
specifier|public
interface|interface
name|AccountIndex
extends|extends
name|Index
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
extends|extends
name|IndexDefinition
operator|.
name|IndexFactory
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|,
name|AccountIndex
argument_list|>
block|{}
annotation|@
name|Override
DECL|method|keyPredicate (Account.Id id)
specifier|default
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|keyPredicate
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|AccountPredicates
operator|.
name|id
argument_list|(
name|getSchema
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

