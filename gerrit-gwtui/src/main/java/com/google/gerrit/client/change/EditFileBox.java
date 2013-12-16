begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|//you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|//You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|//distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|//See the License for the specific language governing permissions and
end_comment

begin_comment
comment|//limitations under the License.
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
name|Gerrit
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
name|VoidResult
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
name|common
operator|.
name|PageLinks
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
name|ClickEvent
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
name|uibinder
operator|.
name|client
operator|.
name|UiHandler
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
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|EditFileBox
class|class
name|EditFileBox
extends|extends
name|EditMessageBox
block|{
DECL|field|id
specifier|private
name|PatchSet
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|file
specifier|private
name|String
name|file
decl_stmt|;
DECL|method|EditFileBox ( PatchSet.Id id, String content, String file)
name|EditFileBox
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|String
name|content
parameter_list|,
name|String
name|file
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|UiHandler
argument_list|(
literal|"save"
argument_list|)
DECL|method|onSave (ClickEvent e)
name|void
name|onSave
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|ChangeFileApi
operator|.
name|putContent
argument_list|(
name|id
argument_list|,
name|file
argument_list|,
name|message
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
operator|new
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|VoidResult
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|id
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|hide
argument_list|()
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
block|{           }
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

