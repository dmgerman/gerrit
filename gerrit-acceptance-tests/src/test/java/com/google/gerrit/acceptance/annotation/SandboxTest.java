begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.annotation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|annotation
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|Sandboxed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
annotation|@
name|Sandboxed
DECL|class|SandboxTest
specifier|public
class|class
name|SandboxTest
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|After
DECL|method|addUser ()
specifier|public
name|void
name|addUser
parameter_list|()
throws|throws
name|Exception
block|{
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|create
argument_list|(
literal|"sandboxuser"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|userNotPresent1 ()
specifier|public
name|void
name|userNotPresent1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|query
argument_list|(
literal|"sandboxuser"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|userNotPresent2 ()
specifier|public
name|void
name|userNotPresent2
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|query
argument_list|(
literal|"sandboxuser"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

