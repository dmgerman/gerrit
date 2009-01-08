begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

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
name|Column
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
name|CompoundKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** An approval (or negative approval) on a change. */
end_comment

begin_class
DECL|class|ChangeApproval
specifier|public
specifier|final
class|class
name|ChangeApproval
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|Column
DECL|field|changeId
specifier|protected
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
annotation|@
name|Column
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
DECL|field|categoryId
specifier|protected
name|ApprovalCategory
operator|.
name|Id
name|categoryId
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|changeId
operator|=
operator|new
name|Change
operator|.
name|Id
argument_list|()
expr_stmt|;
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|()
expr_stmt|;
name|categoryId
operator|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Change.Id change, final Account.Id a, final ApprovalCategory.Id c)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|change
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|,
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|c
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|accountId
operator|=
name|a
expr_stmt|;
name|this
operator|.
name|categoryId
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Change
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
name|members
parameter_list|()
block|{
return|return
operator|new
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|accountId
operator|,
name|categoryId
block|}
empty_stmt|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
comment|/**    * Value assigned by the user.    *<p>    * The precise meaning of "value" is up to each category.    *<p>    * In general:    *<ul>    *<li><b>&lt; 0:</b> The approval is rejected/revoked.</li>    *<li><b>= 0:</b> No indication either way is provided.</li>    *<li><b>&gt; 0:</b> The approval is approved/positive.</li>    *</ul>    * and in the negative and positive direction a magnitude can be assumed.The    * further from 0 the more assertive the approval.    */
annotation|@
name|Column
DECL|field|value
specifier|protected
name|short
name|value
decl_stmt|;
annotation|@
name|Column
DECL|field|granted
specifier|protected
name|Timestamp
name|granted
decl_stmt|;
DECL|method|ChangeApproval ()
specifier|protected
name|ChangeApproval
parameter_list|()
block|{   }
DECL|method|ChangeApproval (final ChangeApproval.Key k, final short v)
specifier|public
name|ChangeApproval
parameter_list|(
specifier|final
name|ChangeApproval
operator|.
name|Key
name|k
parameter_list|,
specifier|final
name|short
name|v
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
name|setValue
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|setGranted
argument_list|()
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|ChangeApproval
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|key
operator|.
name|changeId
return|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|key
operator|.
name|accountId
return|;
block|}
DECL|method|getCategoryId ()
specifier|public
name|ApprovalCategory
operator|.
name|Id
name|getCategoryId
parameter_list|()
block|{
return|return
name|key
operator|.
name|categoryId
return|;
block|}
DECL|method|getValue ()
specifier|public
name|short
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (final short v)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|short
name|v
parameter_list|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|value
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|getGranted ()
specifier|public
name|Timestamp
name|getGranted
parameter_list|()
block|{
return|return
name|granted
return|;
block|}
DECL|method|setGranted ()
specifier|public
name|void
name|setGranted
parameter_list|()
block|{
name|granted
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

