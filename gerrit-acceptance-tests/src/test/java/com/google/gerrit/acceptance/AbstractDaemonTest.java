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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|common
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
name|change
operator|.
name|ChangeJson
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
name|atrScope
operator|.
name|set
argument_list|(
name|atrScope
operator|.
name|newContext
argument_list|(
name|reviewDbProvider
argument_list|,
name|sshSession
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
name|admin
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sshSession
operator|=
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
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
DECL|method|ammendChange (String changeId)
specifier|protected
name|PushOneCommit
operator|.
name|Result
name|ammendChange
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
name|ChangeJson
operator|.
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
name|ChangeJson
operator|.
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
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
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
name|ChangeJson
operator|.
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
block|}
end_class

end_unit

