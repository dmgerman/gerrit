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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|server
operator|.
name|DisallowReadFromChangesReviewDbWrapper
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
name|DisallowReadFromGroupsReviewDbWrapper
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
name|GroupsMigration
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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

begin_class
annotation|@
name|Singleton
DECL|class|NotesMigrationSchemaFactory
specifier|public
class|class
name|NotesMigrationSchemaFactory
implements|implements
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|delegate
decl_stmt|;
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|field|groupsMigration
specifier|private
specifier|final
name|GroupsMigration
name|groupsMigration
decl_stmt|;
annotation|@
name|Inject
DECL|method|NotesMigrationSchemaFactory ( @eviewDbFactory SchemaFactory<ReviewDb> delegate, NotesMigration migration, GroupsMigration groupsMigration)
name|NotesMigrationSchemaFactory
parameter_list|(
annotation|@
name|ReviewDbFactory
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|delegate
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|GroupsMigration
name|groupsMigration
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
name|this
operator|.
name|groupsMigration
operator|=
name|groupsMigration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|open ()
specifier|public
name|ReviewDb
name|open
parameter_list|()
throws|throws
name|OrmException
block|{
comment|// There are two levels at which this class disables access to Changes and related tables,
comment|// corresponding to two phases of the NoteDb migration:
comment|//
comment|// 1. When changes are read from NoteDb but some changes might still have their primary storage
comment|//    in ReviewDb, it is generally programmer error to read changes from ReviewDb. However,
comment|//    since ReviewDb is still the primary storage for most or all changes, we still need to
comment|//    support writing to ReviewDb. This behavior is accomplished by wrapping in a
comment|//    DisallowReadFromChangesReviewDbWrapper.
comment|//
comment|//    Some codepaths might need to be able to read from ReviewDb if they really need to,
comment|//    because they need to operate on the underlying source of truth, for example when reading
comment|//    a change to determine its primary storage. To support this, ReviewDbUtil#unwrapDb can
comment|//    detect and unwrap databases of this type.
comment|//
comment|// 2. After all changes have their primary storage in NoteDb, we can completely shut off access
comment|//    to the change tables. At this point in the migration, we are by definition not using the
comment|//    ReviewDb tables at all; we could even delete the tables at this point, and Gerrit would
comment|//    continue to function.
comment|//
comment|//    This is accomplished by setting the delegate ReviewDb *underneath*
comment|//    DisallowReadFromChanges to be a complete no-op, with NoChangesReviewDbWrapper. With this
comment|//    wrapper, all read operations return no results, and write operations silently do nothing.
comment|//    This wrapper is not a public class and nobody should ever attempt to unwrap it.
comment|// First create the wrappers which can not be removed by ReviewDbUtil#unwrapDb(ReviewDb).
name|ReviewDb
name|db
init|=
name|delegate
operator|.
name|open
argument_list|()
decl_stmt|;
if|if
condition|(
name|migration
operator|.
name|readChanges
argument_list|()
operator|&&
name|migration
operator|.
name|disableChangeReviewDb
argument_list|()
condition|)
block|{
comment|// Disable writes to change tables in ReviewDb (ReviewDb access for changes are No-Ops).
name|db
operator|=
operator|new
name|NoChangesReviewDbWrapper
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupsMigration
operator|.
name|readFromNoteDb
argument_list|()
operator|&&
name|groupsMigration
operator|.
name|disableGroupReviewDb
argument_list|()
condition|)
block|{
comment|// Disable writes to group tables in ReviewDb (ReviewDb access for groups are No-Ops).
name|db
operator|=
operator|new
name|NoGroupsReviewDbWrapper
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
comment|// Second create the wrappers which can be removed by ReviewDbUtil#unwrapDb(ReviewDb).
if|if
condition|(
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
comment|// If reading changes from NoteDb is configured, changes should not be read from ReviewDb.
comment|// Make sure that any attempt to read a change from ReviewDb anyway fails with an exception.
name|db
operator|=
operator|new
name|DisallowReadFromChangesReviewDbWrapper
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupsMigration
operator|.
name|readFromNoteDb
argument_list|()
condition|)
block|{
comment|// If reading groups from NoteDb is configured, groups should not be read from ReviewDb.
comment|// Make sure that any attempt to read a group from ReviewDb anyway fails with an exception.
name|db
operator|=
operator|new
name|DisallowReadFromGroupsReviewDbWrapper
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
return|return
name|db
return|;
block|}
block|}
end_class

end_unit

