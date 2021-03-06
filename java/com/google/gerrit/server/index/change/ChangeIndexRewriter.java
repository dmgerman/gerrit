begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeStatusPredicate
operator|.
name|closed
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeStatusPredicate
operator|.
name|open
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
name|Lists
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
name|gerrit
operator|.
name|entities
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
name|entities
operator|.
name|Change
operator|.
name|Status
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
name|index
operator|.
name|FieldDef
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
name|index
operator|.
name|IndexConfig
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
name|index
operator|.
name|IndexRewriter
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
name|index
operator|.
name|QueryOptions
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
name|index
operator|.
name|Schema
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
name|index
operator|.
name|query
operator|.
name|AndPredicate
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
name|index
operator|.
name|query
operator|.
name|IndexPredicate
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
name|index
operator|.
name|query
operator|.
name|LimitPredicate
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
name|index
operator|.
name|query
operator|.
name|NotPredicate
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
name|index
operator|.
name|query
operator|.
name|OrPredicate
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|index
operator|.
name|query
operator|.
name|QueryParseException
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
name|index
operator|.
name|query
operator|.
name|TooManyTermsInQueryException
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
name|query
operator|.
name|change
operator|.
name|AndChangeSource
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|ChangeDataSource
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
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
name|query
operator|.
name|change
operator|.
name|ChangeStatusPredicate
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
name|query
operator|.
name|change
operator|.
name|OrSource
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|MutableInteger
import|;
end_import

