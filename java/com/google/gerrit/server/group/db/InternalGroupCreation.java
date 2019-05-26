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
DECL|package|com.google.gerrit.server.group.db
package|package
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
name|db
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

begin_comment
comment|/**  * Definition of all properties necessary for a group creation.  *  *<p>An instance of {@code InternalGroupCreation} is a blueprint for a group which should be  * created.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|InternalGroupCreation
specifier|public
specifier|abstract
class|class
name|InternalGroupCreation
block|{
comment|/** Defines the numeric ID the group should have. */
DECL|method|getId ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|Id
name|getId
parameter_list|()
function_decl|;
comment|/** Defines the name the group should have. */
DECL|method|getNameKey ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|NameKey
name|getNameKey
parameter_list|()
function_decl|;
comment|/** Defines the UUID the group should have. */
DECL|method|getGroupUUID ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
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
name|AutoValue_InternalGroupCreation
operator|.
name|Builder
argument_list|()
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
comment|/** @see #getId() */
DECL|method|setId (AccountGroup.Id id)
specifier|public
specifier|abstract
name|InternalGroupCreation
operator|.
name|Builder
name|setId
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
comment|/** @see #getNameKey() */
DECL|method|setNameKey (AccountGroup.NameKey name)
specifier|public
specifier|abstract
name|InternalGroupCreation
operator|.
name|Builder
name|setNameKey
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|)
function_decl|;
comment|/** @see #getGroupUUID() */
DECL|method|setGroupUUID (AccountGroup.UUID groupUuid)
specifier|public
specifier|abstract
name|InternalGroupCreation
operator|.
name|Builder
name|setGroupUUID
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|InternalGroupCreation
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

