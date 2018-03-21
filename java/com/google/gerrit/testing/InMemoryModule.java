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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Scopes
operator|.
name|SINGLETON
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningExecutorService
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
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
name|AuthType
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
name|config
operator|.
name|FactoryModule
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
name|systemstatus
operator|.
name|ServerInformation
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
name|gpg
operator|.
name|GpgModule
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
name|index
operator|.
name|SchemaDefinitions
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
name|index
operator|.
name|project
operator|.
name|ProjectSchemaDefinitions
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
name|metrics
operator|.
name|DisabledMetricMaker
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
name|metrics
operator|.
name|MetricMaker
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
name|GerritPersonIdent
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
name|GerritPersonIdentProvider
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
name|api
operator|.
name|GerritApiModule
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
name|api
operator|.
name|PluginApiModule
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
name|cache
operator|.
name|h2
operator|.
name|DefaultCacheFactory
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
name|AllProjectsName
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
name|AllProjectsNameProvider
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
name|config
operator|.
name|AnonymousCowardName
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
name|AnonymousCowardNameProvider
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
name|CanonicalWebUrlModule
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
name|CanonicalWebUrlProvider
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
name|GerritGlobalModule
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
name|GerritInstanceNameModule
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
name|GerritOptions
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
name|GerritServerConfig
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
name|GerritServerId
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
name|SitePath
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
name|TrackingFooters
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
name|TrackingFootersProvider
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
name|GarbageCollection
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
name|GitRepositoryManager
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
name|LocalMergeSuperSetComputation
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
name|PerThreadRequestScope
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
name|SearchingChangeCacheImpl
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
name|SendEmailExecutor
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
name|IndexModule
operator|.
name|IndexType
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
name|index
operator|.
name|account
operator|.
name|AllAccountsIndexer
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
name|change
operator|.
name|AllChangesIndexer
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
name|change
operator|.
name|ChangeSchemaDefinitions
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
name|group
operator|.
name|AllGroupsIndexer
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
name|group
operator|.
name|GroupSchemaDefinitions
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
name|mail
operator|.
name|SignedTokenEmailTokenVerifier
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
name|ChangeBundleReader
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
name|GwtormChangeBundleReader
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
name|MutableNotesMigration
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
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|DiffExecutor
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
name|permissions
operator|.
name|DefaultPermissionBackendModule
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
name|plugins
operator|.
name|PluginRestApiModule
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
name|plugins
operator|.
name|ServerInformationImpl
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
name|project
operator|.
name|DefaultProjectNameLockManager
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
name|DataSourceType
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
name|InMemoryAccountPatchReviewStore
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
name|NotesMigrationSchemaFactory
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
name|ReviewDbFactory
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
name|SchemaCreator
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
name|securestore
operator|.
name|DefaultSecureStore
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
name|securestore
operator|.
name|SecureStore
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
name|ssh
operator|.
name|NoSshKeyCache
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
name|update
operator|.
name|ChangeUpdateExecutor
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
name|Key
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
name|Module
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
name|Provides
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
name|ProvisionException
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|servlet
operator|.
name|RequestScoped
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|Config
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

