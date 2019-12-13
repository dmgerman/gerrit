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
comment|// limitations under the License
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
name|common
operator|.
name|Nullable
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
name|server
operator|.
name|group
operator|.
name|InternalGroup
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

begin_comment
comment|/** Bundle of service classes that make up the group index. */
end_comment

begin_class
DECL|class|GroupIndexDefinition
specifier|public
class|class
name|GroupIndexDefinition
extends|extends
name|IndexDefinition
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|InternalGroup
argument_list|,
name|GroupIndex
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|GroupIndexDefinition ( GroupIndexCollection indexCollection, GroupIndex.Factory indexFactory, @Nullable AllGroupsIndexer allGroupsIndexer)
name|GroupIndexDefinition
parameter_list|(
name|GroupIndexCollection
name|indexCollection
parameter_list|,
name|GroupIndex
operator|.
name|Factory
name|indexFactory
parameter_list|,
annotation|@
name|Nullable
name|AllGroupsIndexer
name|allGroupsIndexer
parameter_list|)
block|{
name|super
argument_list|(
name|GroupSchemaDefinitions
operator|.
name|INSTANCE
argument_list|,
name|indexCollection
argument_list|,
name|indexFactory
argument_list|,
name|allGroupsIndexer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

