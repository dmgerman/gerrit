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
DECL|package|com.google.gerrit.server.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|accounts
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
name|collect
operator|.
name|ComparisonChain
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
name|Ordering
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
name|extensions
operator|.
name|common
operator|.
name|AccountInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_class
DECL|class|AccountInfoComparator
specifier|public
class|class
name|AccountInfoComparator
extends|extends
name|Ordering
argument_list|<
name|AccountInfo
argument_list|>
implements|implements
name|Comparator
argument_list|<
name|AccountInfo
argument_list|>
block|{
DECL|field|ORDER_NULLS_FIRST
specifier|public
specifier|static
specifier|final
name|AccountInfoComparator
name|ORDER_NULLS_FIRST
init|=
operator|new
name|AccountInfoComparator
argument_list|()
decl_stmt|;
DECL|field|ORDER_NULLS_LAST
specifier|public
specifier|static
specifier|final
name|AccountInfoComparator
name|ORDER_NULLS_LAST
init|=
operator|new
name|AccountInfoComparator
argument_list|()
operator|.
name|setNullsLast
argument_list|()
decl_stmt|;
DECL|field|nullsLast
specifier|private
name|boolean
name|nullsLast
decl_stmt|;
DECL|method|AccountInfoComparator ()
specifier|private
name|AccountInfoComparator
parameter_list|()
block|{}
DECL|method|setNullsLast ()
specifier|private
name|AccountInfoComparator
name|setNullsLast
parameter_list|()
block|{
name|this
operator|.
name|nullsLast
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|compare (AccountInfo a, AccountInfo b)
specifier|public
name|int
name|compare
parameter_list|(
name|AccountInfo
name|a
parameter_list|,
name|AccountInfo
name|b
parameter_list|)
block|{
return|return
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
name|a
operator|.
name|name
argument_list|,
name|b
operator|.
name|name
argument_list|,
name|createOrdering
argument_list|()
argument_list|)
operator|.
name|compare
argument_list|(
name|a
operator|.
name|email
argument_list|,
name|b
operator|.
name|email
argument_list|,
name|createOrdering
argument_list|()
argument_list|)
operator|.
name|compare
argument_list|(
name|a
operator|.
name|_accountId
argument_list|,
name|b
operator|.
name|_accountId
argument_list|,
name|createOrdering
argument_list|()
argument_list|)
operator|.
name|result
argument_list|()
return|;
block|}
DECL|method|createOrdering ()
specifier|private
parameter_list|<
name|S
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|createOrdering
parameter_list|()
block|{
if|if
condition|(
name|nullsLast
condition|)
block|{
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsLast
argument_list|()
return|;
block|}
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
return|;
block|}
block|}
end_class

end_unit

