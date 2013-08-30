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
DECL|package|com.google.gerrit.server.actions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|actions
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
name|extensions
operator|.
name|webui
operator|.
name|UiAction
import|;
end_import

begin_class
DECL|class|ActionInfo
specifier|public
class|class
name|ActionInfo
block|{
DECL|field|method
name|String
name|method
decl_stmt|;
DECL|field|label
name|String
name|label
decl_stmt|;
DECL|field|title
name|String
name|title
decl_stmt|;
DECL|field|enabled
name|Boolean
name|enabled
decl_stmt|;
DECL|method|ActionInfo (UiAction.Description d)
specifier|public
name|ActionInfo
parameter_list|(
name|UiAction
operator|.
name|Description
name|d
parameter_list|)
block|{
name|method
operator|=
name|d
operator|.
name|getMethod
argument_list|()
expr_stmt|;
name|label
operator|=
name|d
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|title
operator|=
name|d
operator|.
name|getTitle
argument_list|()
expr_stmt|;
name|enabled
operator|=
name|d
operator|.
name|isEnabled
argument_list|()
condition|?
literal|true
else|:
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

