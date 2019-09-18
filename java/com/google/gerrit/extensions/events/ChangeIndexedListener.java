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
DECL|package|com.google.gerrit.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|events
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

begin_comment
comment|/** Notified whenever a change is indexed or deleted from the index. */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ChangeIndexedListener
specifier|public
interface|interface
name|ChangeIndexedListener
block|{
comment|/**    * Invoked when a change is scheduled for indexing.    *    * @param projectName project containing the change    * @param id id of the change that was scheduled for indexing    */
DECL|method|onChangeScheduledForIndexing (String projectName, int id)
specifier|default
name|void
name|onChangeScheduledForIndexing
parameter_list|(
name|String
name|projectName
parameter_list|,
name|int
name|id
parameter_list|)
block|{}
comment|/**    * Invoked when a change is scheduled for deletion from indexing.    *    * @param id id of the change that was scheduled for deletion from indexing    */
DECL|method|onChangeScheduledForDeletionFromIndex (int id)
specifier|default
name|void
name|onChangeScheduledForDeletionFromIndex
parameter_list|(
name|int
name|id
parameter_list|)
block|{}
comment|/**    * Invoked when a change is indexed.    *    * @param projectName project containing the change    * @param id indexed change id    */
DECL|method|onChangeIndexed (String projectName, int id)
name|void
name|onChangeIndexed
parameter_list|(
name|String
name|projectName
parameter_list|,
name|int
name|id
parameter_list|)
function_decl|;
comment|/** Invoked when a change is deleted from the index. */
DECL|method|onChangeDeleted (int id)
name|void
name|onChangeDeleted
parameter_list|(
name|int
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

