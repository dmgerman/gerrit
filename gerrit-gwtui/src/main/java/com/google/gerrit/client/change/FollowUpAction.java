begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|changes
operator|.
name|ChangeApi
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
name|info
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Button
import|;
end_import

begin_class
DECL|class|FollowUpAction
class|class
name|FollowUpAction
extends|extends
name|ActionMessageBox
block|{
DECL|field|project
specifier|private
specifier|final
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|private
specifier|final
name|String
name|branch
decl_stmt|;
DECL|field|topic
specifier|private
specifier|final
name|String
name|topic
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|String
name|base
decl_stmt|;
DECL|method|FollowUpAction (Button b, String project, String branch, String topic, String key)
name|FollowUpAction
parameter_list|(
name|Button
name|b
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|topic
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|super
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
name|this
operator|.
name|base
operator|=
name|project
operator|+
literal|"~"
operator|+
name|branch
operator|+
literal|"~"
operator|+
name|key
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|send (String message)
name|void
name|send
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|ChangeApi
operator|.
name|createChange
argument_list|(
name|project
argument_list|,
name|branch
argument_list|,
name|topic
argument_list|,
name|message
argument_list|,
name|base
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeInfo
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
name|result
operator|.
name|projectNameKey
argument_list|()
argument_list|,
name|result
operator|.
name|legacyId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

