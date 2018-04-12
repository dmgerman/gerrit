begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.git.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|testing
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|git
operator|.
name|testing
operator|.
name|PushResultSubject
operator|.
name|parseProcessed
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
name|git
operator|.
name|testing
operator|.
name|PushResultSubject
operator|.
name|trimMessages
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
DECL|class|PushResultSubjectTest
specifier|public
class|class
name|PushResultSubjectTest
block|{
annotation|@
name|Test
DECL|method|testTrimMessages ()
specifier|public
name|void
name|testTrimMessages
parameter_list|()
block|{
name|assertThat
argument_list|(
name|trimMessages
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|trimMessages
argument_list|(
literal|""
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|trimMessages
argument_list|(
literal|" \n "
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|trimMessages
argument_list|(
literal|"\n Foo\nBar\n\nProcessing changes: 1, 2, 3 done   \n"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Foo\nBar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseProcessed ()
specifier|public
name|void
name|testParseProcessed
parameter_list|()
block|{
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|"some other output"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|"Processing changes: done\n"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|"Processing changes: refs: 1, done \n"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|"Processing changes: new: 1, updated: 2, refs: 3, done \n"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"new"
argument_list|,
literal|1
argument_list|,
literal|"updated"
argument_list|,
literal|2
argument_list|,
literal|"refs"
argument_list|,
literal|3
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|"Some\nlonger\nmessage\nProcessing changes: new: 1\r"
operator|+
literal|"Processing changes: new: 1, updated: 1\r"
operator|+
literal|"Processing changes: new: 1, updated: 2, done"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"new"
argument_list|,
literal|1
argument_list|,
literal|"updated"
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// Atypical, but could potentially happen if there is an uncaught exception.
name|assertThat
argument_list|(
name|parseProcessed
argument_list|(
literal|"Processing changes: refs: 1"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

