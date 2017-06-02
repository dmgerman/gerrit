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
name|lifecycle
operator|.
name|LifecycleManager
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
name|client
operator|.
name|AccountGroupMemberAudit
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
name|CurrentUser
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
name|IdentifiedUser
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
name|account
operator|.
name|AccountManager
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
name|account
operator|.
name|AuthRequest
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
name|server
operator|.
name|util
operator|.
name|RequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|InMemoryDatabase
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
name|InMemoryModule
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
name|ResultSet
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
name|Guice
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
name|Injector
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|util
operator|.
name|Providers
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
name|java
operator|.
name|util
operator|.
name|List
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
name|Test
import|;
end_import

begin_class
DECL|class|Schema_150_to_151_Test
specifier|public
class|class
name|Schema_150_to_151_Test
block|{
DECL|field|accountManager
annotation|@
name|Inject
specifier|private
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|userFactory
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|schemaFactory
annotation|@
name|Inject
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|schemaCreator
annotation|@
name|Inject
specifier|private
name|SchemaCreator
name|schemaCreator
decl_stmt|;
DECL|field|requestContext
annotation|@
name|Inject
specifier|private
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|schema151
annotation|@
name|Inject
specifier|private
name|Schema_151
name|schema151
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
comment|// Only for use in setting up/tearing down injector.
DECL|field|inMemoryDatabase
annotation|@
name|Inject
specifier|private
name|InMemoryDatabase
name|inMemoryDatabase
decl_stmt|;
DECL|field|lifecycle
specifier|private
name|LifecycleManager
name|lifecycle
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
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
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|InMemoryModule
argument_list|()
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|lifecycle
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
name|lifecycle
operator|.
name|add
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|lifecycle
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
init|(
name|ReviewDb
name|underlyingDb
init|=
name|inMemoryDatabase
operator|.
name|getDatabase
argument_list|()
operator|.
name|open
argument_list|()
init|)
block|{
name|schemaCreator
operator|.
name|create
argument_list|(
name|underlyingDb
argument_list|)
expr_stmt|;
block|}
name|db
operator|=
name|schemaFactory
operator|.
name|open
argument_list|()
expr_stmt|;
name|Account
operator|.
name|Id
name|userId
init|=
name|accountManager
operator|.
name|authenticate
argument_list|(
name|AuthRequest
operator|.
name|forUser
argument_list|(
literal|"user"
argument_list|)
argument_list|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|IdentifiedUser
name|user
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|userId
argument_list|)
decl_stmt|;
name|requestContext
operator|.
name|setContext
argument_list|(
operator|new
name|RequestContext
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
annotation|@
name|Override
specifier|public
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|getReviewDbProvider
parameter_list|()
block|{
return|return
name|Providers
operator|.
name|of
argument_list|(
name|db
argument_list|)
return|;
block|}
block|}
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
block|{
if|if
condition|(
name|lifecycle
operator|!=
literal|null
condition|)
block|{
name|lifecycle
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|requestContext
operator|.
name|setContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|InMemoryDatabase
operator|.
name|drop
argument_list|(
name|inMemoryDatabase
argument_list|)
expr_stmt|;
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
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|group
operator|.
name|getCreatedOn
argument_list|()
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
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|group
operator|.
name|getCreatedOn
argument_list|()
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
DECL|method|setCreatedOnToVeryOldTimestamp (Id groupId)
specifier|private
name|void
name|setCreatedOnToVeryOldTimestamp
parameter_list|(
name|Id
name|groupId
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
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
name|group
operator|.
name|setCreatedOn
argument_list|(
name|Timestamp
operator|.
name|from
argument_list|(
name|instant
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|group
argument_list|)
argument_list|)
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
name|ResultSet
argument_list|<
name|AccountGroupMemberAudit
argument_list|>
name|groupMemberAudits
init|=
name|db
operator|.
name|accountGroupMembersAudit
argument_list|()
operator|.
name|byGroup
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|db
operator|.
name|accountGroupMembersAudit
argument_list|()
operator|.
name|delete
argument_list|(
name|groupMemberAudits
argument_list|)
expr_stmt|;
block|}
DECL|class|TestUpdateUI
specifier|private
specifier|static
class|class
name|TestUpdateUI
implements|implements
name|UpdateUI
block|{
annotation|@
name|Override
DECL|method|message (String msg)
specifier|public
name|void
name|message
parameter_list|(
name|String
name|msg
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|yesno (boolean def, String msg)
specifier|public
name|boolean
name|yesno
parameter_list|(
name|boolean
name|def
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isBatch ()
specifier|public
name|boolean
name|isBatch
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|pruneSchema (StatementExecutor e, List<String> pruneList)
specifier|public
name|void
name|pruneSchema
parameter_list|(
name|StatementExecutor
name|e
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|pruneList
parameter_list|)
throws|throws
name|OrmException
block|{}
block|}
block|}
end_class

end_unit

