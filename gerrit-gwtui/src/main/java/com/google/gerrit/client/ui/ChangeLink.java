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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_class
DECL|class|ChangeLink
specifier|public
class|class
name|ChangeLink
extends|extends
name|InlineHyperlink
block|{
DECL|method|permalink (Change.Id c)
specifier|public
specifier|static
name|String
name|permalink
parameter_list|(
name|Change
operator|.
name|Id
name|c
parameter_list|)
block|{
return|return
name|GWT
operator|.
name|getHostPageBaseURL
argument_list|()
operator|+
name|c
operator|.
name|get
argument_list|()
return|;
block|}
DECL|field|cid
specifier|protected
name|Change
operator|.
name|Id
name|cid
decl_stmt|;
DECL|method|ChangeLink (Project.NameKey project, Change.Id c, String text)
specifier|public
name|ChangeLink
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|c
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|PageLinks
operator|.
name|toChange
argument_list|(
name|project
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|getElement
argument_list|()
operator|.
name|setPropertyString
argument_list|(
literal|"href"
argument_list|,
name|permalink
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|cid
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|go ()
specifier|public
name|void
name|go
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|getTargetHistoryToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

