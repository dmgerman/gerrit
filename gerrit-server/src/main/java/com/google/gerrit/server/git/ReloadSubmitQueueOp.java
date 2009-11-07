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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|reviewdb
operator|.
name|Branch
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
name|Change
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
name|ReviewDb
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
name|client
operator|.
name|OrmException
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
name|client
operator|.
name|SchemaFactory
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
name|HashSet
import|;
end_import

begin_class
DECL|class|ReloadSubmitQueueOp
specifier|public
class|class
name|ReloadSubmitQueueOp
extends|extends
name|DefaultQueueOp
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|ReloadSubmitQueueOp
name|create
parameter_list|()
function_decl|;
block|}
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
name|ReloadSubmitQueueOp
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|mergeQueue
specifier|private
specifier|final
name|MergeQueue
name|mergeQueue
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReloadSubmitQueueOp (final WorkQueue wq, final SchemaFactory<ReviewDb> sf, final MergeQueue mq)
name|ReloadSubmitQueueOp
parameter_list|(
specifier|final
name|WorkQueue
name|wq
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
specifier|final
name|MergeQueue
name|mq
parameter_list|)
block|{
name|super
argument_list|(
name|wq
argument_list|)
expr_stmt|;
name|schema
operator|=
name|sf
expr_stmt|;
name|mergeQueue
operator|=
name|mq
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
specifier|final
name|HashSet
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|pending
init|=
operator|new
name|HashSet
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
specifier|final
name|Change
name|change
range|:
name|c
operator|.
name|changes
argument_list|()
operator|.
name|allSubmitted
argument_list|()
control|)
block|{
name|pending
operator|.
name|add
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot reload MergeQueue"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Branch
operator|.
name|NameKey
name|branch
range|:
name|pending
control|)
block|{
name|mergeQueue
operator|.
name|schedule
argument_list|(
name|branch
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Reload Submit Queue"
return|;
block|}
block|}
end_class

end_unit

