begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|PushOneCommit
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|List
import|;
end_import

begin_class
DECL|class|TagsIT
specifier|public
class|class
name|TagsIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|listTagsOfNonExistingProject ()
specifier|public
name|void
name|listTagsOfNonExistingProject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/non-existing/tags"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listTagsOfNonExistingProjectWithApi ()
specifier|public
name|void
name|listTagsOfNonExistingProjectWithApi
parameter_list|()
throws|throws
name|Exception
block|{
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
literal|"does-not-exist"
argument_list|)
operator|.
name|tags
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
literal|"does-not-exist"
argument_list|)
operator|.
name|tag
argument_list|(
literal|"tag"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listTagsOfNonVisibleProject ()
specifier|public
name|void
name|listTagsOfNonVisibleProject
parameter_list|()
throws|throws
name|Exception
block|{
name|blockRead
argument_list|(
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|userSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/tags"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listTagsOfNonVisibleProjectWithApi ()
specifier|public
name|void
name|listTagsOfNonVisibleProjectWithApi
parameter_list|()
throws|throws
name|Exception
block|{
name|blockRead
argument_list|(
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
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
operator|.
name|tags
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
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
operator|.
name|tag
argument_list|(
literal|"tag"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listTags ()
specifier|public
name|void
name|listTags
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|project
argument_list|,
literal|"refs/for/refs/heads/master"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|CREATE
argument_list|,
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|,
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Tag
name|tag1
init|=
operator|new
name|PushOneCommit
operator|.
name|Tag
argument_list|(
literal|"v1.0"
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push1
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
name|testRepo
argument_list|)
decl_stmt|;
name|push1
operator|.
name|setTag
argument_list|(
name|tag1
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r1
init|=
name|push1
operator|.
name|to
argument_list|(
literal|"refs/for/master%submit"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|AnnotatedTag
name|tag2
init|=
operator|new
name|PushOneCommit
operator|.
name|AnnotatedTag
argument_list|(
literal|"v2.0"
argument_list|,
literal|"annotation"
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push2
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
name|testRepo
argument_list|)
decl_stmt|;
name|push2
operator|.
name|setTag
argument_list|(
name|tag2
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r2
init|=
name|push2
operator|.
name|to
argument_list|(
literal|"refs/for/master%submit"
argument_list|)
decl_stmt|;
name|r2
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|TagInfo
argument_list|>
name|result
init|=
name|getTags
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|TagInfo
name|t
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/"
operator|+
name|tag1
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|r1
operator|.
name|getCommitId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|t
operator|=
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/"
operator|+
name|tag2
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|object
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|r2
operator|.
name|getCommitId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tag2
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|tagger
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tag2
operator|.
name|tagger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|t
operator|.
name|tagger
operator|.
name|email
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tag2
operator|.
name|tagger
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listTagsOfNonVisibleBranch ()
specifier|public
name|void
name|listTagsOfNonVisibleBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|project
argument_list|,
literal|"refs/for/refs/heads/master"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|project
argument_list|,
literal|"refs/for/refs/heads/hidden"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|CREATE
argument_list|,
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|,
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Tag
name|tag1
init|=
operator|new
name|PushOneCommit
operator|.
name|Tag
argument_list|(
literal|"v1.0"
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push1
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
name|testRepo
argument_list|)
decl_stmt|;
name|push1
operator|.
name|setTag
argument_list|(
name|tag1
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r1
init|=
name|push1
operator|.
name|to
argument_list|(
literal|"refs/for/master%submit"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/hidden"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Tag
name|tag2
init|=
operator|new
name|PushOneCommit
operator|.
name|Tag
argument_list|(
literal|"v2.0"
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push2
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
name|testRepo
argument_list|)
decl_stmt|;
name|push2
operator|.
name|setTag
argument_list|(
name|tag2
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r2
init|=
name|push2
operator|.
name|to
argument_list|(
literal|"refs/for/hidden%submit"
argument_list|)
decl_stmt|;
name|r2
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|TagInfo
argument_list|>
name|result
init|=
name|getTags
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/"
operator|+
name|tag1
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|r1
operator|.
name|getCommitId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/"
operator|+
name|tag2
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|r2
operator|.
name|getCommitId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|blockRead
argument_list|(
name|project
argument_list|,
literal|"refs/heads/hidden"
argument_list|)
expr_stmt|;
name|result
operator|=
name|getTags
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/"
operator|+
name|tag1
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|r1
operator|.
name|getCommitId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTag ()
specifier|public
name|void
name|getTag
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|project
argument_list|,
literal|"refs/for/refs/heads/master"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|CREATE
argument_list|,
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|,
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Tag
name|tag1
init|=
operator|new
name|PushOneCommit
operator|.
name|Tag
argument_list|(
literal|"v1.0"
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push1
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
name|testRepo
argument_list|)
decl_stmt|;
name|push1
operator|.
name|setTag
argument_list|(
name|tag1
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r1
init|=
name|push1
operator|.
name|to
argument_list|(
literal|"refs/for/master%submit"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|TagInfo
name|tagInfo
init|=
name|getTag
argument_list|(
name|tag1
operator|.
name|name
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|tagInfo
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/"
operator|+
name|tag1
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tagInfo
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|r1
operator|.
name|getCommitId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getTag (String ref)
specifier|private
name|TagInfo
name|getTag
parameter_list|(
name|String
name|ref
parameter_list|)
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
operator|.
name|tag
argument_list|(
name|ref
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getTags ()
specifier|private
name|List
argument_list|<
name|TagInfo
argument_list|>
name|getTags
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
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

