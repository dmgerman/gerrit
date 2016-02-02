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
name|server
operator|.
name|PatchLineCommentsUtil
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
name|server
operator|.
name|OrmException
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
name|lib
operator|.
name|ObjectId
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
name|lib
operator|.
name|RefUpdate
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
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|ChangeDelete
specifier|public
class|class
name|ChangeDelete
block|{
DECL|field|plcUtil
specifier|private
specifier|final
name|PatchLineCommentsUtil
name|plcUtil
decl_stmt|;
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|notes
specifier|private
specifier|final
name|ChangeNotes
name|notes
decl_stmt|;
DECL|method|ChangeDelete (PatchLineCommentsUtil plcUtil, Repository repo, ChangeNotes notes)
specifier|public
name|ChangeDelete
parameter_list|(
name|PatchLineCommentsUtil
name|plcUtil
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
block|{
name|this
operator|.
name|plcUtil
operator|=
name|plcUtil
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|notes
operator|=
name|notes
expr_stmt|;
block|}
DECL|method|delete ()
specifier|public
name|void
name|delete
parameter_list|()
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|plcUtil
operator|.
name|deleteAllDraftsFromAllUsers
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|notes
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogMessage
argument_list|(
literal|"Delete change from notedb"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RefUpdate
operator|.
name|Result
name|result
init|=
name|ru
operator|.
name|delete
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|FAST_FORWARD
case|:
case|case
name|FORCED
case|:
case|case
name|NO_CHANGE
case|:
break|break;
case|case
name|IO_FAILURE
case|:
case|case
name|LOCK_FAILURE
case|:
case|case
name|NEW
case|:
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|REJECTED
case|:
case|case
name|REJECTED_CURRENT_BRANCH
case|:
case|case
name|RENAMED
case|:
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to delete change ref %s at %s: %s"
argument_list|,
name|notes
operator|.
name|getRefName
argument_list|()
argument_list|,
name|notes
operator|.
name|getRevision
argument_list|()
argument_list|,
name|result
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

