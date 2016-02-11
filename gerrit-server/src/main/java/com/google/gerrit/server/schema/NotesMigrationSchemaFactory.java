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
annotation|@
name|Inject
DECL|method|NotesMigrationSchemaFactory ( @eviewDbFactory SchemaFactory<ReviewDb> delegate, NotesMigration migration)
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
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|db
return|;
block|}
return|return
operator|new
name|DisabledChangesReviewDbWrapper
argument_list|(
name|db
argument_list|)
return|;
block|}
block|}
end_class

end_unit

