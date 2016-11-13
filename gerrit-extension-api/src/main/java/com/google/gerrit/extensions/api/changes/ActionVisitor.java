begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
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
name|common
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
name|extensions
operator|.
name|common
operator|.
name|RevisionInfo
import|;
end_import

begin_comment
comment|/**  * Extension point called during population of {@link ActionInfo} maps.  *  *<p>Each visitor may mutate the input {@link ActionInfo}, or filter it out of the map entirely.  * When multiple extensions are registered, the order in which they are executed is undefined.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ActionVisitor
specifier|public
interface|interface
name|ActionVisitor
block|{
comment|/**    * Visit a change-level action.    *    *<p>Callers may mutate the input {@link ActionInfo}, or return false to omit the action from the    * map entirely. Inputs other than the {@link ActionInfo} should be considered immutable.    *    * @param name name of the action, as a key into the {@link ActionInfo} map returned by the REST    *     API.    * @param actionInfo action being visited; caller may mutate.    * @param changeInfo information about the change to which this action belongs; caller should    *     treat as immutable.    * @return true if the action should remain in the map, or false to omit it.    */
DECL|method|visit (String name, ActionInfo actionInfo, ChangeInfo changeInfo)
name|boolean
name|visit
parameter_list|(
name|String
name|name
parameter_list|,
name|ActionInfo
name|actionInfo
parameter_list|,
name|ChangeInfo
name|changeInfo
parameter_list|)
function_decl|;
comment|/**    * Visit a revision-level action.    *    *<p>Callers may mutate the input {@link ActionInfo}, or return false to omit the action from the    * map entirely. Inputs other than the {@link ActionInfo} should be considered immutable.    *    * @param name name of the action, as a key into the {@link ActionInfo} map returned by the REST    *     API.    * @param actionInfo action being visited; caller may mutate.    * @param changeInfo information about the change to which this action belongs; caller should    *     treat as immutable.    * @param revisionInfo information about the revision to which this action belongs; caller should    *     treat as immutable.    * @return true if the action should remain in the map, or false to omit it.    */
DECL|method|visit ( String name, ActionInfo actionInfo, ChangeInfo changeInfo, RevisionInfo revisionInfo)
name|boolean
name|visit
parameter_list|(
name|String
name|name
parameter_list|,
name|ActionInfo
name|actionInfo
parameter_list|,
name|ChangeInfo
name|changeInfo
parameter_list|,
name|RevisionInfo
name|revisionInfo
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

