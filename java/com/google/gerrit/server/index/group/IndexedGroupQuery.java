begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.group
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
name|group
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
name|AccountGroup
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
name|index
operator|.
name|QueryOptions
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
name|DataSource
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
name|IndexedQuery
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
name|QueryParseException
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
name|group
operator|.
name|InternalGroup
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
comment|/**  * Wrapper around {@link Predicate}s that is returned by the {@link  * com.google.gerrit.index.IndexRewriter}. See {@link IndexedQuery}.  */
end_comment

begin_class
DECL|class|IndexedGroupQuery
specifier|public
class|class
name|IndexedGroupQuery
extends|extends
name|IndexedQuery
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|InternalGroup
argument_list|>
implements|implements
name|DataSource
argument_list|<
name|InternalGroup
argument_list|>
block|{
DECL|method|createOptions ( IndexConfig config, int start, int limit, Set<String> fields)
specifier|public
specifier|static
name|QueryOptions
name|createOptions
parameter_list|(
name|IndexConfig
name|config
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
comment|// Always include GroupField.UUID since it is needed to load the group from NoteDb.
if|if
condition|(
operator|!
name|fields
operator|.
name|contains
argument_list|(
name|GroupField
operator|.
name|UUID
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|fields
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|fields
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|GroupField
operator|.
name|UUID
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|QueryOptions
operator|.
name|create
argument_list|(
name|config
argument_list|,
name|start
argument_list|,
name|limit
argument_list|,
name|fields
argument_list|)
return|;
block|}
DECL|method|IndexedGroupQuery ( Index<AccountGroup.UUID, InternalGroup> index, Predicate<InternalGroup> pred, QueryOptions opts)
specifier|public
name|IndexedGroupQuery
parameter_list|(
name|Index
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|InternalGroup
argument_list|>
name|index
parameter_list|,
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|pred
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|index
argument_list|,
name|pred
argument_list|,
name|opts
operator|.
name|convertForBackend
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

