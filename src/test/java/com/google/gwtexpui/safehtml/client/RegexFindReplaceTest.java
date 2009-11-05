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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
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

begin_class
DECL|class|RegexFindReplaceTest
specifier|public
class|class
name|RegexFindReplaceTest
extends|extends
name|TestCase
block|{
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
parameter_list|()
block|{
specifier|final
name|String
name|find
init|=
literal|"find"
decl_stmt|;
specifier|final
name|String
name|replace
init|=
literal|"replace"
decl_stmt|;
specifier|final
name|RegexFindReplace
name|a
init|=
operator|new
name|RegexFindReplace
argument_list|(
name|find
argument_list|,
name|replace
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|find
argument_list|,
name|a
operator|.
name|find
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|replace
argument_list|,
name|a
operator|.
name|replace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"find = "
operator|+
name|find
operator|+
literal|", replace = "
operator|+
name|replace
argument_list|,
name|a
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

