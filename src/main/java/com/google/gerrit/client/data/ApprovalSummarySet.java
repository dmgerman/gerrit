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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|client
operator|.
name|reviewdb
operator|.
name|Change
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Contains a set of ApprovalSummary objects, keyed by the change id  * from which they were derived.  */
end_comment

begin_class
DECL|class|ApprovalSummarySet
specifier|public
class|class
name|ApprovalSummarySet
block|{
DECL|field|accounts
specifier|protected
name|AccountInfoCache
name|accounts
decl_stmt|;
DECL|field|summaries
specifier|protected
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ApprovalSummary
argument_list|>
name|summaries
decl_stmt|;
DECL|method|ApprovalSummarySet ()
specifier|protected
name|ApprovalSummarySet
parameter_list|()
block|{   }
DECL|method|ApprovalSummarySet (final AccountInfoCache accts, final Map<Change.Id, ApprovalSummary> map)
specifier|public
name|ApprovalSummarySet
parameter_list|(
specifier|final
name|AccountInfoCache
name|accts
parameter_list|,
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ApprovalSummary
argument_list|>
name|map
parameter_list|)
block|{
name|accounts
operator|=
name|accts
expr_stmt|;
name|summaries
operator|=
operator|new
name|HashMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ApprovalSummary
argument_list|>
argument_list|()
expr_stmt|;
name|summaries
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|getAccountInfoCache ()
specifier|public
name|AccountInfoCache
name|getAccountInfoCache
parameter_list|()
block|{
return|return
name|accounts
return|;
block|}
DECL|method|getSummaryMap ()
specifier|public
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ApprovalSummary
argument_list|>
name|getSummaryMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|summaries
argument_list|)
return|;
block|}
block|}
end_class

end_unit

