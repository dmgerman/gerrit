begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|logical
operator|.
name|shared
operator|.
name|HasValueChangeHandlers
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
name|logical
operator|.
name|shared
operator|.
name|ValueChangeEvent
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
name|logical
operator|.
name|shared
operator|.
name|ValueChangeHandler
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
name|GwtEvent
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
name|HandlerManager
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

begin_class
DECL|class|ListenableValue
specifier|public
class|class
name|ListenableValue
parameter_list|<
name|T
parameter_list|>
implements|implements
name|HasValueChangeHandlers
argument_list|<
name|T
argument_list|>
block|{
DECL|field|manager
specifier|private
name|HandlerManager
name|manager
init|=
operator|new
name|HandlerManager
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|value
specifier|private
name|T
name|value
decl_stmt|;
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|set (final T value)
specifier|public
name|void
name|set
parameter_list|(
specifier|final
name|T
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|fireEvent
argument_list|(
operator|new
name|ValueChangeEvent
argument_list|<
name|T
argument_list|>
argument_list|(
name|value
argument_list|)
block|{}
argument_list|)
expr_stmt|;
block|}
DECL|method|fireEvent (GwtEvent<?> event)
specifier|public
name|void
name|fireEvent
parameter_list|(
name|GwtEvent
argument_list|<
name|?
argument_list|>
name|event
parameter_list|)
block|{
name|manager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|addValueChangeHandler ( ValueChangeHandler<T> handler)
specifier|public
name|HandlerRegistration
name|addValueChangeHandler
parameter_list|(
name|ValueChangeHandler
argument_list|<
name|T
argument_list|>
name|handler
parameter_list|)
block|{
return|return
name|manager
operator|.
name|addHandler
argument_list|(
name|ValueChangeEvent
operator|.
name|getType
argument_list|()
argument_list|,
name|handler
argument_list|)
return|;
block|}
block|}
end_class

end_unit

