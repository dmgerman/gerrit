begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|cache
operator|.
name|Cache
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
name|ImmutableList
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
name|FluentLogger
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
name|common
operator|.
name|data
operator|.
name|AccessSection
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
name|server
operator|.
name|cache
operator|.
name|CacheModule
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
name|server
operator|.
name|util
operator|.
name|MostSpecificComparator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Module
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|IdentityHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Caches the order AccessSections should be sorted for evaluation.  *  *<p>Access specifications for a more specific ref (eg. refs/heads/master rather than refs/heads/*)  * take precedence in ACL evaluations. So for each combination of (ref, list of access specs) we  * have to order the access specs by their distance from the ref to be matched. This is expensive,  * so cache the sorted ordering.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SectionSortCache
specifier|public
class|class
name|SectionSortCache
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|CACHE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CACHE_NAME
init|=
literal|"permission_sort"
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|CACHE_NAME
argument_list|,
name|EntryKey
operator|.
name|class
argument_list|,
name|EntryVal
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SectionSortCache
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|cache
specifier|private
specifier|final
name|Cache
argument_list|<
name|EntryKey
argument_list|,
name|EntryVal
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|SectionSortCache (@amedCACHE_NAME) Cache<EntryKey, EntryVal> cache)
name|SectionSortCache
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE_NAME
argument_list|)
name|Cache
argument_list|<
name|EntryKey
argument_list|,
name|EntryVal
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
comment|// Sorts the given sections, but does not disturb ordering between equally exact sections.
DECL|method|sort (String ref, List<AccessSection> sections)
name|void
name|sort
parameter_list|(
name|String
name|ref
parameter_list|,
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
parameter_list|)
block|{
specifier|final
name|int
name|cnt
init|=
name|sections
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|cnt
operator|<=
literal|1
condition|)
block|{
return|return;
block|}
name|EntryKey
name|key
init|=
name|EntryKey
operator|.
name|create
argument_list|(
name|ref
argument_list|,
name|sections
argument_list|)
decl_stmt|;
name|EntryVal
name|val
init|=
name|cache
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|int
index|[]
name|srcIdx
init|=
name|val
operator|.
name|order
decl_stmt|;
if|if
condition|(
name|srcIdx
operator|!=
literal|null
condition|)
block|{
name|AccessSection
index|[]
name|srcList
init|=
name|copy
argument_list|(
name|sections
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cnt
condition|;
name|i
operator|++
control|)
block|{
name|sections
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|srcList
index|[
name|srcIdx
index|[
name|i
index|]
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Identity transform. No sorting is required.
block|}
block|}
else|else
block|{
name|boolean
name|poison
init|=
literal|false
decl_stmt|;
name|IdentityHashMap
argument_list|<
name|AccessSection
argument_list|,
name|Integer
argument_list|>
name|srcMap
init|=
operator|new
name|IdentityHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cnt
condition|;
name|i
operator|++
control|)
block|{
name|poison
operator||=
name|srcMap
operator|.
name|put
argument_list|(
name|sections
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|i
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|sections
argument_list|,
operator|new
name|MostSpecificComparator
argument_list|(
name|ref
argument_list|)
argument_list|)
expr_stmt|;
name|int
index|[]
name|srcIdx
decl_stmt|;
if|if
condition|(
name|isIdentityTransform
argument_list|(
name|sections
argument_list|,
name|srcMap
argument_list|)
condition|)
block|{
name|srcIdx
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|srcIdx
operator|=
operator|new
name|int
index|[
name|cnt
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cnt
condition|;
name|i
operator|++
control|)
block|{
name|srcIdx
index|[
name|i
index|]
operator|=
name|srcMap
operator|.
name|get
argument_list|(
name|sections
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|poison
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Received duplicate AccessSection instances, not caching sort"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|EntryVal
argument_list|(
name|srcIdx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|copy (List<AccessSection> sections)
specifier|private
specifier|static
name|AccessSection
index|[]
name|copy
parameter_list|(
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
parameter_list|)
block|{
return|return
name|sections
operator|.
name|toArray
argument_list|(
operator|new
name|AccessSection
index|[
name|sections
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|isIdentityTransform ( List<AccessSection> sections, IdentityHashMap<AccessSection, Integer> srcMap)
specifier|private
specifier|static
name|boolean
name|isIdentityTransform
parameter_list|(
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
parameter_list|,
name|IdentityHashMap
argument_list|<
name|AccessSection
argument_list|,
name|Integer
argument_list|>
name|srcMap
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|sections
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|!=
name|srcMap
operator|.
name|get
argument_list|(
name|sections
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|AutoValue
DECL|class|EntryKey
specifier|abstract
specifier|static
class|class
name|EntryKey
block|{
DECL|method|ref ()
specifier|public
specifier|abstract
name|String
name|ref
parameter_list|()
function_decl|;
DECL|method|patterns ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|()
function_decl|;
DECL|method|cachedHashCode ()
specifier|public
specifier|abstract
name|int
name|cachedHashCode
parameter_list|()
function_decl|;
DECL|method|create (String refName, List<AccessSection> sections)
specifier|static
name|EntryKey
name|create
parameter_list|(
name|String
name|refName
parameter_list|,
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
parameter_list|)
block|{
name|int
name|hc
init|=
name|refName
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|patterns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|sections
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccessSection
name|s
range|:
name|sections
control|)
block|{
name|String
name|n
init|=
name|s
operator|.
name|getName
argument_list|()
decl_stmt|;
name|patterns
operator|.
name|add
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|hc
operator|=
name|hc
operator|*
literal|31
operator|+
name|n
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|AutoValue_SectionSortCache_EntryKey
argument_list|(
name|refName
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|patterns
argument_list|)
argument_list|,
name|hc
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|cachedHashCode
argument_list|()
return|;
block|}
block|}
DECL|class|EntryVal
specifier|static
specifier|final
class|class
name|EntryVal
block|{
comment|/**      * Maps the input index to the output index.      *      *<p>For {@code x == order[y]} the expression means move the item at source position {@code x}      * to the output position {@code y}.      */
DECL|field|order
specifier|final
name|int
index|[]
name|order
decl_stmt|;
DECL|method|EntryVal (int[] order)
name|EntryVal
parameter_list|(
name|int
index|[]
name|order
parameter_list|)
block|{
name|this
operator|.
name|order
operator|=
name|order
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