begin_comment
comment|/** Rewriter that pushes boolean logic into the secondary index. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeIndexRewriter
specifier|public
class|class
name|ChangeIndexRewriter
implements|implements
name|IndexRewriter
argument_list|<
name|ChangeData
argument_list|>
block|{
comment|/** Set of all open change statuses. */
DECL|field|OPEN_STATUSES
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|OPEN_STATUSES
decl_stmt|;
comment|/** Set of all closed change statuses. */
DECL|field|CLOSED_STATUSES
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|CLOSED_STATUSES
decl_stmt|;
static|static
block|{
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|open
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|class
argument_list|)
decl_stmt|;
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|closed
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
operator|.
name|Status
name|s
range|:
name|Change
operator|.
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|open
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|closed
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|OPEN_STATUSES
operator|=
name|Sets
operator|.
name|immutableEnumSet
argument_list|(
name|open
argument_list|)
expr_stmt|;
name|CLOSED_STATUSES
operator|=
name|Sets
operator|.
name|immutableEnumSet
argument_list|(
name|closed
argument_list|)
expr_stmt|;
block|}
comment|/**    * Get the set of statuses that changes matching the given predicate may have.    *    * @param in predicate    * @return the maximal set of statuses that any changes matching the input predicates may have,    *     based on examining boolean and {@link ChangeStatusPredicate}s.    */
DECL|method|getPossibleStatus (Predicate<ChangeData> in)
specifier|public
specifier|static
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|getPossibleStatus
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|)
block|{
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|s
init|=
name|extractStatus
argument_list|(
name|in
argument_list|)
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|s
else|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|extractStatus (Predicate<ChangeData> in)
specifier|private
specifier|static
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|extractStatus
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|instanceof
name|ChangeStatusPredicate
condition|)
block|{
name|Status
name|status
init|=
operator|(
operator|(
name|ChangeStatusPredicate
operator|)
name|in
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
return|return
name|status
operator|!=
literal|null
condition|?
name|EnumSet
operator|.
name|of
argument_list|(
name|status
argument_list|)
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|NotPredicate
condition|)
block|{
name|EnumSet
argument_list|<
name|Status
argument_list|>
name|s
init|=
name|extractStatus
argument_list|(
name|in
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|EnumSet
operator|.
name|complementOf
argument_list|(
name|s
argument_list|)
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|OrPredicate
condition|)
block|{
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|r
init|=
literal|null
decl_stmt|;
name|int
name|childrenWithStatus
init|=
literal|0
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
name|in
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|EnumSet
argument_list|<
name|Status
argument_list|>
name|c
init|=
name|extractStatus
argument_list|(
name|in
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|addAll
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|childrenWithStatus
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|r
operator|!=
literal|null
operator|&&
name|childrenWithStatus
operator|<
name|in
operator|.
name|getChildCount
argument_list|()
condition|)
block|{
comment|// At least one child supplied a status but another did not.
comment|// Assume all statuses for the children that did not feed a
comment|// status at this part of the tree. This matches behavior if
comment|// the child was used at the root of a query.
return|return
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|class
argument_list|)
return|;
block|}
return|return
name|r
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|AndPredicate
condition|)
block|{
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|r
init|=
literal|null
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
name|in
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|EnumSet
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|c
init|=
name|extractStatus
argument_list|(
name|in
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|field|indexes
specifier|private
specifier|final
name|ChangeIndexCollection
name|indexes
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|IndexConfig
name|config
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeIndexRewriter (ChangeIndexCollection indexes, IndexConfig config)
name|ChangeIndexRewriter
parameter_list|(
name|ChangeIndexCollection
name|indexes
parameter_list|,
name|IndexConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|rewrite (Predicate<ChangeData> in, QueryOptions opts)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|rewrite
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|s
init|=
name|rewriteImpl
argument_list|(
name|in
argument_list|,
name|opts
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|ChangeDataSource
operator|)
condition|)
block|{
name|in
operator|=
name|Predicate
operator|.
name|and
argument_list|(
name|Predicate
operator|.
name|or
argument_list|(
name|open
argument_list|()
argument_list|,
name|closed
argument_list|()
argument_list|)
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|s
operator|=
name|rewriteImpl
argument_list|(
name|in
argument_list|,
name|opts
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|ChangeDataSource
operator|)
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"invalid query: "
operator|+
name|s
argument_list|)
throw|;
block|}
return|return
name|s
return|;
block|}
DECL|method|rewriteImpl (Predicate<ChangeData> in, QueryOptions opts)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|rewriteImpl
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|ChangeIndex
name|index
init|=
name|indexes
operator|.
name|getSearchIndex
argument_list|()
decl_stmt|;
name|MutableInteger
name|leafTerms
init|=
operator|new
name|MutableInteger
argument_list|()
decl_stmt|;
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|out
init|=
name|rewriteImpl
argument_list|(
name|in
argument_list|,
name|index
argument_list|,
name|opts
argument_list|,
name|leafTerms
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSameInstance
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
operator|||
name|out
operator|instanceof
name|IndexPredicate
condition|)
block|{
return|return
operator|new
name|IndexedChangeQuery
argument_list|(
name|index
argument_list|,
name|out
argument_list|,
name|opts
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|out
operator|==
literal|null
comment|/* cannot rewrite */
condition|)
block|{
return|return
name|in
return|;
block|}
else|else
block|{
return|return
name|out
return|;
block|}
block|}
comment|/**    * Rewrite a single predicate subtree.    *    * @param in predicate to rewrite.    * @param index index whose schema determines which fields are indexed.    * @param opts other query options.    * @param leafTerms number of leaf index query terms encountered so far.    * @return {@code null} if no part of this subtree can be queried in the index directly. {@code    *     in} if this subtree and all its children can be queried directly in the index. Otherwise, a    *     predicate that is semantically equivalent, with some of its subtrees wrapped to query the    *     index directly.    * @throws QueryParseException if the underlying index implementation does not support this    *     predicate.    */
DECL|method|rewriteImpl ( Predicate<ChangeData> in, ChangeIndex index, QueryOptions opts, MutableInteger leafTerms)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|rewriteImpl
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|ChangeIndex
name|index
parameter_list|,
name|QueryOptions
name|opts
parameter_list|,
name|MutableInteger
name|leafTerms
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|isIndexPredicate
argument_list|(
name|in
argument_list|,
name|index
argument_list|)
condition|)
block|{
if|if
condition|(
operator|++
name|leafTerms
operator|.
name|value
operator|>
name|config
operator|.
name|maxTerms
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|TooManyTermsInQueryException
argument_list|()
throw|;
block|}
return|return
name|in
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|LimitPredicate
condition|)
block|{
comment|// Replace any limits with the limit provided by the caller. The caller
comment|// should have already searched the predicate tree for limit predicates
comment|// and included that in their limit computation.
return|return
operator|new
name|LimitPredicate
argument_list|<>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_LIMIT
argument_list|,
name|opts
operator|.
name|limit
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isRewritePossible
argument_list|(
name|in
argument_list|)
condition|)
block|{
if|if
condition|(
name|in
operator|instanceof
name|IndexPredicate
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"Unsupported index predicate: "
operator|+
name|in
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
comment|// magic to indicate "in" cannot be rewritten
block|}
name|int
name|n
init|=
name|in
operator|.
name|getChildCount
argument_list|()
decl_stmt|;
name|BitSet
name|isIndexed
init|=
operator|new
name|BitSet
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|BitSet
name|notIndexed
init|=
operator|new
name|BitSet
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|BitSet
name|rewritten
init|=
operator|new
name|BitSet
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|BitSet
name|changeSource
init|=
operator|new
name|BitSet
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|newChildren
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|n
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
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|c
init|=
name|in
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|nc
init|=
name|rewriteImpl
argument_list|(
name|c
argument_list|,
name|index
argument_list|,
name|opts
argument_list|,
name|leafTerms
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSameInstance
argument_list|(
name|nc
argument_list|,
name|c
argument_list|)
condition|)
block|{
name|isIndexed
operator|.
name|set
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|newChildren
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nc
operator|==
literal|null
comment|/* cannot rewrite c */
condition|)
block|{
name|notIndexed
operator|.
name|set
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|newChildren
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|nc
operator|instanceof
name|ChangeDataSource
condition|)
block|{
name|changeSource
operator|.
name|set
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|rewritten
operator|.
name|set
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|newChildren
operator|.
name|add
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isIndexed
operator|.
name|cardinality
argument_list|()
operator|==
name|n
condition|)
block|{
return|return
name|in
return|;
comment|// All children are indexed, leave as-is for parent.
block|}
elseif|else
if|if
condition|(
name|notIndexed
operator|.
name|cardinality
argument_list|()
operator|==
name|n
condition|)
block|{
return|return
literal|null
return|;
comment|// Can't rewrite any children, so cannot rewrite in.
block|}
elseif|else
if|if
condition|(
name|rewritten
operator|.
name|cardinality
argument_list|()
operator|==
name|n
condition|)
block|{
comment|// All children were rewritten.
if|if
condition|(
name|changeSource
operator|.
name|cardinality
argument_list|()
operator|==
name|n
condition|)
block|{
return|return
name|copy
argument_list|(
name|in
argument_list|,
name|newChildren
argument_list|)
return|;
block|}
return|return
name|in
operator|.
name|copy
argument_list|(
name|newChildren
argument_list|)
return|;
block|}
return|return
name|partitionChildren
argument_list|(
name|in
argument_list|,
name|newChildren
argument_list|,
name|isIndexed
argument_list|,
name|index
argument_list|,
name|opts
argument_list|)
return|;
block|}
DECL|method|isIndexPredicate (Predicate<ChangeData> in, ChangeIndex index)
specifier|private
name|boolean
name|isIndexPredicate
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|ChangeIndex
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|in
operator|instanceof
name|IndexPredicate
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
name|p
init|=
operator|(
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
operator|)
name|in
decl_stmt|;
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|def
init|=
name|p
operator|.
name|getField
argument_list|()
decl_stmt|;
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
init|=
name|index
operator|.
name|getSchema
argument_list|()
decl_stmt|;
return|return
name|schema
operator|.
name|hasField
argument_list|(
name|def
argument_list|)
return|;
block|}
DECL|method|partitionChildren ( Predicate<ChangeData> in, List<Predicate<ChangeData>> newChildren, BitSet isIndexed, ChangeIndex index, QueryOptions opts)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|partitionChildren
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|newChildren
parameter_list|,
name|BitSet
name|isIndexed
parameter_list|,
name|ChangeIndex
name|index
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|isIndexed
operator|.
name|cardinality
argument_list|()
operator|==
literal|1
condition|)
block|{
name|int
name|i
init|=
name|isIndexed
operator|.
name|nextSetBit
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|newChildren
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|IndexedChangeQuery
argument_list|(
name|index
argument_list|,
name|newChildren
operator|.
name|remove
argument_list|(
name|i
argument_list|)
argument_list|,
name|opts
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|copy
argument_list|(
name|in
argument_list|,
name|newChildren
argument_list|)
return|;
block|}
comment|// Group all indexed predicates into a wrapped subtree.
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|indexed
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|isIndexed
operator|.
name|cardinality
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|all
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|newChildren
operator|.
name|size
argument_list|()
operator|-
name|isIndexed
operator|.
name|cardinality
argument_list|()
operator|+
literal|1
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
name|newChildren
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|c
init|=
name|newChildren
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|isIndexed
operator|.
name|get
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|indexed
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|all
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|all
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|IndexedChangeQuery
argument_list|(
name|index
argument_list|,
name|in
operator|.
name|copy
argument_list|(
name|indexed
argument_list|)
argument_list|,
name|opts
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|copy
argument_list|(
name|in
argument_list|,
name|all
argument_list|)
return|;
block|}
DECL|method|copy (Predicate<ChangeData> in, List<Predicate<ChangeData>> all)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|copy
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|all
parameter_list|)
block|{
if|if
condition|(
name|in
operator|instanceof
name|AndPredicate
condition|)
block|{
return|return
operator|new
name|AndChangeSource
argument_list|(
name|all
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|OrPredicate
condition|)
block|{
return|return
operator|new
name|OrSource
argument_list|(
name|all
argument_list|)
return|;
block|}
return|return
name|in
operator|.
name|copy
argument_list|(
name|all
argument_list|)
return|;
block|}
DECL|method|isRewritePossible (Predicate<ChangeData> p)
specifier|private
specifier|static
name|boolean
name|isRewritePossible
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|getChildCount
argument_list|()
operator|>
literal|0
operator|&&
operator|(
name|p
operator|instanceof
name|AndPredicate
operator|||
name|p
operator|instanceof
name|OrPredicate
operator|||
name|p
operator|instanceof
name|NotPredicate
operator|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"ReferenceEquality"
argument_list|)
DECL|method|isSameInstance (T a, T b)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|boolean
name|isSameInstance
parameter_list|(
name|T
name|a
parameter_list|,
name|T
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
return|;
block|}
block|}
end_class

end_unit

