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
name|StatementExecutor
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
name|Provider
import|;
end_import

begin_comment
comment|/** Migrate draft changes to private or wip changes. */
end_comment

begin_class
DECL|class|Schema_159
specifier|public
class|class
name|Schema_159
extends|extends
name|ReviewDbSchemaVersion
block|{
DECL|enum|DraftWorkflowMigrationStrategy
specifier|private
enum|enum
name|DraftWorkflowMigrationStrategy
block|{
DECL|enumConstant|PRIVATE
name|PRIVATE
block|,
DECL|enumConstant|WORK_IN_PROGRESS
name|WORK_IN_PROGRESS
block|}
annotation|@
name|Inject
DECL|method|Schema_159 (Provider<Schema_158> prior)
name|Schema_159
parameter_list|(
name|Provider
argument_list|<
name|Schema_158
argument_list|>
name|prior
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
block|{
name|DraftWorkflowMigrationStrategy
name|strategy
init|=
name|DraftWorkflowMigrationStrategy
operator|.
name|WORK_IN_PROGRESS
decl_stmt|;
if|if
condition|(
name|ui
operator|.
name|yesno
argument_list|(
literal|false
argument_list|,
literal|"Migrate draft changes to private changes (default is work-in-progress)"
argument_list|)
condition|)
block|{
name|strategy
operator|=
name|DraftWorkflowMigrationStrategy
operator|.
name|PRIVATE
expr_stmt|;
block|}
name|ui
operator|.
name|message
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Replace draft changes with %s changes ..."
argument_list|,
name|strategy
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|StatementExecutor
name|e
init|=
name|newExecutor
argument_list|(
name|db
argument_list|)
init|)
block|{
name|String
name|column
init|=
name|strategy
operator|==
name|DraftWorkflowMigrationStrategy
operator|.
name|PRIVATE
condition|?
literal|"is_private"
else|:
literal|"work_in_progress"
decl_stmt|;
comment|// Mark changes private/WIP and NEW if either:
comment|// * they have status DRAFT
comment|// * they have status NEW and have any draft patch sets
name|e
operator|.
name|execute
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE changes "
operator|+
literal|"SET %s = 'Y', "
operator|+
literal|"    status = 'n', "
operator|+
literal|"    created_on = created_on "
operator|+
literal|"WHERE status = 'd' "
operator|+
literal|"  OR (status = 'n' "
operator|+
literal|"      AND EXISTS "
operator|+
literal|"        (SELECT * "
operator|+
literal|"         FROM patch_sets "
operator|+
literal|"         WHERE patch_sets.change_id = changes.change_id "
operator|+
literal|"           AND patch_sets.draft = 'Y')) "
argument_list|,
name|column
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ui
operator|.
name|message
argument_list|(
literal|"done"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

