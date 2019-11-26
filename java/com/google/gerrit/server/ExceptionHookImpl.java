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
name|common
operator|.
name|base
operator|.
name|Predicate
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
name|base
operator|.
name|Throwables
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
name|git
operator|.
name|LockFailureException
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
name|RefUpdate
import|;
end_import

begin_comment
comment|/**  * Class to detect and handle exceptions that are caused by temporary errors, and hence should cause  * a retry of the failed operation.  */
end_comment

begin_class
DECL|class|ExceptionHookImpl
specifier|public
class|class
name|ExceptionHookImpl
implements|implements
name|ExceptionHook
block|{
annotation|@
name|Override
DECL|method|shouldRetry (Throwable throwable)
specifier|public
name|boolean
name|shouldRetry
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|isLockFailure
argument_list|(
name|throwable
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|formatCause (Throwable throwable)
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|formatCause
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|isLockFailure
argument_list|(
name|throwable
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|RefUpdate
operator|.
name|Result
operator|.
name|LOCK_FAILURE
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|isLockFailure (Throwable throwable)
specifier|private
specifier|static
name|boolean
name|isLockFailure
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|isMatching
argument_list|(
name|throwable
argument_list|,
name|t
lambda|->
name|t
operator|instanceof
name|LockFailureException
argument_list|)
return|;
block|}
comment|/**    * Check whether the given exception or any of its causes matches the given predicate.    *    * @param throwable Exception that should be tested    * @param predicate predicate to check if a throwable matches    * @return {@code true} if the given exception or any of its causes matches the given predicate    */
DECL|method|isMatching (Throwable throwable, Predicate<Throwable> predicate)
specifier|private
specifier|static
name|boolean
name|isMatching
parameter_list|(
name|Throwable
name|throwable
parameter_list|,
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Throwables
operator|.
name|getCausalChain
argument_list|(
name|throwable
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|predicate
argument_list|)
return|;
block|}
block|}
end_class

end_unit

