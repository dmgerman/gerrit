begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|server
operator|.
name|notedb
operator|.
name|ChangeNoteUtil
operator|.
name|parseCommitMessageRange
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|util
operator|.
name|RawParseUtils
operator|.
name|decode
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
name|entities
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
name|entities
operator|.
name|RefNames
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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_comment
comment|/**  * Deletes a change message from NoteDb by rewriting the commit history. After deletion, the whole  * change message will be replaced by a new message indicating the original change message has been  * deleted for the given reason.  */
end_comment

begin_class
DECL|class|DeleteChangeMessageRewriter
specifier|public
class|class
name|DeleteChangeMessageRewriter
implements|implements
name|NoteDbRewriter
block|{
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|targetMessageId
specifier|private
specifier|final
name|String
name|targetMessageId
decl_stmt|;
DECL|field|newChangeMessage
specifier|private
specifier|final
name|String
name|newChangeMessage
decl_stmt|;
DECL|method|DeleteChangeMessageRewriter (Change.Id changeId, String targetMessageId, String newChangeMessage)
name|DeleteChangeMessageRewriter
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|targetMessageId
parameter_list|,
name|String
name|newChangeMessage
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|targetMessageId
operator|=
name|requireNonNull
argument_list|(
name|targetMessageId
argument_list|)
expr_stmt|;
name|this
operator|.
name|newChangeMessage
operator|=
name|newChangeMessage
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
name|TOPO
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
name|ObjectId
name|newTipId
init|=
literal|null
decl_stmt|;
name|RevCommit
name|originalCommit
decl_stmt|;
name|boolean
name|startRewrite
init|=
literal|false
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
name|boolean
name|isTargetCommit
init|=
name|originalCommit
operator|.
name|getId
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|targetMessageId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|startRewrite
operator|&&
operator|!
name|isTargetCommit
condition|)
block|{
name|newTipId
operator|=
name|originalCommit
expr_stmt|;
continue|continue;
block|}
name|startRewrite
operator|=
literal|true
expr_stmt|;
name|String
name|newCommitMessage
init|=
name|isTargetCommit
condition|?
name|createNewCommitMessage
argument_list|(
name|originalCommit
argument_list|)
else|:
name|originalCommit
operator|.
name|getFullMessage
argument_list|()
decl_stmt|;
name|newTipId
operator|=
name|rewriteOneCommit
argument_list|(
name|originalCommit
argument_list|,
name|newTipId
argument_list|,
name|newCommitMessage
argument_list|,
name|inserter
argument_list|)
expr_stmt|;
block|}
return|return
name|newTipId
return|;
block|}
DECL|method|createNewCommitMessage (RevCommit commit)
specifier|private
name|String
name|createNewCommitMessage
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
block|{
name|byte
index|[]
name|raw
init|=
name|commit
operator|.
name|getRawBuffer
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|ChangeNoteUtil
operator|.
name|CommitMessageRange
argument_list|>
name|range
init|=
name|parseCommitMessageRange
argument_list|(
name|commit
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|range
operator|.
name|isPresent
argument_list|()
argument_list|,
literal|"failed to parse commit message"
argument_list|)
expr_stmt|;
comment|// Only replace the commit message body, which is the user-provided message. The subject and
comment|// footers are NoteDb metadata.
name|Charset
name|encoding
init|=
name|RawParseUtils
operator|.
name|parseEncoding
argument_list|(
name|raw
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|decode
argument_list|(
name|encoding
argument_list|,
name|raw
argument_list|,
name|range
operator|.
name|get
argument_list|()
operator|.
name|subjectStart
argument_list|()
argument_list|,
name|range
operator|.
name|get
argument_list|()
operator|.
name|changeMessageStart
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|postfix
init|=
name|decode
argument_list|(
name|encoding
argument_list|,
name|raw
argument_list|,
name|range
operator|.
name|get
argument_list|()
operator|.
name|changeMessageEnd
argument_list|()
operator|+
literal|1
argument_list|,
name|raw
operator|.
name|length
argument_list|)
decl_stmt|;
return|return
name|prefix
operator|+
name|newChangeMessage
operator|+
name|postfix
return|;
block|}
comment|/**    * Rewrites one commit.    *    * @param originalCommit the original commit to be rewritten.    * @param parentCommitId the parent of the new commit. For the first rewritten commit, it's the    *     parent of 'originalCommit'. For the latter rewritten commits, it's the commit rewritten    *     just before it.    * @param commitMessage the full commit message of the new commit.    * @param inserter the {@code ObjectInserter} for the rewrite process.    * @return the {@code objectId} of the new commit.    * @throws IOException    */
DECL|method|rewriteOneCommit ( RevCommit originalCommit, ObjectId parentCommitId, String commitMessage, ObjectInserter inserter)
specifier|private
name|ObjectId
name|rewriteOneCommit
parameter_list|(
name|RevCommit
name|originalCommit
parameter_list|,
name|ObjectId
name|parentCommitId
parameter_list|,
name|String
name|commitMessage
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|)
throws|throws
name|IOException
block|{
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentCommitId
operator|!=
literal|null
condition|)
block|{
name|cb
operator|.
name|setParentId
argument_list|(
name|parentCommitId
argument_list|)
expr_stmt|;
block|}
name|cb
operator|.
name|setTreeId
argument_list|(
name|originalCommit
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
name|commitMessage
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

