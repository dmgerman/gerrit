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
name|dom
operator|.
name|client
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|TextArea
import|;
end_import

begin_class
DECL|class|NpTextArea
specifier|public
class|class
name|NpTextArea
extends|extends
name|TextArea
block|{
DECL|method|NpTextArea ()
specifier|public
name|NpTextArea
parameter_list|()
block|{
name|addKeyPressHandler
argument_list|(
name|GlobalKey
operator|.
name|STOP_PROPAGATION
argument_list|)
expr_stmt|;
block|}
DECL|method|NpTextArea (final Element element)
specifier|public
name|NpTextArea
parameter_list|(
specifier|final
name|Element
name|element
parameter_list|)
block|{
name|super
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|addKeyPressHandler
argument_list|(
name|GlobalKey
operator|.
name|STOP_PROPAGATION
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

