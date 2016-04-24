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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assert_
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
name|UseLocalDisk
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
name|inject
operator|.
name|Inject
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
annotation|@
name|NoHttpd
DECL|class|GarbageCollectionIT
specifier|public
class|class
name|GarbageCollectionIT
extends|extends
name|AbstractDaemonTest
block|{
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
name|project2
operator|=
name|createProject
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|project3
operator|=
name|createProject
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|UseLocalDisk
DECL|method|testGc ()
specifier|public
name|void
name|testGc
parameter_list|()
throws|throws
name|Exception
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
name|project
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
name|assert_
argument_list|()
operator|.
name|withFailureMessage
argument_list|(
name|sshSession
operator|.
name|getError
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|sshSession
operator|.
name|hasError
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|project
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
annotation|@
name|UseLocalDisk
DECL|method|testGcAll ()
specifier|public
name|void
name|testGcAll
parameter_list|()
throws|throws
name|Exception
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
name|assert_
argument_list|()
operator|.
name|withFailureMessage
argument_list|(
name|sshSession
operator|.
name|getError
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|sshSession
operator|.
name|hasError
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|project
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
name|Exception
block|{
name|SshSession
name|s
init|=
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|s
operator|.
name|exec
argument_list|(
literal|"gerrit gc --all"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|s
operator|.
name|hasError
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|String
name|error
init|=
name|s
operator|.
name|getError
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|error
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertError
argument_list|(
literal|"One of the following capabilities is required to access this"
operator|+
literal|" resource: [runGC, maintainServer]"
argument_list|,
name|error
argument_list|)
expr_stmt|;
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|UseLocalDisk
DECL|method|testGcAlreadyScheduled ()
specifier|public
name|void
name|testGcAlreadyScheduled
parameter_list|()
throws|throws
name|Exception
block|{
name|gcQueue
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|project
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
name|project
argument_list|,
name|project2
argument_list|,
name|project3
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|hasErrors
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|getErrors
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
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
name|assertThat
argument_list|(
name|error
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|GarbageCollectionResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|GC_ALREADY_SCHEDULED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|error
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|project
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
name|assertThat
argument_list|(
name|response
argument_list|)
operator|.
name|contains
argument_list|(
name|expectedError
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
name|assertThat
argument_list|(
name|response
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
operator|.
name|doesNotContain
argument_list|(
literal|"error"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

