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
DECL|package|com.google.gerrit.acceptance.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|ssh
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
name|git
operator|.
name|GitUtil
operator|.
name|createProject
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertTrue
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
name|AccountCreator
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
name|GcAssert
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
name|SshSession
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
name|common
operator|.
name|data
operator|.
name|GarbageCollectionResult
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
name|GarbageCollectionQueue
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
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
name|Locale
import|;
end_import

begin_class
DECL|class|GarbageCollectionIT
specifier|public
class|class
name|GarbageCollectionIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Inject
DECL|field|accounts
specifier|private
name|AccountCreator
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|field|allProjects
specifier|private
name|AllProjectsName
name|allProjects
decl_stmt|;
annotation|@
name|Inject
DECL|field|garbageCollectionFactory
specifier|private
name|GarbageCollection
operator|.
name|Factory
name|garbageCollectionFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|gcQueue
specifier|private
name|GarbageCollectionQueue
name|gcQueue
decl_stmt|;
annotation|@
name|Inject
DECL|field|gcAssert
specifier|private
name|GcAssert
name|gcAssert
decl_stmt|;
DECL|field|admin
specifier|private
name|TestAccount
name|admin
decl_stmt|;
DECL|field|sshSession
specifier|private
name|SshSession
name|sshSession
decl_stmt|;
DECL|field|project1
specifier|private
name|Project
operator|.
name|NameKey
name|project1
decl_stmt|;
DECL|field|project2
specifier|private
name|Project
operator|.
name|NameKey
name|project2
decl_stmt|;
DECL|field|project3
specifier|private
name|Project
operator|.
name|NameKey
name|project3
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
name|admin
operator|=
name|accounts
operator|.
name|create
argument_list|(
literal|"admin"
argument_list|,
literal|"admin@example.com"
argument_list|,
literal|"Administrator"
argument_list|,
literal|"Administrators"
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
name|project1
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project1
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|project2
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project2
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|project3
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project3
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGc ()
specifier|public
name|void
name|testGc
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
block|{
name|String
name|response
init|=
name|sshSession
operator|.
name|exec
argument_list|(
literal|"gerrit gc \""
operator|+
name|project1
operator|.
name|get
argument_list|()
operator|+
literal|"\" \""
operator|+
name|project2
operator|.
name|get
argument_list|()
operator|+
literal|"\""
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|sshSession
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoError
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|gcAssert
operator|.
name|assertHasPackFile
argument_list|(
name|project1
argument_list|,
name|project2
argument_list|)
expr_stmt|;
name|gcAssert
operator|.
name|assertHasNoPackFile
argument_list|(
name|allProjects
argument_list|,
name|project3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGcAll ()
specifier|public
name|void
name|testGcAll
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
block|{
name|String
name|response
init|=
name|sshSession
operator|.
name|exec
argument_list|(
literal|"gerrit gc --all"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|sshSession
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoError
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|gcAssert
operator|.
name|assertHasPackFile
argument_list|(
name|allProjects
argument_list|,
name|project1
argument_list|,
name|project2
argument_list|,
name|project3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGcWithoutCapability_Error ()
specifier|public
name|void
name|testGcWithoutCapability_Error
parameter_list|()
throws|throws
name|IOException
throws|,
name|OrmException
throws|,
name|JSchException
block|{
name|SshSession
name|s
init|=
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
name|accounts
operator|.
name|create
argument_list|(
literal|"user"
argument_list|,
literal|"user@example.com"
argument_list|,
literal|"User"
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|.
name|exec
argument_list|(
literal|"gerrit gc --all"
argument_list|)
expr_stmt|;
name|assertError
argument_list|(
literal|"Capability runGC is required to access this resource"
argument_list|,
name|s
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGcAlreadyScheduled ()
specifier|public
name|void
name|testGcAlreadyScheduled
parameter_list|()
block|{
name|gcQueue
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|project1
argument_list|)
argument_list|)
expr_stmt|;
name|GarbageCollectionResult
name|result
init|=
name|garbageCollectionFactory
operator|.
name|create
argument_list|()
operator|.
name|run
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|allProjects
argument_list|,
name|project1
argument_list|,
name|project2
argument_list|,
name|project3
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|hasErrors
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|GarbageCollectionResult
operator|.
name|Error
name|error
init|=
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|GarbageCollectionResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|GC_ALREADY_SCHEDULED
argument_list|,
name|error
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|project1
argument_list|,
name|error
operator|.
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertError (String expectedError, String response)
specifier|private
name|void
name|assertError
parameter_list|(
name|String
name|expectedError
parameter_list|,
name|String
name|response
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|response
argument_list|,
name|response
operator|.
name|contains
argument_list|(
name|expectedError
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNoError (String response)
specifier|private
name|void
name|assertNoError
parameter_list|(
name|String
name|response
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|response
argument_list|,
name|response
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

