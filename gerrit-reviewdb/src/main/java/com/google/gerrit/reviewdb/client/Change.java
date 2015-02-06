begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_CHANGES
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
name|ChangeStatus
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
name|client
operator|.
name|Column
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
name|client
operator|.
name|IntKey
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
name|client
operator|.
name|RowVersion
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
name|client
operator|.
name|StringKey
import|;
end_import

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
name|Arrays
import|;
end_import

begin_comment
comment|/**  * A change proposed to be merged into a {@link Branch}.  *<p>  * The data graph rooted below a Change can be quite complex:  *  *<pre>  *   {@link Change}  *     |  *     +- {@link ChangeMessage}:&quot;cover letter&quot; or general comment.  *     |  *     +- {@link PatchSet}: a single variant of this change.  *          |  *          +- {@link PatchSetApproval}: a +/- vote on the change's current state.  *          |  *          +- {@link PatchSetAncestor}: parents of this change's commit.  *          |  *          +- {@link PatchLineComment}: comment about a specific line  *</pre>  *<p>  *<h5>PatchSets</h5>  *<p>  * Every change has at least one PatchSet. A change starts out with one  * PatchSet, the initial proposal put forth by the change owner. This  * {@link Account} is usually also listed as the author and committer in the  * PatchSetInfo.  *<p>  * The {@link PatchSetAncestor} entities are a mirror of the Git commit  * metadata, providing access to the information without needing direct  * accessing Git. These entities are actually legacy artifacts from Gerrit 1.x  * and could be removed, replaced by direct RevCommit access.  *<p>  * Each PatchSet contains zero or more Patch records, detailing the file paths  * impacted by the change (otherwise known as, the file paths the author  * added/deleted/modified). Sometimes a merge commit can contain zero patches,  * if the merge has no conflicts, or has no impact other than to cut off a line  * of development.  *<p>  * Each PatchLineComment is a draft or a published comment about a single line  * of the associated file. These are the inline comment entities created by  * users as they perform a review.  *<p>  * When additional PatchSets appear under a change, these PatchSets reference  *<i>replacement</i> commits; alternative commits that could be made to the  * project instead of the original commit referenced by the first PatchSet.  *<p>  * A change has at most one current PatchSet. The current PatchSet is updated  * when a new replacement PatchSet is uploaded. When a change is submitted, the  * current patch set is what is merged into the destination branch.  *<p>  *<h5>ChangeMessage</h5>  *<p>  * The ChangeMessage entity is a general free-form comment about the whole  * change, rather than PatchLineComment's file and line specific context. The  * ChangeMessage appears at the start of any email generated by Gerrit, and is  * shown on the change overview page, rather than in a file-specific context.  * Users often use this entity to describe general remarks about the overall  * concept proposed by the change.  *<p>  *<h5>PatchSetApproval</h5>  *<p>  * PatchSetApproval entities exist to fill in the<i>cells</i> of the approvals  * table in the web UI. That is, a single PatchSetApproval record's key is the  * tuple {@code (PatchSet,Account,ApprovalCategory)}. Each PatchSetApproval  * carries with it a small score value, typically within the range -2..+2.  *<p>  * If an Account has created only PatchSetApprovals with a score value of 0, the  * Change shows in their dashboard, and they are said to be CC'd (carbon copied)  * on the Change, but are not a direct reviewer. This often happens when an  * account was specified at upload time with the {@code --cc} command line flag,  * or have published comments, but left the approval scores at 0 ("No Score").  *<p>  * If an Account has one or more PatchSetApprovals with a score != 0, the Change  * shows in their dashboard, and they are said to be an active reviewer. Such  * individuals are highlighted when notice of a replacement patch set is sent,  * or when notice of the change submission occurs.  */
end_comment

