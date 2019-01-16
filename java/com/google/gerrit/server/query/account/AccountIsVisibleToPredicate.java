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
DECL|package|com.google.gerrit.server.query.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|account
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
name|flogger
operator|.
name|FluentLogger
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
name|exceptions
operator|.
name|StorageException
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
name|index
operator|.
name|query
operator|.
name|IsVisibleToPredicate
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
name|server
operator|.
name|account
operator|.
name|AccountControl
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
name|server
operator|.
name|account
operator|.
name|AccountState
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
name|server
operator|.
name|index
operator|.
name|IndexUtils
import|;
end_import

begin_class
DECL|class|AccountIsVisibleToPredicate
specifier|public
class|class
name|AccountIsVisibleToPredicate
extends|extends
name|IsVisibleToPredicate
argument_list|<
name|AccountState
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|accountControl
specifier|protected
specifier|final
name|AccountControl
name|accountControl
decl_stmt|;
DECL|method|AccountIsVisibleToPredicate (AccountControl accountControl)
specifier|public
name|AccountIsVisibleToPredicate
parameter_list|(
name|AccountControl
name|accountControl
parameter_list|)
block|{
name|super
argument_list|(
name|AccountQueryBuilder
operator|.
name|FIELD_VISIBLETO
argument_list|,
name|IndexUtils
operator|.
name|describe
argument_list|(
name|accountControl
operator|.
name|getUser
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountControl
operator|=
name|accountControl
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (AccountState accountState)
specifier|public
name|boolean
name|match
parameter_list|(
name|AccountState
name|accountState
parameter_list|)
throws|throws
name|StorageException
block|{
name|boolean
name|canSee
init|=
name|accountControl
operator|.
name|canSee
argument_list|(
name|accountState
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|canSee
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Filter out non-visisble account: %s"
argument_list|,
name|accountState
argument_list|)
expr_stmt|;
block|}
return|return
name|canSee
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

