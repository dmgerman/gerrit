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
name|reviewdb
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Request parameters to update the changes the user has toggled. */
end_comment

begin_class
DECL|class|ToggleStarRequest
specifier|public
class|class
name|ToggleStarRequest
block|{
DECL|field|add
specifier|protected
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|add
decl_stmt|;
DECL|field|remove
specifier|protected
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|remove
decl_stmt|;
comment|/**    * Request an update to the change's star status.    *     * @param id unique id of the change, must not be null.    * @param on true if the change should now be starred; false if it should now    *        be not starred.    */
DECL|method|toggle (final Change.Id id, final boolean on)
specifier|public
name|void
name|toggle
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|boolean
name|on
parameter_list|)
block|{
if|if
condition|(
name|on
condition|)
block|{
if|if
condition|(
name|add
operator|==
literal|null
condition|)
block|{
name|add
operator|=
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|add
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|remove
operator|!=
literal|null
condition|)
block|{
name|remove
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|remove
operator|==
literal|null
condition|)
block|{
name|remove
operator|=
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|remove
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|add
operator|!=
literal|null
condition|)
block|{
name|add
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** Get the set of changes which should have stars added; may be null. */
DECL|method|getAddSet ()
specifier|public
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|getAddSet
parameter_list|()
block|{
return|return
name|add
return|;
block|}
comment|/** Get the set of changes which should have stars removed; may be null. */
DECL|method|getRemoveSet ()
specifier|public
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|getRemoveSet
parameter_list|()
block|{
return|return
name|remove
return|;
block|}
block|}
end_class

end_unit

