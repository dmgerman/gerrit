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
name|IndexConfig
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
name|AccountIndexCollection
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
name|InternalQuery
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|List
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
DECL|class|InternalAccountQuery
specifier|public
class|class
name|InternalAccountQuery
extends|extends
name|InternalQuery
argument_list|<
name|AccountState
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|InternalAccountQuery (AccountQueryProcessor queryProcessor, AccountIndexCollection indexes, IndexConfig indexConfig)
name|InternalAccountQuery
parameter_list|(
name|AccountQueryProcessor
name|queryProcessor
parameter_list|,
name|AccountIndexCollection
name|indexes
parameter_list|,
name|IndexConfig
name|indexConfig
parameter_list|)
block|{
name|super
argument_list|(
name|queryProcessor
argument_list|,
name|indexes
argument_list|,
name|indexConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setLimit (int n)
specifier|public
name|InternalAccountQuery
name|setLimit
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|super
operator|.
name|setLimit
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|enforceVisibility (boolean enforce)
specifier|public
name|InternalAccountQuery
name|enforceVisibility
parameter_list|(
name|boolean
name|enforce
parameter_list|)
block|{
name|super
operator|.
name|enforceVisibility
argument_list|(
name|enforce
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|setRequestedFields (Set<String> fields)
specifier|public
name|InternalAccountQuery
name|setRequestedFields
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
name|super
operator|.
name|setRequestedFields
argument_list|(
name|fields
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|noFields ()
specifier|public
name|InternalAccountQuery
name|noFields
parameter_list|()
block|{
name|super
operator|.
name|noFields
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|byExternalId (String externalId)
specifier|public
name|List
argument_list|<
name|AccountState
argument_list|>
name|byExternalId
parameter_list|(
name|String
name|externalId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|AccountPredicates
operator|.
name|externalId
argument_list|(
name|externalId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byWatchedProject (Project.NameKey project)
specifier|public
name|List
argument_list|<
name|AccountState
argument_list|>
name|byWatchedProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|AccountPredicates
operator|.
name|watchedProject
argument_list|(
name|project
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

