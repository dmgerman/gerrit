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
name|common
operator|.
name|base
operator|.
name|CharMatcher
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
name|Strings
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
DECL|class|HashtagsUtil
specifier|public
class|class
name|HashtagsUtil
block|{
DECL|class|InvalidHashtagException
specifier|public
specifier|static
class|class
name|InvalidHashtagException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|hashtagsMayNotContainCommas ()
specifier|static
name|InvalidHashtagException
name|hashtagsMayNotContainCommas
parameter_list|()
block|{
return|return
operator|new
name|InvalidHashtagException
argument_list|(
literal|"hashtags may not contain commas"
argument_list|)
return|;
block|}
DECL|method|InvalidHashtagException (String message)
name|InvalidHashtagException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|LEADER
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|LEADER
init|=
name|CharMatcher
operator|.
name|whitespace
argument_list|()
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'#'
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|String
name|PATTERN
init|=
literal|"(?:\\s|\\A)#[\\p{L}[0-9]-_]+"
decl_stmt|;
DECL|method|cleanupHashtag (String hashtag)
specifier|public
specifier|static
name|String
name|cleanupHashtag
parameter_list|(
name|String
name|hashtag
parameter_list|)
block|{
name|hashtag
operator|=
name|LEADER
operator|.
name|trimLeadingFrom
argument_list|(
name|hashtag
argument_list|)
expr_stmt|;
name|hashtag
operator|=
name|CharMatcher
operator|.
name|whitespace
argument_list|()
operator|.
name|trimTrailingFrom
argument_list|(
name|hashtag
argument_list|)
expr_stmt|;
return|return
name|hashtag
return|;
block|}
DECL|method|extractTags (String input)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|extractTags
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
argument_list|)
condition|)
block|{
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|PATTERN
argument_list|)
operator|.
name|matcher
argument_list|(
name|input
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|cleanupHashtag
argument_list|(
name|matcher
operator|.
name|group
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|extractTags (Set<String> input)
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|extractTags
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|input
parameter_list|)
throws|throws
name|InvalidHashtagException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
name|HashSet
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|hashtag
range|:
name|input
control|)
block|{
if|if
condition|(
name|hashtag
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
throw|throw
name|InvalidHashtagException
operator|.
name|hashtagsMayNotContainCommas
argument_list|()
throw|;
block|}
name|hashtag
operator|=
name|cleanupHashtag
argument_list|(
name|hashtag
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hashtag
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|hashtag
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|HashtagsUtil ()
specifier|private
name|HashtagsUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

