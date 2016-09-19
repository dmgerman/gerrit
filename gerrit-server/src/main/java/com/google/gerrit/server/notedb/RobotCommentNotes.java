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
name|common
operator|.
name|collect
operator|.
name|ArrayListMultimap
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
name|ImmutableListMultimap
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
name|Multimap
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|reviewdb
operator|.
name|client
operator|.
name|RevId
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
name|reviewdb
operator|.
name|client
operator|.
name|RobotComment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|AssistedInject
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
name|errors
operator|.
name|ConfigInvalidException
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
name|ObjectReader
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
name|notes
operator|.
name|NoteMap
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|RobotCommentNotes
specifier|public
class|class
name|RobotCommentNotes
extends|extends
name|AbstractChangeNotes
argument_list|<
name|RobotCommentNotes
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Change change)
name|RobotCommentNotes
name|create
parameter_list|(
name|Change
name|change
parameter_list|)
function_decl|;
block|}
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|comments
specifier|private
name|ImmutableListMultimap
argument_list|<
name|RevId
argument_list|,
name|RobotComment
argument_list|>
name|comments
decl_stmt|;
DECL|field|revisionNoteMap
specifier|private
name|RevisionNoteMap
argument_list|<
name|RobotCommentsRevisionNote
argument_list|>
name|revisionNoteMap
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|RobotCommentNotes ( Args args, @Assisted Change change)
name|RobotCommentNotes
parameter_list|(
name|Args
name|args
parameter_list|,
annotation|@
name|Assisted
name|Change
name|change
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
block|}
DECL|method|getRevisionNoteMap ()
name|RevisionNoteMap
argument_list|<
name|RobotCommentsRevisionNote
argument_list|>
name|getRevisionNoteMap
parameter_list|()
block|{
return|return
name|revisionNoteMap
return|;
block|}
DECL|method|getComments ()
specifier|public
name|ImmutableListMultimap
argument_list|<
name|RevId
argument_list|,
name|RobotComment
argument_list|>
name|getComments
parameter_list|()
block|{
return|return
name|comments
return|;
block|}
DECL|method|containsComment (RobotComment c)
specifier|public
name|boolean
name|containsComment
parameter_list|(
name|RobotComment
name|c
parameter_list|)
block|{
for|for
control|(
name|RobotComment
name|existing
range|:
name|comments
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|key
operator|.
name|equals
argument_list|(
name|existing
operator|.
name|key
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|protected
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|RefNames
operator|.
name|robotCommentsRef
argument_list|(
name|getChangeId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad (LoadHandle handle)
specifier|protected
name|void
name|onLoad
parameter_list|(
name|LoadHandle
name|handle
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|ObjectId
name|rev
init|=
name|handle
operator|.
name|id
argument_list|()
decl_stmt|;
if|if
condition|(
name|rev
operator|==
literal|null
condition|)
block|{
name|loadDefaults
argument_list|()
expr_stmt|;
return|return;
block|}
name|RevCommit
name|tipCommit
init|=
name|handle
operator|.
name|walk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|rev
argument_list|)
decl_stmt|;
name|ObjectReader
name|reader
init|=
name|handle
operator|.
name|walk
argument_list|()
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|revisionNoteMap
operator|=
name|RevisionNoteMap
operator|.
name|parseRobotComments
argument_list|(
name|args
operator|.
name|noteUtil
argument_list|,
name|reader
argument_list|,
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|tipCommit
argument_list|)
argument_list|)
expr_stmt|;
name|Multimap
argument_list|<
name|RevId
argument_list|,
name|RobotComment
argument_list|>
name|cs
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|RobotCommentsRevisionNote
name|rn
range|:
name|revisionNoteMap
operator|.
name|revisionNotes
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|RobotComment
name|c
range|:
name|rn
operator|.
name|getComments
argument_list|()
control|)
block|{
name|cs
operator|.
name|put
argument_list|(
operator|new
name|RevId
argument_list|(
name|c
operator|.
name|revId
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|comments
operator|=
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|cs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadDefaults ()
specifier|protected
name|void
name|loadDefaults
parameter_list|()
block|{
name|comments
operator|=
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectName ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
block|{
return|return
name|change
operator|.
name|getProject
argument_list|()
return|;
block|}
block|}
end_class

end_unit

