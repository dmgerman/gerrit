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
name|ThrowingConsumer
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
DECL|class|TestGroupUpdate
specifier|public
specifier|abstract
class|class
name|TestGroupUpdate
block|{
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
DECL|method|groupUpdater ()
specifier|abstract
name|ThrowingConsumer
argument_list|<
name|TestGroupUpdate
argument_list|>
name|groupUpdater
parameter_list|()
function_decl|;
DECL|method|builder (ThrowingConsumer<TestGroupUpdate> groupUpdater)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|TestGroupUpdate
argument_list|>
name|groupUpdater
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestGroupUpdate
operator|.
name|Builder
argument_list|()
operator|.
name|groupUpdater
argument_list|(
name|groupUpdater
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
DECL|method|groupUpdater (ThrowingConsumer<TestGroupUpdate> groupUpdater)
specifier|abstract
name|Builder
name|groupUpdater
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|TestGroupUpdate
argument_list|>
name|groupUpdater
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|TestGroupUpdate
name|autoBuild
parameter_list|()
function_decl|;
comment|/** Executes the group update as specified. */
DECL|method|update ()
specifier|public
name|void
name|update
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGroupUpdate
name|groupUpdater
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|groupUpdater
operator|.
name|groupUpdater
argument_list|()
operator|.
name|accept
argument_list|(
name|groupUpdater
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

