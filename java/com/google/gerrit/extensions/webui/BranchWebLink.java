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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
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
name|annotations
operator|.
name|ExtensionPoint
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
name|common
operator|.
name|WebLinkInfo
import|;
end_import

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|BranchWebLink
specifier|public
interface|interface
name|BranchWebLink
extends|extends
name|WebLink
block|{
comment|/**    * {@link com.google.gerrit.extensions.common.WebLinkInfo} describing a link from a branch to an    * external service.    *    *<p>In order for the web link to be visible {@link    * com.google.gerrit.extensions.common.WebLinkInfo#url} and {@link    * com.google.gerrit.extensions.common.WebLinkInfo#name} must be set.    *    *<p>    *    * @param projectName Name of the project    * @param branchName Name of the branch    * @return WebLinkInfo that links to branch in external service, null if there should be no link.    */
DECL|method|getBranchWebLink (String projectName, String branchName)
name|WebLinkInfo
name|getBranchWebLink
parameter_list|(
name|String
name|projectName
parameter_list|,
name|String
name|branchName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

