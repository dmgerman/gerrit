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
name|client
operator|.
name|AccountGroup
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
name|group
operator|.
name|SystemGroupBackend
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
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|Schema_87
specifier|public
class|class
name|Schema_87
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_87 (Provider<Schema_86> prior)
name|Schema_87
parameter_list|(
name|Provider
argument_list|<
name|Schema_86
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
throws|,
name|SQLException
block|{
try|try
init|(
name|PreparedStatement
name|uuidRetrieval
init|=
name|prepareStatement
argument_list|(
name|db
argument_list|,
literal|"SELECT group_uuid FROM account_groups WHERE group_id = ?"
argument_list|)
init|;
name|PreparedStatement
name|groupDeletion
operator|=
name|prepareStatement
argument_list|(
name|db
argument_list|,
literal|"DELETE FROM account_groups WHERE group_id = ?"
argument_list|)
init|;
name|PreparedStatement
name|groupNameDeletion
operator|=
name|prepareStatement
argument_list|(
name|db
argument_list|,
literal|"DELETE FROM account_group_names WHERE group_id = ?"
argument_list|)
init|)
block|{
for|for
control|(
name|AccountGroup
operator|.
name|Id
name|id
range|:
name|scanSystemGroups
argument_list|(
name|db
argument_list|)
control|)
block|{
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupUuid
init|=
name|getUuid
argument_list|(
name|uuidRetrieval
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupUuid
operator|.
name|filter
argument_list|(
name|SystemGroupBackend
operator|::
name|isSystemGroup
argument_list|)
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|groupDeletion
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|groupDeletion
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
name|groupNameDeletion
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|groupNameDeletion
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getUuid ( PreparedStatement uuidRetrieval, AccountGroup.Id id)
specifier|private
specifier|static
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getUuid
parameter_list|(
name|PreparedStatement
name|uuidRetrieval
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|SQLException
block|{
name|uuidRetrieval
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|ResultSet
name|uuidResults
init|=
name|uuidRetrieval
operator|.
name|executeQuery
argument_list|()
init|)
block|{
if|if
condition|(
name|uuidResults
operator|.
name|first
argument_list|()
condition|)
block|{
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|uuidResults
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|scanSystemGroups (ReviewDb db)
specifier|private
specifier|static
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|scanSystemGroups
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|Statement
name|stmt
init|=
name|newStatement
argument_list|(
name|db
argument_list|)
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT group_id FROM account_groups WHERE group_type = 'SYSTEM'"
argument_list|)
init|)
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|ids
operator|.
name|add
argument_list|(
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ids
return|;
block|}
block|}
block|}
end_class

end_unit
