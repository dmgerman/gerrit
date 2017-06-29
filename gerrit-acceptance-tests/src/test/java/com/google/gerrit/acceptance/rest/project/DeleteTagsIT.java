begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|toList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|R_TAGS
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
name|Lists
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|DeleteTagsInput
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
name|projects
operator|.
name|ProjectApi
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
name|projects
operator|.
name|TagInfo
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
name|projects
operator|.
name|TagInput
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
name|ResourceConflictException
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
name|List
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
name|revwalk
operator|.
name|RevCommit
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
annotation|@
name|NoHttpd
DECL|class|DeleteTagsIT
specifier|public
class|class
name|DeleteTagsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|TAGS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|TAGS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"refs/tags/test-1"
argument_list|,
literal|"refs/tags/test-2"
argument_list|,
literal|"refs/tags/test-3"
argument_list|,
literal|"test-4"
argument_list|)
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
for|for
control|(
name|String
name|name
range|:
name|TAGS
control|)
block|{
name|project
argument_list|()
operator|.
name|tag
argument_list|(
name|name
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|TagInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|TAGS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTags ()
specifier|public
name|void
name|deleteTags
parameter_list|()
throws|throws
name|Exception
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|RevCommit
argument_list|>
name|initialRevisions
init|=
name|initialRevisions
argument_list|(
name|TAGS
argument_list|)
decl_stmt|;
name|DeleteTagsInput
name|input
init|=
operator|new
name|DeleteTagsInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|tags
operator|=
name|TAGS
expr_stmt|;
name|project
argument_list|()
operator|.
name|deleteTags
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertTagsDeleted
argument_list|()
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialRevisions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagsForbidden ()
specifier|public
name|void
name|deleteTagsForbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|DeleteTagsInput
name|input
init|=
operator|new
name|DeleteTagsInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|tags
operator|=
name|TAGS
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
try|try
block|{
name|project
argument_list|()
operator|.
name|deleteTags
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ResourceConflictException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|errorMessageForTags
argument_list|(
name|TAGS
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|assertTags
argument_list|(
name|TAGS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagsNotFound ()
specifier|public
name|void
name|deleteTagsNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|DeleteTagsInput
name|input
init|=
operator|new
name|DeleteTagsInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tags
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|TAGS
argument_list|)
decl_stmt|;
name|tags
operator|.
name|add
argument_list|(
literal|"refs/tags/does-not-exist"
argument_list|)
expr_stmt|;
name|input
operator|.
name|tags
operator|=
name|tags
expr_stmt|;
try|try
block|{
name|project
argument_list|()
operator|.
name|deleteTags
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ResourceConflictException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|errorMessageForTags
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"refs/tags/does-not-exist"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTagsDeleted
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagsNotFoundContinue ()
specifier|public
name|void
name|deleteTagsNotFoundContinue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// If it fails on the first tag in the input, it should still
comment|// continue to process the remaining tags.
name|DeleteTagsInput
name|input
init|=
operator|new
name|DeleteTagsInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tags
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"refs/tags/does-not-exist"
argument_list|)
decl_stmt|;
name|tags
operator|.
name|addAll
argument_list|(
name|TAGS
argument_list|)
expr_stmt|;
name|input
operator|.
name|tags
operator|=
name|tags
expr_stmt|;
try|try
block|{
name|project
argument_list|()
operator|.
name|deleteTags
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ResourceConflictException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|errorMessageForTags
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"refs/tags/does-not-exist"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTagsDeleted
argument_list|()
expr_stmt|;
block|}
DECL|method|errorMessageForTags (List<String> tags)
specifier|private
name|String
name|errorMessageForTags
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|tags
parameter_list|)
block|{
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|tag
range|:
name|tags
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"Cannot delete "
argument_list|)
operator|.
name|append
argument_list|(
name|prefixRef
argument_list|(
name|tag
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|": it doesn't exist or you do not have permission "
argument_list|)
operator|.
name|append
argument_list|(
literal|"to delete it\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|message
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|initialRevisions (List<String> tags)
specifier|private
name|HashMap
argument_list|<
name|String
argument_list|,
name|RevCommit
argument_list|>
name|initialRevisions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|tags
parameter_list|)
throws|throws
name|Exception
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|RevCommit
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|tag
range|:
name|tags
control|)
block|{
name|String
name|ref
init|=
name|prefixRef
argument_list|(
name|tag
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|ref
argument_list|,
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|ref
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|assertRefUpdatedEvents (HashMap<String, RevCommit> revisions)
specifier|private
name|void
name|assertRefUpdatedEvents
parameter_list|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|RevCommit
argument_list|>
name|revisions
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|tag
range|:
name|revisions
operator|.
name|keySet
argument_list|()
control|)
block|{
name|RevCommit
name|revision
init|=
name|revisions
operator|.
name|get
argument_list|(
name|prefixRef
argument_list|(
name|tag
argument_list|)
argument_list|)
decl_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|prefixRef
argument_list|(
name|tag
argument_list|)
argument_list|,
literal|null
argument_list|,
name|revision
argument_list|,
name|revision
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|prefixRef (String ref)
specifier|private
name|String
name|prefixRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|startsWith
argument_list|(
name|R_TAGS
argument_list|)
condition|?
name|ref
else|:
name|R_TAGS
operator|+
name|ref
return|;
block|}
DECL|method|project ()
specifier|private
name|ProjectApi
name|project
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertTags (List<String> expected)
specifier|private
name|void
name|assertTags
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|TagInfo
argument_list|>
name|actualTags
init|=
name|project
argument_list|()
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|actualNames
init|=
name|Iterables
operator|.
name|transform
argument_list|(
name|actualTags
argument_list|,
name|b
lambda|->
name|b
operator|.
name|ref
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actualNames
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expected
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|t
lambda|->
name|prefixRef
argument_list|(
name|t
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|assertTagsDeleted ()
specifier|private
name|void
name|assertTagsDeleted
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTags
argument_list|(
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

