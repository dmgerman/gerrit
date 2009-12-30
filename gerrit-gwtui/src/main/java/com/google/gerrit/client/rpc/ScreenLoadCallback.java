begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
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
name|NotFoundScreen
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
name|ui
operator|.
name|Screen
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
name|errors
operator|.
name|NoSuchEntityException
import|;
end_import

begin_comment
comment|/** Callback switching {@link NoSuchEntityException} to {@link NotFoundScreen} */
end_comment

begin_class
DECL|class|ScreenLoadCallback
specifier|public
specifier|abstract
class|class
name|ScreenLoadCallback
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GerritCallback
argument_list|<
name|T
argument_list|>
block|{
DECL|field|screen
specifier|private
specifier|final
name|Screen
name|screen
decl_stmt|;
DECL|method|ScreenLoadCallback (final Screen s)
specifier|public
name|ScreenLoadCallback
parameter_list|(
specifier|final
name|Screen
name|s
parameter_list|)
block|{
name|screen
operator|=
name|s
expr_stmt|;
block|}
DECL|method|onSuccess (final T result)
specifier|public
specifier|final
name|void
name|onSuccess
parameter_list|(
specifier|final
name|T
name|result
parameter_list|)
block|{
if|if
condition|(
name|screen
operator|.
name|isAttached
argument_list|()
condition|)
block|{
name|preDisplay
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|screen
operator|.
name|display
argument_list|()
expr_stmt|;
name|postDisplay
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|preDisplay (T result)
specifier|protected
specifier|abstract
name|void
name|preDisplay
parameter_list|(
name|T
name|result
parameter_list|)
function_decl|;
DECL|method|postDisplay ()
specifier|protected
name|void
name|postDisplay
parameter_list|()
block|{   }
annotation|@
name|Override
DECL|method|onFailure (final Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
name|isNoSuchEntity
argument_list|(
name|caught
argument_list|)
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|screen
operator|.
name|getToken
argument_list|()
argument_list|,
operator|new
name|NotFoundScreen
argument_list|()
argument_list|)
expr_stmt|;
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
end_class

end_unit

