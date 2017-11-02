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
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.query.change
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
operator|.
name|change
package|;
end_package

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
name|assertTrue
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|server
operator|.
name|OrmException
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RegexPathPredicateTest
specifier|public
class|class
name|RegexPathPredicateTest
block|{
annotation|@
name|Test
DECL|method|prefixOnlyOptimization ()
specifier|public
name|void
name|prefixOnlyOptimization
parameter_list|()
throws|throws
name|OrmException
block|{
name|RegexPathPredicate
name|p
init|=
name|predicate
argument_list|(
literal|"^a/b/.*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a/b/source.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"source.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a/b/source.c"
argument_list|,
literal|"a/c/test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a/bb/source.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|prefixReducesSearchSpace ()
specifier|public
name|void
name|prefixReducesSearchSpace
parameter_list|()
throws|throws
name|OrmException
block|{
name|RegexPathPredicate
name|p
init|=
name|predicate
argument_list|(
literal|"^a/b/.*\\.[ch]"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a/b/source.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a/b/source.res"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"source.res"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a/b/a.a"
argument_list|,
literal|"a/b/a.d"
argument_list|,
literal|"a/b/a.h"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|fileExtension_Constant ()
specifier|public
name|void
name|fileExtension_Constant
parameter_list|()
throws|throws
name|OrmException
block|{
name|RegexPathPredicate
name|p
init|=
name|predicate
argument_list|(
literal|"^.*\\.res"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"test.res"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"foo/bar/test.res"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"test.res.bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|fileExtension_CharacterGroup ()
specifier|public
name|void
name|fileExtension_CharacterGroup
parameter_list|()
throws|throws
name|OrmException
block|{
name|RegexPathPredicate
name|p
init|=
name|predicate
argument_list|(
literal|"^.*\\.[ch]"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"test.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"test.h"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"test.C"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|endOfString ()
specifier|public
name|void
name|endOfString
parameter_list|()
throws|throws
name|OrmException
block|{
name|assertTrue
argument_list|(
name|predicate
argument_list|(
literal|"^a$"
argument_list|)
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
argument_list|(
literal|"^a$"
argument_list|)
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a$"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
argument_list|(
literal|"^a\\$"
argument_list|)
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|predicate
argument_list|(
literal|"^a\\$"
argument_list|)
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"a$"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exactMatch ()
specifier|public
name|void
name|exactMatch
parameter_list|()
throws|throws
name|OrmException
block|{
name|RegexPathPredicate
name|p
init|=
name|predicate
argument_list|(
literal|"^foo.c"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"foo.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"foo.cc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|match
argument_list|(
name|change
argument_list|(
literal|"bar.c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|predicate (String pattern)
specifier|private
specifier|static
name|RegexPathPredicate
name|predicate
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|RegexPathPredicate
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|change (String... files)
specifier|private
specifier|static
name|ChangeData
name|change
parameter_list|(
name|String
modifier|...
name|files
parameter_list|)
throws|throws
name|OrmException
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|ChangeData
name|cd
init|=
name|ChangeData
operator|.
name|createForTest
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project"
argument_list|)
argument_list|,
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|cd
operator|.
name|setCurrentFilePaths
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|files
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cd
return|;
block|}
block|}
end_class

end_unit
