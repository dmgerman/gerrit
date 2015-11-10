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
DECL|package|com.google.gerrit.server.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|Predicate
operator|.
name|and
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
name|Predicate
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|List
import|;
end_import

begin_class
DECL|class|NotPredicateTest
specifier|public
class|class
name|NotPredicateTest
extends|extends
name|PredicateTest
block|{
annotation|@
name|Test
DECL|method|testNotNot ()
specifier|public
name|void
name|testNotNot
parameter_list|()
block|{
specifier|final
name|TestPredicate
name|p
init|=
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|n
init|=
name|not
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|n
operator|instanceof
name|NotPredicate
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|p
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|not
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChildren ()
specifier|public
name|void
name|testChildren
parameter_list|()
block|{
specifier|final
name|TestPredicate
name|p
init|=
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|n
init|=
name|not
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|n
operator|.
name|getChildCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|n
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChildrenUnmodifiable ()
specifier|public
name|void
name|testChildrenUnmodifiable
parameter_list|()
block|{
specifier|final
name|TestPredicate
name|p
init|=
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|n
init|=
name|not
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|n
operator|.
name|getChildren
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertOnlyChild
argument_list|(
literal|"clear"
argument_list|,
name|p
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|n
operator|.
name|getChildren
argument_list|()
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertOnlyChild
argument_list|(
literal|"remove(0)"
argument_list|,
name|p
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|n
operator|.
name|getChildren
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertOnlyChild
argument_list|(
literal|"remove(0)"
argument_list|,
name|p
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
DECL|method|assertOnlyChild (String o, Predicate<String> c, Predicate<String> p)
specifier|private
specifier|static
name|void
name|assertOnlyChild
parameter_list|(
name|String
name|o
parameter_list|,
name|Predicate
argument_list|<
name|String
argument_list|>
name|c
parameter_list|,
name|Predicate
argument_list|<
name|String
argument_list|>
name|p
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|o
operator|+
literal|" did not affect child"
argument_list|,
literal|1
argument_list|,
name|p
operator|.
name|getChildCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o
operator|+
literal|" did not affect child"
argument_list|,
name|c
argument_list|,
name|p
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"-author:bob"
argument_list|,
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"alice"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
literal|"author"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
operator|.
name|hashCode
argument_list|()
operator|==
name|not
argument_list|(
name|f
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|not
argument_list|(
name|f
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
operator|.
name|hashCode
argument_list|()
operator|==
name|not
argument_list|(
name|f
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
block|{
specifier|final
name|TestPredicate
name|a
init|=
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"alice"
argument_list|)
decl_stmt|;
specifier|final
name|TestPredicate
name|b
init|=
name|f
argument_list|(
literal|"author"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|TestPredicate
argument_list|>
name|sa
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|a
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|TestPredicate
argument_list|>
name|sb
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|b
argument_list|)
decl_stmt|;
specifier|final
name|Predicate
name|n
init|=
name|not
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|n
argument_list|,
name|n
operator|.
name|copy
argument_list|(
name|sa
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sa
argument_list|,
name|n
operator|.
name|copy
argument_list|(
name|sa
argument_list|)
operator|.
name|getChildren
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|n
argument_list|,
name|n
operator|.
name|copy
argument_list|(
name|sb
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sb
argument_list|,
name|n
operator|.
name|copy
argument_list|(
name|sb
argument_list|)
operator|.
name|getChildren
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|n
operator|.
name|copy
argument_list|(
name|Collections
operator|.
expr|<
name|Predicate
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Expected exactly one child"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|n
operator|.
name|copy
argument_list|(
name|and
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
operator|.
name|getChildren
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Expected exactly one child"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

