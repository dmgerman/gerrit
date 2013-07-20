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
DECL|package|com.google.gwtexpui.user.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|user
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|shared
operator|.
name|GwtEvent
import|;
end_import

begin_class
DECL|class|DialogVisibleEvent
specifier|public
class|class
name|DialogVisibleEvent
extends|extends
name|GwtEvent
argument_list|<
name|DialogVisibleHandler
argument_list|>
block|{
DECL|field|TYPE
specifier|private
specifier|static
name|Type
argument_list|<
name|DialogVisibleHandler
argument_list|>
name|TYPE
decl_stmt|;
DECL|method|getType ()
specifier|public
specifier|static
name|Type
argument_list|<
name|DialogVisibleHandler
argument_list|>
name|getType
parameter_list|()
block|{
if|if
condition|(
name|TYPE
operator|==
literal|null
condition|)
block|{
name|TYPE
operator|=
operator|new
name|Type
argument_list|<
name|DialogVisibleHandler
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|TYPE
return|;
block|}
DECL|field|visible
specifier|private
specifier|final
name|boolean
name|visible
decl_stmt|;
DECL|method|DialogVisibleEvent (boolean visible)
name|DialogVisibleEvent
parameter_list|(
name|boolean
name|visible
parameter_list|)
block|{
name|this
operator|.
name|visible
operator|=
name|visible
expr_stmt|;
block|}
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|visible
return|;
block|}
annotation|@
name|Override
DECL|method|getAssociatedType ()
specifier|public
name|Type
argument_list|<
name|DialogVisibleHandler
argument_list|>
name|getAssociatedType
parameter_list|()
block|{
return|return
name|getType
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|dispatch (DialogVisibleHandler handler)
specifier|protected
name|void
name|dispatch
parameter_list|(
name|DialogVisibleHandler
name|handler
parameter_list|)
block|{
name|handler
operator|.
name|onDialogVisible
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

