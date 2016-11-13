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
name|DiffWebLinkInfo
import|;
end_import

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|DiffWebLink
specifier|public
interface|interface
name|DiffWebLink
extends|extends
name|WebLink
block|{
comment|/**    * {@link com.google.gerrit.extensions.common.DiffWebLinkInfo} describing a link from a file diff    * to an external service.    *    *<p>In order for the web link to be visible {@link    * com.google.gerrit.extensions.common.WebLinkInfo#url} and {@link    * com.google.gerrit.extensions.common.WebLinkInfo#name} must be set.    *    *<p>    *    * @param projectName Name of the project    * @param changeId ID of the change    * @param patchSetIdA Patch set ID of side A,<code>null</code> if no base patch set was selected    * @param revisionA Name of the revision of side A (e.g. branch or commit ID)    * @param fileNameA Name of the file of side A    * @param patchSetIdB Patch set ID of side B    * @param revisionB Name of the revision of side B (e.g. branch or commit ID)    * @param fileNameB Name of the file of side B    * @return WebLinkInfo that links to file diff in external service, null if there should be no    *     link.    */
DECL|method|getDiffLink ( String projectName, int changeId, Integer patchSetIdA, String revisionA, String fileNameA, int patchSetIdB, String revisionB, String fileNameB)
name|DiffWebLinkInfo
name|getDiffLink
parameter_list|(
name|String
name|projectName
parameter_list|,
name|int
name|changeId
parameter_list|,
name|Integer
name|patchSetIdA
parameter_list|,
name|String
name|revisionA
parameter_list|,
name|String
name|fileNameA
parameter_list|,
name|int
name|patchSetIdB
parameter_list|,
name|String
name|revisionB
parameter_list|,
name|String
name|fileNameB
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

