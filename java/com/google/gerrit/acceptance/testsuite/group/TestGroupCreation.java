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
DECL|package|com.google.gerrit.acceptance.testsuite.group
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
name|group
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
name|acceptance
operator|.
name|testsuite
operator|.
name|ThrowingFunction
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

begin_class
annotation|@
name|AutoValue
DECL|class|TestGroupCreation
specifier|public
specifier|abstract
class|class
name|TestGroupCreation
block|{
DECL|method|name ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|name
parameter_list|()
function_decl|;
DECL|method|description ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|()
function_decl|;
DECL|method|groupCreator ()
specifier|abstract
name|ThrowingFunction
argument_list|<
name|TestGroupCreation
argument_list|,
name|TestGroup
argument_list|>
name|groupCreator
parameter_list|()
function_decl|;
DECL|method|builder (ThrowingFunction<TestGroupCreation, TestGroup> groupCreator)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|ThrowingFunction
argument_list|<
name|TestGroupCreation
argument_list|,
name|TestGroup
argument_list|>
name|groupCreator
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestGroupCreation
operator|.
name|Builder
argument_list|()
operator|.
name|groupCreator
argument_list|(
name|groupCreator
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
DECL|method|name (String name)
specifier|public
specifier|abstract
name|Builder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|description (String description)
specifier|public
specifier|abstract
name|Builder
name|description
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
DECL|method|clearDescription ()
specifier|public
name|Builder
name|clearDescription
parameter_list|()
block|{
return|return
name|description
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|groupCreator (ThrowingFunction<TestGroupCreation, TestGroup> groupCreator)
specifier|abstract
name|Builder
name|groupCreator
parameter_list|(
name|ThrowingFunction
argument_list|<
name|TestGroupCreation
argument_list|,
name|TestGroup
argument_list|>
name|groupCreator
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|TestGroupCreation
name|autoBuild
parameter_list|()
function_decl|;
comment|/**      * Executes the group creation as specified.      *      * @return the created {@code TestGroup}      */
DECL|method|create ()
specifier|public
name|TestGroup
name|create
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGroupCreation
name|groupCreation
init|=
name|autoBuild
argument_list|()
decl_stmt|;
return|return
name|groupCreation
operator|.
name|groupCreator
argument_list|()
operator|.
name|apply
argument_list|(
name|groupCreation
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

