begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.receive
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|receive
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
name|annotations
operator|.
name|VisibleForTesting
import|;
end_import

begin_class
DECL|class|ReceiveConstants
specifier|public
specifier|final
class|class
name|ReceiveConstants
block|{
annotation|@
name|VisibleForTesting
DECL|field|ONLY_OWNER_CAN_MODIFY_WIP
specifier|public
specifier|static
specifier|final
name|String
name|ONLY_OWNER_CAN_MODIFY_WIP
init|=
literal|"only change owner can modify Work-in-Progress"
decl_stmt|;
DECL|field|COMMAND_REJECTION_MESSAGE_FOOTER
specifier|static
specifier|final
name|String
name|COMMAND_REJECTION_MESSAGE_FOOTER
init|=
literal|"Please read the documentation and contact an administrator\n"
operator|+
literal|"if you feel the configuration is incorrect"
decl_stmt|;
DECL|field|SAME_CHANGE_ID_IN_MULTIPLE_CHANGES
specifier|static
specifier|final
name|String
name|SAME_CHANGE_ID_IN_MULTIPLE_CHANGES
init|=
literal|"same Change-Id in multiple changes.\n"
operator|+
literal|"Squash the commits with the same Change-Id or "
operator|+
literal|"ensure Change-Ids are unique for each commit"
decl_stmt|;
DECL|method|ReceiveConstants ()
specifier|private
name|ReceiveConstants
parameter_list|()
block|{}
block|}
end_class

end_unit

