begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
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
name|group
operator|.
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
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
name|NoHttpd
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
name|PushOneCommit
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
name|common
operator|.
name|data
operator|.
name|Permission
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|NoHttpd
DECL|class|DraftChangeBlockedIT
specifier|public
class|class
name|DraftChangeBlockedIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|block
argument_list|(
literal|"refs/drafts/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pushDraftChange_Blocked ()
specifier|public
name|void
name|pushDraftChange_Blocked
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create draft by pushing to 'refs/drafts/'
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/drafts/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"cannot upload drafts"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pushDraftChangeMagic_Blocked ()
specifier|public
name|void
name|pushDraftChangeMagic_Blocked
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create draft by using 'draft' option
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master%draft"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"cannot upload drafts"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

