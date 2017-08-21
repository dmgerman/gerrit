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
comment|/** Class to store information about different source browser types. */
end_comment

begin_class
DECL|class|GitwebType
specifier|public
class|class
name|GitwebType
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|branch
specifier|private
name|String
name|branch
decl_stmt|;
DECL|field|file
specifier|private
name|String
name|file
decl_stmt|;
DECL|field|fileHistory
specifier|private
name|String
name|fileHistory
decl_stmt|;
DECL|field|project
specifier|private
name|String
name|project
decl_stmt|;
DECL|field|revision
specifier|private
name|String
name|revision
decl_stmt|;
DECL|field|rootTree
specifier|private
name|String
name|rootTree
decl_stmt|;
DECL|field|tag
specifier|private
name|String
name|tag
decl_stmt|;
DECL|field|pathSeparator
specifier|private
name|char
name|pathSeparator
init|=
literal|'/'
decl_stmt|;
DECL|field|urlEncode
specifier|private
name|boolean
name|urlEncode
init|=
literal|true
decl_stmt|;
comment|/** @return name displayed in links. */
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
comment|/**    * Set the name displayed in links.    *    * @param name new name.    */
DECL|method|setLinkName (String name)
specifier|public
name|void
name|setLinkName
parameter_list|(
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
comment|/** @return parameterized string for the branch URL. */
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
comment|/**    * Set the parameterized string for the branch URL.    *    * @param str new string.    */
DECL|method|setBranch (String str)
specifier|public
name|void
name|setBranch
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|branch
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return parameterized string for the tag URL. */
DECL|method|getTag ()
specifier|public
name|String
name|getTag
parameter_list|()
block|{
return|return
name|tag
return|;
block|}
comment|/**    * Set the parameterized string for the tag URL.    *    * @param str new string.    */
DECL|method|setTag (String str)
specifier|public
name|void
name|setTag
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|tag
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return parameterized string for the file URL. */
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
comment|/**    * Set the parameterized string for the file URL.    *    * @param str new string.    */
DECL|method|setFile (String str)
specifier|public
name|void
name|setFile
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|file
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return parameterized string for the file history URL. */
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
comment|/**    * Set the parameterized string for the file history URL.    *    * @param str new string.    */
DECL|method|setFileHistory (String str)
specifier|public
name|void
name|setFileHistory
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|fileHistory
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return parameterized string for the project URL. */
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
comment|/**    * Set the parameterized string for the project URL.    *    * @param str new string.    */
DECL|method|setProject (String str)
specifier|public
name|void
name|setProject
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|project
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return parameterized string for the revision URL. */
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
comment|/**    * Set the parameterized string for the revision URL.    *    * @param str new string.    */
DECL|method|setRevision (String str)
specifier|public
name|void
name|setRevision
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|revision
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return parameterized string for the root tree URL. */
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
comment|/**    * Set the parameterized string for the root tree URL.    *    * @param str new string.    */
DECL|method|setRootTree (String str)
specifier|public
name|void
name|setRootTree
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|rootTree
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return path separator used for branch and project names. */
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
comment|/**    * Set the custom path separator.    *    * @param separator new separator.    */
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
comment|/** @return whether to URL encode path segments. */
DECL|method|getUrlEncode ()
specifier|public
name|boolean
name|getUrlEncode
parameter_list|()
block|{
return|return
name|urlEncode
return|;
block|}
comment|/**    * Set whether to URL encode path segments.    *    * @param urlEncode new value.    */
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
comment|/**    * Replace standard path separator with custom configured path separator.    *    * @param urlSegment URL segment (e.g. branch or project name) in which to replace the path    *     separator.    * @return the segment with the standard path separator replaced by the custom {@link    *     #getPathSeparator()}.    */
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
block|}
end_class

end_unit

