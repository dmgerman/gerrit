begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|UsedAt
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
name|java
operator|.
name|util
operator|.
name|Optional
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
comment|/** Thrown when updating a ref in Git fails. */
end_comment

begin_class
DECL|class|GitUpdateFailureException
specifier|public
class|class
name|GitUpdateFailureException
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
DECL|field|failures
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|GitUpdateFailure
argument_list|>
name|failures
decl_stmt|;
DECL|method|GitUpdateFailureException (String message, RefUpdate refUpdate)
specifier|public
name|GitUpdateFailureException
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
name|this
operator|.
name|failures
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|GitUpdateFailure
operator|.
name|create
argument_list|(
name|refUpdate
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|GitUpdateFailureException (String message, BatchRefUpdate batchRefUpdate)
specifier|public
name|GitUpdateFailureException
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
name|this
operator|.
name|failures
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
operator|!=
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
argument_list|)
operator|.
name|map
argument_list|(
name|GitUpdateFailure
operator|::
name|create
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** @return the names of the refs for which the update failed. */
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
name|failures
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|GitUpdateFailure
operator|::
name|ref
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
comment|/** @return the failures that caused this exception. */
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|getFailures ()
specifier|public
name|ImmutableList
argument_list|<
name|GitUpdateFailure
argument_list|>
name|getFailures
parameter_list|()
block|{
return|return
name|failures
return|;
block|}
annotation|@
name|AutoValue
DECL|class|GitUpdateFailure
specifier|public
specifier|abstract
specifier|static
class|class
name|GitUpdateFailure
block|{
DECL|method|create (RefUpdate refUpdate)
specifier|private
specifier|static
name|GitUpdateFailure
name|create
parameter_list|(
name|RefUpdate
name|refUpdate
parameter_list|)
block|{
return|return
name|builder
argument_list|()
operator|.
name|ref
argument_list|(
name|refUpdate
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|result
argument_list|(
name|refUpdate
operator|.
name|getResult
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|create (ReceiveCommand receiveCommand)
specifier|private
specifier|static
name|GitUpdateFailure
name|create
parameter_list|(
name|ReceiveCommand
name|receiveCommand
parameter_list|)
block|{
return|return
name|builder
argument_list|()
operator|.
name|ref
argument_list|(
name|receiveCommand
operator|.
name|getRefName
argument_list|()
argument_list|)
operator|.
name|result
argument_list|(
name|receiveCommand
operator|.
name|getResult
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|message
argument_list|(
name|receiveCommand
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|ref ()
specifier|public
specifier|abstract
name|String
name|ref
parameter_list|()
function_decl|;
DECL|method|result ()
specifier|public
specifier|abstract
name|String
name|result
parameter_list|()
function_decl|;
DECL|method|message ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|message
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|GitUpdateFailure
operator|.
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_GitUpdateFailureException_GitUpdateFailure
operator|.
name|Builder
argument_list|()
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|ref (String ref)
specifier|abstract
name|Builder
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
function_decl|;
DECL|method|result (String result)
specifier|abstract
name|Builder
name|result
parameter_list|(
name|String
name|result
parameter_list|)
function_decl|;
DECL|method|message (@ullable String message)
specifier|abstract
name|Builder
name|message
parameter_list|(
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|abstract
name|GitUpdateFailure
name|build
parameter_list|()
function_decl|;
block|}
block|}
block|}
end_class

end_unit

