begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
comment|// limitations under the License
end_comment

begin_package
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/** Utilities for manipulating Collections . */
end_comment

begin_class
DECL|class|CollectionsUtil
specifier|public
class|class
name|CollectionsUtil
block|{
comment|/**    * Checks if any of the elements in the first collection can be found in the    * second collection.    *    * @param findAnyOfThese which elements to look for.    * @param inThisCollection where to look for them.    * @param<E> type of the elements in question.    * @return {@code true} if any of the elements in {@code findAnyOfThese} can    *         be found in {@code inThisCollection}, {@code false} otherwise.    */
DECL|method|isAnyIncludedIn (Collection<E> findAnyOfThese, Collection<E> inThisCollection)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|boolean
name|isAnyIncludedIn
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|findAnyOfThese
parameter_list|,
name|Collection
argument_list|<
name|E
argument_list|>
name|inThisCollection
parameter_list|)
block|{
for|for
control|(
name|E
name|findThisItem
range|:
name|findAnyOfThese
control|)
block|{
if|if
condition|(
name|inThisCollection
operator|.
name|contains
argument_list|(
name|findThisItem
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|CollectionsUtil ()
specifier|private
name|CollectionsUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

