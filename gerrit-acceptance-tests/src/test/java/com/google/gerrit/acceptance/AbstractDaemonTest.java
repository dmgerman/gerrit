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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

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
name|Before
import|;
end_import

begin_class
DECL|class|AbstractDaemonTest
specifier|public
specifier|abstract
class|class
name|AbstractDaemonTest
block|{
DECL|field|server
specifier|protected
name|GerritServer
name|server
decl_stmt|;
annotation|@
name|Before
DECL|method|beforeTest ()
specifier|public
specifier|final
name|void
name|beforeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
name|GerritServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|server
operator|.
name|getTestInjector
argument_list|()
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|afterTest ()
specifier|public
specifier|final
name|void
name|afterTest
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

