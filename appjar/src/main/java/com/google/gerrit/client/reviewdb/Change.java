begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

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
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** A change recommended to be inserted into {@link Branch}. */
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
annotation|@
name|Column
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
block|}
DECL|field|MIN_OPEN
specifier|private
specifier|static
specifier|final
name|char
name|MIN_OPEN
init|=
literal|'a'
decl_stmt|;
DECL|field|STATUS_NEW
specifier|protected
specifier|static
specifier|final
name|char
name|STATUS_NEW
init|=
literal|'n'
decl_stmt|;
DECL|field|STATUS_SUBMITTED
specifier|protected
specifier|static
specifier|final
name|char
name|STATUS_SUBMITTED
init|=
literal|'s'
decl_stmt|;
DECL|field|MAX_OPEN
specifier|private
specifier|static
specifier|final
name|char
name|MAX_OPEN
init|=
literal|'z'
decl_stmt|;
DECL|field|STATUS_MERGED
specifier|protected
specifier|static
specifier|final
name|char
name|STATUS_MERGED
init|=
literal|'M'
decl_stmt|;
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
DECL|enumConstant|NEW
name|NEW
parameter_list|(
name|STATUS_NEW
parameter_list|)
operator|,
DECL|enumConstant|SUBMITTED
constructor|SUBMITTED(STATUS_SUBMITTED
block|)
enum|,
DECL|enumConstant|MERGED
name|MERGED
parameter_list|(
name|STATUS_MERGED
parameter_list|)
operator|,
DECL|enumConstant|ABANDONED
constructor|ABANDONED('A'
block|)
class|;
end_class

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

begin_constructor
DECL|method|Status (final char c)
specifier|private
name|Status
parameter_list|(
specifier|final
name|char
name|c
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

begin_comment
unit|}
comment|/** Locally assigned unique identifier of the change */
end_comment

begin_decl_stmt
unit|@
name|Column
DECL|field|changeId
specifier|protected
name|Id
name|changeId
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** optimistic locking */
end_comment

begin_decl_stmt
annotation|@
name|Column
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
DECL|field|lastUpdatedOn
specifier|protected
name|Timestamp
name|lastUpdatedOn
decl_stmt|;
end_decl_stmt

begin_decl_stmt
annotation|@
name|Column
argument_list|(
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
DECL|field|dest
specifier|protected
name|Branch
operator|.
name|NameKey
name|dest
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Is the change currently open? Set to {@link #status}.isOpen(). */
end_comment

begin_decl_stmt
annotation|@
name|Column
DECL|field|open
specifier|protected
name|boolean
name|open
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Current state code; see {@link Status}. */
end_comment

begin_decl_stmt
annotation|@
name|Column
DECL|field|status
specifier|protected
name|char
name|status
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** The total number of {@link PatchSet} children in this Change. */
end_comment

begin_decl_stmt
annotation|@
name|Column
DECL|field|nbrPatchSets
specifier|protected
name|int
name|nbrPatchSets
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** The current patch set. */
end_comment

begin_decl_stmt
annotation|@
name|Column
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
DECL|field|subject
specifier|protected
name|String
name|subject
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
DECL|method|Change (final Change.Id newId, final Account.Id ownedBy, final Branch.NameKey forBranch)
specifier|public
name|Change
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|newId
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|ownedBy
parameter_list|,
specifier|final
name|Branch
operator|.
name|NameKey
name|forBranch
parameter_list|)
block|{
name|changeId
operator|=
name|newId
expr_stmt|;
name|createdOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
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
DECL|method|updated ()
specifier|public
name|void
name|updated
parameter_list|()
block|{
name|lastUpdatedOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
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
block|}
end_function

begin_comment
comment|/**    * Allocate a new PatchSet id within this change.    *<p>    *<b>Note: This makes the change dirty. Call update() after.</b>    */
end_comment

begin_function
DECL|method|newPatchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|newPatchSetId
parameter_list|()
block|{
return|return
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
operator|++
name|nbrPatchSets
argument_list|)
return|;
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
DECL|method|setStatus (final Status newStatus)
specifier|public
name|void
name|setStatus
parameter_list|(
specifier|final
name|Status
name|newStatus
parameter_list|)
block|{
name|open
operator|=
name|newStatus
operator|.
name|isOpen
argument_list|()
expr_stmt|;
name|status
operator|=
name|newStatus
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