begin_class
DECL|class|Change
specifier|public
specifier|final
class|class
name|Change
block|{
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{     }
DECL|method|Id (final int id)
specifier|public
name|Id
parameter_list|(
specifier|final
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|set (int newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|int
name|newValue
parameter_list|)
block|{
name|id
operator|=
name|newValue
expr_stmt|;
block|}
comment|/** Parse a Change.Id out of a string representation. */
DECL|method|parse (final String str)
specifier|public
specifier|static
name|Id
name|parse
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
specifier|final
name|Id
name|r
init|=
operator|new
name|Id
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|fromRef (String ref)
specifier|public
specifier|static
name|Id
name|fromRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|int
name|cs
init|=
name|startIndex
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|ce
init|=
name|nextNonDigit
argument_list|(
name|ref
argument_list|,
name|cs
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|substring
argument_list|(
name|ce
argument_list|)
operator|.
name|equals
argument_list|(
name|RefNames
operator|.
name|META_SUFFIX
argument_list|)
operator|||
name|PatchSet
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|ref
argument_list|,
name|ce
argument_list|)
operator|>=
literal|0
condition|)
block|{
return|return
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|ref
operator|.
name|substring
argument_list|(
name|cs
argument_list|,
name|ce
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|startIndex (String ref)
specifier|static
name|int
name|startIndex
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|==
literal|null
operator|||
operator|!
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_CHANGES
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// Last 2 digits.
name|int
name|ls
init|=
name|REFS_CHANGES
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|le
init|=
name|nextNonDigit
argument_list|(
name|ref
argument_list|,
name|ls
argument_list|)
decl_stmt|;
if|if
condition|(
name|le
operator|-
name|ls
operator|!=
literal|2
operator|||
name|le
operator|>=
name|ref
operator|.
name|length
argument_list|()
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|le
argument_list|)
operator|!=
literal|'/'
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// Change ID.
name|int
name|cs
init|=
name|le
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|cs
operator|>=
name|ref
operator|.
name|length
argument_list|()
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|cs
argument_list|)
operator|==
literal|'0'
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|ce
init|=
name|nextNonDigit
argument_list|(
name|ref
argument_list|,
name|cs
argument_list|)
decl_stmt|;
if|if
condition|(
name|ce
operator|>=
name|ref
operator|.
name|length
argument_list|()
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|ce
argument_list|)
operator|!=
literal|'/'
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
switch|switch
condition|(
name|ce
operator|-
name|cs
condition|)
block|{
case|case
literal|0
case|:
return|return
operator|-
literal|1
return|;
case|case
literal|1
case|:
if|if
condition|(
name|ref
operator|.
name|charAt
argument_list|(
name|ls
argument_list|)
operator|!=
literal|'0'
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|ls
operator|+
literal|1
argument_list|)
operator|!=
name|ref
operator|.
name|charAt
argument_list|(
name|cs
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
break|break;
default|default:
if|if
condition|(
name|ref
operator|.
name|charAt
argument_list|(
name|ls
argument_list|)
operator|!=
name|ref
operator|.
name|charAt
argument_list|(
name|ce
operator|-
literal|2
argument_list|)
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|ls
operator|+
literal|1
argument_list|)
operator|!=
name|ref
operator|.
name|charAt
argument_list|(
name|ce
operator|-
literal|1
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
break|break;
block|}
return|return
name|cs
return|;
block|}
DECL|method|nextNonDigit (String s, int i)
specifier|static
name|int
name|nextNonDigit
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|i
parameter_list|)
block|{
while|while
condition|(
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
operator|&&
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|>=
literal|'0'
operator|&&
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|<=
literal|'9'
condition|)
block|{
name|i
operator|++
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
block|}
comment|/** Globally unique identification of this change. */
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|length
operator|=
literal|60
argument_list|)
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{     }
DECL|method|Key (final String id)
specifier|public
name|Key
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|id
operator|=
name|newValue
expr_stmt|;
block|}
comment|/** Construct a key that is after all keys prefixed by this key. */
DECL|method|max ()
specifier|public
name|Key
name|max
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|revEnd
init|=
operator|new
name|StringBuilder
argument_list|(
name|get
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|revEnd
operator|.
name|append
argument_list|(
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|revEnd
operator|.
name|append
argument_list|(
literal|'\u9fa5'
argument_list|)
expr_stmt|;
return|return
operator|new
name|Key
argument_list|(
name|revEnd
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/** Obtain a shorter version of this key string, using a leading prefix. */
DECL|method|abbreviate ()
specifier|public
name|String
name|abbreviate
parameter_list|()
block|{
specifier|final
name|String
name|s
init|=
name|get
argument_list|()
decl_stmt|;
return|return
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|s
operator|.
name|length
argument_list|()
argument_list|,
literal|9
argument_list|)
argument_list|)
return|;
block|}
comment|/** Parse a Change.Key out of a string representation. */
DECL|method|parse (final String str)
specifier|public
specifier|static
name|Key
name|parse
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
specifier|final
name|Key
name|r
init|=
operator|new
name|Key
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
comment|/** Minimum database status constant for an open change. */
DECL|field|MIN_OPEN
specifier|private
specifier|static
specifier|final
name|char
name|MIN_OPEN
init|=
literal|'a'
decl_stmt|;
comment|/** Database constant for {@link Status#NEW}. */
DECL|field|STATUS_NEW
specifier|public
specifier|static
specifier|final
name|char
name|STATUS_NEW
init|=
literal|'n'
decl_stmt|;
comment|/** Database constant for {@link Status#SUBMITTED}. */
DECL|field|STATUS_SUBMITTED
specifier|public
specifier|static
specifier|final
name|char
name|STATUS_SUBMITTED
init|=
literal|'s'
decl_stmt|;
comment|/** Database constant for {@link Status#DRAFT}. */
DECL|field|STATUS_DRAFT
specifier|public
specifier|static
specifier|final
name|char
name|STATUS_DRAFT
init|=
literal|'d'
decl_stmt|;
comment|/** Maximum database status constant for an open change. */
DECL|field|MAX_OPEN
specifier|private
specifier|static
specifier|final
name|char
name|MAX_OPEN
init|=
literal|'z'
decl_stmt|;
comment|/** Database constant for {@link Status#MERGED}. */
DECL|field|STATUS_MERGED
specifier|public
specifier|static
specifier|final
name|char
name|STATUS_MERGED
init|=
literal|'M'
decl_stmt|;
comment|/** ID number of the first patch set in a change. */
DECL|field|INITIAL_PATCH_SET_ID
specifier|public
specifier|static
specifier|final
name|int
name|INITIAL_PATCH_SET_ID
init|=
literal|1
decl_stmt|;
comment|/**    * Current state within the basic workflow of the change.    *    *<p>    * Within the database, lower case codes ('a'..'z') indicate a change that is    * still open, and that can be modified/refined further, while upper case    * codes ('A'..'Z') indicate a change that is closed and cannot be further    * modified.    * */
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
comment|/**      * Change is open and pending review, or review is in progress.      *      *<p>      * This is the default state assigned to a change when it is first created      * in the database. A change stays in the NEW state throughout its review      * cycle, until the change is submitted or abandoned.      *      *<p>      * Changes in the NEW state can be moved to:      *<ul>      *<li>{@link #SUBMITTED} - when the Submit Patch Set action is used;      *<li>{@link #ABANDONED} - when the Abandon action is used.      *</ul>      */
DECL|enumConstant|NEW
name|NEW
parameter_list|(
name|STATUS_NEW
parameter_list|,
name|ChangeStatus
operator|.
name|NEW
parameter_list|)
operator|,
comment|/**      * Change is open, but has been submitted to the merge queue.      *      *<p>      * A change enters the SUBMITTED state when an authorized user presses the      * "submit" action through the web UI, requesting that Gerrit merge the      * change's current patch set into the destination branch.      *      *<p>      * Typically a change resides in the SUBMITTED for only a brief sub-second      * period while the merge queue fires and the destination branch is updated.      * However, if a dependency commit (a {@link PatchSetAncestor}, directly or      * transitively) is not yet merged into the branch, the change will hang in      * the SUBMITTED state indefinitely.      *      *<p>      * Changes in the SUBMITTED state can be moved to:      *<ul>      *<li>{@link #NEW} - when a replacement patch set is supplied, OR when a      * merge conflict is detected;      *<li>{@link #MERGED} - when the change has been successfully merged into      * the destination branch;      *<li>{@link #ABANDONED} - when the Abandon action is used.      *</ul>      */
DECL|enumConstant|SUBMITTED
constructor|SUBMITTED(STATUS_SUBMITTED
operator|,
constructor|ChangeStatus.SUBMITTED
block|)
enum|,
comment|/**      * Change is a draft change that only consists of draft patchsets.      *      *<p>      * This is a change that is not meant to be submitted or reviewed yet. If      * the uploader publishes the change, it becomes a NEW change.      * Publishing is a one-way action, a change cannot return to DRAFT status.      * Draft changes are only visible to the uploader and those explicitly      * added as reviewers.      *      *<p>      * Changes in the DRAFT state can be moved to:      *<ul>      *<li>{@link #NEW} - when the change is published, it becomes a new change;      *</ul>      */
DECL|enumConstant|DRAFT
name|DRAFT
parameter_list|(
name|STATUS_DRAFT
parameter_list|,
name|ChangeStatus
operator|.
name|DRAFT
parameter_list|)
operator|,
comment|/**      * Change is closed, and submitted to its destination branch.      *      *<p>      * Once a change has been merged, it cannot be further modified by adding a      * replacement patch set. Draft comments however may be published,      * supporting a post-submit review.      */
DECL|enumConstant|MERGED
constructor|MERGED(STATUS_MERGED
operator|,
constructor|ChangeStatus.MERGED
block|)
operator|,
comment|/**      * Change is closed, but was not submitted to its destination branch.      *      *<p>      * Once a change has been abandoned, it cannot be further modified by adding      * a replacement patch set, and it cannot be merged. Draft comments however      * may be published, permitting reviewers to send constructive feedback.      */
DECL|enumConstant|ABANDONED
name|ABANDONED
argument_list|(
literal|'A'
argument_list|,
name|ChangeStatus
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
end_class

begin_static
static|static
block|{
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|Status
operator|.
name|values
argument_list|()
operator|.
name|length
operator|!=
name|ChangeStatus
operator|.
name|values
argument_list|()
operator|.
name|length
condition|)
block|{
name|ok
operator|=
literal|false
expr_stmt|;
block|}
for|for
control|(
name|Status
name|s
range|:
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
name|ok
operator|&=
name|s
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|s
operator|.
name|changeStatus
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ok
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Mismatched status mapping: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|Status
operator|.
name|values
argument_list|()
argument_list|)
operator|+
literal|" != "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|ChangeStatus
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
end_static

begin_decl_stmt
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|closed
specifier|private
specifier|final
name|boolean
name|closed
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|changeStatus
specifier|private
specifier|final
name|ChangeStatus
name|changeStatus
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|Status (char c, ChangeStatus cs)
specifier|private
name|Status
parameter_list|(
name|char
name|c
parameter_list|,
name|ChangeStatus
name|cs
parameter_list|)
block|{
name|code
operator|=
name|c
expr_stmt|;
name|closed
operator|=
operator|!
operator|(
name|MIN_OPEN
operator|<=
name|c
operator|&&
name|c
operator|<=
name|MAX_OPEN
operator|)
expr_stmt|;
name|changeStatus
operator|=
name|cs
expr_stmt|;
block|}
end_constructor

begin_function
DECL|method|getCode ()
specifier|public
name|char
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
end_function

begin_function
DECL|method|isOpen ()
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
operator|!
name|closed
return|;
block|}
end_function

begin_function
DECL|method|isClosed ()
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
end_function

begin_function
DECL|method|asChangeStatus ()
specifier|public
name|ChangeStatus
name|asChangeStatus
parameter_list|()
block|{
return|return
name|changeStatus
return|;
block|}
end_function

begin_function
DECL|method|forCode (final char c)
specifier|public
specifier|static
name|Status
name|forCode
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Status
name|s
range|:
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|code
operator|==
name|c
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
end_function

begin_function
DECL|method|forChangeStatus (ChangeStatus cs)
specifier|public
specifier|static
name|Status
name|forChangeStatus
parameter_list|(
name|ChangeStatus
name|cs
parameter_list|)
block|{
for|for
control|(
name|Status
name|s
range|:
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|changeStatus
operator|==
name|cs
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
end_function

begin_comment
unit|}
comment|/** Locally assigned unique identifier of the change */
end_comment

begin_expr_stmt
unit|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|changeId
specifier|protected
name|Id
name|changeId
expr_stmt|;
end_expr_stmt

begin_comment
comment|/** Globally assigned unique identifier of the change */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|changeKey
specifier|protected
name|Key
name|changeKey
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** optimistic locking */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|)
annotation|@
name|RowVersion
DECL|field|rowVersion
specifier|protected
name|int
name|rowVersion
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** When this change was first introduced into the database. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|)
DECL|field|createdOn
specifier|protected
name|Timestamp
name|createdOn
decl_stmt|;
end_decl_stmt

begin_comment
comment|/**    * When was a meaningful modification last made to this record's data    *<p>    * Note, this update timestamp includes its children.    */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|5
argument_list|)
DECL|field|lastUpdatedOn
specifier|protected
name|Timestamp
name|lastUpdatedOn
decl_stmt|;
end_decl_stmt

