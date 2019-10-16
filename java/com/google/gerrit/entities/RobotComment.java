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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|RobotComment
specifier|public
class|class
name|RobotComment
extends|extends
name|Comment
block|{
DECL|field|robotId
specifier|public
name|String
name|robotId
decl_stmt|;
DECL|field|robotRunId
specifier|public
name|String
name|robotRunId
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|properties
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
DECL|field|fixSuggestions
specifier|public
name|List
argument_list|<
name|FixSuggestion
argument_list|>
name|fixSuggestions
decl_stmt|;
DECL|method|RobotComment ( Key key, Account.Id author, Timestamp writtenOn, short side, String message, String serverId, String robotId, String robotRunId)
specifier|public
name|RobotComment
parameter_list|(
name|Key
name|key
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|,
name|Timestamp
name|writtenOn
parameter_list|,
name|short
name|side
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|serverId
parameter_list|,
name|String
name|robotId
parameter_list|,
name|String
name|robotRunId
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|author
argument_list|,
name|writtenOn
argument_list|,
name|side
argument_list|,
name|message
argument_list|,
name|serverId
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|robotId
operator|=
name|robotId
expr_stmt|;
name|this
operator|.
name|robotRunId
operator|=
name|robotRunId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toStringHelper
argument_list|()
operator|.
name|add
argument_list|(
literal|"robotId"
argument_list|,
name|robotId
argument_list|)
operator|.
name|add
argument_list|(
literal|"robotRunId"
argument_list|,
name|robotRunId
argument_list|)
operator|.
name|add
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
operator|.
name|add
argument_list|(
literal|"properties"
argument_list|,
name|Objects
operator|.
name|toString
argument_list|(
name|properties
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"fixSuggestions"
argument_list|,
name|Objects
operator|.
name|toString
argument_list|(
name|fixSuggestions
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

