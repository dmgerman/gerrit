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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
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
operator|.
name|Status
operator|.
name|PUBLISHED
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|OBJ_BLOB
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
name|Comment
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
name|assistedinject
operator|.
name|Assisted
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
name|stream
operator|.
name|Collectors
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
name|CommitBuilder
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
name|ObjectInserter
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevSort
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
name|RevWalk
import|;
end_import

begin_comment
comment|/**  * Deletes a published comment from NoteDb by rewriting the commit history. Instead of deleting the  * whole comment, it just replaces the comment's message with a new message.  */
end_comment

begin_class
DECL|class|DeleteCommentRewriter
specifier|public
class|class
name|DeleteCommentRewriter
implements|implements
name|NoteDbRewriter
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
comment|/**      * Creates a DeleteCommentRewriter instance.      *      * @param id the id of the change which contains the target comment.      * @param uuid the uuid of the target comment.      * @param newMessage the message used to replace the old message of the target comment.      * @return the DeleteCommentRewriter instance      */
DECL|method|create ( Change.Id id, @Assisted(R) String uuid, @Assisted(R) String newMessage)
name|DeleteCommentRewriter
name|create
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"uuid"
argument_list|)
name|String
name|uuid
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"newMessage"
argument_list|)
name|String
name|newMessage
parameter_list|)
function_decl|;
block|}
DECL|field|noteUtil
specifier|private
specifier|final
name|ChangeNoteUtil
name|noteUtil
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|uuid
specifier|private
specifier|final
name|String
name|uuid
decl_stmt|;
DECL|field|newMessage
specifier|private
specifier|final
name|String
name|newMessage
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteCommentRewriter ( ChangeNoteUtil noteUtil, @Assisted Change.Id changeId, @Assisted(R) String uuid, @Assisted(R) String newMessage)
name|DeleteCommentRewriter
parameter_list|(
name|ChangeNoteUtil
name|noteUtil
parameter_list|,
annotation|@
name|Assisted
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"uuid"
argument_list|)
name|String
name|uuid
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"newMessage"
argument_list|)
name|String
name|newMessage
parameter_list|)
block|{
name|this
operator|.
name|noteUtil
operator|=
name|noteUtil
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|uuid
operator|=
name|uuid
expr_stmt|;
name|this
operator|.
name|newMessage
operator|=
name|newMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|RefNames
operator|.
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|rewriteCommitHistory (RevWalk revWalk, ObjectInserter inserter, ObjectId currTip)
specifier|public
name|ObjectId
name|rewriteCommitHistory
parameter_list|(
name|RevWalk
name|revWalk
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|ObjectId
name|currTip
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|OrmException
block|{
name|checkArgument
argument_list|(
operator|!
name|currTip
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Walk from the first commit of the branch.
name|revWalk
operator|.
name|reset
argument_list|()
expr_stmt|;
name|revWalk
operator|.
name|markStart
argument_list|(
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|currTip
argument_list|)
argument_list|)
expr_stmt|;
name|revWalk
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|REVERSE
argument_list|)
expr_stmt|;
name|ObjectReader
name|reader
init|=
name|revWalk
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|ObjectId
name|newTip
init|=
name|revWalk
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// The first commit will not be rewritten.
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|parentComments
init|=
name|getPublishedComments
argument_list|(
name|noteUtil
argument_list|,
name|changeId
argument_list|,
name|reader
argument_list|,
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|newTip
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|rewrite
init|=
literal|false
decl_stmt|;
name|RevCommit
name|originalCommit
decl_stmt|;
while|while
condition|(
operator|(
name|originalCommit
operator|=
name|revWalk
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|NoteMap
name|noteMap
init|=
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|originalCommit
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|currComments
init|=
name|getPublishedComments
argument_list|(
name|noteUtil
argument_list|,
name|changeId
argument_list|,
name|reader
argument_list|,
name|noteMap
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rewrite
operator|&&
name|currComments
operator|.
name|containsKey
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
name|rewrite
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|rewrite
condition|)
block|{
name|parentComments
operator|=
name|currComments
expr_stmt|;
name|newTip
operator|=
name|originalCommit
expr_stmt|;
continue|continue;
block|}
name|List
argument_list|<
name|Comment
argument_list|>
name|putInComments
init|=
name|getPutInComments
argument_list|(
name|parentComments
argument_list|,
name|currComments
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|deletedComments
init|=
name|getDeletedComments
argument_list|(
name|parentComments
argument_list|,
name|currComments
argument_list|)
decl_stmt|;
name|newTip
operator|=
name|rewriteCommit
argument_list|(
name|originalCommit
argument_list|,
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|newTip
argument_list|)
argument_list|)
argument_list|,
name|newTip
argument_list|,
name|inserter
argument_list|,
name|reader
argument_list|,
name|putInComments
argument_list|,
name|deletedComments
argument_list|)
expr_stmt|;
name|parentComments
operator|=
name|currComments
expr_stmt|;
block|}
return|return
name|newTip
return|;
block|}
comment|/**    * Gets all the comments which are presented at a commit. Note they include the comments put in by    * the previous commits.    */
annotation|@
name|VisibleForTesting
DECL|method|getPublishedComments ( ChangeNoteUtil noteUtil, Change.Id changeId, ObjectReader reader, NoteMap noteMap)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|getPublishedComments
parameter_list|(
name|ChangeNoteUtil
name|noteUtil
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|ObjectReader
name|reader
parameter_list|,
name|NoteMap
name|noteMap
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|RevisionNoteMap
operator|.
name|parse
argument_list|(
name|noteUtil
argument_list|,
name|changeId
argument_list|,
name|reader
argument_list|,
name|noteMap
argument_list|,
name|PUBLISHED
argument_list|)
operator|.
name|revisionNotes
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|n
lambda|->
name|n
operator|.
name|getComments
argument_list|()
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|c
lambda|->
name|c
operator|.
name|key
operator|.
name|uuid
argument_list|,
name|c
lambda|->
name|c
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Gets the comments put in by the current commit. The message of the target comment will be    * replaced by the new message.    *    * @param parMap the comment map of the parent commit.    * @param curMap the comment map of the current commit.    * @return The comments put in by the current commit.    */
DECL|method|getPutInComments (Map<String, Comment> parMap, Map<String, Comment> curMap)
specifier|private
name|List
argument_list|<
name|Comment
argument_list|>
name|getPutInComments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|parMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|curMap
parameter_list|)
block|{
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|curMap
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|parMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Comment
name|comment
init|=
name|curMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
name|comment
operator|.
name|message
operator|=
name|newMessage
expr_stmt|;
block|}
name|comments
operator|.
name|add
argument_list|(
name|comment
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|comments
return|;
block|}
comment|/**    * Gets the comments deleted by the current commit.    *    * @param parMap the comment map of the parent commit.    * @param curMap the comment map of the current commit.    * @return The comments deleted by the current commit.    */
DECL|method|getDeletedComments ( Map<String, Comment> parMap, Map<String, Comment> curMap)
specifier|private
name|List
argument_list|<
name|Comment
argument_list|>
name|getDeletedComments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|parMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Comment
argument_list|>
name|curMap
parameter_list|)
block|{
return|return
name|parMap
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
operator|!
name|curMap
operator|.
name|containsKey
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Rewrites one commit.    *    * @param originalCommit the original commit to be rewritten.    * @param parentNoteMap the {@code NoteMap} of the new commit's parent.    * @param parentId the {@code ObjectId} of the new commit's parent.    * @param inserter the {@code ObjectInserter} for the rewrite process.    * @param reader the {@code ObjectReader} for the rewrite process.    * @param putInComments the comments put in by this commit.    * @param deletedComments the comments deleted by this commit.    * @return the {@code objectId} of the new commit.    * @throws IOException    * @throws ConfigInvalidException    */
DECL|method|rewriteCommit ( RevCommit originalCommit, NoteMap parentNoteMap, ObjectId parentId, ObjectInserter inserter, ObjectReader reader, List<Comment> putInComments, List<Comment> deletedComments)
specifier|private
name|ObjectId
name|rewriteCommit
parameter_list|(
name|RevCommit
name|originalCommit
parameter_list|,
name|NoteMap
name|parentNoteMap
parameter_list|,
name|ObjectId
name|parentId
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|ObjectReader
name|reader
parameter_list|,
name|List
argument_list|<
name|Comment
argument_list|>
name|putInComments
parameter_list|,
name|List
argument_list|<
name|Comment
argument_list|>
name|deletedComments
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|RevisionNoteMap
argument_list|<
name|ChangeRevisionNote
argument_list|>
name|revNotesMap
init|=
name|RevisionNoteMap
operator|.
name|parse
argument_list|(
name|noteUtil
argument_list|,
name|changeId
argument_list|,
name|reader
argument_list|,
name|parentNoteMap
argument_list|,
name|PUBLISHED
argument_list|)
decl_stmt|;
name|RevisionNoteBuilder
operator|.
name|Cache
name|cache
init|=
operator|new
name|RevisionNoteBuilder
operator|.
name|Cache
argument_list|(
name|revNotesMap
argument_list|)
decl_stmt|;
for|for
control|(
name|Comment
name|c
range|:
name|putInComments
control|)
block|{
name|cache
operator|.
name|get
argument_list|(
operator|new
name|RevId
argument_list|(
name|c
operator|.
name|revId
argument_list|)
argument_list|)
operator|.
name|putComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Comment
name|c
range|:
name|deletedComments
control|)
block|{
name|cache
operator|.
name|get
argument_list|(
operator|new
name|RevId
argument_list|(
name|c
operator|.
name|revId
argument_list|)
argument_list|)
operator|.
name|deleteComment
argument_list|(
name|c
operator|.
name|key
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|RevId
argument_list|,
name|RevisionNoteBuilder
argument_list|>
name|builders
init|=
name|cache
operator|.
name|getBuilders
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|RevId
argument_list|,
name|RevisionNoteBuilder
argument_list|>
name|entry
range|:
name|builders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ObjectId
name|objectId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|build
argument_list|(
name|noteUtil
argument_list|,
name|noteUtil
operator|.
name|getWriteJson
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|revNotesMap
operator|.
name|noteMap
operator|.
name|remove
argument_list|(
name|objectId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|revNotesMap
operator|.
name|noteMap
operator|.
name|set
argument_list|(
name|objectId
argument_list|,
name|inserter
operator|.
name|insert
argument_list|(
name|OBJ_BLOB
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setParentId
argument_list|(
name|parentId
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|revNotesMap
operator|.
name|noteMap
operator|.
name|writeTree
argument_list|(
name|inserter
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
name|originalCommit
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|originalCommit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|originalCommit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setEncoding
argument_list|(
name|originalCommit
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|inserter
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
return|;
block|}
block|}
end_class

end_unit

