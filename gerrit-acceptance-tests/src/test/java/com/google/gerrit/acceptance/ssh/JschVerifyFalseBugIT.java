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
DECL|package|com.google.gerrit.acceptance.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|ssh
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|cloneProject
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_class
annotation|@
name|NoHttpd
comment|// To see this test failing with 'verify: false', at least in the Jcsh 0.1.51
comment|// remove bouncycastle libs from the classpath, and run:
comment|// buck test //gerrit-acceptance-tests/src/test/java/com/google/gerrit/acceptance/ssh:JschVerifyFalseBugIT
DECL|class|JschVerifyFalseBugIT
specifier|public
class|class
name|JschVerifyFalseBugIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
annotation|@
name|Ignore
comment|// we know it works now, so let's not clone a project 500 times ;-)
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|test (int threads)
specifier|private
name|void
name|test
parameter_list|(
name|int
name|threads
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|Callable
argument_list|<
name|Void
argument_list|>
name|task
init|=
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|String
name|p
init|=
literal|"p"
operator|+
name|i
decl_stmt|;
name|createProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|cloneProject
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|p
argument_list|)
argument_list|,
name|sshSession
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|List
argument_list|<
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|>
name|nCopies
init|=
name|Collections
operator|.
name|nCopies
argument_list|(
name|threads
argument_list|,
name|task
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|Void
argument_list|>
argument_list|>
name|futures
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|threads
argument_list|)
operator|.
name|invokeAll
argument_list|(
name|nCopies
argument_list|)
decl_stmt|;
for|for
control|(
name|Future
argument_list|<
name|Void
argument_list|>
name|future
range|:
name|futures
control|)
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|futures
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|threads
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

