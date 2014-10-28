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
DECL|package|com.google.gerrit.acceptance.api.accounts
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
name|accounts
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
name|AccountInfo
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
DECL|class|AccountIT
specifier|public
class|class
name|AccountIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|get ()
specifier|public
name|void
name|get
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountInfo
name|info
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
literal|"admin"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Administrator"
argument_list|,
name|info
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"admin@example.com"
argument_list|,
name|info
operator|.
name|email
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"admin"
argument_list|,
name|info
operator|.
name|username
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|self ()
specifier|public
name|void
name|self
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountInfo
name|info
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Administrator"
argument_list|,
name|info
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"admin@example.com"
argument_list|,
name|info
operator|.
name|email
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"admin"
argument_list|,
name|info
operator|.
name|username
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|starUnstarChange ()
specifier|public
name|void
name|starUnstarChange
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|triplet
init|=
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|starChange
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getChange
argument_list|(
name|triplet
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|unstarChange
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|triplet
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

