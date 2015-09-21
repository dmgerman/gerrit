begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|assertEquals
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
DECL|class|HighlightSuggestionTest
specifier|public
class|class
name|HighlightSuggestionTest
block|{
annotation|@
name|Test
DECL|method|singleHighlight ()
specifier|public
name|void
name|singleHighlight
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|keyword
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"somethingkeysomething"
decl_stmt|;
name|HighlightSuggestion
name|suggestion
init|=
operator|new
name|HighlightSuggestion
argument_list|(
name|keyword
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"something<strong>key</strong>something"
argument_list|,
name|suggestion
operator|.
name|getDisplayString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|suggestion
operator|.
name|getReplacementString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noHighlight ()
specifier|public
name|void
name|noHighlight
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|keyword
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"something"
decl_stmt|;
name|HighlightSuggestion
name|suggestion
init|=
operator|new
name|HighlightSuggestion
argument_list|(
name|keyword
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|suggestion
operator|.
name|getDisplayString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|suggestion
operator|.
name|getReplacementString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doubleHighlight ()
specifier|public
name|void
name|doubleHighlight
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|keyword
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"somethingkeysomethingkeysomething"
decl_stmt|;
name|HighlightSuggestion
name|suggestion
init|=
operator|new
name|HighlightSuggestion
argument_list|(
name|keyword
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"something<strong>key</strong>something<strong>key</strong>something"
argument_list|,
name|suggestion
operator|.
name|getDisplayString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|suggestion
operator|.
name|getReplacementString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

