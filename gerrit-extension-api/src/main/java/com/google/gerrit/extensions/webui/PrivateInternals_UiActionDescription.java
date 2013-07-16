begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
package|;
end_package

begin_comment
comment|/**  * Internal implementation helper for Gerrit Code Review server.  *<p>  * Extensions and plugins should not invoke this class.  */
end_comment

begin_class
DECL|class|PrivateInternals_UiActionDescription
specifier|public
class|class
name|PrivateInternals_UiActionDescription
block|{
DECL|method|setMethod (UiAction.Description d, String method)
specifier|public
specifier|static
name|void
name|setMethod
parameter_list|(
name|UiAction
operator|.
name|Description
name|d
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|d
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
DECL|method|setId (UiAction.Description d, String id)
specifier|public
specifier|static
name|void
name|setId
parameter_list|(
name|UiAction
operator|.
name|Description
name|d
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|d
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|PrivateInternals_UiActionDescription ()
specifier|private
name|PrivateInternals_UiActionDescription
parameter_list|()
block|{   }
block|}
end_class

end_unit

