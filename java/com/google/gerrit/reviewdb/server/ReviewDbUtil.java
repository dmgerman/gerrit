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
DECL|package|com.google.gerrit.reviewdb.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
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
name|collect
operator|.
name|Ordering
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|IntKey
import|;
end_import

begin_comment
comment|/** Static utilities for ReviewDb types. */
end_comment

begin_class
DECL|class|ReviewDbUtil
specifier|public
class|class
name|ReviewDbUtil
block|{
DECL|field|INT_KEY_ORDERING
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|?
extends|extends
name|IntKey
argument_list|<
name|?
argument_list|>
argument_list|>
name|INT_KEY_ORDERING
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
operator|.
operator|<
name|IntKey
argument_list|<
name|?
argument_list|>
operator|>
name|onResultOf
argument_list|(
name|IntKey
operator|::
name|get
argument_list|)
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
comment|/**    * Null-safe ordering over arbitrary subclass of {@code IntKey}.    *    *<p>In some cases, {@code Comparator.comparing(Change.Id::get)} may be shorter and cleaner.    * However, this method may be preferable in some cases:    *    *<ul>    *<li>This ordering is null-safe over both input and the result of {@link IntKey#get()}; {@code    *       comparing} is only a good idea if all inputs are obviously non-null.    *<li>{@code intKeyOrdering().sortedCopy(iterable)} is shorter than the stream equivalent.    *<li>Creating derived comparators may be more readable with {@link Ordering} method chaining    *       rather than static {@code Comparator} methods.    *</ul>    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|intKeyOrdering ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|IntKey
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Ordering
argument_list|<
name|K
argument_list|>
name|intKeyOrdering
parameter_list|()
block|{
return|return
operator|(
name|Ordering
argument_list|<
name|K
argument_list|>
operator|)
name|INT_KEY_ORDERING
return|;
block|}
DECL|method|unwrapDb (ReviewDb db)
specifier|public
specifier|static
name|ReviewDb
name|unwrapDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
block|{
if|if
condition|(
name|db
operator|instanceof
name|DisallowReadFromChangesReviewDbWrapper
condition|)
block|{
return|return
name|unwrapDb
argument_list|(
operator|(
operator|(
name|DisallowReadFromChangesReviewDbWrapper
operator|)
name|db
operator|)
operator|.
name|unsafeGetDelegate
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|db
operator|instanceof
name|DisallowReadFromGroupsReviewDbWrapper
condition|)
block|{
return|return
name|unwrapDb
argument_list|(
operator|(
operator|(
name|DisallowReadFromGroupsReviewDbWrapper
operator|)
name|db
operator|)
operator|.
name|unsafeGetDelegate
argument_list|()
argument_list|)
return|;
block|}
return|return
name|db
return|;
block|}
DECL|method|ReviewDbUtil ()
specifier|private
name|ReviewDbUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

