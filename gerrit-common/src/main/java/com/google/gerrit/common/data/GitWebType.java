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
comment|/**    * Get a GitWebType based on the given name.    *    * @param name Name to look for.    * @return GitWebType from the given name, else null if not found.    */
DECL|method|fromName (final String name)
specifier|public
specifier|static
name|GitWebType
name|fromName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
specifier|final
name|GitWebType
name|type
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
operator|||
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"gitweb"
argument_list|)
condition|)
block|{
name|type
operator|=
operator|new
name|GitWebType
argument_list|()
expr_stmt|;
name|type
operator|.
name|setLinkName
argument_list|(
literal|"gitweb"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setProject
argument_list|(
literal|"?p=${project}.git;a=summary"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setRevision
argument_list|(
literal|"?p=${project}.git;a=commit;h=${commit}"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setBranch
argument_list|(
literal|"?p=${project}.git;a=shortlog;h=${branch}"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"cgit"
argument_list|)
condition|)
block|{
name|type
operator|=
operator|new
name|GitWebType
argument_list|()
expr_stmt|;
name|type
operator|.
name|setLinkName
argument_list|(
literal|"cgit"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setProject
argument_list|(
literal|"${project}/summary"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setRevision
argument_list|(
literal|"${project}/commit/?id=${commit}"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setBranch
argument_list|(
literal|"${project}/log/?h=${branch}"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"custom"
argument_list|)
condition|)
block|{
name|type
operator|=
operator|new
name|GitWebType
argument_list|()
expr_stmt|;
comment|// The custom name is not defined, let's keep the old style of using GitWeb
name|type
operator|.
name|setLinkName
argument_list|(
literal|"gitweb"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|type
return|;
block|}
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
comment|/** ParamertizedString for project view url. */
DECL|field|project
specifier|private
name|String
name|project
decl_stmt|;
comment|/** ParamertizedString for branch view url. */
DECL|field|branch
specifier|private
name|String
name|branch
decl_stmt|;
comment|/** Private default constructor for gson. */
DECL|method|GitWebType ()
specifier|protected
name|GitWebType
parameter_list|()
block|{   }
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
if|if
condition|(
name|pattern
operator|!=
literal|null
operator|&&
operator|!
name|pattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|branch
operator|=
name|pattern
expr_stmt|;
block|}
block|}
comment|/**    * Set the pattern for link-name type.    *    * @param pattern The pattern for link-name type    */
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
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
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
if|if
condition|(
name|pattern
operator|!=
literal|null
operator|&&
operator|!
name|pattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|project
operator|=
name|pattern
expr_stmt|;
block|}
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
if|if
condition|(
name|pattern
operator|!=
literal|null
operator|&&
operator|!
name|pattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|revision
operator|=
name|pattern
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

