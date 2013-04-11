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
name|Document
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
name|dom
operator|.
name|client
operator|.
name|Node
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|HasKeyPressHandlers
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyPressEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyPressHandler
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
name|event
operator|.
name|shared
operator|.
name|HandlerRegistration
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
name|RootPanel
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
name|Widget
import|;
end_import

begin_class
DECL|class|DocWidget
class|class
name|DocWidget
extends|extends
name|Widget
implements|implements
name|HasKeyPressHandlers
block|{
DECL|field|me
specifier|private
specifier|static
name|DocWidget
name|me
decl_stmt|;
DECL|method|get ()
specifier|static
name|DocWidget
name|get
parameter_list|()
block|{
if|if
condition|(
name|me
operator|==
literal|null
condition|)
block|{
name|me
operator|=
operator|new
name|DocWidget
argument_list|()
expr_stmt|;
block|}
return|return
name|me
return|;
block|}
DECL|method|DocWidget ()
specifier|private
name|DocWidget
parameter_list|()
block|{
name|setElement
argument_list|(
operator|(
name|Element
operator|)
name|docnode
argument_list|()
argument_list|)
expr_stmt|;
name|onAttach
argument_list|()
expr_stmt|;
name|RootPanel
operator|.
name|detachOnWindowClose
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addKeyPressHandler (KeyPressHandler handler)
specifier|public
name|HandlerRegistration
name|addKeyPressHandler
parameter_list|(
name|KeyPressHandler
name|handler
parameter_list|)
block|{
return|return
name|addDomHandler
argument_list|(
name|handler
argument_list|,
name|KeyPressEvent
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|docnode ()
specifier|private
specifier|static
name|Node
name|docnode
parameter_list|()
block|{
return|return
name|Document
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

