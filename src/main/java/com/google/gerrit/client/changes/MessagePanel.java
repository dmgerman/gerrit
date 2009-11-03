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
name|reviewdb
operator|.
name|ChangeMessage
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
name|Composite
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_class
DECL|class|MessagePanel
specifier|public
class|class
name|MessagePanel
extends|extends
name|Composite
block|{
DECL|field|isRecent
name|boolean
name|isRecent
decl_stmt|;
DECL|method|MessagePanel (final ChangeMessage msg)
specifier|public
name|MessagePanel
parameter_list|(
specifier|final
name|ChangeMessage
name|msg
parameter_list|)
block|{
specifier|final
name|Widget
name|l
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|wikify
argument_list|()
operator|.
name|replaceAll
argument_list|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getCommentLinks
argument_list|()
argument_list|)
operator|.
name|toBlockWidget
argument_list|()
decl_stmt|;
name|l
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-ChangeMessage-Message"
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

