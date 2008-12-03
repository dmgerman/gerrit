begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|data
operator|.
name|ChangeInfo
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
name|reviewdb
operator|.
name|Change
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

begin_class
DECL|class|ChangeScreen
specifier|public
class|class
name|ChangeScreen
extends|extends
name|Screen
block|{
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|method|ChangeScreen (final Change.Id toShow)
specifier|public
name|ChangeScreen
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|toShow
parameter_list|)
block|{
name|changeId
operator|=
name|toShow
expr_stmt|;
block|}
DECL|method|ChangeScreen (final ChangeInfo c)
specifier|public
name|ChangeScreen
parameter_list|(
specifier|final
name|ChangeInfo
name|c
parameter_list|)
block|{
name|this
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScreenCacheToken ()
specifier|public
name|Object
name|getScreenCacheToken
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|recycleThis (final Screen newScreen)
specifier|public
name|Screen
name|recycleThis
parameter_list|(
specifier|final
name|Screen
name|newScreen
parameter_list|)
block|{
name|changeId
operator|=
operator|(
operator|(
name|ChangeScreen
operator|)
name|newScreen
operator|)
operator|.
name|changeId
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|public
name|void
name|onLoad
parameter_list|()
block|{
name|setTitleText
argument_list|(
literal|"Change "
operator|+
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

