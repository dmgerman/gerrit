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
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
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
name|common
operator|.
name|data
operator|.
name|Permission
operator|.
name|LABEL
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
name|allow
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
name|category
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
name|value
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
name|LabelType
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
name|Branch
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
name|Change
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
name|group
operator|.
name|SystemGroupBackend
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
name|testutil
operator|.
name|InMemoryRepositoryManager
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
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|compiler
operator|.
name|CompileException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|JavaObjectTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|StructureTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|SymbolTerm
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
name|io
operator|.
name|PushbackReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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

begin_class
DECL|class|GerritCommonTest
specifier|public
class|class
name|GerritCommonTest
extends|extends
name|PrologTestCase
block|{
DECL|field|V
specifier|private
specifier|final
name|LabelType
name|V
init|=
name|category
argument_list|(
literal|"Verified"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Verified"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Fails"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|Q
specifier|private
specifier|final
name|LabelType
name|Q
init|=
name|category
argument_list|(
literal|"Qualified"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Qualified"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Fails"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|localKey
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|localKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"local"
argument_list|)
decl_stmt|;
DECL|field|local
specifier|private
name|ProjectConfig
name|local
decl_stmt|;
DECL|field|util
specifier|private
name|Util
name|util
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
name|util
operator|=
operator|new
name|Util
argument_list|()
expr_stmt|;
name|load
argument_list|(
literal|"gerrit"
argument_list|,
literal|"gerrit_common_test.pl"
argument_list|,
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
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"rules"
argument_list|,
literal|null
argument_list|,
literal|"reductionLimit"
argument_list|,
literal|1300
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"rules"
argument_list|,
literal|null
argument_list|,
literal|"compileReductionLimit"
argument_list|,
operator|(
name|int
operator|)
literal|1e6
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PrologEnvironment
operator|.
name|Args
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|PrologEnvironment
operator|.
name|Args
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|cfg
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|local
operator|=
operator|new
name|ProjectConfig
argument_list|(
name|localKey
argument_list|)
expr_stmt|;
name|local
operator|.
name|load
argument_list|(
name|InMemoryRepositoryManager
operator|.
name|newRepository
argument_list|(
name|localKey
argument_list|)
argument_list|)
expr_stmt|;
name|Q
operator|.
name|setRefPatterns
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"refs/heads/develop"
argument_list|)
argument_list|)
expr_stmt|;
name|local
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|V
operator|.
name|getName
argument_list|()
argument_list|,
name|V
argument_list|)
expr_stmt|;
name|local
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|Q
operator|.
name|getName
argument_list|()
argument_list|,
name|Q
argument_list|)
expr_stmt|;
name|util
operator|.
name|add
argument_list|(
name|local
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|local
argument_list|,
name|LABEL
operator|+
name|V
operator|.
name|getName
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|,
operator|+
literal|1
argument_list|,
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|local
argument_list|,
name|LABEL
operator|+
name|Q
operator|.
name|getName
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|,
operator|+
literal|1
argument_list|,
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUpEnvironment (PrologEnvironment env)
specifier|protected
name|void
name|setUpEnvironment
parameter_list|(
name|PrologEnvironment
name|env
parameter_list|)
block|{
name|Change
name|change
init|=
operator|new
name|Change
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
literal|"Ibeef"
argument_list|)
argument_list|,
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|2
argument_list|)
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|localKey
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|CHANGE_CONTROL
argument_list|,
name|util
operator|.
name|user
argument_list|(
name|local
argument_list|)
operator|.
name|controlFor
argument_list|(
name|change
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGerritCommon ()
specifier|public
name|void
name|testGerritCommon
parameter_list|()
block|{
name|runPrologBasedTests
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReductionLimit ()
specifier|public
name|void
name|testReductionLimit
parameter_list|()
throws|throws
name|CompileException
block|{
name|PrologEnvironment
name|env
init|=
name|envFactory
operator|.
name|create
argument_list|(
name|machine
argument_list|)
decl_stmt|;
name|setUpEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|env
operator|.
name|setEnabled
argument_list|(
name|Prolog
operator|.
name|Feature
operator|.
name|IO
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|script
init|=
literal|"loopy :- b(5).\n"
operator|+
literal|"b(N) :- N> 0, !, S = N - 1, b(S).\n"
operator|+
literal|"b(_) :- true.\n"
decl_stmt|;
name|SymbolTerm
name|nameTerm
init|=
name|SymbolTerm
operator|.
name|create
argument_list|(
literal|"testReductionLimit"
argument_list|)
decl_stmt|;
name|JavaObjectTerm
name|inTerm
init|=
operator|new
name|JavaObjectTerm
argument_list|(
operator|new
name|PushbackReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|script
argument_list|)
argument_list|,
name|Prolog
operator|.
name|PUSHBACK_SIZE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|env
operator|.
name|execute
argument_list|(
name|Prolog
operator|.
name|BUILTIN
argument_list|,
literal|"consult_stream"
argument_list|,
name|nameTerm
argument_list|,
name|inTerm
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot consult "
operator|+
name|nameTerm
argument_list|)
throw|;
block|}
try|try
block|{
name|env
operator|.
name|once
argument_list|(
name|Prolog
operator|.
name|BUILTIN
argument_list|,
literal|"call"
argument_list|,
operator|new
name|StructureTerm
argument_list|(
literal|":"
argument_list|,
name|SymbolTerm
operator|.
name|create
argument_list|(
literal|"user"
argument_list|)
argument_list|,
name|SymbolTerm
operator|.
name|create
argument_list|(
literal|"loopy"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"long running loop did not abort with ReductionLimitException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ReductionLimitException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"exceeded reduction limit of 1300"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