begin_comment
comment|// DELETED: id = 6 (sortkey)
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|7
argument_list|,
name|name
operator|=
literal|"owner_account_id"
argument_list|)
DECL|field|owner
specifier|protected
name|Account
operator|.
name|Id
name|owner
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** The branch (and project) this change merges into. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|8
argument_list|)
DECL|field|dest
specifier|protected
name|Branch
operator|.
name|NameKey
name|dest
decl_stmt|;
end_decl_stmt

begin_comment
comment|// DELETED: id = 9 (open)
end_comment

begin_comment
comment|/** Current state code; see {@link Status}. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|10
argument_list|)
DECL|field|status
specifier|protected
name|char
name|status
decl_stmt|;
end_decl_stmt

begin_comment
comment|// DELETED: id = 11 (nbrPatchSets)
end_comment

begin_comment
comment|/** The current patch set. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|12
argument_list|)
DECL|field|currentPatchSetId
specifier|protected
name|int
name|currentPatchSetId
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Subject from the current patch set. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|13
argument_list|)
DECL|field|subject
specifier|protected
name|String
name|subject
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Topic name assigned by the user, if any. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|14
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|topic
specifier|protected
name|String
name|topic
decl_stmt|;
end_decl_stmt

begin_comment
comment|// DELETED: id = 15 (lastSha1MergeTested)
end_comment

begin_comment
comment|// DELETED: id = 16 (mergeable)
end_comment

begin_comment
comment|/**    * First line of first patch set's commit message.    *    * Unlike {@link #subject}, this string does not change if future patch sets    * change the first line.    */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|17
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|originalSubject
specifier|protected
name|String
name|originalSubject
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|Change ()
specifier|protected
name|Change
parameter_list|()
block|{   }
end_constructor

