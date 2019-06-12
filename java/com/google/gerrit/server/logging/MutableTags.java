begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSetMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MultimapBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|SetMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|backend
operator|.
name|Tags
import|;
end_import

begin_class
DECL|class|MutableTags
specifier|public
class|class
name|MutableTags
block|{
DECL|field|tagMap
specifier|private
specifier|final
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tagMap
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|hashSetValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|field|tags
specifier|private
name|Tags
name|tags
init|=
name|Tags
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|method|getTags ()
specifier|public
name|Tags
name|getTags
parameter_list|()
block|{
return|return
name|tags
return|;
block|}
comment|/**    * Adds a tag if a tag with the same name and value doesn't exist yet.    *    * @param name the name of the tag    * @param value the value of the tag    * @return {@code true} if the tag was added, {@code false} if the tag was not added because it    *     already exists    */
DECL|method|add (String name, String value)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|name
argument_list|,
literal|"tag name is required"
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|value
argument_list|,
literal|"tag value is required"
argument_list|)
expr_stmt|;
name|boolean
name|ret
init|=
name|tagMap
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
condition|)
block|{
name|buildTags
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/**    * Removes the tag with the given name and value.    *    * @param name the name of the tag    * @param value the value of the tag    */
DECL|method|remove (String name, String value)
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|name
argument_list|,
literal|"tag name is required"
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|value
argument_list|,
literal|"tag value is required"
argument_list|)
expr_stmt|;
if|if
condition|(
name|tagMap
operator|.
name|remove
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
condition|)
block|{
name|buildTags
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Checks if the contained tag map is empty.    *    * @return {@code true} if there are no tags, otherwise {@code false}    */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|tagMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/** Clears all tags. */
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|tagMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|tags
operator|=
name|Tags
operator|.
name|empty
argument_list|()
expr_stmt|;
block|}
comment|/**    * Returns the tags as Multimap.    *    * @return the tags as Multimap    */
DECL|method|asMap ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
name|ImmutableSetMultimap
operator|.
name|copyOf
argument_list|(
name|tagMap
argument_list|)
return|;
block|}
comment|/**    * Replaces the existing tags with the provided tags.    *    * @param tags the tags that should be set.    */
DECL|method|set (ImmutableSetMultimap<String, String> tags)
name|void
name|set
parameter_list|(
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tags
parameter_list|)
block|{
name|tagMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|tags
operator|.
name|forEach
argument_list|(
name|tagMap
operator|::
name|put
argument_list|)
expr_stmt|;
name|buildTags
argument_list|()
expr_stmt|;
block|}
DECL|method|buildTags ()
specifier|private
name|void
name|buildTags
parameter_list|()
block|{
if|if
condition|(
name|tagMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|tags
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|tags
operator|=
name|Tags
operator|.
name|empty
argument_list|()
expr_stmt|;
return|return;
block|}
name|Tags
operator|.
name|Builder
name|tagsBuilder
init|=
name|Tags
operator|.
name|builder
argument_list|()
decl_stmt|;
name|tagMap
operator|.
name|forEach
argument_list|(
name|tagsBuilder
operator|::
name|addTag
argument_list|)
expr_stmt|;
name|tags
operator|=
name|tagsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|buildTags
argument_list|()
expr_stmt|;
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"tags"
argument_list|,
name|tags
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

