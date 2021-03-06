begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|server
operator|.
name|util
operator|.
name|time
operator|.
name|TimeUtil
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_class
annotation|@
name|AutoValue
annotation|@
name|Ignore
DECL|class|TestGroup
specifier|public
specifier|abstract
class|class
name|TestGroup
block|{
DECL|method|getNameKey ()
specifier|abstract
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|NameKey
argument_list|>
name|getNameKey
parameter_list|()
function_decl|;
DECL|method|getGroupUuid ()
specifier|abstract
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getGroupUuid
parameter_list|()
function_decl|;
DECL|method|getId ()
specifier|abstract
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getId
parameter_list|()
function_decl|;
DECL|method|getCreatedOn ()
specifier|abstract
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|getCreatedOn
parameter_list|()
function_decl|;
DECL|method|getOwnerGroupUuid ()
specifier|abstract
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getOwnerGroupUuid
parameter_list|()
function_decl|;
DECL|method|getDescription ()
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|getDescription
parameter_list|()
function_decl|;
DECL|method|isVisibleToAll ()
specifier|abstract
name|boolean
name|isVisibleToAll
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_TestGroup
operator|.
name|Builder
argument_list|()
operator|.
name|setVisibleToAll
argument_list|(
literal|false
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|setNameKey (AccountGroup.NameKey nameKey)
specifier|public
specifier|abstract
name|Builder
name|setNameKey
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|nameKey
parameter_list|)
function_decl|;
DECL|method|setName (String name)
specifier|public
name|Builder
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|setNameKey
argument_list|(
name|AccountGroup
operator|.
name|nameKey
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setGroupUuid (AccountGroup.UUID uuid)
specifier|public
specifier|abstract
name|Builder
name|setGroupUuid
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
function_decl|;
DECL|method|setId (AccountGroup.Id id)
specifier|public
specifier|abstract
name|Builder
name|setId
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
DECL|method|setCreatedOn (Timestamp createdOn)
specifier|public
specifier|abstract
name|Builder
name|setCreatedOn
parameter_list|(
name|Timestamp
name|createdOn
parameter_list|)
function_decl|;
DECL|method|setOwnerGroupUuid (AccountGroup.UUID ownerGroupUuid)
specifier|public
specifier|abstract
name|Builder
name|setOwnerGroupUuid
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|ownerGroupUuid
parameter_list|)
function_decl|;
DECL|method|setDescription (String description)
specifier|public
specifier|abstract
name|Builder
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
DECL|method|setVisibleToAll (boolean visibleToAll)
specifier|public
specifier|abstract
name|Builder
name|setVisibleToAll
parameter_list|(
name|boolean
name|visibleToAll
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|public
specifier|abstract
name|TestGroup
name|autoBuild
parameter_list|()
function_decl|;
DECL|method|build ()
specifier|public
name|AccountGroup
name|build
parameter_list|()
block|{
name|TestGroup
name|testGroup
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|AccountGroup
operator|.
name|NameKey
name|name
init|=
name|testGroup
operator|.
name|getNameKey
argument_list|()
operator|.
name|orElse
argument_list|(
name|AccountGroup
operator|.
name|nameKey
argument_list|(
literal|"users"
argument_list|)
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|id
init|=
name|testGroup
operator|.
name|getId
argument_list|()
operator|.
name|orElse
argument_list|(
name|AccountGroup
operator|.
name|id
argument_list|(
name|Math
operator|.
name|abs
argument_list|(
name|name
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|uuid
init|=
name|testGroup
operator|.
name|getGroupUuid
argument_list|()
operator|.
name|orElse
argument_list|(
name|AccountGroup
operator|.
name|uuid
argument_list|(
name|name
operator|+
literal|"-UUID"
argument_list|)
argument_list|)
decl_stmt|;
name|Timestamp
name|createdOn
init|=
name|testGroup
operator|.
name|getCreatedOn
argument_list|()
operator|.
name|orElseGet
argument_list|(
name|TimeUtil
operator|::
name|nowTs
argument_list|)
decl_stmt|;
name|AccountGroup
name|accountGroup
init|=
operator|new
name|AccountGroup
argument_list|(
name|name
argument_list|,
name|id
argument_list|,
name|uuid
argument_list|,
name|createdOn
argument_list|)
decl_stmt|;
name|testGroup
operator|.
name|getOwnerGroupUuid
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|accountGroup
operator|::
name|setOwnerGroupUUID
argument_list|)
expr_stmt|;
name|testGroup
operator|.
name|getDescription
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|accountGroup
operator|::
name|setDescription
argument_list|)
expr_stmt|;
name|accountGroup
operator|.
name|setVisibleToAll
argument_list|(
name|testGroup
operator|.
name|isVisibleToAll
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|accountGroup
return|;
block|}
block|}
block|}
end_class

end_unit

