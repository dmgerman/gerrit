begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Branch
import|;
end_import

begin_class
DECL|class|RefEvent
specifier|public
specifier|abstract
class|class
name|RefEvent
extends|extends
name|ProjectEvent
block|{
DECL|method|RefEvent (String type)
specifier|protected
name|RefEvent
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|getBranchNameKey ()
specifier|public
name|Branch
operator|.
name|NameKey
name|getBranchNameKey
parameter_list|()
block|{
return|return
name|Branch
operator|.
name|nameKey
argument_list|(
name|getProjectNameKey
argument_list|()
argument_list|,
name|getRefName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRefName ()
specifier|public
specifier|abstract
name|String
name|getRefName
parameter_list|()
function_decl|;
block|}
end_class

end_unit