begin_class
DECL|class|InMemoryModule
specifier|public
class|class
name|InMemoryModule
extends|extends
name|FactoryModule
block|{
DECL|method|newDefaultConfig ()
specifier|public
specifier|static
name|Config
name|newDefaultConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|setDefaults
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|method|setDefaults (Config cfg)
specifier|public
specifier|static
name|void
name|setDefaults
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|cfg
operator|.
name|setEnum
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|,
name|AuthType
operator|.
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"allProjects"
argument_list|,
literal|"Test-Projects"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basePath"
argument_list|,
literal|"git"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalWebUrl"
argument_list|,
literal|"http://test/"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"name"
argument_list|,
literal|"Gerrit Code Review"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"email"
argument_list|,
literal|"gerrit@localhost"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|unset
argument_list|(
literal|"cache"
argument_list|,
literal|null
argument_list|,
literal|"directory"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|,
literal|"lucene"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"lucene"
argument_list|,
literal|"testInmemory"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"threadPoolSize"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"enableSignedPush"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"certNonceSeed"
argument_list|,
literal|"sekret"
argument_list|)
expr_stmt|;
block|}
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|notesMigration
specifier|private
specifier|final
name|MutableNotesMigration
name|notesMigration
decl_stmt|;
DECL|method|InMemoryModule ()
specifier|public
name|InMemoryModule
parameter_list|()
block|{
name|this
argument_list|(
name|newDefaultConfig
argument_list|()
argument_list|,
name|NoteDbMode
operator|.
name|newNotesMigrationFromEnv
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|InMemoryModule (Config cfg, MutableNotesMigration notesMigration)
specifier|public
name|InMemoryModule
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|MutableNotesMigration
name|notesMigration
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|notesMigration
operator|=
name|notesMigration
expr_stmt|;
block|}
DECL|method|inject (Object instance)
specifier|public
name|void
name|inject
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|Guice
operator|.
name|createInjector
argument_list|(
name|this
argument_list|)
operator|.
name|injectMembers
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
comment|// Do NOT bind @RemotePeer, as it is bound in a child injector of
comment|// ChangeMergeQueue (bound via GerritGlobalModule below), so there cannot be
comment|// a binding in the parent injector. If you need @RemotePeer, you must bind
comment|// it in a child injector of the one containing InMemoryModule. But unless
comment|// you really need to test something request-scoped, you likely don't
comment|// actually need it.
comment|// For simplicity, don't create child injectors, just use this one to get a
comment|// few required modules.
name|Injector
name|cfgInjector
init|=
name|Guice
operator|.
name|createInjector
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
name|bind
argument_list|(
name|Config
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|GerritServerConfig
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|MetricMaker
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DisabledMetricMaker
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|cfgInjector
operator|.
name|getInstance
argument_list|(
name|GerritGlobalModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|GerritApiModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|PluginApiModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|DefaultPermissionBackendModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|SearchingChangeCacheImpl
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|GarbageCollection
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bindScope
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|,
name|PerThreadRequestScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
comment|// TODO(dborowitz): Use Jimfs. The biggest blocker is that JGit does not support Path-based
comment|// Configs, only FileBasedConfig.
name|bind
argument_list|(
name|Path
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|SitePath
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Config
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|GerritServerConfig
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GerritOptions
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|GerritOptions
argument_list|(
name|cfg
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PersonIdent
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|GerritPersonIdent
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|GerritPersonIdentProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|AnonymousCowardName
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|AnonymousCowardNameProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|GerritServerId
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
literal|"gerrit"
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AllProjectsName
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|AllProjectsNameProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AllUsersName
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|AllUsersNameProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GitRepositoryManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|InMemoryRepositoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|InMemoryRepositoryManager
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|TrackingFooters
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|TrackingFootersProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|MutableNotesMigration
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|notesMigration
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|NotesMigration
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MutableNotesMigration
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ListeningExecutorService
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|ChangeUpdateExecutor
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|InMemoryH2Type
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ChangeBundleReader
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GwtormChangeBundleReader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SecureStore
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultSecureStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|TypeLiteral
argument_list|<
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
name|schemaFactory
init|=
operator|new
name|TypeLiteral
argument_list|<
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|bind
argument_list|(
name|schemaFactory
argument_list|)
operator|.
name|to
argument_list|(
name|NotesMigrationSchemaFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|schemaFactory
argument_list|,
name|ReviewDbFactory
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|InMemoryDatabase
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|NoSshKeyCache
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|GerritInstanceNameModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|CanonicalWebUrlModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|String
argument_list|>
argument_list|>
name|provider
parameter_list|()
block|{
return|return
name|CanonicalWebUrlProvider
operator|.
name|class
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Replacement of DiffExecutorModule to not use thread pool in the tests
name|install
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
block|{}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|DiffExecutor
specifier|public
name|ExecutorService
name|createDiffExecutor
parameter_list|()
block|{
return|return
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|DefaultCacheFactory
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|FakeEmailSender
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|SignedTokenEmailTokenVerifier
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|GpgModule
argument_list|(
name|cfg
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|InMemoryAccountPatchReviewStore
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|LocalMergeSuperSetComputation
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AllAccountsIndexer
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AllChangesIndexer
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AllGroupsIndexer
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|IndexType
name|indexType
init|=
literal|null
decl_stmt|;
try|try
block|{
name|indexType
operator|=
name|cfg
operator|.
name|getEnum
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|,
name|IndexType
operator|.
name|LUCENE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Custom index type, caller must provide their own module.
block|}
if|if
condition|(
name|indexType
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|indexType
condition|)
block|{
case|case
name|LUCENE
case|:
name|install
argument_list|(
name|luceneIndexModule
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|ELASTICSEARCH
case|:
name|install
argument_list|(
name|elasticIndexModule
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"index type unsupported in tests: "
operator|+
name|indexType
argument_list|)
throw|;
block|}
block|}
name|bind
argument_list|(
name|ServerInformationImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ServerInformation
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerInformationImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|PluginRestApiModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|DefaultProjectNameLockManager
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|SendEmailExecutor
DECL|method|createSendEmailExecutor ()
specifier|public
name|ExecutorService
name|createSendEmailExecutor
parameter_list|()
block|{
return|return
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
return|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getInMemoryDatabase (SchemaCreator schemaCreator)
name|InMemoryDatabase
name|getInMemoryDatabase
parameter_list|(
name|SchemaCreator
name|schemaCreator
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
operator|new
name|InMemoryDatabase
argument_list|(
name|schemaCreator
argument_list|)
return|;
block|}
DECL|method|luceneIndexModule ()
specifier|private
name|Module
name|luceneIndexModule
parameter_list|()
block|{
return|return
name|indexModule
argument_list|(
literal|"com.google.gerrit.lucene.LuceneIndexModule"
argument_list|)
return|;
block|}
DECL|method|elasticIndexModule ()
specifier|private
name|Module
name|elasticIndexModule
parameter_list|()
block|{
return|return
name|indexModule
argument_list|(
literal|"com.google.gerrit.elasticsearch.ElasticIndexModule"
argument_list|)
return|;
block|}
DECL|method|indexModule (String moduleClassName)
specifier|private
name|Module
name|indexModule
parameter_list|(
name|String
name|moduleClassName
parameter_list|)
block|{
try|try
block|{
name|boolean
name|slave
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"container"
argument_list|,
literal|"slave"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|moduleClassName
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|clazz
operator|.
name|getMethod
argument_list|(
literal|"singleVersionWithExplicitVersions"
argument_list|,
name|Map
operator|.
name|class
argument_list|,
name|int
operator|.
name|class
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|Module
operator|)
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|getSingleSchemaVersions
argument_list|()
argument_list|,
literal|0
argument_list|,
name|slave
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
decl||
name|SecurityException
decl||
name|NoSuchMethodException
decl||
name|IllegalArgumentException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|ProvisionException
name|pe
init|=
operator|new
name|ProvisionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
DECL|method|getSingleSchemaVersions ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getSingleSchemaVersions
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|putSchemaVersion
argument_list|(
name|singleVersions
argument_list|,
name|AccountSchemaDefinitions
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|putSchemaVersion
argument_list|(
name|singleVersions
argument_list|,
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|putSchemaVersion
argument_list|(
name|singleVersions
argument_list|,
name|GroupSchemaDefinitions
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|putSchemaVersion
argument_list|(
name|singleVersions
argument_list|,
name|ProjectSchemaDefinitions
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|singleVersions
return|;
block|}
DECL|method|putSchemaVersion ( Map<String, Integer> singleVersions, SchemaDefinitions<?> schemaDef)
specifier|private
name|void
name|putSchemaVersion
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
parameter_list|,
name|SchemaDefinitions
argument_list|<
name|?
argument_list|>
name|schemaDef
parameter_list|)
block|{
name|String
name|schemaName
init|=
name|schemaDef
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|version
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
literal|"lucene"
argument_list|,
name|schemaName
operator|+
literal|"TestVersion"
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|version
operator|>
literal|0
condition|)
block|{
name|checkState
argument_list|(
operator|!
name|singleVersions
operator|.
name|containsKey
argument_list|(
name|schemaName
argument_list|)
argument_list|,
literal|"version for schema %s was alreay set"
argument_list|,
name|schemaName
argument_list|)
expr_stmt|;
name|singleVersions
operator|.
name|put
argument_list|(
name|schemaName
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

