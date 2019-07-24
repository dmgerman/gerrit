begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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

begin_comment
comment|/**  * Allows plugins to contribute a value to the change ETag computation.  *  *<p>Plugins can affect the result of the get change / get change details REST endpoints by:  *  *<ul>  *<li>providing plugin defined attributes to {@link  *       com.google.gerrit.extensions.common.ChangeInfo#plugins} (see {@link  *       ChangeAttributeFactory})  *<li>implementing a {@link com.google.gerrit.server.rules.SubmitRule} which affects the  *       computation of {@link com.google.gerrit.extensions.common.ChangeInfo#submittable}  *</ul>  *  *<p>If the plugin defined part of {@link com.google.gerrit.extensions.common.ChangeInfo} depends  * on plugin specific data, callers that use the change ETags to avoid unneeded recomputations of  * ChangeInfos may see outdated plugin attributes and/or outdated submittable information, because a  * ChangeInfo is only reloaded if the change ETag changes.  *  *<p>By implementating this interface plugins can contribute to the change ETag computation and  * thus ensure that the ETag changes when the plugin data was changed. This way it is ensured that  * callers do not see outdated ChangeInfos.  *  * @see ChangeResource#getETag()  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ChangeETagComputation
specifier|public
interface|interface
name|ChangeETagComputation
block|{
comment|/**    * Computes an ETag of plugin-specific data for the given change.    *    *<p><strong>Note:</strong> Change ETags are computed very frequently and the computation must be    * cheap. Take good care to not perform any expensive computations when implementing this.    *    * @param projectName the name of the project that contains the change    * @param changeId ID of the change for which the ETag should be computed    * @return the ETag    */
DECL|method|getETag (Project.NameKey projectName, Change.Id changeId)
name|String
name|getETag
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

