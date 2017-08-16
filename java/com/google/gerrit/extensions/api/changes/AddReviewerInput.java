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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|changes
package|;
end_package

begin_import
import|import static
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
operator|.
name|REVIEWER
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
name|client
operator|.
name|ReviewerState
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
name|restapi
operator|.
name|DefaultInput
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

begin_class
DECL|class|AddReviewerInput
specifier|public
class|class
name|AddReviewerInput
block|{
DECL|field|reviewer
annotation|@
name|DefaultInput
specifier|public
name|String
name|reviewer
decl_stmt|;
DECL|field|confirmed
specifier|public
name|Boolean
name|confirmed
decl_stmt|;
DECL|field|state
specifier|public
name|ReviewerState
name|state
decl_stmt|;
DECL|field|notify
specifier|public
name|NotifyHandling
name|notify
decl_stmt|;
DECL|field|notifyDetails
specifier|public
name|Map
argument_list|<
name|RecipientType
argument_list|,
name|NotifyInfo
argument_list|>
name|notifyDetails
decl_stmt|;
DECL|method|confirmed ()
specifier|public
name|boolean
name|confirmed
parameter_list|()
block|{
return|return
operator|(
name|confirmed
operator|!=
literal|null
operator|)
condition|?
name|confirmed
else|:
literal|false
return|;
block|}
DECL|method|state ()
specifier|public
name|ReviewerState
name|state
parameter_list|()
block|{
return|return
operator|(
name|state
operator|!=
literal|null
operator|)
condition|?
name|state
else|:
name|REVIEWER
return|;
block|}
block|}
end_class

end_unit

