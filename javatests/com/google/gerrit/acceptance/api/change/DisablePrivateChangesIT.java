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
DECL|package|com.google.gerrit.acceptance.api.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
name|config
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|DisablePrivateChangesIT
specifier|public
class|class
name|DisablePrivateChangesIT
extends|extends
name|AbstractDaemonTest
block|{
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
DECL|method|createPrivateChangeWithDisablePrivateChangesTrue ()
specifier|public
name|void
name|createPrivateChangeWithDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|(
name|project
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
literal|true
expr_stmt|;
name|MethodNotAllowedException
name|thrown
init|=
name|assertThrows
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"private changes are disabled"
argument_list|)
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
DECL|method|createNonPrivateChangeWithDisablePrivateChangesTrue ()
specifier|public
name|void
name|createNonPrivateChangeWithDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|(
name|project
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
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createPrivateChangeWithDisablePrivateChangesFalse ()
specifier|public
name|void
name|createPrivateChangeWithDisablePrivateChangesFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|(
name|project
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
literal|true
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
DECL|method|pushPrivatesWithDisablePrivateChangesTrue ()
specifier|public
name|void
name|pushPrivatesWithDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|admin
operator|.
name|newIdent
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
DECL|method|pushWithDisablePrivateChangesTrue ()
specifier|public
name|void
name|pushWithDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
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
DECL|method|setPrivateWithDisablePrivateChangesTrue ()
specifier|public
name|void
name|setPrivateWithDisablePrivateChangesTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|MethodNotAllowedException
name|thrown
init|=
name|assertThrows
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|"set private"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"private changes are disabled"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setPrivateWithDisablePrivateChangesFalse ()
specifier|public
name|void
name|setPrivateWithDisablePrivateChangesFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|"set private"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
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
block|}
end_class

end_unit

