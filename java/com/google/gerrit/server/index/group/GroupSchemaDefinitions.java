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

begin_comment
comment|/** Definition of group index versions (schemata). See {@link SchemaDefinitions}. */
end_comment

begin_class
DECL|class|GroupSchemaDefinitions
specifier|public
class|class
name|GroupSchemaDefinitions
extends|extends
name|SchemaDefinitions
argument_list|<
name|InternalGroup
argument_list|>
block|{
annotation|@
name|Deprecated
DECL|field|V2
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V2
init|=
name|schema
argument_list|(
name|GroupField
operator|.
name|DESCRIPTION
argument_list|,
name|GroupField
operator|.
name|ID
argument_list|,
name|GroupField
operator|.
name|IS_VISIBLE_TO_ALL
argument_list|,
name|GroupField
operator|.
name|NAME
argument_list|,
name|GroupField
operator|.
name|NAME_PART
argument_list|,
name|GroupField
operator|.
name|OWNER_UUID
argument_list|,
name|GroupField
operator|.
name|UUID
argument_list|)
decl_stmt|;
DECL|field|V3
annotation|@
name|Deprecated
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V3
init|=
name|schema
argument_list|(
name|V2
argument_list|,
name|GroupField
operator|.
name|CREATED_ON
argument_list|)
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|V4
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V4
init|=
name|schema
argument_list|(
name|V3
argument_list|,
name|GroupField
operator|.
name|MEMBER
argument_list|,
name|GroupField
operator|.
name|SUBGROUP
argument_list|)
decl_stmt|;
DECL|field|V5
annotation|@
name|Deprecated
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V5
init|=
name|schema
argument_list|(
name|V4
argument_list|,
name|GroupField
operator|.
name|REF_STATE
argument_list|)
decl_stmt|;
comment|// Bump Lucene version requires reindexing
DECL|field|V6
annotation|@
name|Deprecated
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V6
init|=
name|schema
argument_list|(
name|V5
argument_list|)
decl_stmt|;
comment|// Lucene index was changed to add an additional field for sorting.
DECL|field|V7
annotation|@
name|Deprecated
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V7
init|=
name|schema
argument_list|(
name|V6
argument_list|)
decl_stmt|;
comment|// New numeric types: use dimensional points using the k-d tree geo-spatial data structure
comment|// to offer fast single- and multi-dimensional numeric range.
DECL|field|V8
specifier|static
specifier|final
name|Schema
argument_list|<
name|InternalGroup
argument_list|>
name|V8
init|=
name|schema
argument_list|(
name|V7
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|/** Singleton instance of the schema definitions. This is one per JVM. */
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|GroupSchemaDefinitions
name|INSTANCE
init|=
operator|new
name|GroupSchemaDefinitions
argument_list|()
decl_stmt|;
DECL|method|GroupSchemaDefinitions ()
specifier|private
name|GroupSchemaDefinitions
parameter_list|()
block|{
name|super
argument_list|(
literal|"groups"
argument_list|,
name|InternalGroup
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

