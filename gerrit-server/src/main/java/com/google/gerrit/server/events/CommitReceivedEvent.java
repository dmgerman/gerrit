begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|IdentifiedUser
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
name|RevCommit
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
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_class
DECL|class|CommitReceivedEvent
specifier|public
class|class
name|CommitReceivedEvent
extends|extends
name|ChangeEvent
block|{
DECL|field|command
specifier|public
specifier|final
name|ReceiveCommand
name|command
decl_stmt|;
DECL|field|project
specifier|public
specifier|final
name|Project
name|project
decl_stmt|;
DECL|field|refName
specifier|public
specifier|final
name|String
name|refName
decl_stmt|;
DECL|field|commit
specifier|public
specifier|final
name|RevCommit
name|commit
decl_stmt|;
DECL|field|user
specifier|public
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|method|CommitReceivedEvent (ReceiveCommand command, Project project, String refName, RevCommit commit, IdentifiedUser user)
specifier|public
name|CommitReceivedEvent
parameter_list|(
name|ReceiveCommand
name|command
parameter_list|,
name|Project
name|project
parameter_list|,
name|String
name|refName
parameter_list|,
name|RevCommit
name|commit
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
name|this
operator|.
name|commit
operator|=
name|commit
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
block|}
end_class

end_unit

