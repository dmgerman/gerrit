begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|inject
operator|.
name|servlet
operator|.
name|RequestScoped
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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

begin_comment
comment|/**  * Registers cleanup activities to be completed when a scope ends.  */
end_comment

begin_class
annotation|@
name|RequestScoped
DECL|class|RequestCleanup
specifier|public
class|class
name|RequestCleanup
implements|implements
name|Runnable
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RequestCleanup
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cleanup
specifier|private
specifier|final
name|List
argument_list|<
name|Runnable
argument_list|>
name|cleanup
init|=
operator|new
name|LinkedList
argument_list|<
name|Runnable
argument_list|>
argument_list|()
decl_stmt|;
comment|/** Register a task to be completed after the request ends. */
DECL|method|add (final Runnable task)
specifier|public
name|void
name|add
parameter_list|(
specifier|final
name|Runnable
name|task
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cleanup
init|)
block|{
name|cleanup
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|cleanup
init|)
block|{
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|Runnable
argument_list|>
name|i
init|=
name|cleanup
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
try|try
block|{
name|i
operator|.
name|next
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to execute per-request cleanup"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

