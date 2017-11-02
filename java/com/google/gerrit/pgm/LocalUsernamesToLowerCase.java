begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_GERRIT
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
name|server
operator|.
name|schema
operator|.
name|DataSourceProvider
operator|.
name|Context
operator|.
name|MULTI_USER
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
name|pgm
operator|.
name|util
operator|.
name|SiteProgram
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
name|externalids
operator|.
name|DisabledExternalIdCache
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
name|externalids
operator|.
name|ExternalId
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
name|externalids
operator|.
name|ExternalIds
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
name|externalids
operator|.
name|ExternalIdsBatchUpdate
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
name|index
operator|.
name|account
operator|.
name|AccountSchemaDefinitions
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
name|schema
operator|.
name|SchemaVersionCheck
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
name|AbstractModule
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|ProgressMonitor
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
name|TextProgressMonitor
import|;
end_import

begin_comment
comment|/** Converts the local username for all accounts to lower case */
end_comment

begin_class
DECL|class|LocalUsernamesToLowerCase
specifier|public
class|class
name|LocalUsernamesToLowerCase
extends|extends
name|SiteProgram
block|{
DECL|field|manager
specifier|private
specifier|final
name|LifecycleManager
name|manager
init|=
operator|new
name|LifecycleManager
argument_list|()
decl_stmt|;
DECL|field|monitor
specifier|private
specifier|final
name|TextProgressMonitor
name|monitor
init|=
operator|new
name|TextProgressMonitor
argument_list|()
decl_stmt|;
DECL|field|externalIds
annotation|@
name|Inject
specifier|private
name|ExternalIds
name|externalIds
decl_stmt|;
DECL|field|externalIdsBatchUpdate
annotation|@
name|Inject
specifier|private
name|ExternalIdsBatchUpdate
name|externalIdsBatchUpdate
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|dbInjector
init|=
name|createDbInjector
argument_list|(
name|MULTI_USER
argument_list|)
decl_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|dbInjector
argument_list|,
name|dbInjector
operator|.
name|createChildInjector
argument_list|(
name|SchemaVersionCheck
operator|.
name|module
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
name|dbInjector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
comment|// The LocalUsernamesToLowerCase program needs to access all external IDs only
comment|// once to update them. After the update they are not accessed again. Hence the
comment|// LocalUsernamesToLowerCase program doesn't benefit from caching external IDs and
comment|// the external ID cache can be disabled.
name|install
argument_list|(
name|DisabledExternalIdCache
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|todo
init|=
name|externalIds
operator|.
name|all
argument_list|()
decl_stmt|;
name|monitor
operator|.
name|beginTask
argument_list|(
literal|"Converting local usernames"
argument_list|,
name|todo
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ExternalId
name|extId
range|:
name|todo
control|)
block|{
name|convertLocalUserToLowerCase
argument_list|(
name|extId
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|externalIdsBatchUpdate
operator|.
name|commit
argument_list|(
literal|"Convert local usernames to lower case"
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|endTask
argument_list|()
expr_stmt|;
name|int
name|exitCode
init|=
name|reindexAccounts
argument_list|()
decl_stmt|;
name|manager
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|exitCode
return|;
block|}
DECL|method|convertLocalUserToLowerCase (ExternalId extId)
specifier|private
name|void
name|convertLocalUserToLowerCase
parameter_list|(
name|ExternalId
name|extId
parameter_list|)
block|{
if|if
condition|(
name|extId
operator|.
name|isScheme
argument_list|(
name|SCHEME_GERRIT
argument_list|)
condition|)
block|{
name|String
name|localUser
init|=
name|extId
operator|.
name|key
argument_list|()
operator|.
name|id
argument_list|()
decl_stmt|;
name|String
name|localUserLowerCase
init|=
name|localUser
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|localUser
operator|.
name|equals
argument_list|(
name|localUserLowerCase
argument_list|)
condition|)
block|{
name|ExternalId
name|extIdLowerCase
init|=
name|ExternalId
operator|.
name|create
argument_list|(
name|SCHEME_GERRIT
argument_list|,
name|localUserLowerCase
argument_list|,
name|extId
operator|.
name|accountId
argument_list|()
argument_list|,
name|extId
operator|.
name|email
argument_list|()
argument_list|,
name|extId
operator|.
name|password
argument_list|()
argument_list|)
decl_stmt|;
name|externalIdsBatchUpdate
operator|.
name|replace
argument_list|(
name|extId
argument_list|,
name|extIdLowerCase
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|reindexAccounts ()
specifier|private
name|int
name|reindexAccounts
parameter_list|()
throws|throws
name|Exception
block|{
name|monitor
operator|.
name|beginTask
argument_list|(
literal|"Reindex accounts"
argument_list|,
name|ProgressMonitor
operator|.
name|UNKNOWN
argument_list|)
expr_stmt|;
name|String
index|[]
name|reindexArgs
init|=
block|{
literal|"--site-path"
block|,
name|getSitePath
argument_list|()
operator|.
name|toString
argument_list|()
block|,
literal|"--index"
block|,
name|AccountSchemaDefinitions
operator|.
name|NAME
block|}
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Migration complete, reindexing accounts with:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  reindex "
operator|+
name|String
operator|.
name|join
argument_list|(
literal|" "
argument_list|,
name|reindexArgs
argument_list|)
argument_list|)
expr_stmt|;
name|Reindex
name|reindexPgm
init|=
operator|new
name|Reindex
argument_list|()
decl_stmt|;
name|int
name|exitCode
init|=
name|reindexPgm
operator|.
name|main
argument_list|(
name|reindexArgs
argument_list|)
decl_stmt|;
name|monitor
operator|.
name|endTask
argument_list|()
expr_stmt|;
return|return
name|exitCode
return|;
block|}
block|}
end_class

end_unit
