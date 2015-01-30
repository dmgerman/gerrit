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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|GitUtil
operator|.
name|cloneProject
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
name|GitUtil
operator|.
name|createProject
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
name|GitUtil
operator|.
name|initSsh
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
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
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
name|project
operator|.
name|Util
operator|.
name|block
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
name|base
operator|.
name|Joiner
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
name|primitives
operator|.
name|Chars
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
name|AcceptanceTestRequestScope
operator|.
name|Context
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
name|AccessSection
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
name|Permission
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
name|PermissionRule
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
name|GerritApi
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
name|changes
operator|.
name|RevisionApi
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
name|ListChangesOption
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
name|ChangeInfo
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
name|RestApiException
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
name|Project
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
name|OutputFormat
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
name|GroupCache
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
name|server
operator|.
name|git
operator|.
name|ProjectConfig
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
name|ChangeIndexer
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
name|ProjectCache
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
name|Util
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
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|ConfigSuite
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
name|TempFileUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
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
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
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
name|api
operator|.
name|Git
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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
name|errors
operator|.
name|ConfigInvalidException
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|rules
operator|.
name|TestRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
DECL|class|AbstractDaemonTest
specifier|public
specifier|abstract
class|class
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Parameter
DECL|field|baseConfig
specifier|public
name|Config
name|baseConfig
decl_stmt|;
annotation|@
name|Inject
DECL|field|allProjects
specifier|protected
name|AllProjectsName
name|allProjects
decl_stmt|;
annotation|@
name|Inject
DECL|field|accounts
specifier|protected
name|AccountCreator
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|field|reviewDbProvider
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|reviewDbProvider
decl_stmt|;
annotation|@
name|Inject
DECL|field|gApi
specifier|protected
name|GerritApi
name|gApi
decl_stmt|;
annotation|@
name|Inject
DECL|field|atrScope
specifier|private
name|AcceptanceTestRequestScope
name|atrScope
decl_stmt|;
annotation|@
name|Inject
DECL|field|identifiedUserFactory
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|pushFactory
specifier|protected
name|PushOneCommit
operator|.
name|Factory
name|pushFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|metaDataUpdateFactory
specifier|protected
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|projectCache
specifier|protected
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|field|groupCache
specifier|protected
name|GroupCache
name|groupCache
decl_stmt|;
annotation|@
name|Inject
DECL|field|repoManager
specifier|protected
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Inject
DECL|field|indexer
specifier|protected
name|ChangeIndexer
name|indexer
decl_stmt|;
annotation|@
name|Inject
DECL|field|queryProvider
specifier|protected
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
annotation|@
name|Inject
DECL|field|cfg
specifier|protected
annotation|@
name|GerritServerConfig
name|Config
name|cfg
decl_stmt|;
DECL|field|git
specifier|protected
name|Git
name|git
decl_stmt|;
DECL|field|server
specifier|protected
name|GerritServer
name|server
decl_stmt|;
DECL|field|admin
specifier|protected
name|TestAccount
name|admin
decl_stmt|;
DECL|field|user
specifier|protected
name|TestAccount
name|user
decl_stmt|;
DECL|field|adminSession
specifier|protected
name|RestSession
name|adminSession
decl_stmt|;
DECL|field|userSession
specifier|protected
name|RestSession
name|userSession
decl_stmt|;
DECL|field|sshSession
specifier|protected
name|SshSession
name|sshSession
decl_stmt|;
DECL|field|db
specifier|protected
name|ReviewDb
name|db
decl_stmt|;
DECL|field|project
specifier|protected
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
annotation|@
name|Rule
DECL|field|testRunner
specifier|public
name|TestRule
name|testRunner
init|=
operator|new
name|TestRule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Statement
name|apply
parameter_list|(
specifier|final
name|Statement
name|base
parameter_list|,
specifier|final
name|Description
name|description
parameter_list|)
block|{
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|boolean
name|mem
init|=
name|description
operator|.
name|getAnnotation
argument_list|(
name|UseLocalDisk
operator|.
name|class
argument_list|)
operator|==
literal|null
decl_stmt|;
name|boolean
name|enableHttpd
init|=
name|description
operator|.
name|getAnnotation
argument_list|(
name|NoHttpd
operator|.
name|class
argument_list|)
operator|==
literal|null
operator|&&
name|description
operator|.
name|getTestClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|NoHttpd
operator|.
name|class
argument_list|)
operator|==
literal|null
decl_stmt|;
name|beforeTest
argument_list|(
name|config
argument_list|(
name|description
argument_list|)
argument_list|,
name|mem
argument_list|,
name|enableHttpd
argument_list|)
expr_stmt|;
name|base
operator|.
name|evaluate
argument_list|()
expr_stmt|;
name|afterTest
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
DECL|method|config (Description description)
specifier|private
name|Config
name|config
parameter_list|(
name|Description
name|description
parameter_list|)
block|{
name|GerritConfigs
name|cfgs
init|=
name|description
operator|.
name|getAnnotation
argument_list|(
name|GerritConfigs
operator|.
name|class
argument_list|)
decl_stmt|;
name|GerritConfig
name|cfg
init|=
name|description
operator|.
name|getAnnotation
argument_list|(
name|GerritConfig
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfgs
operator|!=
literal|null
operator|&&
name|cfg
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Use either @GerritConfigs or @GerritConfig not both"
argument_list|)
throw|;
block|}
if|if
condition|(
name|cfgs
operator|!=
literal|null
condition|)
block|{
return|return
name|ConfigAnnotationParser
operator|.
name|parse
argument_list|(
name|baseConfig
argument_list|,
name|cfgs
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|cfg
operator|!=
literal|null
condition|)
block|{
return|return
name|ConfigAnnotationParser
operator|.
name|parse
argument_list|(
name|baseConfig
argument_list|,
name|cfg
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|baseConfig
return|;
block|}
block|}
DECL|method|wholeTopicEnabledConfig ()
specifier|protected
specifier|static
name|Config
name|wholeTopicEnabledConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"submitWholeTopic"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|method|allowDraftsDisabledConfig ()
specifier|protected
specifier|static
name|Config
name|allowDraftsDisabledConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"allowDrafts"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|method|isAllowDrafts ()
specifier|protected
name|boolean
name|isAllowDrafts
parameter_list|()
block|{
return|return
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|"allowDrafts"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|beforeTest (Config cfg, boolean memory, boolean enableHttpd)
specifier|private
name|void
name|beforeTest
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|boolean
name|memory
parameter_list|,
name|boolean
name|enableHttpd
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|=
name|startServer
argument_list|(
name|cfg
argument_list|,
name|memory
argument_list|,
name|enableHttpd
argument_list|)
expr_stmt|;
name|server
operator|.
name|getTestInjector
argument_list|()
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|admin
operator|=
name|accounts
operator|.
name|admin
argument_list|()
expr_stmt|;
name|user
operator|=
name|accounts
operator|.
name|user
argument_list|()
expr_stmt|;
name|adminSession
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|userSession
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|initSsh
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|db
operator|=
name|reviewDbProvider
operator|.
name|open
argument_list|()
expr_stmt|;
name|Context
name|ctx
init|=
name|newRequestContext
argument_list|(
name|admin
argument_list|)
decl_stmt|;
name|atrScope
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|sshSession
operator|=
name|ctx
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|git
operator|=
name|cloneProject
argument_list|(
name|sshSession
operator|.
name|getUrl
argument_list|()
operator|+
literal|"/"
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|startServer (Config cfg, boolean memory, boolean enableHttpd)
specifier|protected
name|GerritServer
name|startServer
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|boolean
name|memory
parameter_list|,
name|boolean
name|enableHttpd
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|GerritServer
operator|.
name|start
argument_list|(
name|cfg
argument_list|,
name|memory
argument_list|,
name|enableHttpd
argument_list|)
return|;
block|}
DECL|method|afterTest ()
specifier|private
name|void
name|afterTest
parameter_list|()
throws|throws
name|Exception
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
name|sshSession
operator|.
name|close
argument_list|()
expr_stmt|;
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|TempFileUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
DECL|method|createChange ()
specifier|protected
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
return|;
block|}
DECL|field|RANDOM
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Character
argument_list|>
name|RANDOM
init|=
name|Chars
operator|.
name|asList
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|,
literal|'f'
block|,
literal|'g'
block|,
literal|'h'
block|}
argument_list|)
decl_stmt|;
DECL|method|amendChange (String changeId)
specifier|protected
name|PushOneCommit
operator|.
name|Result
name|amendChange
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|Collections
operator|.
name|shuffle
argument_list|(
name|RANDOM
argument_list|)
expr_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|,
operator|new
name|String
argument_list|(
name|Chars
operator|.
name|toArray
argument_list|(
name|RANDOM
argument_list|)
argument_list|)
argument_list|,
name|changeId
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
return|;
block|}
DECL|method|getChange (String changeId, ListChangesOption... options)
specifier|protected
name|ChangeInfo
name|getChange
parameter_list|(
name|String
name|changeId
parameter_list|,
name|ListChangesOption
modifier|...
name|options
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getChange
argument_list|(
name|adminSession
argument_list|,
name|changeId
argument_list|,
name|options
argument_list|)
return|;
block|}
DECL|method|getChange (RestSession session, String changeId, ListChangesOption... options)
specifier|protected
name|ChangeInfo
name|getChange
parameter_list|(
name|RestSession
name|session
parameter_list|,
name|String
name|changeId
parameter_list|,
name|ListChangesOption
modifier|...
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|q
init|=
name|options
operator|.
name|length
operator|>
literal|0
condition|?
literal|"?o="
operator|+
name|Joiner
operator|.
name|on
argument_list|(
literal|"&o="
argument_list|)
operator|.
name|join
argument_list|(
name|options
argument_list|)
else|:
literal|""
decl_stmt|;
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
name|q
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
return|return
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|ChangeInfo
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|info (String id)
specifier|protected
name|ChangeInfo
name|info
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|info
argument_list|()
return|;
block|}
DECL|method|get (String id)
specifier|protected
name|ChangeInfo
name|get
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|get (String id, ListChangesOption... options)
specifier|protected
name|ChangeInfo
name|get
parameter_list|(
name|String
name|id
parameter_list|,
name|ListChangesOption
modifier|...
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|s
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
decl_stmt|;
name|s
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|get
argument_list|(
name|s
argument_list|)
return|;
block|}
DECL|method|query (String q)
specifier|protected
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|query
parameter_list|(
name|String
name|q
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|newRequestContext (TestAccount account)
specifier|private
name|Context
name|newRequestContext
parameter_list|(
name|TestAccount
name|account
parameter_list|)
block|{
return|return
name|atrScope
operator|.
name|newContext
argument_list|(
name|reviewDbProvider
argument_list|,
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
argument_list|,
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
name|db
argument_list|)
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setApiUser (TestAccount account)
specifier|protected
name|Context
name|setApiUser
parameter_list|(
name|TestAccount
name|account
parameter_list|)
block|{
return|return
name|atrScope
operator|.
name|set
argument_list|(
name|newRequestContext
argument_list|(
name|account
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newGson ()
specifier|protected
specifier|static
name|Gson
name|newGson
parameter_list|()
block|{
return|return
name|OutputFormat
operator|.
name|JSON_COMPACT
operator|.
name|newGson
argument_list|()
return|;
block|}
DECL|method|revision (PushOneCommit.Result r)
specifier|protected
name|RevisionApi
name|revision
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
return|;
block|}
DECL|method|allow (String permission, AccountGroup.UUID id, String ref)
specifier|protected
name|void
name|allow
parameter_list|(
name|String
name|permission
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|cfg
argument_list|,
name|permission
argument_list|,
name|id
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
DECL|method|allowGlobalCapability (String capabilityName, AccountGroup.UUID id)
specifier|protected
name|void
name|allowGlobalCapability
parameter_list|(
name|String
name|capabilityName
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|allProjects
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|cfg
argument_list|,
name|capabilityName
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|allProjects
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
DECL|method|deny (String permission, AccountGroup.UUID id, String ref)
specifier|protected
name|void
name|deny
parameter_list|(
name|String
name|permission
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|Util
operator|.
name|deny
argument_list|(
name|cfg
argument_list|,
name|permission
argument_list|,
name|id
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
DECL|method|saveProjectConfig (Project.NameKey p, ProjectConfig cfg)
specifier|protected
name|void
name|saveProjectConfig
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
name|ProjectConfig
name|cfg
parameter_list|)
throws|throws
name|Exception
block|{
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|p
argument_list|)
decl_stmt|;
try|try
block|{
name|cfg
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|projectCache
operator|.
name|evict
argument_list|(
name|cfg
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|grant (String permission, Project.NameKey project, String ref)
specifier|protected
name|void
name|grant
parameter_list|(
name|String
name|permission
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Grant %s on %s"
argument_list|,
name|permission
argument_list|,
name|ref
argument_list|)
argument_list|)
expr_stmt|;
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|AccessSection
name|s
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|ref
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Permission
name|p
init|=
name|s
operator|.
name|getPermission
argument_list|(
name|permission
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccountGroup
name|adminGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
argument_list|)
decl_stmt|;
name|p
operator|.
name|add
argument_list|(
operator|new
name|PermissionRule
argument_list|(
name|config
operator|.
name|resolve
argument_list|(
name|adminGroup
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|blockRead (Project.NameKey project, String ref)
specifier|protected
name|void
name|blockRead
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|block
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
DECL|method|pushTo (String ref)
specifier|protected
name|PushOneCommit
operator|.
name|Result
name|pushTo
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
name|ref
argument_list|)
return|;
block|}
block|}
end_class

end_unit

