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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|Collections
import|;
end_import

begin_comment
comment|/** Utilities for manipulating patch sets. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PatchSetUtil
specifier|public
class|class
name|PatchSetUtil
block|{
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
annotation|@
name|Inject
DECL|method|PatchSetUtil (NotesMigration migration)
name|PatchSetUtil
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
DECL|method|current (ReviewDb db, ChangeNotes notes)
specifier|public
name|PatchSet
name|current
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|get
argument_list|(
name|db
argument_list|,
name|notes
argument_list|,
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// TODO(dborowitz): Read from notedb.
DECL|method|get (ReviewDb db, ChangeNotes notes, PatchSet.Id psId)
specifier|public
name|PatchSet
name|get
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
return|return
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
return|;
block|}
DECL|method|byChange (ReviewDb db, ChangeNotes notes)
specifier|public
name|ImmutableList
argument_list|<
name|PatchSet
argument_list|>
name|byChange
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|ChangeUtil
operator|.
name|PS_ID_ORDER
operator|.
name|immutableSortedCopy
argument_list|(
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|insert (ReviewDb db, ChangeUpdate update, PatchSet.Id psId, RevCommit commit, boolean draft, Iterable<String> groups, String pushCertificate)
specifier|public
name|PatchSet
name|insert
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|RevCommit
name|commit
parameter_list|,
name|boolean
name|draft
parameter_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|,
name|String
name|pushCertificate
parameter_list|)
throws|throws
name|OrmException
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|update
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|equals
argument_list|(
name|changeId
argument_list|)
argument_list|,
literal|"cannot insert patch set %s on change %s"
argument_list|,
name|psId
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
if|if
condition|(
name|update
operator|.
name|getPatchSetId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
name|update
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|psId
argument_list|)
argument_list|,
literal|"cannot insert patch set %s on update for %s"
argument_list|,
name|psId
argument_list|,
name|update
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|update
operator|.
name|setPatchSetId
argument_list|(
name|psId
argument_list|)
expr_stmt|;
block|}
name|PatchSet
name|ps
init|=
operator|new
name|PatchSet
argument_list|(
name|psId
argument_list|)
decl_stmt|;
name|ps
operator|.
name|setRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|commit
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setUploader
argument_list|(
name|update
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setCreatedOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|update
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setDraft
argument_list|(
name|draft
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setGroups
argument_list|(
name|groups
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setPushCertificate
argument_list|(
name|pushCertificate
argument_list|)
expr_stmt|;
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|ps
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|update
operator|.
name|getChange
argument_list|()
operator|.
name|getSubject
argument_list|()
operator|.
name|equals
argument_list|(
name|commit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
condition|)
block|{
name|update
operator|.
name|setSubject
argument_list|(
name|commit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|migration
operator|.
name|writeChanges
argument_list|()
condition|)
block|{
comment|// TODO(dborowitz): Write to notedb.
block|}
return|return
name|ps
return|;
block|}
block|}
end_class

end_unit

