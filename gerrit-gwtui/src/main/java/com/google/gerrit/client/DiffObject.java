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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DefaultBase
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
name|PatchSet
import|;
end_import

begin_comment
comment|/**  * Represent an object that can be diffed. This can be either a regular patch set, the base of a  * patch set, the parent of a merge, the auto-merge of a merge or an edit patch set.  */
end_comment

begin_class
DECL|class|DiffObject
specifier|public
class|class
name|DiffObject
block|{
DECL|field|AUTO_MERGE
specifier|public
specifier|static
specifier|final
name|String
name|AUTO_MERGE
init|=
literal|"AutoMerge"
decl_stmt|;
comment|/**    * Parses a string that represents a diff object.    *    *<p>The following string representations are supported:    *    *<ul>    *<li>a positive integer: represents a patch set    *<li>a negative integer: represents a parent of a merge patch set    *<li>'0': represents the edit patch set    *<li>empty string or null: represents the parent of a 1-parent patch set, also called base    *<li>'AutoMerge': represents the auto-merge of a merge patch set    *</ul>    *    * @param changeId the ID of the change to which the diff object belongs    * @param str the string representation of the diff object    * @return the parsed diff object, {@code null} if str cannot be parsed as diff object    */
DECL|method|parse (Change.Id changeId, String str)
specifier|public
specifier|static
name|DiffObject
name|parse
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
operator|||
name|str
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|DiffObject
argument_list|(
literal|false
argument_list|)
return|;
block|}
if|if
condition|(
name|AUTO_MERGE
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
return|return
operator|new
name|DiffObject
argument_list|(
literal|true
argument_list|)
return|;
block|}
return|return
operator|new
name|DiffObject
argument_list|(
name|Dispatcher
operator|.
name|toPsId
argument_list|(
name|changeId
argument_list|,
name|str
argument_list|)
argument_list|)
return|;
block|}
comment|/** Create a DiffObject that represents the parent of a 1-parent patch set. */
DECL|method|base ()
specifier|public
specifier|static
name|DiffObject
name|base
parameter_list|()
block|{
return|return
operator|new
name|DiffObject
argument_list|(
literal|false
argument_list|)
return|;
block|}
comment|/** Create a DiffObject that represents the auto-merge for a merge patch set. */
DECL|method|autoMerge ()
specifier|public
specifier|static
name|DiffObject
name|autoMerge
parameter_list|()
block|{
return|return
operator|new
name|DiffObject
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/** Create a DiffObject that represents a patch set. */
DECL|method|patchSet (PatchSet.Id psId)
specifier|public
specifier|static
name|DiffObject
name|patchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
return|return
operator|new
name|DiffObject
argument_list|(
name|psId
argument_list|)
return|;
block|}
DECL|field|psId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|autoMerge
specifier|private
specifier|final
name|boolean
name|autoMerge
decl_stmt|;
DECL|method|DiffObject (PatchSet.Id psId)
specifier|private
name|DiffObject
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
name|this
operator|.
name|psId
operator|=
name|psId
expr_stmt|;
name|this
operator|.
name|autoMerge
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|DiffObject (boolean autoMerge)
specifier|private
name|DiffObject
parameter_list|(
name|boolean
name|autoMerge
parameter_list|)
block|{
name|this
operator|.
name|psId
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|autoMerge
operator|=
name|autoMerge
expr_stmt|;
block|}
DECL|method|isBase ()
specifier|public
name|boolean
name|isBase
parameter_list|()
block|{
return|return
name|psId
operator|==
literal|null
operator|&&
operator|!
name|autoMerge
return|;
block|}
DECL|method|isAutoMerge ()
specifier|public
name|boolean
name|isAutoMerge
parameter_list|()
block|{
return|return
name|psId
operator|==
literal|null
operator|&&
name|autoMerge
return|;
block|}
DECL|method|isBaseOrAutoMerge ()
specifier|public
name|boolean
name|isBaseOrAutoMerge
parameter_list|()
block|{
return|return
name|psId
operator|==
literal|null
return|;
block|}
DECL|method|isPatchSet ()
specifier|public
name|boolean
name|isPatchSet
parameter_list|()
block|{
return|return
name|psId
operator|!=
literal|null
operator|&&
name|psId
operator|.
name|get
argument_list|()
operator|>
literal|0
return|;
block|}
DECL|method|isParent ()
specifier|public
name|boolean
name|isParent
parameter_list|()
block|{
return|return
name|psId
operator|!=
literal|null
operator|&&
name|psId
operator|.
name|get
argument_list|()
operator|<
literal|0
return|;
block|}
DECL|method|isEdit ()
specifier|public
name|boolean
name|isEdit
parameter_list|()
block|{
return|return
name|psId
operator|!=
literal|null
operator|&&
name|psId
operator|.
name|get
argument_list|()
operator|==
literal|0
return|;
block|}
comment|/**    * Returns the DiffObject as PatchSet.Id.    *    * @return PatchSet.Id with an id> 0 for a regular patch set; PatchSet.Id with an id< 0 for a    *     parent of a merge; PatchSet.Id with id == 0 for an edit patch set; {@code null} for the    *     base of a 1-parent patch set and for the auto-merge of a merge patch set    */
DECL|method|asPatchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|asPatchSetId
parameter_list|()
block|{
return|return
name|psId
return|;
block|}
comment|/**    * Returns the parent number for a parent of a merge.    *    * @return 1-based parent number, 0 if this DiffObject is not a parent of a merge    */
DECL|method|getParentNum ()
specifier|public
name|int
name|getParentNum
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isParent
argument_list|()
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
operator|-
name|psId
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of this DiffObject that can be used in URLs.    *    *<p>The following string representations are returned:    *    *<ul>    *<li>a positive integer for a patch set    *<li>a negative integer for a parent of a merge patch set    *<li>'0' for the edit patch set    *<li>{@code null} for the parent of a 1-parent patch set, also called base    *<li>'AutoMerge' for the auto-merge of a merge patch set    *</ul>    *    * @return string representation of this DiffObject    */
DECL|method|asString ()
specifier|public
name|String
name|asString
parameter_list|()
block|{
if|if
condition|(
name|autoMerge
condition|)
block|{
if|if
condition|(
name|Gerrit
operator|.
name|getUserPreferences
argument_list|()
operator|.
name|defaultBaseForMerges
argument_list|()
operator|!=
name|DefaultBase
operator|.
name|AUTO_MERGE
condition|)
block|{
return|return
name|AUTO_MERGE
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|psId
operator|!=
literal|null
condition|)
block|{
return|return
name|psId
operator|.
name|getId
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|isPatchSet
argument_list|()
condition|)
block|{
return|return
literal|"Patch Set "
operator|+
name|psId
operator|.
name|getId
argument_list|()
return|;
block|}
if|if
condition|(
name|isParent
argument_list|()
condition|)
block|{
return|return
literal|"Parent "
operator|+
name|psId
operator|.
name|getId
argument_list|()
return|;
block|}
if|if
condition|(
name|isEdit
argument_list|()
condition|)
block|{
return|return
literal|"Edit Patch Set"
return|;
block|}
if|if
condition|(
name|isAutoMerge
argument_list|()
condition|)
block|{
return|return
literal|"Auto Merge"
return|;
block|}
return|return
literal|"Base"
return|;
block|}
block|}
end_class

end_unit

