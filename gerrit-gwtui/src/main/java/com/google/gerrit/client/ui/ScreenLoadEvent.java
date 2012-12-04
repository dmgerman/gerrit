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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
DECL|class|ScreenLoadEvent
specifier|public
class|class
name|ScreenLoadEvent
extends|extends
name|GwtEvent
argument_list|<
name|ScreenLoadHandler
argument_list|>
block|{
DECL|field|screen
specifier|private
specifier|final
name|Screen
name|screen
decl_stmt|;
DECL|method|ScreenLoadEvent (Screen screen)
specifier|public
name|ScreenLoadEvent
parameter_list|(
name|Screen
name|screen
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|screen
operator|=
name|screen
expr_stmt|;
block|}
DECL|field|TYPE
specifier|public
specifier|static
specifier|final
name|Type
argument_list|<
name|ScreenLoadHandler
argument_list|>
name|TYPE
init|=
operator|new
name|Type
argument_list|<
name|ScreenLoadHandler
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|dispatch (ScreenLoadHandler handler)
specifier|protected
name|void
name|dispatch
parameter_list|(
name|ScreenLoadHandler
name|handler
parameter_list|)
block|{
name|handler
operator|.
name|onScreenLoad
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAssociatedType ()
specifier|public
name|GwtEvent
operator|.
name|Type
argument_list|<
name|ScreenLoadHandler
argument_list|>
name|getAssociatedType
parameter_list|()
block|{
return|return
name|TYPE
return|;
block|}
DECL|method|getScreen ()
specifier|public
name|Screen
name|getScreen
parameter_list|()
block|{
return|return
name|screen
return|;
block|}
block|}
end_class

end_unit

