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
name|server
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
name|server
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
name|server
operator|.
name|index
operator|.
name|FieldDef
operator|.
name|integer
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
name|server
operator|.
name|index
operator|.
name|FieldDef
operator|.
name|prefix
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
name|server
operator|.
name|index
operator|.
name|FieldDef
operator|.
name|timestamp
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
name|SchemaUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Secondary index schemas for groups. */
end_comment

begin_class
DECL|class|GroupField
specifier|public
class|class
name|GroupField
block|{
comment|/** Legacy group ID. */
DECL|field|ID
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
argument_list|,
name|Integer
argument_list|>
name|ID
init|=
name|integer
argument_list|(
literal|"id"
argument_list|)
operator|.
name|build
argument_list|(
name|g
lambda|->
name|g
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Group UUID. */
DECL|field|UUID
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
argument_list|,
name|String
argument_list|>
name|UUID
init|=
name|exact
argument_list|(
literal|"uuid"
argument_list|)
operator|.
name|stored
argument_list|()
operator|.
name|build
argument_list|(
name|g
lambda|->
name|g
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Group owner UUID. */
DECL|field|OWNER_UUID
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
argument_list|,
name|String
argument_list|>
name|OWNER_UUID
init|=
name|exact
argument_list|(
literal|"owner_uuid"
argument_list|)
operator|.
name|build
argument_list|(
name|g
lambda|->
name|g
operator|.
name|getOwnerGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Timestamp indicating when this group was created. */
DECL|field|CREATED_ON
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
argument_list|,
name|Timestamp
argument_list|>
name|CREATED_ON
init|=
name|timestamp
argument_list|(
literal|"created_on"
argument_list|)
operator|.
name|build
argument_list|(
name|AccountGroup
operator|::
name|getCreatedOn
argument_list|)
decl_stmt|;
comment|/** Group name. */
DECL|field|NAME
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
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
name|build
argument_list|(
name|AccountGroup
operator|::
name|getName
argument_list|)
decl_stmt|;
comment|/** Prefix match on group name parts. */
DECL|field|NAME_PART
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
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
name|g
lambda|->
name|SchemaUtil
operator|.
name|getNameParts
argument_list|(
name|g
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|/** Group description. */
DECL|field|DESCRIPTION
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
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
name|AccountGroup
operator|::
name|getDescription
argument_list|)
decl_stmt|;
comment|/** Whether the group is visible to all users. */
DECL|field|IS_VISIBLE_TO_ALL
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|AccountGroup
argument_list|,
name|String
argument_list|>
name|IS_VISIBLE_TO_ALL
init|=
name|exact
argument_list|(
literal|"is_visible_to_all"
argument_list|)
operator|.
name|build
argument_list|(
name|g
lambda|->
name|g
operator|.
name|isVisibleToAll
argument_list|()
condition|?
literal|"1"
else|:
literal|"0"
argument_list|)
decl_stmt|;
block|}
end_class

end_unit

