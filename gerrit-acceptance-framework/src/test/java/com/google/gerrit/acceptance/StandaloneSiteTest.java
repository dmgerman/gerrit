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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
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
name|fail
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
name|Streams
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
name|launcher
operator|.
name|GerritLauncher
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
name|config
operator|.
name|SitePaths
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
name|ManualRequestContext
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
name|OneOffRequestContext
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|RuleChain
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
name|TemporaryFolder
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
annotation|@
name|UseLocalDisk
DECL|class|StandaloneSiteTest
specifier|public
specifier|abstract
class|class
name|StandaloneSiteTest
block|{
DECL|class|ServerContext
specifier|protected
class|class
name|ServerContext
implements|implements
name|RequestContext
implements|,
name|AutoCloseable
block|{
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|ctx
specifier|private
specifier|final
name|ManualRequestContext
name|ctx
decl_stmt|;
DECL|method|ServerContext (GerritServer server)
specifier|private
name|ServerContext
parameter_list|(
name|GerritServer
name|server
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
name|Injector
name|i
init|=
name|server
operator|.
name|getTestInjector
argument_list|()
decl_stmt|;
if|if
condition|(
name|adminId
operator|==
literal|null
condition|)
block|{
name|adminId
operator|=
name|i
operator|.
name|getInstance
argument_list|(
name|AccountCreator
operator|.
name|class
argument_list|)
operator|.
name|admin
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
name|ctx
operator|=
name|i
operator|.
name|getInstance
argument_list|(
name|OneOffRequestContext
operator|.
name|class
argument_list|)
operator|.
name|openAs
argument_list|(
name|adminId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|ctx
operator|.
name|getUser
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getReviewDbProvider ()
specifier|public
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|getReviewDbProvider
parameter_list|()
block|{
return|return
name|ctx
operator|.
name|getReviewDbProvider
argument_list|()
return|;
block|}
DECL|method|getInjector ()
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|server
operator|.
name|getTestInjector
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|server
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|field|baseConfig
annotation|@
name|ConfigSuite
operator|.
name|Parameter
specifier|public
name|Config
name|baseConfig
decl_stmt|;
DECL|field|configName
annotation|@
name|ConfigSuite
operator|.
name|Name
specifier|private
name|String
name|configName
decl_stmt|;
DECL|field|tempSiteDir
specifier|private
specifier|final
name|TemporaryFolder
name|tempSiteDir
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
DECL|field|testRunner
specifier|private
specifier|final
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
name|Statement
name|base
parameter_list|,
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
name|beforeTest
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|base
operator|.
name|evaluate
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
DECL|field|ruleChain
annotation|@
name|Rule
specifier|public
name|RuleChain
name|ruleChain
init|=
name|RuleChain
operator|.
name|outerRule
argument_list|(
name|tempSiteDir
argument_list|)
operator|.
name|around
argument_list|(
name|testRunner
argument_list|)
decl_stmt|;
DECL|field|sitePaths
specifier|protected
name|SitePaths
name|sitePaths
decl_stmt|;
DECL|field|serverDesc
specifier|private
name|GerritServer
operator|.
name|Description
name|serverDesc
decl_stmt|;
DECL|field|adminId
specifier|private
name|Account
operator|.
name|Id
name|adminId
decl_stmt|;
DECL|method|beforeTest (Description description)
specifier|private
name|void
name|beforeTest
parameter_list|(
name|Description
name|description
parameter_list|)
throws|throws
name|Exception
block|{
name|serverDesc
operator|=
name|GerritServer
operator|.
name|Description
operator|.
name|forTestMethod
argument_list|(
name|description
argument_list|,
name|configName
argument_list|)
expr_stmt|;
name|sitePaths
operator|=
operator|new
name|SitePaths
argument_list|(
name|tempSiteDir
operator|.
name|getRoot
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|GerritServer
operator|.
name|init
argument_list|(
name|serverDesc
argument_list|,
name|baseConfig
argument_list|,
name|sitePaths
operator|.
name|site_path
argument_list|)
expr_stmt|;
block|}
DECL|method|startServer ()
specifier|protected
name|ServerContext
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ServerContext
argument_list|(
name|startImpl
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertServerStartupFails ()
specifier|protected
name|void
name|assertServerStartupFails
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|GerritServer
name|server
init|=
name|startImpl
argument_list|()
init|)
block|{
name|fail
argument_list|(
literal|"expected server startup to fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GerritServer
operator|.
name|StartupException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
DECL|method|startImpl ()
specifier|private
name|GerritServer
name|startImpl
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|GerritServer
operator|.
name|start
argument_list|(
name|serverDesc
argument_list|,
name|baseConfig
argument_list|,
name|sitePaths
operator|.
name|site_path
argument_list|)
return|;
block|}
DECL|method|runGerrit (String... args)
specifier|protected
specifier|static
name|void
name|runGerrit
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|GerritLauncher
operator|.
name|mainImpl
argument_list|(
name|args
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"gerrit.war "
operator|+
name|Arrays
operator|.
name|stream
argument_list|(
name|args
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|" "
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SafeVarargs
DECL|method|runGerrit (Iterable<String>.... multiArgs)
specifier|protected
specifier|static
name|void
name|runGerrit
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
modifier|...
name|multiArgs
parameter_list|)
throws|throws
name|Exception
block|{
name|runGerrit
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|multiArgs
argument_list|)
operator|.
name|flatMap
argument_list|(
name|args
lambda|->
name|Streams
operator|.
name|stream
argument_list|(
name|args
argument_list|)
argument_list|)
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

