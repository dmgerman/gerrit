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
name|Link
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
name|ChangeScreen
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
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
name|DOM
import|;
end_import

begin_class
DECL|class|ChangeLink
specifier|public
class|class
name|ChangeLink
extends|extends
name|DirectScreenLink
block|{
DECL|field|id
specifier|protected
name|Change
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|info
specifier|private
name|ChangeInfo
name|info
decl_stmt|;
DECL|method|ChangeLink (final String text, final Change.Id c)
specifier|public
name|ChangeLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Change
operator|.
name|Id
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|Link
operator|.
name|toChange
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|String
name|plink
init|=
name|GWT
operator|.
name|getModuleBaseURL
argument_list|()
operator|+
name|c
operator|.
name|get
argument_list|()
decl_stmt|;
name|DOM
operator|.
name|setElementProperty
argument_list|(
name|DOM
operator|.
name|getFirstChild
argument_list|(
name|getElement
argument_list|()
argument_list|)
argument_list|,
literal|"href"
argument_list|,
name|plink
argument_list|)
expr_stmt|;
name|id
operator|=
name|c
expr_stmt|;
block|}
DECL|method|ChangeLink (final String text, final ChangeInfo c)
specifier|public
name|ChangeLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|ChangeInfo
name|c
parameter_list|)
block|{
name|this
argument_list|(
name|text
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createScreen ()
specifier|protected
name|Screen
name|createScreen
parameter_list|()
block|{
return|return
name|info
operator|!=
literal|null
condition|?
operator|new
name|ChangeScreen
argument_list|(
name|info
argument_list|)
else|:
operator|new
name|ChangeScreen
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit

