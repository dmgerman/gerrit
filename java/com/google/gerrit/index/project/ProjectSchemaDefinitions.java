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
DECL|package|com.google.gerrit.index.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|project
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|SchemaUtil
operator|.
name|schema
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
name|Schema
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
name|SchemaDefinitions
import|;
end_import

begin_comment
comment|/** Definition of project index versions (schemata). See {@link SchemaDefinitions}. */
end_comment

begin_class
DECL|class|ProjectSchemaDefinitions
specifier|public
class|class
name|ProjectSchemaDefinitions
extends|extends
name|SchemaDefinitions
argument_list|<
name|ProjectData
argument_list|>
block|{
annotation|@
name|Deprecated
DECL|field|V1
specifier|static
specifier|final
name|Schema
argument_list|<
name|ProjectData
argument_list|>
name|V1
init|=
name|schema
argument_list|(
name|ProjectField
operator|.
name|NAME
argument_list|,
name|ProjectField
operator|.
name|DESCRIPTION
argument_list|,
name|ProjectField
operator|.
name|PARENT_NAME
argument_list|,
name|ProjectField
operator|.
name|NAME_PART
argument_list|,
name|ProjectField
operator|.
name|ANCESTOR_NAME
argument_list|)
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|V2
specifier|static
specifier|final
name|Schema
argument_list|<
name|ProjectData
argument_list|>
name|V2
init|=
name|schema
argument_list|(
name|V1
argument_list|,
name|ProjectField
operator|.
name|STATE
argument_list|,
name|ProjectField
operator|.
name|REF_STATE
argument_list|)
decl_stmt|;
comment|// Bump Lucene version requires reindexing
DECL|field|V3
annotation|@
name|Deprecated
specifier|static
specifier|final
name|Schema
argument_list|<
name|ProjectData
argument_list|>
name|V3
init|=
name|schema
argument_list|(
name|V2
argument_list|)
decl_stmt|;
comment|// Lucene index was changed to add an additional field for sorting.
DECL|field|V4
specifier|static
specifier|final
name|Schema
argument_list|<
name|ProjectData
argument_list|>
name|V4
init|=
name|schema
argument_list|(
name|V3
argument_list|)
decl_stmt|;
comment|/**    * Name of the project index to be used when contacting index backends or loading configurations.    */
DECL|field|NAME
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"projects"
decl_stmt|;
comment|/** Singleton instance of the schema definitions. This is one per JVM. */
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|ProjectSchemaDefinitions
name|INSTANCE
init|=
operator|new
name|ProjectSchemaDefinitions
argument_list|()
decl_stmt|;
DECL|method|ProjectSchemaDefinitions ()
specifier|private
name|ProjectSchemaDefinitions
parameter_list|()
block|{
name|super
argument_list|(
name|NAME
argument_list|,
name|ProjectData
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

