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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|client
operator|.
name|ReviewerState
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|FooterKey
import|;
end_import

begin_comment
comment|/** State of a reviewer on a change. */
end_comment

begin_enum
DECL|enum|ReviewerStateInternal
specifier|public
enum|enum
name|ReviewerStateInternal
block|{
comment|/** The user has contributed at least one nonzero vote on the change. */
DECL|enumConstant|REVIEWER
name|REVIEWER
argument_list|(
literal|"Reviewer"
argument_list|,
name|ReviewerState
operator|.
name|REVIEWER
argument_list|)
block|,
comment|/** The reviewer was added to the change, but has not voted. */
DECL|enumConstant|CC
name|CC
argument_list|(
literal|"CC"
argument_list|,
name|ReviewerState
operator|.
name|CC
argument_list|)
block|,
comment|/** The user was previously a reviewer on the change, but was removed. */
DECL|enumConstant|REMOVED
name|REMOVED
argument_list|(
literal|"Removed"
argument_list|,
name|ReviewerState
operator|.
name|REMOVED
argument_list|)
block|;
DECL|method|fromReviewerState (ReviewerState state)
specifier|public
specifier|static
name|ReviewerStateInternal
name|fromReviewerState
parameter_list|(
name|ReviewerState
name|state
parameter_list|)
block|{
return|return
name|ReviewerStateInternal
operator|.
name|values
argument_list|()
index|[
name|state
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
static|static
block|{
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|ReviewerStateInternal
operator|.
name|values
argument_list|()
operator|.
name|length
operator|!=
name|ReviewerState
operator|.
name|values
argument_list|()
operator|.
name|length
condition|)
block|{
name|ok
operator|=
literal|false
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ReviewerStateInternal
operator|.
name|values
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ok
operator|&=
name|ReviewerState
operator|.
name|values
argument_list|()
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|ReviewerStateInternal
operator|.
name|values
argument_list|()
index|[
name|i
index|]
operator|.
name|state
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ok
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Mismatched reviewer state mapping: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|ReviewerStateInternal
operator|.
name|values
argument_list|()
argument_list|)
operator|+
literal|" != "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|ReviewerState
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|field|footer
specifier|private
specifier|final
name|String
name|footer
decl_stmt|;
DECL|field|state
specifier|private
specifier|final
name|ReviewerState
name|state
decl_stmt|;
DECL|method|ReviewerStateInternal (String footer, ReviewerState state)
name|ReviewerStateInternal
parameter_list|(
name|String
name|footer
parameter_list|,
name|ReviewerState
name|state
parameter_list|)
block|{
name|this
operator|.
name|footer
operator|=
name|footer
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getFooterKey ()
name|FooterKey
name|getFooterKey
parameter_list|()
block|{
return|return
operator|new
name|FooterKey
argument_list|(
name|footer
argument_list|)
return|;
block|}
DECL|method|getByEmailFooterKey ()
name|FooterKey
name|getByEmailFooterKey
parameter_list|()
block|{
return|return
operator|new
name|FooterKey
argument_list|(
name|footer
operator|+
literal|"-email"
argument_list|)
return|;
block|}
DECL|method|asReviewerState ()
specifier|public
name|ReviewerState
name|asReviewerState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
block|}
end_enum

end_unit

