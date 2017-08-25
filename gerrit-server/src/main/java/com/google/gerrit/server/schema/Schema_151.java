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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/** A schema which adds the 'created on' field to groups. */
end_comment

begin_class
DECL|class|Schema_151
specifier|public
class|class
name|Schema_151
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_151 (Provider<Schema_150> prior)
specifier|protected
name|Schema_151
parameter_list|(
name|Provider
argument_list|<
name|Schema_150
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
name|groupUpdate
init|=
name|prepareStatement
argument_list|(
name|db
argument_list|,
literal|"UPDATE account_groups SET created_on = ? WHERE group_id = ?"
argument_list|)
init|;
name|PreparedStatement
name|addedOnRetrieval
operator|=
name|prepareStatement
argument_list|(
name|db
argument_list|,
literal|"SELECT added_on FROM account_group_members_audit WHERE group_id = ?"
operator|+
literal|" ORDER BY added_on ASC"
argument_list|)
init|)
block|{
name|List
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|accountGroups
init|=
name|getAllGroupIds
argument_list|(
name|db
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|Id
name|groupId
range|:
name|accountGroups
control|)
block|{
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|firstTimeMentioned
init|=
name|getFirstTimeMentioned
argument_list|(
name|addedOnRetrieval
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|Timestamp
name|createdOn
init|=
name|firstTimeMentioned
operator|.
name|orElseGet
argument_list|(
name|AccountGroup
operator|::
name|auditCreationInstantTs
argument_list|)
decl_stmt|;
name|groupUpdate
operator|.
name|setTimestamp
argument_list|(
literal|1
argument_list|,
name|createdOn
argument_list|)
expr_stmt|;
name|groupUpdate
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|groupUpdate
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getFirstTimeMentioned ( PreparedStatement addedOnRetrieval, AccountGroup.Id groupId)
specifier|private
specifier|static
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|getFirstTimeMentioned
parameter_list|(
name|PreparedStatement
name|addedOnRetrieval
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|SQLException
block|{
name|addedOnRetrieval
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|ResultSet
name|resultSet
init|=
name|addedOnRetrieval
operator|.
name|executeQuery
argument_list|()
init|)
block|{
if|if
condition|(
name|resultSet
operator|.
name|first
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|resultSet
operator|.
name|getTimestamp
argument_list|(
literal|1
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|getAllGroupIds (ReviewDb db)
specifier|private
specifier|static
name|List
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getAllGroupIds
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
literal|"SELECT group_id FROM account_groups"
argument_list|)
init|)
block|{
name|List
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groupIds
init|=
operator|new
name|ArrayList
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
name|groupIds
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
name|groupIds
return|;
block|}
block|}
block|}
end_class

end_unit