begin_constructor
DECL|method|Change (Change.Key newKey, Change.Id newId, Account.Id ownedBy, Branch.NameKey forBranch, Timestamp ts)
specifier|public
name|Change
parameter_list|(
name|Change
operator|.
name|Key
name|newKey
parameter_list|,
name|Change
operator|.
name|Id
name|newId
parameter_list|,
name|Account
operator|.
name|Id
name|ownedBy
parameter_list|,
name|Branch
operator|.
name|NameKey
name|forBranch
parameter_list|,
name|Timestamp
name|ts
parameter_list|)
block|{
name|changeKey
operator|=
name|newKey
expr_stmt|;
name|changeId
operator|=
name|newId
expr_stmt|;
name|createdOn
operator|=
name|ts
expr_stmt|;
name|lastUpdatedOn
operator|=
name|createdOn
expr_stmt|;
name|owner
operator|=
name|ownedBy
expr_stmt|;
name|dest
operator|=
name|forBranch
expr_stmt|;
name|setStatus
argument_list|(
name|Status
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
end_constructor

begin_constructor
DECL|method|Change (Change other)
specifier|public
name|Change
parameter_list|(
name|Change
name|other
parameter_list|)
block|{
name|changeId
operator|=
name|other
operator|.
name|changeId
expr_stmt|;
name|changeKey
operator|=
name|other
operator|.
name|changeKey
expr_stmt|;
name|rowVersion
operator|=
name|other
operator|.
name|rowVersion
expr_stmt|;
name|createdOn
operator|=
name|other
operator|.
name|createdOn
expr_stmt|;
name|lastUpdatedOn
operator|=
name|other
operator|.
name|lastUpdatedOn
expr_stmt|;
name|owner
operator|=
name|other
operator|.
name|owner
expr_stmt|;
name|dest
operator|=
name|other
operator|.
name|dest
expr_stmt|;
name|status
operator|=
name|other
operator|.
name|status
expr_stmt|;
name|currentPatchSetId
operator|=
name|other
operator|.
name|currentPatchSetId
expr_stmt|;
name|subject
operator|=
name|other
operator|.
name|subject
expr_stmt|;
name|originalSubject
operator|=
name|other
operator|.
name|originalSubject
expr_stmt|;
name|topic
operator|=
name|other
operator|.
name|topic
expr_stmt|;
block|}
end_constructor

begin_comment
comment|/** Legacy 32 bit integer identity for a change. */
end_comment

begin_function
DECL|method|getId ()
specifier|public
name|Change
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
end_function

begin_comment
comment|/** Legacy 32 bit integer identity for a change. */
end_comment

begin_function
DECL|method|getChangeId ()
specifier|public
name|int
name|getChangeId
parameter_list|()
block|{
return|return
name|changeId
operator|.
name|get
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/** The Change-Id tag out of the initial commit, or a natural key. */
end_comment

begin_function
DECL|method|getKey ()
specifier|public
name|Change
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|changeKey
return|;
block|}
end_function

begin_function
DECL|method|setKey (final Change.Key k)
specifier|public
name|void
name|setKey
parameter_list|(
specifier|final
name|Change
operator|.
name|Key
name|k
parameter_list|)
block|{
name|changeKey
operator|=
name|k
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getCreatedOn ()
specifier|public
name|Timestamp
name|getCreatedOn
parameter_list|()
block|{
return|return
name|createdOn
return|;
block|}
end_function

begin_function
DECL|method|getLastUpdatedOn ()
specifier|public
name|Timestamp
name|getLastUpdatedOn
parameter_list|()
block|{
return|return
name|lastUpdatedOn
return|;
block|}
end_function

begin_function
DECL|method|setLastUpdatedOn (Timestamp now)
specifier|public
name|void
name|setLastUpdatedOn
parameter_list|(
name|Timestamp
name|now
parameter_list|)
block|{
name|lastUpdatedOn
operator|=
name|now
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getRowVersion ()
specifier|public
name|int
name|getRowVersion
parameter_list|()
block|{
return|return
name|rowVersion
return|;
block|}
end_function

begin_function
DECL|method|getOwner ()
specifier|public
name|Account
operator|.
name|Id
name|getOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
end_function

begin_function
DECL|method|getDest ()
specifier|public
name|Branch
operator|.
name|NameKey
name|getDest
parameter_list|()
block|{
return|return
name|dest
return|;
block|}
end_function

begin_function
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|dest
operator|.
name|getParentKey
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
end_function

begin_function
DECL|method|getOriginalSubject ()
specifier|public
name|String
name|getOriginalSubject
parameter_list|()
block|{
return|return
name|originalSubject
operator|!=
literal|null
condition|?
name|originalSubject
else|:
name|subject
return|;
block|}
end_function

begin_comment
comment|/** Get the id of the most current {@link PatchSet} in this change. */
end_comment

begin_function
DECL|method|currentPatchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|currentPatchSetId
parameter_list|()
block|{
if|if
condition|(
name|currentPatchSetId
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
name|currentPatchSetId
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
end_function

begin_function
DECL|method|setCurrentPatchSet (final PatchSetInfo ps)
specifier|public
name|void
name|setCurrentPatchSet
parameter_list|(
specifier|final
name|PatchSetInfo
name|ps
parameter_list|)
block|{
if|if
condition|(
name|originalSubject
operator|==
literal|null
operator|&&
name|subject
operator|!=
literal|null
condition|)
block|{
comment|// Change was created before schema upgrade. Use the last subject
comment|// associated with this change, as the most recent discussion will
comment|// be under that thread in an email client such as GMail.
name|originalSubject
operator|=
name|subject
expr_stmt|;
block|}
name|currentPatchSetId
operator|=
name|ps
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|subject
operator|=
name|ps
operator|.
name|getSubject
argument_list|()
expr_stmt|;
if|if
condition|(
name|originalSubject
operator|==
literal|null
condition|)
block|{
comment|// Newly created changes remember the first commit's subject.
name|originalSubject
operator|=
name|subject
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|Status
operator|.
name|forCode
argument_list|(
name|status
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|setStatus (Status newStatus)
specifier|public
name|void
name|setStatus
parameter_list|(
name|Status
name|newStatus
parameter_list|)
block|{
name|status
operator|=
name|newStatus
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
end_function

begin_function
DECL|method|setTopic (String topic)
specifier|public
name|void
name|setTopic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
operator|.
name|append
argument_list|(
name|changeId
argument_list|)
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|changeKey
argument_list|)
operator|.
name|append
argument_list|(
literal|"), "
argument_list|)
operator|.
name|append
argument_list|(
literal|"dest="
argument_list|)
operator|.
name|append
argument_list|(
name|dest
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
literal|"status="
argument_list|)
operator|.
name|append
argument_list|(
name|status
argument_list|)
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

