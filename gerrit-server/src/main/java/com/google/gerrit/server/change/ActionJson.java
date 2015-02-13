begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|extensions
operator|.
name|common
operator|.
name|ActionInfo
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
name|extensions
operator|.
name|webui
operator|.
name|UiAction
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
name|server
operator|.
name|CurrentUser
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
name|server
operator|.
name|extensions
operator|.
name|webui
operator|.
name|UiActions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|util
operator|.
name|Providers
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ActionJson
specifier|public
class|class
name|ActionJson
block|{
DECL|field|revisions
specifier|private
specifier|final
name|Revisions
name|revisions
decl_stmt|;
annotation|@
name|Inject
DECL|method|ActionJson (Revisions revisions)
name|ActionJson
parameter_list|(
name|Revisions
name|revisions
parameter_list|)
block|{
name|this
operator|.
name|revisions
operator|=
name|revisions
expr_stmt|;
block|}
DECL|method|format (RevisionResource rsrc)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|format
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
block|{
return|return
name|toActionMap
argument_list|(
name|rsrc
argument_list|)
return|;
block|}
DECL|method|toActionMap (RevisionResource rsrc)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|toActionMap
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|out
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
init|=
name|Providers
operator|.
name|of
argument_list|(
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getCurrentUser
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|UiAction
operator|.
name|Description
name|d
range|:
name|UiActions
operator|.
name|from
argument_list|(
name|revisions
argument_list|,
name|rsrc
argument_list|,
name|userProvider
argument_list|)
control|)
block|{
name|out
operator|.
name|put
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|ActionInfo
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|out
return|;
block|}
block|}
end_class

end_unit

