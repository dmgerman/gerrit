begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|PatchLineComment
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
name|PatchSet
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
name|server
operator|.
name|ReviewDb
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
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|ChangeUpdate
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
name|notedb
operator|.
name|NotesMigration
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
import|;
end_import

begin_comment
comment|/**  * Utility functions to manipulate PatchLineComments.  *<p>  * These methods either query for and update PatchLineComments in the NoteDb or  * ReviewDb, depending on the state of the NotesMigration.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PatchLineCommentsUtil
specifier|public
class|class
name|PatchLineCommentsUtil
block|{
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|PatchLineCommentsUtil (NotesMigration migration)
specifier|public
name|PatchLineCommentsUtil
parameter_list|(
name|NotesMigration
name|migration
parameter_list|)
block|{
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
block|}
DECL|method|publishedByChangeFile (ReviewDb db, ChangeNotes notes, Change.Id changeId, String file)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByChangeFile
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|file
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readPublishedComments
argument_list|()
condition|)
block|{
return|return
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|publishedByChangeFile
argument_list|(
name|changeId
argument_list|,
name|file
argument_list|)
operator|.
name|toList
argument_list|()
return|;
block|}
name|notes
operator|.
name|load
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|commentsOnFile
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
decl_stmt|;
comment|// We must iterate through all comments to find the ones on this file.
name|addCommentsInFile
argument_list|(
name|commentsOnFile
argument_list|,
name|notes
operator|.
name|getBaseComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|addCommentsInFile
argument_list|(
name|commentsOnFile
argument_list|,
name|notes
operator|.
name|getPatchSetComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|commentsOnFile
argument_list|,
name|ChangeNotes
operator|.
name|PatchLineCommentComparator
argument_list|)
expr_stmt|;
return|return
name|commentsOnFile
return|;
block|}
DECL|method|publishedByPatchSet (ReviewDb db, ChangeNotes notes, PatchSet.Id psId)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByPatchSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readPublishedComments
argument_list|()
condition|)
block|{
return|return
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|publishedByPatchSet
argument_list|(
name|psId
argument_list|)
operator|.
name|toList
argument_list|()
return|;
block|}
name|notes
operator|.
name|load
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|commentsOnPs
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
decl_stmt|;
name|commentsOnPs
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getPatchSetComments
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
argument_list|)
expr_stmt|;
name|commentsOnPs
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getBaseComments
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|commentsOnPs
return|;
block|}
DECL|method|addCommentsInFile ( Collection<PatchLineComment> commentsOnFile, Collection<PatchLineComment> allComments, String file)
specifier|private
specifier|static
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|addCommentsInFile
parameter_list|(
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|commentsOnFile
parameter_list|,
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|allComments
parameter_list|,
name|String
name|file
parameter_list|)
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|allComments
control|)
block|{
name|String
name|currentFilename
init|=
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|getFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentFilename
operator|.
name|equals
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|commentsOnFile
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|commentsOnFile
return|;
block|}
DECL|method|addPublishedComments (ReviewDb db, ChangeUpdate update, Iterable<PatchLineComment> comments)
specifier|public
name|void
name|addPublishedComments
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Iterable
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|comments
control|)
block|{
name|update
operator|.
name|putComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|upsert
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

