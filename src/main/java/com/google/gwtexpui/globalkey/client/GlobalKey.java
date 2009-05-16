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
name|shared
operator|.
name|HandlerRegistration
import|;
end_import

begin_class
DECL|class|GlobalKey
specifier|public
class|class
name|GlobalKey
block|{
DECL|field|keyApplication
specifier|private
specifier|static
name|KeyCommandSet
name|keyApplication
decl_stmt|;
DECL|field|keys
specifier|static
name|KeyCommandSet
name|keys
decl_stmt|;
DECL|method|init ()
specifier|private
specifier|static
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|keys
operator|==
literal|null
condition|)
block|{
name|keys
operator|=
operator|new
name|KeyCommandSet
argument_list|()
expr_stmt|;
name|DocWidget
operator|.
name|get
argument_list|()
operator|.
name|addKeyPressHandler
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|keyApplication
operator|=
operator|new
name|KeyCommandSet
argument_list|(
name|Util
operator|.
name|C
operator|.
name|applicationSection
argument_list|()
argument_list|)
expr_stmt|;
name|keyApplication
operator|.
name|add
argument_list|(
operator|new
name|ShowHelpCommand
argument_list|()
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|keyApplication
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addApplication (final KeyCommand key)
specifier|public
specifier|static
name|HandlerRegistration
name|addApplication
parameter_list|(
specifier|final
name|KeyCommand
name|key
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|keyApplication
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
operator|new
name|HandlerRegistration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|removeHandler
parameter_list|()
block|{
name|keys
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|keyApplication
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|add (final KeyCommandSet set)
specifier|public
specifier|static
name|HandlerRegistration
name|add
parameter_list|(
specifier|final
name|KeyCommandSet
name|set
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|set
argument_list|)
expr_stmt|;
return|return
operator|new
name|HandlerRegistration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|removeHandler
parameter_list|()
block|{
name|keys
operator|.
name|remove
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|filter (final KeyCommandFilter filter)
specifier|public
specifier|static
name|void
name|filter
parameter_list|(
specifier|final
name|KeyCommandFilter
name|filter
parameter_list|)
block|{
name|keys
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
DECL|method|GlobalKey ()
specifier|private
name|GlobalKey
parameter_list|()
block|{   }
block|}
end_class

end_unit

