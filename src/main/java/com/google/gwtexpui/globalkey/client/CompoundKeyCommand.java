begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gwtexpui.globalkey.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
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
name|dom
operator|.
name|client
operator|.
name|KeyPressEvent
import|;
end_import

begin_class
DECL|class|CompoundKeyCommand
specifier|public
specifier|final
class|class
name|CompoundKeyCommand
extends|extends
name|KeyCommand
block|{
DECL|field|set
specifier|final
name|KeyCommandSet
name|set
decl_stmt|;
DECL|method|CompoundKeyCommand (int mask, char key, String help, KeyCommandSet s)
specifier|public
name|CompoundKeyCommand
parameter_list|(
name|int
name|mask
parameter_list|,
name|char
name|key
parameter_list|,
name|String
name|help
parameter_list|,
name|KeyCommandSet
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|mask
argument_list|,
name|key
argument_list|,
name|help
argument_list|)
expr_stmt|;
name|set
operator|=
name|s
expr_stmt|;
block|}
DECL|method|CompoundKeyCommand (int mask, int key, String help, KeyCommandSet s)
specifier|public
name|CompoundKeyCommand
parameter_list|(
name|int
name|mask
parameter_list|,
name|int
name|key
parameter_list|,
name|String
name|help
parameter_list|,
name|KeyCommandSet
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|mask
argument_list|,
name|key
argument_list|,
name|help
argument_list|)
expr_stmt|;
name|set
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getSet ()
specifier|public
name|KeyCommandSet
name|getSet
parameter_list|()
block|{
return|return
name|set
return|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent event)
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|GlobalKey
operator|.
name|temporaryWithTimeout
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

