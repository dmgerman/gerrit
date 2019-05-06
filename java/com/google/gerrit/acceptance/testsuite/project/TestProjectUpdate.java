begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
operator|.
name|project
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|acceptance
operator|.
name|testsuite
operator|.
name|ThrowingConsumer
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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

begin_class
annotation|@
name|AutoValue
DECL|class|TestProjectUpdate
specifier|public
specifier|abstract
class|class
name|TestProjectUpdate
block|{
comment|/** Starts a builder for allowing a permission. */
DECL|method|allow (String name)
specifier|public
specifier|static
name|TestPermission
operator|.
name|Builder
name|allow
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|TestPermission
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|action
argument_list|(
name|PermissionRule
operator|.
name|Action
operator|.
name|ALLOW
argument_list|)
return|;
block|}
comment|/** Starts a builder for denying a permission. */
DECL|method|deny (String name)
specifier|public
specifier|static
name|TestPermission
operator|.
name|Builder
name|deny
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|TestPermission
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|action
argument_list|(
name|PermissionRule
operator|.
name|Action
operator|.
name|DENY
argument_list|)
return|;
block|}
comment|/** Starts a builder for blocking a permission. */
DECL|method|block (String name)
specifier|public
specifier|static
name|TestPermission
operator|.
name|Builder
name|block
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|TestPermission
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|action
argument_list|(
name|PermissionRule
operator|.
name|Action
operator|.
name|BLOCK
argument_list|)
return|;
block|}
comment|/**    * Records a permission to be updated.    *    *<p>Not used for permissions that have ranges (label permissions) or global capabilities.    */
annotation|@
name|AutoValue
DECL|class|TestPermission
specifier|public
specifier|abstract
specifier|static
class|class
name|TestPermission
block|{
DECL|method|builder ()
specifier|private
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_TestProjectUpdate_TestPermission
operator|.
name|Builder
argument_list|()
operator|.
name|force
argument_list|(
literal|false
argument_list|)
return|;
block|}
DECL|method|name ()
specifier|abstract
name|String
name|name
parameter_list|()
function_decl|;
DECL|method|ref ()
specifier|abstract
name|String
name|ref
parameter_list|()
function_decl|;
DECL|method|group ()
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|()
function_decl|;
DECL|method|action ()
specifier|abstract
name|PermissionRule
operator|.
name|Action
name|action
parameter_list|()
function_decl|;
DECL|method|force ()
specifier|abstract
name|boolean
name|force
parameter_list|()
function_decl|;
comment|/** Builder for {@link TestPermission}. */
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
DECL|method|name (String name)
specifier|abstract
name|Builder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/** Sets the ref pattern used on the permission. */
DECL|method|ref (String ref)
specifier|public
specifier|abstract
name|Builder
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
function_decl|;
comment|/** Sets the group to which the permission applies. */
DECL|method|group (AccountGroup.UUID groupUuid)
specifier|public
specifier|abstract
name|Builder
name|group
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
function_decl|;
DECL|method|action (PermissionRule.Action action)
specifier|abstract
name|Builder
name|action
parameter_list|(
name|PermissionRule
operator|.
name|Action
name|action
parameter_list|)
function_decl|;
comment|/** Sets whether the permission is a force permission. */
DECL|method|force (boolean force)
specifier|public
specifier|abstract
name|Builder
name|force
parameter_list|(
name|boolean
name|force
parameter_list|)
function_decl|;
comment|/** Builds the {@link TestPermission}. */
DECL|method|build ()
specifier|public
specifier|abstract
name|TestPermission
name|build
parameter_list|()
function_decl|;
block|}
block|}
DECL|method|builder (ThrowingConsumer<TestProjectUpdate> projectUpdater)
specifier|static
name|Builder
name|builder
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|TestProjectUpdate
argument_list|>
name|projectUpdater
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestProjectUpdate
operator|.
name|Builder
argument_list|()
operator|.
name|projectUpdater
argument_list|(
name|projectUpdater
argument_list|)
return|;
block|}
comment|/** Builder for {@link TestProjectUpdate}. */
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
DECL|method|addedPermissionsBuilder ()
specifier|abstract
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|TestPermission
argument_list|>
name|addedPermissionsBuilder
parameter_list|()
function_decl|;
comment|/** Adds a permission to be included in this update. */
DECL|method|add (TestPermission testPermission)
specifier|public
name|Builder
name|add
parameter_list|(
name|TestPermission
name|testPermission
parameter_list|)
block|{
name|addedPermissionsBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|testPermission
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Adds a permission to be included in this update. */
DECL|method|add (TestPermission.Builder testPermissionBuilder)
specifier|public
name|Builder
name|add
parameter_list|(
name|TestPermission
operator|.
name|Builder
name|testPermissionBuilder
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|testPermissionBuilder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|projectUpdater (ThrowingConsumer<TestProjectUpdate> projectUpdater)
specifier|abstract
name|Builder
name|projectUpdater
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|TestProjectUpdate
argument_list|>
name|projectUpdater
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|TestProjectUpdate
name|autoBuild
parameter_list|()
function_decl|;
comment|/** Executes the update, updating the underlying project. */
DECL|method|update ()
specifier|public
name|void
name|update
parameter_list|()
block|{
name|TestProjectUpdate
name|projectUpdate
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|projectUpdate
operator|.
name|projectUpdater
argument_list|()
operator|.
name|acceptAndThrowSilently
argument_list|(
name|projectUpdate
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addedPermissions ()
specifier|abstract
name|ImmutableList
argument_list|<
name|TestPermission
argument_list|>
name|addedPermissions
parameter_list|()
function_decl|;
DECL|method|projectUpdater ()
specifier|abstract
name|ThrowingConsumer
argument_list|<
name|TestProjectUpdate
argument_list|>
name|projectUpdater
parameter_list|()
function_decl|;
block|}
end_class

end_unit

