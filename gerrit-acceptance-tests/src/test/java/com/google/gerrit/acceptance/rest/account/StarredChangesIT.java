begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.account
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
name|account
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
operator|.
name|Result
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
name|RestResponse
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
DECL|class|StarredChangesIT
specifier|public
class|class
name|StarredChangesIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|starredChangeState ()
specifier|public
name|void
name|starredChangeState
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|Result
name|c1
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Result
name|c2
init|=
name|createChange
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|true
argument_list|,
name|c1
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|true
argument_list|,
name|c2
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getChange
argument_list|(
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getChange
argument_list|(
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|false
argument_list|,
name|c1
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|false
argument_list|,
name|c2
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
block|}
DECL|method|starChange (boolean on, Change.Id id)
specifier|private
name|void
name|starChange
parameter_list|(
name|boolean
name|on
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|url
init|=
literal|"/accounts/self/starred.changes/"
operator|+
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|on
condition|)
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|put
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|delete
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

