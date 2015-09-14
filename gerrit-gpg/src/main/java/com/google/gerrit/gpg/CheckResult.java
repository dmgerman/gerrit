begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
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
name|extensions
operator|.
name|common
operator|.
name|GpgKeyInfo
operator|.
name|Status
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
name|Arrays
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

begin_comment
comment|/** Result of checking an object like a key or signature. */
end_comment

begin_class
DECL|class|CheckResult
specifier|public
class|class
name|CheckResult
block|{
DECL|method|ok (String... problems)
specifier|static
name|CheckResult
name|ok
parameter_list|(
name|String
modifier|...
name|problems
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Status
operator|.
name|OK
argument_list|,
name|problems
argument_list|)
return|;
block|}
DECL|method|bad (String... problems)
specifier|static
name|CheckResult
name|bad
parameter_list|(
name|String
modifier|...
name|problems
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Status
operator|.
name|BAD
argument_list|,
name|problems
argument_list|)
return|;
block|}
DECL|method|trusted ()
specifier|static
name|CheckResult
name|trusted
parameter_list|()
block|{
return|return
operator|new
name|CheckResult
argument_list|(
name|Status
operator|.
name|TRUSTED
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|create (Status status, String... problems)
specifier|static
name|CheckResult
name|create
parameter_list|(
name|Status
name|status
parameter_list|,
name|String
modifier|...
name|problems
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|problemList
init|=
name|problems
operator|.
name|length
operator|>
literal|0
condition|?
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|problems
argument_list|)
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
decl_stmt|;
return|return
operator|new
name|CheckResult
argument_list|(
name|status
argument_list|,
name|problemList
argument_list|)
return|;
block|}
DECL|method|create (Status status, List<String> problems)
specifier|static
name|CheckResult
name|create
parameter_list|(
name|Status
name|status
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|problems
parameter_list|)
block|{
return|return
operator|new
name|CheckResult
argument_list|(
name|status
argument_list|,
name|Collections
operator|.
name|unmodifiableList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|problems
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (List<String> problems)
specifier|static
name|CheckResult
name|create
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|problems
parameter_list|)
block|{
return|return
operator|new
name|CheckResult
argument_list|(
name|problems
operator|.
name|isEmpty
argument_list|()
condition|?
name|Status
operator|.
name|OK
else|:
name|Status
operator|.
name|BAD
argument_list|,
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|problems
argument_list|)
argument_list|)
return|;
block|}
DECL|field|status
specifier|private
specifier|final
name|Status
name|status
decl_stmt|;
DECL|field|problems
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|problems
decl_stmt|;
DECL|method|CheckResult (Status status, List<String> problems)
specifier|private
name|CheckResult
parameter_list|(
name|Status
name|status
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|problems
parameter_list|)
block|{
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"status must not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|this
operator|.
name|problems
operator|=
name|problems
expr_stmt|;
block|}
comment|/** @return whether the result has status {@link Status#OK} or better. */
DECL|method|isOk ()
specifier|public
name|boolean
name|isOk
parameter_list|()
block|{
return|return
name|status
operator|.
name|compareTo
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/** @return whether the result has status {@link Status#TRUSTED} or better. */
DECL|method|isTrusted ()
specifier|public
name|boolean
name|isTrusted
parameter_list|()
block|{
return|return
name|status
operator|.
name|compareTo
argument_list|(
name|Status
operator|.
name|TRUSTED
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/** @return the status enum value associated with the object. */
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
comment|/** @return any problems encountered during checking. */
DECL|method|getProblems ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProblems
parameter_list|()
block|{
return|return
name|problems
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|status
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|problems
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|i
operator|==
literal|0
condition|?
literal|": "
else|:
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|problems
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

