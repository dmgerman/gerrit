begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|ImmutableMap
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
name|Maps
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
name|Sets
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
name|Account
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
name|ChangeMessage
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
name|client
operator|.
name|PatchSetApproval
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
name|TrackingId
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
name|ChangeUtil
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
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
name|query
operator|.
name|change
operator|.
name|ChangeStatusPredicate
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Fields indexed on change documents.  *<p>  * Each field corresponds to both a field name supported by  * {@link ChangeQueryBuilder} for querying that field, and a method on  * {@link ChangeData} used for populating the corresponding document fields in  * the secondary index.  *<p>  * Used to generate a schema for index implementations that require one.  */
end_comment

begin_class
DECL|class|ChangeField
specifier|public
class|class
name|ChangeField
block|{
comment|/** Increment whenever making schema changes. */
DECL|field|SCHEMA_VERSION
specifier|public
specifier|static
specifier|final
name|int
name|SCHEMA_VERSION
init|=
literal|15
decl_stmt|;
comment|/** Legacy change ID. */
DECL|field|LEGACY_ID
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Integer
argument_list|>
name|LEGACY_ID
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|Integer
argument_list|>
argument_list|(
literal|"_id"
argument_list|,
name|FieldType
operator|.
name|INTEGER
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
block|{
return|return
name|input
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Newer style Change-Id key. */
DECL|field|ID
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|ID
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
literal|"change_id"
argument_list|,
name|FieldType
operator|.
name|PREFIX
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Change status string, in the same format as {@code status:}. */
DECL|field|STATUS
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|STATUS
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_STATUS
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|ChangeStatusPredicate
operator|.
name|VALUES
operator|.
name|get
argument_list|(
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getStatus
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|/** Project containing the change. */
DECL|field|PROJECT
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|PROJECT
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_PROJECT
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Reference (aka branch) the change will submit onto. */
DECL|field|REF
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|REF
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_REF
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Topic, a short annotation on the branch. */
DECL|field|TOPIC
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|TOPIC
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_TOPIC
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getTopic
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Last update time since January 1, 1970. */
DECL|field|UPDATED
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Timestamp
argument_list|>
name|UPDATED
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|Timestamp
argument_list|>
argument_list|(
literal|"updated"
argument_list|,
name|FieldType
operator|.
name|TIMESTAMP
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Timestamp
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getLastUpdatedOn
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Sort key field, duplicates {@link #UPDATED}. */
annotation|@
name|Deprecated
DECL|field|SORTKEY
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Long
argument_list|>
name|SORTKEY
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|Long
argument_list|>
argument_list|(
literal|"sortkey"
argument_list|,
name|FieldType
operator|.
name|LONG
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Long
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|ChangeUtil
operator|.
name|parseSortKey
argument_list|(
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getSortKey
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|/** List of filenames modified in the current patch set. */
DECL|field|FILE
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|FILE
init|=
operator|new
name|FieldDef
operator|.
name|Repeatable
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_FILE
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|currentFilePaths
argument_list|(
name|args
operator|.
name|db
argument_list|,
name|args
operator|.
name|patchListCache
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|/** Owner/creator of the change. */
DECL|field|OWNER
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Integer
argument_list|>
name|OWNER
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_OWNER
argument_list|,
name|FieldType
operator|.
name|INTEGER
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|input
operator|.
name|change
argument_list|(
name|args
operator|.
name|db
argument_list|)
operator|.
name|getOwner
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** Reviewer(s) associated with the change. */
DECL|field|REVIEWER
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Iterable
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|REVIEWER
init|=
operator|new
name|FieldDef
operator|.
name|Repeatable
argument_list|<
name|ChangeData
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_REVIEWER
argument_list|,
name|FieldType
operator|.
name|INTEGER
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|input
operator|.
name|allApprovals
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|a
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
decl_stmt|;
comment|/** Commit id of any PatchSet on the change */
DECL|field|COMMIT
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|COMMIT
init|=
operator|new
name|FieldDef
operator|.
name|Repeatable
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_COMMIT
argument_list|,
name|FieldType
operator|.
name|PREFIX
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|revisions
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
name|ps
range|:
name|input
operator|.
name|patches
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
name|revisions
operator|.
name|add
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|revisions
return|;
block|}
block|}
decl_stmt|;
comment|/** Tracking id extracted from a footer. */
DECL|field|TR
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|TR
init|=
operator|new
name|FieldDef
operator|.
name|Repeatable
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_TR
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|TrackingId
name|id
range|:
name|input
operator|.
name|trackingIds
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|id
operator|.
name|getTrackingId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
decl_stmt|;
comment|/** List of labels on the current patch set. */
DECL|field|LABEL
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|LABEL
init|=
operator|new
name|FieldDef
operator|.
name|Repeatable
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_LABEL
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|allApprovals
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|distinctApprovals
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|input
operator|.
name|currentApprovals
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|allApprovals
operator|.
name|add
argument_list|(
name|formatLabel
argument_list|(
name|a
operator|.
name|getLabel
argument_list|()
argument_list|,
name|a
operator|.
name|getValue
argument_list|()
argument_list|,
name|a
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|distinctApprovals
operator|.
name|add
argument_list|(
name|formatLabel
argument_list|(
name|a
operator|.
name|getLabel
argument_list|()
argument_list|,
name|a
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|allApprovals
operator|.
name|addAll
argument_list|(
name|distinctApprovals
argument_list|)
expr_stmt|;
return|return
name|allApprovals
return|;
block|}
block|}
decl_stmt|;
comment|/** Set true if the change has a non-zero label score. */
DECL|field|REVIEWED
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|REVIEWED
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
literal|"reviewed"
argument_list|,
name|FieldType
operator|.
name|EXACT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|input
operator|.
name|currentApprovals
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
condition|)
block|{
return|return
literal|"1"
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
DECL|method|formatLabel (String label, int value)
specifier|public
specifier|static
name|String
name|formatLabel
parameter_list|(
name|String
name|label
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|formatLabel
argument_list|(
name|label
argument_list|,
name|value
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|formatLabel (String label, int value, Account.Id accountId)
specifier|public
specifier|static
name|String
name|formatLabel
parameter_list|(
name|String
name|label
parameter_list|,
name|int
name|value
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|label
operator|.
name|toLowerCase
argument_list|()
operator|+
operator|(
name|value
operator|>=
literal|0
condition|?
literal|"+"
else|:
literal|""
operator|)
operator|+
name|value
operator|+
operator|(
name|accountId
operator|!=
literal|null
condition|?
literal|","
operator|+
name|accountId
operator|.
name|get
argument_list|()
else|:
literal|""
operator|)
return|;
block|}
comment|/** Commit message of the current patch set. */
DECL|field|COMMIT_MESSAGE
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
name|COMMIT_MESSAGE
init|=
operator|new
name|FieldDef
operator|.
name|Single
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_MESSAGE
argument_list|,
name|FieldType
operator|.
name|FULL_TEXT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
block|{
return|return
name|input
operator|.
name|commitMessage
argument_list|(
name|args
operator|.
name|repoManager
argument_list|,
name|args
operator|.
name|db
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
comment|/** Summary or inline comment. */
DECL|field|COMMENT
specifier|public
specifier|static
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|COMMENT
init|=
operator|new
name|FieldDef
operator|.
name|Repeatable
argument_list|<
name|ChangeData
argument_list|,
name|String
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_COMMENT
argument_list|,
name|FieldType
operator|.
name|FULL_TEXT
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|get
parameter_list|(
name|ChangeData
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchLineComment
name|c
range|:
name|input
operator|.
name|comments
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|c
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ChangeMessage
name|m
range|:
name|input
operator|.
name|messages
argument_list|(
name|args
operator|.
name|db
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|m
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
decl_stmt|;
DECL|field|ALL
specifier|public
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
argument_list|>
name|ALL
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
argument_list|>
name|fields
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|ChangeField
operator|.
name|class
operator|.
name|getFields
argument_list|()
control|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isPublic
argument_list|(
name|f
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|Modifier
operator|.
name|isStatic
argument_list|(
name|f
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|Modifier
operator|.
name|isFinal
argument_list|(
name|f
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|FieldDef
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|ParameterizedType
name|t
init|=
operator|(
name|ParameterizedType
operator|)
name|f
operator|.
name|getGenericType
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
operator|==
name|ChangeData
operator|.
name|class
condition|)
block|{
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|fd
init|=
operator|(
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
operator|)
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|fields
operator|.
name|put
argument_list|(
name|fd
operator|.
name|getName
argument_list|()
argument_list|,
name|fd
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExceptionInInitializerError
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExceptionInInitializerError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|ExceptionInInitializerError
argument_list|(
literal|"non-ChangeData ChangeField: "
operator|+
name|f
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|fields
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExceptionInInitializerError
argument_list|(
literal|"no ChangeFields found"
argument_list|)
throw|;
block|}
name|ALL
operator|=
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|fields
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

