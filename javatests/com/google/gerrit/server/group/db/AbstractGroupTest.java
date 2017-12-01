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
DECL|package|com.google.gerrit.server.group.db
package|package
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
name|db
package|;
end_package

begin_import
import|import static
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
name|testing
operator|.
name|CommitInfoSubject
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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|CommitInfo
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
name|RefNames
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
name|config
operator|.
name|AllUsersName
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
name|config
operator|.
name|AllUsersNameProvider
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|git
operator|.
name|CommitUtil
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
name|git
operator|.
name|MetaDataUpdate
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
name|testing
operator|.
name|GerritBaseTests
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
name|testing
operator|.
name|InMemoryRepositoryManager
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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|lib
operator|.
name|PersonIdent
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
name|lib
operator|.
name|Ref
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
name|lib
operator|.
name|Repository
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
name|RevWalk
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
name|Ignore
import|;
end_import

begin_class
annotation|@
name|Ignore
DECL|class|AbstractGroupTest
specifier|public
class|class
name|AbstractGroupTest
extends|extends
name|GerritBaseTests
block|{
DECL|field|TZ
specifier|protected
specifier|static
specifier|final
name|TimeZone
name|TZ
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"America/Los_Angeles"
argument_list|)
decl_stmt|;
DECL|field|SERVER_ID
specifier|protected
specifier|static
specifier|final
name|String
name|SERVER_ID
init|=
literal|"server-id"
decl_stmt|;
DECL|field|SERVER_NAME
specifier|protected
specifier|static
specifier|final
name|String
name|SERVER_NAME
init|=
literal|"Gerrit Server"
decl_stmt|;
DECL|field|SERVER_EMAIL
specifier|protected
specifier|static
specifier|final
name|String
name|SERVER_EMAIL
init|=
literal|"noreply@gerritcodereview.com"
decl_stmt|;
DECL|field|SERVER_ACCOUNT_NUMBER
specifier|protected
specifier|static
specifier|final
name|int
name|SERVER_ACCOUNT_NUMBER
init|=
literal|100000
decl_stmt|;
DECL|field|USER_ACCOUNT_NUMBER
specifier|protected
specifier|static
specifier|final
name|int
name|USER_ACCOUNT_NUMBER
init|=
literal|100001
decl_stmt|;
DECL|field|allUsersName
specifier|protected
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|repoManager
specifier|protected
name|InMemoryRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersRepo
specifier|protected
name|Repository
name|allUsersRepo
decl_stmt|;
DECL|field|serverAccountId
specifier|protected
name|Account
operator|.
name|Id
name|serverAccountId
decl_stmt|;
DECL|field|serverIdent
specifier|protected
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|userId
specifier|protected
name|Account
operator|.
name|Id
name|userId
decl_stmt|;
DECL|field|userIdent
specifier|protected
name|PersonIdent
name|userIdent
decl_stmt|;
annotation|@
name|Before
DECL|method|abstractGroupTestSetUp ()
specifier|public
name|void
name|abstractGroupTestSetUp
parameter_list|()
throws|throws
name|Exception
block|{
name|allUsersName
operator|=
operator|new
name|AllUsersName
argument_list|(
name|AllUsersNameProvider
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
name|repoManager
operator|=
operator|new
name|InMemoryRepositoryManager
argument_list|()
expr_stmt|;
name|allUsersRepo
operator|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|allUsersName
argument_list|)
expr_stmt|;
name|serverAccountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|SERVER_ACCOUNT_NUMBER
argument_list|)
expr_stmt|;
name|serverIdent
operator|=
operator|new
name|PersonIdent
argument_list|(
name|SERVER_NAME
argument_list|,
name|SERVER_EMAIL
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|TZ
argument_list|)
expr_stmt|;
name|userId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|USER_ACCOUNT_NUMBER
argument_list|)
expr_stmt|;
name|userIdent
operator|=
name|newPersonIdent
argument_list|(
name|userId
argument_list|,
name|serverIdent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|abstractGroupTestTearDown ()
specifier|public
name|void
name|abstractGroupTestTearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|allUsersRepo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|getTipTimestamp (AccountGroup.UUID uuid)
specifier|protected
name|Timestamp
name|getTipTimestamp
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|allUsersRepo
argument_list|)
init|)
block|{
name|Ref
name|ref
init|=
name|allUsersRepo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|refsGroups
argument_list|(
name|uuid
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|ref
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|Timestamp
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|getAuthorIdent
argument_list|()
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|assertTipCommit ( AccountGroup.UUID uuid, String expectedMessage, String expectedName, String expectedEmail)
specifier|protected
name|void
name|assertTipCommit
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
name|String
name|expectedMessage
parameter_list|,
name|String
name|expectedName
parameter_list|,
name|String
name|expectedEmail
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|allUsersRepo
argument_list|)
init|)
block|{
name|Ref
name|ref
init|=
name|allUsersRepo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|refsGroups
argument_list|(
name|uuid
argument_list|)
argument_list|)
decl_stmt|;
name|assertCommit
argument_list|(
name|CommitUtil
operator|.
name|toCommitInfo
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|,
name|rw
argument_list|)
argument_list|,
name|expectedMessage
argument_list|,
name|expectedName
argument_list|,
name|expectedEmail
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertServerCommit (CommitInfo commitInfo, String expectedMessage)
specifier|protected
specifier|static
name|void
name|assertServerCommit
parameter_list|(
name|CommitInfo
name|commitInfo
parameter_list|,
name|String
name|expectedMessage
parameter_list|)
block|{
name|assertCommit
argument_list|(
name|commitInfo
argument_list|,
name|expectedMessage
argument_list|,
name|SERVER_NAME
argument_list|,
name|SERVER_EMAIL
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCommit ( CommitInfo commitInfo, String expectedMessage, String expectedName, String expectedEmail)
specifier|protected
specifier|static
name|void
name|assertCommit
parameter_list|(
name|CommitInfo
name|commitInfo
parameter_list|,
name|String
name|expectedMessage
parameter_list|,
name|String
name|expectedName
parameter_list|,
name|String
name|expectedEmail
parameter_list|)
block|{
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|expectedMessage
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|expectedName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|expectedEmail
argument_list|)
expr_stmt|;
comment|// Committer should always be the server, regardless of author.
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|SERVER_NAME
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|SERVER_EMAIL
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|date
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|commitInfo
operator|.
name|author
operator|.
name|date
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commitInfo
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|tz
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|commitInfo
operator|.
name|author
operator|.
name|tz
argument_list|)
expr_stmt|;
block|}
DECL|method|createMetaDataUpdate (PersonIdent authorIdent)
specifier|protected
name|MetaDataUpdate
name|createMetaDataUpdate
parameter_list|(
name|PersonIdent
name|authorIdent
parameter_list|)
block|{
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allUsersName
argument_list|,
name|allUsersRepo
argument_list|)
decl_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|authorIdent
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|serverIdent
argument_list|)
expr_stmt|;
comment|// Committer is always the server identity.
return|return
name|md
return|;
block|}
DECL|method|newPersonIdent ()
specifier|protected
specifier|static
name|PersonIdent
name|newPersonIdent
parameter_list|()
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|SERVER_NAME
argument_list|,
name|SERVER_EMAIL
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|TZ
argument_list|)
return|;
block|}
DECL|method|newPersonIdent (Account.Id id, PersonIdent ident)
specifier|protected
specifier|static
name|PersonIdent
name|newPersonIdent
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|PersonIdent
name|ident
parameter_list|)
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|getAccountName
argument_list|(
name|id
argument_list|)
argument_list|,
name|getAccountEmail
argument_list|(
name|id
argument_list|)
argument_list|,
name|ident
operator|.
name|getWhen
argument_list|()
argument_list|,
name|ident
operator|.
name|getTimeZone
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getAuditLogFormatter ()
specifier|protected
specifier|static
name|AuditLogFormatter
name|getAuditLogFormatter
parameter_list|()
block|{
return|return
operator|new
name|AuditLogFormatter
argument_list|(
name|AbstractGroupTest
operator|::
name|getAccount
argument_list|,
name|AbstractGroupTest
operator|::
name|getGroup
argument_list|,
name|SERVER_ID
argument_list|)
return|;
block|}
DECL|method|getAccount (Account.Id id)
specifier|private
specifier|static
name|Optional
argument_list|<
name|Account
argument_list|>
name|getAccount
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|Account
name|account
init|=
operator|new
name|Account
argument_list|(
name|id
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|account
operator|.
name|setFullName
argument_list|(
literal|"Account "
operator|+
name|id
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
name|account
argument_list|)
return|;
block|}
DECL|method|getGroup (AccountGroup.UUID uuid)
specifier|private
specifier|static
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|getGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|GroupDescription
operator|.
name|Basic
name|group
init|=
operator|new
name|GroupDescription
operator|.
name|Basic
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Group"
return|;
block|}
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
name|group
argument_list|)
return|;
block|}
DECL|method|getAccountName (Account.Id id)
specifier|protected
specifier|static
name|String
name|getAccountName
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
literal|"Account "
operator|+
name|id
return|;
block|}
DECL|method|getAccountEmail (Account.Id id)
specifier|protected
specifier|static
name|String
name|getAccountEmail
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s@%s"
argument_list|,
name|id
argument_list|,
name|SERVER_ID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

