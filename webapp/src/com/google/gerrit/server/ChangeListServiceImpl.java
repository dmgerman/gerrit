begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|ChangeHeader
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
name|client
operator|.
name|data
operator|.
name|ChangeListService
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
name|client
operator|.
name|data
operator|.
name|MineResult
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
name|client
operator|.
name|data
operator|.
name|ProjectIdentity
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
name|client
operator|.
name|data
operator|.
name|UserIdentity
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
DECL|class|ChangeListServiceImpl
specifier|public
class|class
name|ChangeListServiceImpl
extends|extends
name|GerritJsonServlet
implements|implements
name|ChangeListService
block|{
annotation|@
name|Override
DECL|method|createServiceHandle ()
specifier|protected
name|Object
name|createServiceHandle
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|this
return|;
block|}
DECL|method|mine (final AsyncCallback<MineResult> callback)
specifier|public
name|void
name|mine
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|MineResult
argument_list|>
name|callback
parameter_list|)
block|{
specifier|final
name|MineResult
name|r
init|=
operator|new
name|MineResult
argument_list|()
decl_stmt|;
name|r
operator|.
name|byMe
operator|=
operator|new
name|ArrayList
argument_list|<
name|ChangeHeader
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|10
init|;
name|i
operator|<
literal|10
operator|+
literal|2
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|ChangeHeader
name|c
init|=
operator|new
name|ChangeHeader
argument_list|()
decl_stmt|;
name|c
operator|.
name|id
operator|=
name|i
expr_stmt|;
name|c
operator|.
name|subject
operator|=
literal|"Change "
operator|+
name|i
expr_stmt|;
name|c
operator|.
name|owner
operator|=
operator|new
name|UserIdentity
argument_list|()
expr_stmt|;
name|c
operator|.
name|owner
operator|.
name|fullName
operator|=
literal|"User "
operator|+
name|i
expr_stmt|;
name|c
operator|.
name|project
operator|=
operator|new
name|ProjectIdentity
argument_list|()
expr_stmt|;
name|c
operator|.
name|project
operator|.
name|name
operator|=
literal|"platform/test"
expr_stmt|;
name|c
operator|.
name|lastUpdate
operator|=
operator|new
name|Date
argument_list|()
expr_stmt|;
name|r
operator|.
name|byMe
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|onSuccess
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

