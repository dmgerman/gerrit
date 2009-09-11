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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ParamertizedStringTest
specifier|public
class|class
name|ParamertizedStringTest
extends|extends
name|TestCase
block|{
DECL|method|testEmptyString ()
specifier|public
name|void
name|testEmptyString
parameter_list|()
block|{
specifier|final
name|ParamertizedString
name|p
init|=
operator|new
name|ParamertizedString
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|p
operator|.
name|getRawPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|a
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|p
operator|.
name|replace
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsis1 ()
specifier|public
name|void
name|testAsis1
parameter_list|()
block|{
specifier|final
name|ParamertizedString
name|p
init|=
name|ParamertizedString
operator|.
name|asis
argument_list|(
literal|"${bar}c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"${bar}c"
argument_list|,
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"${bar}c"
argument_list|,
name|p
operator|.
name|getRawPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|a
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|a
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"frobinator"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"${bar}c"
argument_list|,
name|p
operator|.
name|replace
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplace1 ()
specifier|public
name|void
name|testReplace1
parameter_list|()
block|{
specifier|final
name|ParamertizedString
name|p
init|=
operator|new
name|ParamertizedString
argument_list|(
literal|"${bar}c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"${bar}c"
argument_list|,
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{0}c"
argument_list|,
name|p
operator|.
name|getRawPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|a
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|a
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"frobinator"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"frobinator"
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"frobinatorc"
argument_list|,
name|p
operator|.
name|replace
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplace2 ()
specifier|public
name|void
name|testReplace2
parameter_list|()
block|{
specifier|final
name|ParamertizedString
name|p
init|=
operator|new
name|ParamertizedString
argument_list|(
literal|"a${bar}c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a${bar}c"
argument_list|,
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a{0}c"
argument_list|,
name|p
operator|.
name|getRawPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|a
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|a
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"frobinator"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"frobinator"
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"afrobinatorc"
argument_list|,
name|p
operator|.
name|replace
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplace3 ()
specifier|public
name|void
name|testReplace3
parameter_list|()
block|{
specifier|final
name|ParamertizedString
name|p
init|=
operator|new
name|ParamertizedString
argument_list|(
literal|"a${bar}"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a${bar}"
argument_list|,
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a{0}"
argument_list|,
name|p
operator|.
name|getRawPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|a
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|a
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"frobinator"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"frobinator"
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"afrobinator"
argument_list|,
name|p
operator|.
name|replace
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplace4 ()
specifier|public
name|void
name|testReplace4
parameter_list|()
block|{
specifier|final
name|ParamertizedString
name|p
init|=
operator|new
name|ParamertizedString
argument_list|(
literal|"a${bar}c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a${bar}c"
argument_list|,
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a{0}c"
argument_list|,
name|p
operator|.
name|getRawPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|getParameterNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|a
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|p
operator|.
name|bind
argument_list|(
name|a
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ac"
argument_list|,
name|p
operator|.
name|replace
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

