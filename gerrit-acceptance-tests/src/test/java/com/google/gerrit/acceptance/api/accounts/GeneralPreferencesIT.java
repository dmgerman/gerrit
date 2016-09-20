begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|accounts
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
name|gerrit
operator|.
name|acceptance
operator|.
name|AssertUtil
operator|.
name|assertPrefs
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|NoHttpd
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
name|acceptance
operator|.
name|TestAccount
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
name|client
operator|.
name|GeneralPreferencesInfo
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DateFormat
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DefaultBase
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DiffView
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DownloadCommand
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|EmailStrategy
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|ReviewCategoryStrategy
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|TimeFormat
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
name|client
operator|.
name|MenuItem
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
name|inject
operator|.
name|Inject
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
name|RefUpdate
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
name|HashMap
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|GeneralPreferencesIT
specifier|public
class|class
name|GeneralPreferencesIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Inject
DECL|field|allUsers
specifier|private
name|AllUsersName
name|allUsers
decl_stmt|;
DECL|field|user42
specifier|private
name|TestAccount
name|user42
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
name|String
name|name
init|=
name|name
argument_list|(
literal|"user42"
argument_list|)
decl_stmt|;
name|user42
operator|=
name|accounts
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|name
operator|+
literal|"@example.com"
argument_list|,
literal|"User 42"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
throws|throws
name|Exception
block|{
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|user42
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|setPreferences
argument_list|(
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsers
argument_list|)
init|)
block|{
if|if
condition|(
name|git
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|REFS_USERS_DEFAULT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|RefUpdate
name|u
init|=
name|git
operator|.
name|updateRef
argument_list|(
name|RefNames
operator|.
name|REFS_USERS_DEFAULT
argument_list|)
decl_stmt|;
name|u
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|delete
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|RefUpdate
operator|.
name|Result
operator|.
name|FORCED
argument_list|)
expr_stmt|;
block|}
block|}
name|accountCache
operator|.
name|evictAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getAndSetPreferences ()
specifier|public
name|void
name|getAndSetPreferences
parameter_list|()
throws|throws
name|Exception
block|{
name|GeneralPreferencesInfo
name|o
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|user42
operator|.
name|id
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
name|assertPrefs
argument_list|(
name|o
argument_list|,
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|,
literal|"my"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o
operator|.
name|my
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|GeneralPreferencesInfo
name|i
init|=
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
comment|// change all default values
name|i
operator|.
name|changesPerPage
operator|*=
operator|-
literal|1
expr_stmt|;
name|i
operator|.
name|showSiteHeader
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|useFlashClipboard
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|downloadCommand
operator|=
name|DownloadCommand
operator|.
name|REPO_DOWNLOAD
expr_stmt|;
name|i
operator|.
name|dateFormat
operator|=
name|DateFormat
operator|.
name|US
expr_stmt|;
name|i
operator|.
name|timeFormat
operator|=
name|TimeFormat
operator|.
name|HHMM_24
expr_stmt|;
name|i
operator|.
name|emailStrategy
operator|=
name|EmailStrategy
operator|.
name|DISABLED
expr_stmt|;
name|i
operator|.
name|defaultBaseForMerges
operator|=
name|DefaultBase
operator|.
name|AUTO_MERGE
expr_stmt|;
name|i
operator|.
name|highlightAssigneeInChangeTable
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|relativeDateInChangeTable
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|sizeBarInChangeTable
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|legacycidInChangeTable
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|muteCommonPathPrefixes
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|signedOffBy
operator|^=
literal|true
expr_stmt|;
name|i
operator|.
name|reviewCategoryStrategy
operator|=
name|ReviewCategoryStrategy
operator|.
name|ABBREV
expr_stmt|;
name|i
operator|.
name|diffView
operator|=
name|DiffView
operator|.
name|UNIFIED_DIFF
expr_stmt|;
name|i
operator|.
name|my
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|i
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"name"
argument_list|,
literal|"url"
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|.
name|urlAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|i
operator|.
name|urlAliases
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|o
operator|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|user42
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|setPreferences
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertPrefs
argument_list|(
name|o
argument_list|,
name|i
argument_list|,
literal|"my"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o
operator|.
name|my
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getPreferencesWithConfiguredDefaults ()
specifier|public
name|void
name|getPreferencesWithConfiguredDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|GeneralPreferencesInfo
name|d
init|=
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
name|int
name|newChangesPerPage
init|=
name|d
operator|.
name|changesPerPage
operator|*
literal|2
decl_stmt|;
name|GeneralPreferencesInfo
name|update
init|=
operator|new
name|GeneralPreferencesInfo
argument_list|()
decl_stmt|;
name|update
operator|.
name|changesPerPage
operator|=
name|newChangesPerPage
expr_stmt|;
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|setDefaultPreferences
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|GeneralPreferencesInfo
name|o
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|user42
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
comment|// assert configured defaults
name|assertThat
argument_list|(
name|o
operator|.
name|changesPerPage
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newChangesPerPage
argument_list|)
expr_stmt|;
comment|// assert hard-coded defaults
name|assertPrefs
argument_list|(
name|o
argument_list|,
name|d
argument_list|,
literal|"my"
argument_list|,
literal|"changesPerPage"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

