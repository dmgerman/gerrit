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
name|Project
import|;
end_import

begin_comment
comment|/** Link to the default dashboard of a project. */
end_comment

begin_class
DECL|class|ProjectLink
specifier|public
class|class
name|ProjectLink
extends|extends
name|InlineHyperlink
block|{
DECL|method|ProjectLink (Project.NameKey proj)
specifier|public
name|ProjectLink
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|)
block|{
name|this
argument_list|(
name|proj
operator|.
name|get
argument_list|()
argument_list|,
name|proj
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectLink (String text, Project.NameKey proj)
specifier|public
name|ProjectLink
parameter_list|(
name|String
name|text
parameter_list|,
name|Project
operator|.
name|NameKey
name|proj
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|PageLinks
operator|.
name|toProjectDefaultDashboard
argument_list|(
name|proj
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

