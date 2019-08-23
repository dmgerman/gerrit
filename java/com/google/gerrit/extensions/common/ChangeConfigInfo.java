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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_class
DECL|class|ChangeConfigInfo
specifier|public
class|class
name|ChangeConfigInfo
block|{
DECL|field|allowBlame
specifier|public
name|Boolean
name|allowBlame
decl_stmt|;
DECL|field|showAssigneeInChangesTable
specifier|public
name|Boolean
name|showAssigneeInChangesTable
decl_stmt|;
DECL|field|disablePrivateChanges
specifier|public
name|Boolean
name|disablePrivateChanges
decl_stmt|;
DECL|field|largeChange
specifier|public
name|int
name|largeChange
decl_stmt|;
DECL|field|replyLabel
specifier|public
name|String
name|replyLabel
decl_stmt|;
DECL|field|replyTooltip
specifier|public
name|String
name|replyTooltip
decl_stmt|;
DECL|field|updateDelay
specifier|public
name|int
name|updateDelay
decl_stmt|;
DECL|field|submitWholeTopic
specifier|public
name|Boolean
name|submitWholeTopic
decl_stmt|;
DECL|field|excludeMergeableInChangeInfo
specifier|public
name|Boolean
name|excludeMergeableInChangeInfo
decl_stmt|;
block|}
end_class

end_unit

