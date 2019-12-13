begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.change
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
name|change
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
name|Change
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
comment|/** Bundle of service classes that make up the change index. */
end_comment

begin_class
DECL|class|ChangeIndexDefinition
specifier|public
class|class
name|ChangeIndexDefinition
extends|extends
name|IndexDefinition
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|,
name|ChangeIndex
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|ChangeIndexDefinition ( ChangeIndexCollection indexCollection, ChangeIndex.Factory indexFactory, @Nullable AllChangesIndexer allChangesIndexer)
name|ChangeIndexDefinition
parameter_list|(
name|ChangeIndexCollection
name|indexCollection
parameter_list|,
name|ChangeIndex
operator|.
name|Factory
name|indexFactory
parameter_list|,
annotation|@
name|Nullable
name|AllChangesIndexer
name|allChangesIndexer
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
argument_list|,
name|indexCollection
argument_list|,
name|indexFactory
argument_list|,
name|allChangesIndexer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

