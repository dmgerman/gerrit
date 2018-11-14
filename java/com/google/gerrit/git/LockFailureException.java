begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|BatchRefUpdate
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
name|lib
operator|.
name|RefUpdate
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
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_comment
comment|/** Thrown when updating a ref in Git fails with LOCK_FAILURE. */
end_comment

begin_class
DECL|class|LockFailureException
specifier|public
class|class
name|LockFailureException
extends|extends
name|IOException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|refs
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|refs
decl_stmt|;
DECL|method|LockFailureException (String message, RefUpdate refUpdate)
specifier|public
name|LockFailureException
parameter_list|(
name|String
name|message
parameter_list|,
name|RefUpdate
name|refUpdate
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|refs
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|refUpdate
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|LockFailureException (String message, BatchRefUpdate batchRefUpdate)
specifier|public
name|LockFailureException
parameter_list|(
name|String
name|message
parameter_list|,
name|BatchRefUpdate
name|batchRefUpdate
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|refs
operator|=
name|batchRefUpdate
operator|.
name|getCommands
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getResult
argument_list|()
operator|==
name|ReceiveCommand
operator|.
name|Result
operator|.
name|LOCK_FAILURE
argument_list|)
operator|.
name|map
argument_list|(
name|ReceiveCommand
operator|::
name|getRefName
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Subset of ref names that caused the lock failure. */
DECL|method|getFailedRefs ()
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|getFailedRefs
parameter_list|()
block|{
return|return
name|refs
return|;
block|}
block|}
end_class

end_unit

