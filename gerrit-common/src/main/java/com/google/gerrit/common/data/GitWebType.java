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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_comment
comment|/** Class to store information about different gitweb types. */
end_comment

begin_class
DECL|class|GitWebType
specifier|public
class|class
name|GitWebType
block|{
comment|/** name of the type. */
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|/** String for revision view url. */
DECL|field|revision
specifier|private
name|String
name|revision
decl_stmt|;
comment|/** ParameterizedString for project view url. */
DECL|field|project
specifier|private
name|String
name|project
decl_stmt|;
comment|/** ParameterizedString for branch view url. */
DECL|field|branch
specifier|private
name|String
name|branch
decl_stmt|;
comment|/** ParameterizedString for root tree view url. */
DECL|field|rootTree
specifier|private
name|String
name|rootTree
decl_stmt|;
comment|/** ParameterizedString for file view url. */
DECL|field|file
specifier|private
name|String
name|file
decl_stmt|;
comment|/** ParameterizedString for file history view url. */
DECL|field|fileHistory
specifier|private
name|String
name|fileHistory
decl_stmt|;
comment|/** Character to substitute the standard path separator '/' in branch and     * project names */
DECL|field|pathSeparator
specifier|private
name|char
name|pathSeparator
init|=
literal|'/'
decl_stmt|;
comment|/** Whether to include links to draft patch sets */
DECL|field|linkDrafts
specifier|private
name|boolean
name|linkDrafts
init|=
literal|true
decl_stmt|;
comment|/** Whether to encode URL segments */
DECL|field|urlEncode
specifier|private
name|boolean
name|urlEncode
init|=
literal|true
decl_stmt|;
comment|/**    * Get the String for branch view.    *    * @return The String for branch view    */
DECL|method|getBranch ()
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
comment|/**    * Get the String for link-name of the type.    *    * @return The String for link-name of the type    */
DECL|method|getLinkName ()
specifier|public
name|String
name|getLinkName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**    * Get the String for project view.    *    * @return The String for project view    */
DECL|method|getProject ()
specifier|public
name|String
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
comment|/**    * Get the String for revision view.    *    * @return The String for revision view    */
DECL|method|getRevision ()
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
comment|/**    * Get the String for root tree view.    *    * @return The String for root tree view    */
DECL|method|getRootTree ()
specifier|public
name|String
name|getRootTree
parameter_list|()
block|{
return|return
name|rootTree
return|;
block|}
comment|/**    * Get the String for file view.    *    * @return The String for file view    */
DECL|method|getFile ()
specifier|public
name|String
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
comment|/**    * Get the String for file history view.    *    * @return The String for file history view    */
DECL|method|getFileHistory ()
specifier|public
name|String
name|getFileHistory
parameter_list|()
block|{
return|return
name|fileHistory
return|;
block|}
comment|/**    * Get the path separator used for branch and project names.    *    * @return The path separator.    */
DECL|method|getPathSeparator ()
specifier|public
name|char
name|getPathSeparator
parameter_list|()
block|{
return|return
name|pathSeparator
return|;
block|}
comment|/**    * Get whether to link to draft patch sets    *    * @return True to link    */
DECL|method|getLinkDrafts ()
specifier|public
name|boolean
name|getLinkDrafts
parameter_list|()
block|{
return|return
name|linkDrafts
return|;
block|}
comment|/**    * Set the pattern for branch view.    *    * @param pattern The pattern for branch view    */
DECL|method|setBranch (final String pattern)
specifier|public
name|void
name|setBranch
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|branch
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**    * Set the pattern for link-name type.    *    * @param name The link-name type    */
DECL|method|setLinkName (final String name)
specifier|public
name|void
name|setLinkName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**    * Set the pattern for project view.    *    * @param pattern The pattern for project view    */
DECL|method|setProject (final String pattern)
specifier|public
name|void
name|setProject
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|project
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**    * Set the pattern for revision view.    *    * @param pattern The pattern for revision view    */
DECL|method|setRevision (final String pattern)
specifier|public
name|void
name|setRevision
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|revision
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**    * Set the pattern for root tree view.    *    * @param pattern The pattern for root tree view    */
DECL|method|setRootTree (final String pattern)
specifier|public
name|void
name|setRootTree
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|rootTree
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**    * Set the pattern for file view.    *    * @param pattern The pattern for file view    */
DECL|method|setFile (final String pattern)
specifier|public
name|void
name|setFile
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|file
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**    * Set the pattern for file history view.    *    * @param pattern The pattern for file history view    */
DECL|method|setFileHistory (final String pattern)
specifier|public
name|void
name|setFileHistory
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|fileHistory
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**    * Replace the standard path separator ('/') in a branch name or project    * name with a custom path separator configured by the property    * gitweb.pathSeparator.    * @param urlSegment The branch or project to replace the path separator in    * @return the urlSegment with the standard path separator replaced by the    * custom path separator    */
DECL|method|replacePathSeparator (String urlSegment)
specifier|public
name|String
name|replacePathSeparator
parameter_list|(
name|String
name|urlSegment
parameter_list|)
block|{
if|if
condition|(
literal|'/'
operator|!=
name|pathSeparator
condition|)
block|{
return|return
name|urlSegment
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|pathSeparator
argument_list|)
return|;
block|}
return|return
name|urlSegment
return|;
block|}
comment|/**    * Set the custom path separator    * @param separator The custom path separator    */
DECL|method|setPathSeparator (char separator)
specifier|public
name|void
name|setPathSeparator
parameter_list|(
name|char
name|separator
parameter_list|)
block|{
name|this
operator|.
name|pathSeparator
operator|=
name|separator
expr_stmt|;
block|}
DECL|method|setLinkDrafts (boolean linkDrafts)
specifier|public
name|void
name|setLinkDrafts
parameter_list|(
name|boolean
name|linkDrafts
parameter_list|)
block|{
name|this
operator|.
name|linkDrafts
operator|=
name|linkDrafts
expr_stmt|;
block|}
DECL|method|isUrlEncode ()
specifier|public
name|boolean
name|isUrlEncode
parameter_list|()
block|{
return|return
name|urlEncode
return|;
block|}
DECL|method|setUrlEncode (boolean urlEncode)
specifier|public
name|void
name|setUrlEncode
parameter_list|(
name|boolean
name|urlEncode
parameter_list|)
block|{
name|this
operator|.
name|urlEncode
operator|=
name|urlEncode
expr_stmt|;
block|}
block|}
end_class

end_unit

