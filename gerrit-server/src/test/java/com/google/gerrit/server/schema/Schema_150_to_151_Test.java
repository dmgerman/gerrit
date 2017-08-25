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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|TruthJUnit
operator|.
name|assume
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
name|common
operator|.
name|TimeUtil
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
name|api
operator|.
name|groups
operator|.
name|GroupInput
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
name|common
operator|.
name|GroupInfo
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
name|restapi
operator|.
name|TopLevelResource
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
name|client
operator|.
name|AccountGroup
operator|.
name|Id
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
name|CreateGroup
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
name|testutil
operator|.
name|SchemaUpgradeTestEnvironment
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
name|testutil
operator|.
name|TestUpdateUI
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
name|jdbc
operator|.
name|JdbcSchema
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
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Month
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|Schema_150_to_151_Test
specifier|public
class|class
name|Schema_150_to_151_Test
block|{
DECL|field|testEnv
annotation|@
name|Rule
specifier|public
name|SchemaUpgradeTestEnvironment
name|testEnv
init|=
operator|new
name|SchemaUpgradeTestEnvironment
argument_list|()
decl_stmt|;
DECL|field|createGroupFactory
annotation|@
name|Inject
specifier|private
name|CreateGroup
operator|.
name|Factory
name|createGroupFactory
decl_stmt|;
DECL|field|schema151
annotation|@
name|Inject
specifier|private
name|Schema_151
name|schema151
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|field|createdOnRetrieval
specifier|private
name|PreparedStatement
name|createdOnRetrieval
decl_stmt|;
DECL|field|createdOnUpdate
specifier|private
name|PreparedStatement
name|createdOnUpdate
decl_stmt|;
DECL|field|auditEntryDeletion
specifier|private
name|PreparedStatement
name|auditEntryDeletion
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|testEnv
operator|.
name|getInjector
argument_list|()
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|db
operator|=
name|testEnv
operator|.
name|getDb
argument_list|()
expr_stmt|;
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|db
operator|instanceof
name|JdbcSchema
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|connection
operator|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|createdOnRetrieval
operator|=
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT created_on FROM account_groups WHERE group_id = ?"
argument_list|)
expr_stmt|;
name|createdOnUpdate
operator|=
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE account_groups SET created_on = ? WHERE group_id = ?"
argument_list|)
expr_stmt|;
name|auditEntryDeletion
operator|=
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"DELETE FROM account_group_members_audit WHERE group_id = ?"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|auditEntryDeletion
operator|!=
literal|null
condition|)
block|{
name|auditEntryDeletion
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|createdOnUpdate
operator|!=
literal|null
condition|)
block|{
name|createdOnUpdate
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|createdOnRetrieval
operator|!=
literal|null
condition|)
block|{
name|createdOnRetrieval
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|createdOnIsPopulatedForGroupsCreatedAfterAudit ()
specifier|public
name|void
name|createdOnIsPopulatedForGroupsCreatedAfterAudit
parameter_list|()
throws|throws
name|Exception
block|{
name|Timestamp
name|testStartTime
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|createGroup
argument_list|(
literal|"Group for schema migration"
argument_list|)
decl_stmt|;
name|setCreatedOnToVeryOldTimestamp
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|schema151
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|Timestamp
name|createdOn
init|=
name|getCreatedOn
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|createdOn
argument_list|)
operator|.
name|isAtLeast
argument_list|(
name|testStartTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createdOnIsPopulatedForGroupsCreatedBeforeAudit ()
specifier|public
name|void
name|createdOnIsPopulatedForGroupsCreatedBeforeAudit
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|createGroup
argument_list|(
literal|"Ancient group for schema migration"
argument_list|)
decl_stmt|;
name|setCreatedOnToVeryOldTimestamp
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|removeAuditEntriesFor
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|schema151
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|Timestamp
name|createdOn
init|=
name|getCreatedOn
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|createdOn
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|AccountGroup
operator|.
name|auditCreationInstantTs
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createGroup (String name)
specifier|private
name|AccountGroup
operator|.
name|Id
name|createGroup
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|GroupInput
name|groupInput
init|=
operator|new
name|GroupInput
argument_list|()
decl_stmt|;
name|groupInput
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|GroupInfo
name|groupInfo
init|=
name|createGroupFactory
operator|.
name|create
argument_list|(
name|name
argument_list|)
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|groupInput
argument_list|)
decl_stmt|;
return|return
operator|new
name|Id
argument_list|(
name|groupInfo
operator|.
name|groupId
argument_list|)
return|;
block|}
DECL|method|getCreatedOn (Id groupId)
specifier|private
name|Timestamp
name|getCreatedOn
parameter_list|(
name|Id
name|groupId
parameter_list|)
throws|throws
name|Exception
block|{
name|createdOnRetrieval
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
name|results
init|=
name|createdOnRetrieval
operator|.
name|executeQuery
argument_list|()
init|)
block|{
if|if
condition|(
name|results
operator|.
name|first
argument_list|()
condition|)
block|{
return|return
name|results
operator|.
name|getTimestamp
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|setCreatedOnToVeryOldTimestamp (Id groupId)
specifier|private
name|void
name|setCreatedOnToVeryOldTimestamp
parameter_list|(
name|Id
name|groupId
parameter_list|)
throws|throws
name|Exception
block|{
name|createdOnUpdate
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
name|Instant
name|instant
init|=
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|1800
argument_list|,
name|Month
operator|.
name|JANUARY
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
operator|.
name|toInstant
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|createdOnUpdate
operator|.
name|setTimestamp
argument_list|(
literal|1
argument_list|,
name|Timestamp
operator|.
name|from
argument_list|(
name|instant
argument_list|)
argument_list|)
expr_stmt|;
name|createdOnUpdate
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
name|createdOnUpdate
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
DECL|method|removeAuditEntriesFor (AccountGroup.Id groupId)
specifier|private
name|void
name|removeAuditEntriesFor
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|Exception
block|{
name|auditEntryDeletion
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
name|auditEntryDeletion
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

