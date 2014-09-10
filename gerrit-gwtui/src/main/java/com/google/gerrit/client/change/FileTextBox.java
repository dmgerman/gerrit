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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
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
name|client
operator|.
name|changes
operator|.
name|ChangeFileApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|GerritCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|RestApi
import|;
end_import

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
name|PatchSet
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
name|BlurEvent
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
name|BlurHandler
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
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|NpTextArea
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|NpTextBox
import|;
end_import

begin_class
DECL|class|FileTextBox
class|class
name|FileTextBox
extends|extends
name|NpTextBox
block|{
DECL|field|blurHandler
specifier|private
name|HandlerRegistration
name|blurHandler
decl_stmt|;
DECL|field|textArea
specifier|private
name|NpTextArea
name|textArea
decl_stmt|;
DECL|field|id
specifier|private
name|PatchSet
operator|.
name|Id
name|id
decl_stmt|;
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|blurHandler
operator|=
name|addBlurHandler
argument_list|(
operator|new
name|BlurHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onBlur
parameter_list|(
name|BlurEvent
name|event
parameter_list|)
block|{
name|loadFileContent
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
block|{
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
name|blurHandler
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
block|}
DECL|method|set (PatchSet.Id id, NpTextArea content)
name|void
name|set
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|NpTextArea
name|content
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|textArea
operator|=
name|content
expr_stmt|;
block|}
DECL|method|loadFileContent ()
specifier|private
name|void
name|loadFileContent
parameter_list|()
block|{
name|ChangeFileApi
operator|.
name|getContent
argument_list|(
name|id
argument_list|,
name|getText
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|String
name|result
parameter_list|)
block|{
name|textArea
operator|.
name|setText
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
name|RestApi
operator|.
name|isNotFound
argument_list|(
name|caught
argument_list|)
condition|)
block|{
comment|// that means that the file doesn't exist in the repository
block|}
else|else
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

