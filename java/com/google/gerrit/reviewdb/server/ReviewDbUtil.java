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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|Ordering
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
name|Sets
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
name|Column
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|TreeSet
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
name|DisallowedReviewDb
condition|)
block|{
return|return
name|unwrapDb
argument_list|(
operator|(
operator|(
name|DisallowedReviewDb
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
DECL|method|checkColumns (Class<?> clazz, Integer... expected)
specifier|public
specifier|static
name|void
name|checkColumns
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Integer
modifier|...
name|expected
parameter_list|)
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|ids
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|clazz
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|Column
name|col
init|=
name|f
operator|.
name|getAnnotation
argument_list|(
name|Column
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|!=
literal|null
condition|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|col
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Set
argument_list|<
name|Integer
argument_list|>
name|expectedIds
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|ids
operator|.
name|equals
argument_list|(
name|expectedIds
argument_list|)
argument_list|,
literal|"Unexpected column set for %s: %s != %s"
argument_list|,
name|clazz
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|ids
argument_list|,
name|expectedIds
argument_list|)
expr_stmt|;
block|}
DECL|method|ReviewDbUtil ()
specifier|private
name|ReviewDbUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

