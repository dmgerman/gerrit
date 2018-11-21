begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|change
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
name|GerritConfig
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|ProjectOperations
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ConfigInput
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
name|extensions
operator|.
name|client
operator|.
name|InheritableBoolean
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
name|extensions
operator|.
name|common
operator|.
name|ChangeInfo
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
name|extensions
operator|.
name|common
operator|.
name|ChangeInput
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
name|extensions
operator|.
name|restapi
operator|.
name|MethodNotAllowedException
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
name|inject
operator|.
name|Inject
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|InMemoryRepository
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
name|junit
operator|.
name|TestRepository
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
name|revwalk
operator|.
name|RevCommit
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
DECL|class|PrivateByDefaultIT
specifier|public
class|class
name|PrivateByDefaultIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|project1
specifier|private
name|Project
operator|.
name|NameKey
name|project1
decl_stmt|;
DECL|field|project2
specifier|private
name|Project
operator|.
name|NameKey
name|project2
decl_stmt|;
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
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
name|project1
operator|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|project2
operator|=
name|createProject
argument_list|(
literal|"project-2"
argument_list|,
name|project1
argument_list|)
expr_stmt|;
name|setPrivateByDefault
argument_list|(
name|project1
argument_list|,
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangeWithPrivateByDefaultEnabled ()
specifier|public
name|void
name|createChangeWithPrivateByDefaultEnabled
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|(
name|project2
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"empty change"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangeBypassPrivateByDefaultEnabled ()
specifier|public
name|void
name|createChangeBypassPrivateByDefaultEnabled
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|(
name|project2
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"empty change"
argument_list|)
decl_stmt|;
name|input
operator|.
name|isPrivate
operator|=
literal|false
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangeWithPrivateByDefaultDisabled ()
specifier|public
name|void
name|createChangeWithPrivateByDefaultDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
operator|new
name|ChangeInput
argument_list|(
name|project2
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"empty change"
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangeWithPrivateByDefaultInherited ()
specifier|public
name|void
name|createChangeWithPrivateByDefaultInherited
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project1
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
operator|new
name|ChangeInput
argument_list|(
name|project2
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"empty change"
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"change.disablePrivateChanges"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|createChangeWithPrivateByDefaultAndDisablePrivateChangesTrue ()
specifier|public
name|void
name|createChangeWithPrivateByDefaultAndDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|(
name|project2
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"empty change"
argument_list|)
decl_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"private changes are disabled"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pushWithPrivateByDefaultEnabled ()
specifier|public
name|void
name|pushWithPrivateByDefaultEnabled
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|createChange
argument_list|(
name|project2
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|isPrivate
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pushBypassPrivateByDefaultEnabled ()
specifier|public
name|void
name|pushBypassPrivateByDefaultEnabled
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|createChange
argument_list|(
name|project2
argument_list|,
literal|"refs/for/master%remove-private"
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|isPrivate
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pushWithPrivateByDefaultDisabled ()
specifier|public
name|void
name|pushWithPrivateByDefaultDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|createChange
argument_list|(
name|project2
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|isPrivate
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pushBypassPrivateByDefaultInherited ()
specifier|public
name|void
name|pushBypassPrivateByDefaultInherited
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project1
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|createChange
argument_list|(
name|project2
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|isPrivate
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"change.disablePrivateChanges"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|pushPrivatesWithPrivateByDefaultAndDisablePrivateChangesTrue ()
specifier|public
name|void
name|pushPrivatesWithPrivateByDefaultAndDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|testRepo
init|=
name|cloneProject
argument_list|(
name|project2
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master%private"
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertErrorStatus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"change.disablePrivateChanges"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|pushDraftsWithPrivateByDefaultAndDisablePrivateChangesTrue ()
specifier|public
name|void
name|pushDraftsWithPrivateByDefaultAndDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|setPrivateByDefault
argument_list|(
name|project2
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|testRepo
init|=
name|cloneProject
argument_list|(
name|project2
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master%draft"
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertErrorStatus
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|result
operator|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/drafts/master"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertErrorStatus
argument_list|()
expr_stmt|;
block|}
DECL|method|setPrivateByDefault (Project.NameKey proj, InheritableBoolean value)
specifier|private
name|void
name|setPrivateByDefault
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
name|InheritableBoolean
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|ConfigInput
name|input
init|=
operator|new
name|ConfigInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|privateByDefault
operator|=
name|value
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|proj
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|config
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
DECL|method|createChange (Project.NameKey proj)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createChange
argument_list|(
name|proj
argument_list|,
literal|"refs/for/master"
argument_list|)
return|;
block|}
DECL|method|createChange (Project.NameKey proj, String ref)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|testRepo
init|=
name|cloneProject
argument_list|(
name|proj
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|push
operator|.
name|to
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

