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
DECL|package|com.google.gerrit.server.schema.testing
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
name|assertWithMessage
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
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|lib
operator|.
name|BlobBasedConfig
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
name|Ref
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

begin_class
DECL|class|AllProjectsCreatorTestUtil
specifier|public
class|class
name|AllProjectsCreatorTestUtil
block|{
DECL|field|DEFAULT_ALL_PROJECTS_PROJECT_SECTION
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|DEFAULT_ALL_PROJECTS_PROJECT_SECTION
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[project]"
argument_list|,
literal|"  description = Access inherited by all other projects."
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_ALL_PROJECTS_RECEIVE_SECTION
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|DEFAULT_ALL_PROJECTS_RECEIVE_SECTION
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[receive]"
argument_list|,
literal|"  requireContributorAgreement = false"
argument_list|,
literal|"  requireSignedOffBy = false"
argument_list|,
literal|"  enableSignedPush = false"
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_ALL_PROJECTS_SUBMIT_SECTION
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|DEFAULT_ALL_PROJECTS_SUBMIT_SECTION
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[submit]"
argument_list|,
literal|"  mergeContent = true"
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_ALL_PROJECTS_CAPABILITY_SECTION
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|DEFAULT_ALL_PROJECTS_CAPABILITY_SECTION
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[capability]"
argument_list|,
literal|"  administrateServer = group Administrators"
argument_list|,
literal|"  priority = batch group Non-Interactive Users"
argument_list|,
literal|"  streamEvents = group Non-Interactive Users"
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_ALL_PROJECTS_ACCESS_SECTION
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|DEFAULT_ALL_PROJECTS_ACCESS_SECTION
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[access \"refs/*\"]"
argument_list|,
literal|"  read = group Administrators"
argument_list|,
literal|"  read = group Anonymous Users"
argument_list|,
literal|"[access \"refs/for/*\"]"
argument_list|,
literal|"  addPatchSet = group Registered Users"
argument_list|,
literal|"[access \"refs/for/refs/*\"]"
argument_list|,
literal|"  push = group Registered Users"
argument_list|,
literal|"  pushMerge = group Registered Users"
argument_list|,
literal|"[access \"refs/heads/*\"]"
argument_list|,
literal|"  create = group Administrators"
argument_list|,
literal|"  create = group Project Owners"
argument_list|,
literal|"  editTopicName = +force group Administrators"
argument_list|,
literal|"  editTopicName = +force group Project Owners"
argument_list|,
literal|"  forgeAuthor = group Registered Users"
argument_list|,
literal|"  forgeCommitter = group Administrators"
argument_list|,
literal|"  forgeCommitter = group Project Owners"
argument_list|,
literal|"  label-Code-Review = -2..+2 group Administrators"
argument_list|,
literal|"  label-Code-Review = -2..+2 group Project Owners"
argument_list|,
literal|"  label-Code-Review = -1..+1 group Registered Users"
argument_list|,
literal|"  push = group Administrators"
argument_list|,
literal|"  push = group Project Owners"
argument_list|,
literal|"  submit = group Administrators"
argument_list|,
literal|"  submit = group Project Owners"
argument_list|,
literal|"[access \"refs/meta/config\"]"
argument_list|,
literal|"  exclusiveGroupPermissions = read"
argument_list|,
literal|"  create = group Administrators"
argument_list|,
literal|"  create = group Project Owners"
argument_list|,
literal|"  label-Code-Review = -2..+2 group Administrators"
argument_list|,
literal|"  label-Code-Review = -2..+2 group Project Owners"
argument_list|,
literal|"  push = group Administrators"
argument_list|,
literal|"  push = group Project Owners"
argument_list|,
literal|"  read = group Administrators"
argument_list|,
literal|"  read = group Project Owners"
argument_list|,
literal|"  submit = group Administrators"
argument_list|,
literal|"  submit = group Project Owners"
argument_list|,
literal|"[access \"refs/tags/*\"]"
argument_list|,
literal|"  create = group Administrators"
argument_list|,
literal|"  create = group Project Owners"
argument_list|,
literal|"  createSignedTag = group Administrators"
argument_list|,
literal|"  createSignedTag = group Project Owners"
argument_list|,
literal|"  createTag = group Administrators"
argument_list|,
literal|"  createTag = group Project Owners"
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_ALL_PROJECTS_LABEL_SECTION
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|DEFAULT_ALL_PROJECTS_LABEL_SECTION
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[label \"Code-Review\"]"
argument_list|,
literal|"  function = MaxWithBlock"
argument_list|,
literal|"  defaultValue = 0"
argument_list|,
literal|"  copyMinScore = true"
argument_list|,
literal|"  copyAllScoresOnTrivialRebase = true"
argument_list|,
literal|"  value = -2 This shall not be merged"
argument_list|,
literal|"  value = -1 I would prefer this is not merged as is"
argument_list|,
literal|"  value = 0 No score"
argument_list|,
literal|"  value = +1 Looks good to me, but someone else must approve"
argument_list|,
literal|"  value = +2 Looks good to me, approved"
argument_list|)
decl_stmt|;
DECL|method|getDefaultAllProjectsWithAllDefaultSections ()
specifier|public
specifier|static
name|String
name|getDefaultAllProjectsWithAllDefaultSections
parameter_list|()
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|Iterables
operator|.
name|concat
argument_list|(
name|DEFAULT_ALL_PROJECTS_PROJECT_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_RECEIVE_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_SUBMIT_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_CAPABILITY_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_ACCESS_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_LABEL_SECTION
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getAllProjectsWithoutDefaultAcls ()
specifier|public
specifier|static
name|String
name|getAllProjectsWithoutDefaultAcls
parameter_list|()
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|Iterables
operator|.
name|concat
argument_list|(
name|DEFAULT_ALL_PROJECTS_PROJECT_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_RECEIVE_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_SUBMIT_SECTION
argument_list|,
name|DEFAULT_ALL_PROJECTS_LABEL_SECTION
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
return|;
block|}
comment|// Loads the "project.config" from the All-Projects repo.
DECL|method|readAllProjectsConfig ( GitRepositoryManager repoManager, AllProjectsName allProjectsName)
specifier|public
specifier|static
name|Config
name|readAllProjectsConfig
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allProjectsName
argument_list|)
init|)
block|{
name|Ref
name|configRef
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
return|return
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|repo
argument_list|,
name|configRef
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"project.config"
argument_list|)
return|;
block|}
block|}
DECL|method|assertTwoConfigsEquivalent (Config config1, Config config2)
specifier|public
specifier|static
name|void
name|assertTwoConfigsEquivalent
parameter_list|(
name|Config
name|config1
parameter_list|,
name|Config
name|config2
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|sections1
init|=
name|config1
operator|.
name|getSections
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|sections2
init|=
name|config2
operator|.
name|getSections
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|sections1
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|sections2
argument_list|)
expr_stmt|;
name|sections1
operator|.
name|forEach
argument_list|(
name|s
lambda|->
name|assertSectionEquivalent
argument_list|(
name|config1
argument_list|,
name|config2
argument_list|,
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSectionEquivalent (Config config1, Config config2, String section)
specifier|public
specifier|static
name|void
name|assertSectionEquivalent
parameter_list|(
name|Config
name|config1
parameter_list|,
name|Config
name|config2
parameter_list|,
name|String
name|section
parameter_list|)
block|{
name|assertSubsectionEquivalent
argument_list|(
name|config1
argument_list|,
name|config2
argument_list|,
name|section
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|subsections1
init|=
name|config1
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|subsections2
init|=
name|config2
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
literal|"section \"%s\""
argument_list|,
name|section
argument_list|)
operator|.
name|that
argument_list|(
name|subsections1
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|subsections2
argument_list|)
expr_stmt|;
name|subsections1
operator|.
name|forEach
argument_list|(
name|s
lambda|->
name|assertSubsectionEquivalent
argument_list|(
name|config1
argument_list|,
name|config2
argument_list|,
name|section
argument_list|,
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSubsectionEquivalent ( Config config1, Config config2, String section, String subsection)
specifier|private
specifier|static
name|void
name|assertSubsectionEquivalent
parameter_list|(
name|Config
name|config1
parameter_list|,
name|Config
name|config2
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|subsectionNames1
init|=
name|config1
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|subsectionNames2
init|=
name|config2
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|String
operator|.
name|format
argument_list|(
literal|"subsection \"%s\" of section \"%s\""
argument_list|,
name|subsection
argument_list|,
name|section
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
name|name
argument_list|)
operator|.
name|that
argument_list|(
name|subsectionNames1
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|subsectionNames2
argument_list|)
expr_stmt|;
name|subsectionNames1
operator|.
name|forEach
argument_list|(
name|n
lambda|->
name|assertWithMessage
argument_list|(
name|name
argument_list|)
operator|.
name|that
argument_list|(
name|config1
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|n
argument_list|)
argument_list|)
operator|.
name|asList
argument_list|()
operator|.
name|containsExactlyElementsIn
argument_list|(
name|config2
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|n
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|AllProjectsCreatorTestUtil ()
specifier|private
name|AllProjectsCreatorTestUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

