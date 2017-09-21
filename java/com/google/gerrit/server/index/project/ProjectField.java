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
DECL|package|com.google.gerrit.server.index.project
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
name|FieldDef
operator|.
name|exact
import|;
end_import

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
name|FieldDef
operator|.
name|fullText
import|;
end_import

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
name|FieldDef
operator|.
name|prefix
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
name|Iterables
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
name|SchemaUtil
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
name|project
operator|.
name|ProjectData
import|;
end_import

begin_comment
comment|/** Index schema for projects. */
end_comment

begin_class
DECL|class|ProjectField
specifier|public
class|class
name|ProjectField
block|{
DECL|field|NAME
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ProjectData
argument_list|,
name|String
argument_list|>
name|NAME
init|=
name|exact
argument_list|(
literal|"name"
argument_list|)
operator|.
name|stored
argument_list|()
operator|.
name|build
argument_list|(
name|p
lambda|->
name|p
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|DESCRIPTION
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ProjectData
argument_list|,
name|String
argument_list|>
name|DESCRIPTION
init|=
name|fullText
argument_list|(
literal|"description"
argument_list|)
operator|.
name|build
argument_list|(
name|p
lambda|->
name|p
operator|.
name|getProject
argument_list|()
operator|.
name|getDescription
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|PARENT_NAME
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ProjectData
argument_list|,
name|String
argument_list|>
name|PARENT_NAME
init|=
name|exact
argument_list|(
literal|"parent_name"
argument_list|)
operator|.
name|build
argument_list|(
name|p
lambda|->
name|p
operator|.
name|getProject
argument_list|()
operator|.
name|getParentName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|NAME_PART
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ProjectData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|NAME_PART
init|=
name|prefix
argument_list|(
literal|"name_part"
argument_list|)
operator|.
name|buildRepeatable
argument_list|(
name|p
lambda|->
name|SchemaUtil
operator|.
name|getNameParts
argument_list|(
name|p
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|ANCESTOR_NAME
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ProjectData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|ANCESTOR_NAME
init|=
name|exact
argument_list|(
literal|"ancestor_name"
argument_list|)
operator|.
name|buildRepeatable
argument_list|(
name|p
lambda|->
name|Iterables
operator|.
name|transform
argument_list|(
name|p
operator|.
name|getAncestors
argument_list|()
argument_list|,
name|n
lambda|->
name|n
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
block|}
end_class

end_unit

