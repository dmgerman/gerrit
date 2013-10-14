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
DECL|package|com.google.gerrit.server.index
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
name|index
operator|.
name|IndexedChangeQuery
operator|.
name|replaceSortKeyPredicates
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
name|server
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
name|ChangeQueryBuilder
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|IndexedChangeQueryTest
specifier|public
class|class
name|IndexedChangeQueryTest
extends|extends
name|TestCase
block|{
DECL|field|index
specifier|private
name|FakeIndex
name|index
decl_stmt|;
DECL|field|queryBuilder
specifier|private
name|ChangeQueryBuilder
name|queryBuilder
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|index
operator|=
operator|new
name|FakeIndex
argument_list|(
name|FakeIndex
operator|.
name|V2
argument_list|)
expr_stmt|;
name|IndexCollection
name|indexes
init|=
operator|new
name|IndexCollection
argument_list|()
decl_stmt|;
name|indexes
operator|.
name|setSearchIndex
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|queryBuilder
operator|=
operator|new
name|FakeQueryBuilder
argument_list|(
name|indexes
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplaceSortKeyPredicate_NoSortKey ()
specifier|public
name|void
name|testReplaceSortKeyPredicate_NoSortKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
init|=
name|parse
argument_list|(
literal|"foo:a bar:b OR (foo:b bar:a)"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|replaceSortKeyPredicates
argument_list|(
name|p
argument_list|,
literal|"1234"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplaceSortKeyPredicate_TopLevelSortKey ()
specifier|public
name|void
name|testReplaceSortKeyPredicate_TopLevelSortKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
decl_stmt|;
name|p
operator|=
name|parse
argument_list|(
literal|"foo:a bar:b sortkey_before:1234 OR (foo:b bar:a)"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|parse
argument_list|(
literal|"foo:a bar:b sortkey_before:5678 OR (foo:b bar:a)"
argument_list|)
argument_list|,
name|replaceSortKeyPredicates
argument_list|(
name|p
argument_list|,
literal|"5678"
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|=
name|parse
argument_list|(
literal|"foo:a bar:b sortkey_after:1234 OR (foo:b bar:a)"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|parse
argument_list|(
literal|"foo:a bar:b sortkey_after:5678 OR (foo:b bar:a)"
argument_list|)
argument_list|,
name|replaceSortKeyPredicates
argument_list|(
name|p
argument_list|,
literal|"5678"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplaceSortKeyPredicate_NestedSortKey ()
specifier|public
name|void
name|testReplaceSortKeyPredicate_NestedSortKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
decl_stmt|;
name|p
operator|=
name|parse
argument_list|(
literal|"foo:a bar:b OR (foo:b bar:a AND sortkey_before:1234)"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|parse
argument_list|(
literal|"foo:a bar:b OR (foo:b bar:a sortkey_before:5678)"
argument_list|)
argument_list|,
name|replaceSortKeyPredicates
argument_list|(
name|p
argument_list|,
literal|"5678"
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|=
name|parse
argument_list|(
literal|"foo:a bar:b OR (foo:b bar:a AND sortkey_after:1234)"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|parse
argument_list|(
literal|"foo:a bar:b OR (foo:b bar:a sortkey_after:5678)"
argument_list|)
argument_list|,
name|replaceSortKeyPredicates
argument_list|(
name|p
argument_list|,
literal|"5678"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|parse (String query)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|parse
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|QueryParseException
block|{
return|return
name|queryBuilder
operator|.
name|parse
argument_list|(
name|query
argument_list|)
return|;
block|}
block|}
end_class

end_unit

