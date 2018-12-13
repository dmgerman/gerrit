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
name|DisallowedReviewDb
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
annotation|@
name|Inject
DECL|method|NotesMigrationSchemaFactory ()
name|NotesMigrationSchemaFactory
parameter_list|()
block|{}
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
comment|//    DisallowedReviewDb.
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
comment|//    DisallowReadFromChanges to be a complete no-op, with NoChangesReviewDb. With this
comment|//    stub implementation, all read operations return no results, and write operations silently
comment|//    do nothing. This implementation is not a public class and callers couldn't do anything
comment|//    useful with it even if it were.
comment|// Disable writes to change tables in ReviewDb (ReviewDb access for changes are No-Ops); all
comment|// other table accesses throw runtime exceptions.
name|ReviewDb
name|db
init|=
operator|new
name|NoChangesReviewDb
argument_list|()
decl_stmt|;
comment|// Second create the wrappers which can be removed by ReviewDbUtil#unwrapDb(ReviewDb).
comment|// ReviewDb is no longer supported, make sure that any attempt to read a change from ReviewDb
comment|// anyway fails with an exception.
name|db
operator|=
operator|new
name|DisallowedReviewDb
argument_list|(
name|db
argument_list|)
expr_stmt|;
return|return
name|db
return|;
block|}
block|}
end_class

end_unit

