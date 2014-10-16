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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|Util
operator|.
name|block
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|MetaDataUpdate
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
name|git
operator|.
name|ProjectConfig
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|allProjects
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|block
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|ANONYMOUS_USERS
argument_list|,
literal|"refs/drafts/*"
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|cfg
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushDraftChange_Blocked ()
specifier|public
name|void
name|testPushDraftChange_Blocked
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
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
DECL|method|testPushDraftChangeMagic_Blocked ()
specifier|public
name|void
name|testPushDraftChangeMagic_Blocked
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
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
DECL|method|saveProjectConfig (ProjectConfig cfg)
specifier|private
name|void
name|saveProjectConfig
parameter_list|(
name|ProjectConfig
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|allProjects
argument_list|)
decl_stmt|;
try|try
block|{
name|cfg
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

