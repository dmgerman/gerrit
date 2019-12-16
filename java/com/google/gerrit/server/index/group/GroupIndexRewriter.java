begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.group
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
name|group
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
name|server
operator|.
name|group
operator|.
name|InternalGroup
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

begin_comment
comment|/** Rewriter for the group index. See {@link IndexRewriter} for details. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GroupIndexRewriter
specifier|public
class|class
name|GroupIndexRewriter
implements|implements
name|IndexRewriter
argument_list|<
name|InternalGroup
argument_list|>
block|{
DECL|field|indexes
specifier|private
specifier|final
name|GroupIndexCollection
name|indexes
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupIndexRewriter (GroupIndexCollection indexes)
name|GroupIndexRewriter
parameter_list|(
name|GroupIndexCollection
name|indexes
parameter_list|)
block|{
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|rewrite (Predicate<InternalGroup> in, QueryOptions opts)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|rewrite
parameter_list|(
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|in
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|GroupIndex
name|index
init|=
name|indexes
operator|.
name|getSearchIndex
argument_list|()
decl_stmt|;
name|requireNonNull
argument_list|(
name|index
argument_list|,
literal|"no active search index configured for groups"
argument_list|)
expr_stmt|;
return|return
operator|new
name|IndexedGroupQuery
argument_list|(
name|index
argument_list|,
name|in
argument_list|,
name|opts
argument_list|)
return|;
block|}
block|}
end_class

end_unit

