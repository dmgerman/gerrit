begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|server
operator|.
name|schema
operator|.
name|AllProjectsInput
operator|.
name|getDefaultCodeReviewLabel
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
name|testing
operator|.
name|AllProjectsCreatorTestUtil
operator|.
name|assertSectionEquivalent
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
name|testing
operator|.
name|AllProjectsCreatorTestUtil
operator|.
name|assertTwoConfigsEquivalent
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
name|testing
operator|.
name|AllProjectsCreatorTestUtil
operator|.
name|getAllProjectsWithoutDefaultAcls
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
name|testing
operator|.
name|AllProjectsCreatorTestUtil
operator|.
name|getDefaultAllProjectsWithAllDefaultSections
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
name|testing
operator|.
name|AllProjectsCreatorTestUtil
operator|.
name|readAllProjectsConfig
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
name|ImmutableList
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
name|GroupReference
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
name|common
operator|.
name|data
operator|.
name|LabelValue
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
name|InheritableBoolean
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
name|BooleanProjectConfig
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
name|account
operator|.
name|GroupUUID
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
name|notedb
operator|.
name|Sequences
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
name|InMemoryModule
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

begin_class
DECL|class|AllProjectsCreatorTest
specifier|public
class|class
name|AllProjectsCreatorTest
block|{
DECL|field|TEST_LABEL
specifier|private
specifier|static
specifier|final
name|LabelType
name|TEST_LABEL
init|=
operator|new
name|LabelType
argument_list|(
literal|"Test-Label"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
literal|"Two"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"Zero"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|"One"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|TEST_LABEL_STRING
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LABEL_STRING
init|=
name|String
operator|.
name|join
argument_list|(
literal|"\n"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[label \"Test-Label\"]"
argument_list|,
literal|"\tfunction = MaxWithBlock"
argument_list|,
literal|"\tdefaultValue = 0"
argument_list|,
literal|"\tvalue = 0 Zero"
argument_list|,
literal|"\tvalue = +1 One"
argument_list|,
literal|"\tvalue = +2 Two"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|allProjectsName
annotation|@
name|Inject
specifier|private
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|serverUser
annotation|@
name|Inject
annotation|@
name|GerritPersonIdent
specifier|private
name|PersonIdent
name|serverUser
decl_stmt|;
DECL|field|allProjectsCreator
annotation|@
name|Inject
specifier|private
name|AllProjectsCreator
name|allProjectsCreator
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
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
name|InMemoryModule
name|inMemoryModule
init|=
operator|new
name|InMemoryModule
argument_list|()
decl_stmt|;
name|inMemoryModule
operator|.
name|inject
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Creates an empty All-Projects.
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|allProjectsName
argument_list|)
init|)
block|{
comment|// Intentionally empty.
block|}
block|}
annotation|@
name|Test
DECL|method|createDefaultAllProjectsConfig ()
specifier|public
name|void
name|createDefaultAllProjectsConfig
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Loads the expected configs.
name|Config
name|expectedConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|expectedConfig
operator|.
name|fromText
argument_list|(
name|getDefaultAllProjectsWithAllDefaultSections
argument_list|()
argument_list|)
expr_stmt|;
name|GroupReference
name|adminsGroup
init|=
name|createGroupReference
argument_list|(
literal|"Administrators"
argument_list|)
decl_stmt|;
name|GroupReference
name|batchUsersGroup
init|=
name|createGroupReference
argument_list|(
literal|"Non-Interactive Users"
argument_list|)
decl_stmt|;
name|AllProjectsInput
name|allProjectsInput
init|=
name|AllProjectsInput
operator|.
name|builder
argument_list|()
operator|.
name|administratorsGroup
argument_list|(
name|adminsGroup
argument_list|)
operator|.
name|batchUsersGroup
argument_list|(
name|batchUsersGroup
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|allProjectsCreator
operator|.
name|create
argument_list|(
name|allProjectsInput
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
name|readAllProjectsConfig
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|)
decl_stmt|;
name|assertTwoConfigsEquivalent
argument_list|(
name|config
argument_list|,
name|expectedConfig
argument_list|)
expr_stmt|;
block|}
DECL|method|createGroupReference (String name)
specifier|private
name|GroupReference
name|createGroupReference
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|GroupUUID
operator|.
name|make
argument_list|(
name|name
argument_list|,
name|serverUser
argument_list|)
decl_stmt|;
return|return
operator|new
name|GroupReference
argument_list|(
name|groupUuid
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|createAllProjectsWithNewCodeReviewLabel ()
specifier|public
name|void
name|createAllProjectsWithNewCodeReviewLabel
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|expectedLabelConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|expectedLabelConfig
operator|.
name|fromText
argument_list|(
name|TEST_LABEL_STRING
argument_list|)
expr_stmt|;
name|AllProjectsInput
name|allProjectsInput
init|=
name|AllProjectsInput
operator|.
name|builder
argument_list|()
operator|.
name|codeReviewLabel
argument_list|(
name|TEST_LABEL
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|allProjectsCreator
operator|.
name|create
argument_list|(
name|allProjectsInput
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
name|readAllProjectsConfig
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|)
decl_stmt|;
name|assertSectionEquivalent
argument_list|(
name|config
argument_list|,
name|expectedLabelConfig
argument_list|,
literal|"label"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAllProjectsWithProjectDescription ()
specifier|public
name|void
name|createAllProjectsWithProjectDescription
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testDescription
init|=
literal|"test description"
decl_stmt|;
name|AllProjectsInput
name|allProjectsInput
init|=
name|AllProjectsInput
operator|.
name|builder
argument_list|()
operator|.
name|projectDescription
argument_list|(
name|testDescription
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|allProjectsCreator
operator|.
name|create
argument_list|(
name|allProjectsInput
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
name|readAllProjectsConfig
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|config
operator|.
name|getString
argument_list|(
literal|"project"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|testDescription
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAllProjectsWithBooleanConfigs ()
specifier|public
name|void
name|createAllProjectsWithBooleanConfigs
parameter_list|()
throws|throws
name|Exception
block|{
name|AllProjectsInput
name|allProjectsInput
init|=
name|AllProjectsInput
operator|.
name|builderWithNoDefault
argument_list|()
operator|.
name|codeReviewLabel
argument_list|(
name|getDefaultCodeReviewLabel
argument_list|()
argument_list|)
operator|.
name|firstChangeIdForNoteDb
argument_list|(
name|Sequences
operator|.
name|FIRST_CHANGE_ID
argument_list|)
operator|.
name|addBooleanProjectConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|REJECT_EMPTY_COMMIT
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
operator|.
name|initDefaultAcls
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|allProjectsCreator
operator|.
name|create
argument_list|(
name|allProjectsInput
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
name|readAllProjectsConfig
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"submit"
argument_list|,
literal|null
argument_list|,
literal|"rejectEmptyCommit"
argument_list|,
literal|false
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAllProjectsWithoutInitializingDefaultACLs ()
specifier|public
name|void
name|createAllProjectsWithoutInitializingDefaultACLs
parameter_list|()
throws|throws
name|Exception
block|{
name|AllProjectsInput
name|allProjectsInput
init|=
name|AllProjectsInput
operator|.
name|builder
argument_list|()
operator|.
name|initDefaultAcls
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|allProjectsCreator
operator|.
name|create
argument_list|(
name|allProjectsInput
argument_list|)
expr_stmt|;
name|Config
name|expectedConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|expectedConfig
operator|.
name|fromText
argument_list|(
name|getAllProjectsWithoutDefaultAcls
argument_list|()
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
name|readAllProjectsConfig
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|)
decl_stmt|;
name|assertTwoConfigsEquivalent
argument_list|(
name|config
argument_list|,
name|expectedConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAllProjectsOnlyInitializingProjectDescription ()
specifier|public
name|void
name|createAllProjectsOnlyInitializingProjectDescription
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|description
init|=
literal|"a project.config with just a project description"
decl_stmt|;
name|AllProjectsInput
name|allProjectsInput
init|=
name|AllProjectsInput
operator|.
name|builderWithNoDefault
argument_list|()
operator|.
name|firstChangeIdForNoteDb
argument_list|(
name|Sequences
operator|.
name|FIRST_CHANGE_ID
argument_list|)
operator|.
name|projectDescription
argument_list|(
name|description
argument_list|)
operator|.
name|initDefaultAcls
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|allProjectsCreator
operator|.
name|create
argument_list|(
name|allProjectsInput
argument_list|)
expr_stmt|;
name|Config
name|expectedConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|expectedConfig
operator|.
name|setString
argument_list|(
literal|"project"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
name|readAllProjectsConfig
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|)
decl_stmt|;
name|assertTwoConfigsEquivalent
argument_list|(
name|config
argument_list|,
name|expectedConfig
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

