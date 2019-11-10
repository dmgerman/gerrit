begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Supplier
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
name|server
operator|.
name|data
operator|.
name|AccountAttribute
import|;
end_import

begin_class
DECL|class|ReviewerAddedEvent
specifier|public
class|class
name|ReviewerAddedEvent
extends|extends
name|PatchSetEvent
block|{
DECL|field|TYPE
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"reviewer-added"
decl_stmt|;
DECL|field|reviewer
specifier|public
name|Supplier
argument_list|<
name|AccountAttribute
argument_list|>
name|reviewer
decl_stmt|;
DECL|field|adder
specifier|public
name|Supplier
argument_list|<
name|AccountAttribute
argument_list|>
name|adder
decl_stmt|;
DECL|method|ReviewerAddedEvent (Change change)
specifier|public
name|ReviewerAddedEvent
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|super
argument_list|(
name|TYPE
argument_list|,
name|change
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

