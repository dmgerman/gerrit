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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|FluentIterable
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
name|ProjectApi
operator|.
name|ListRefsRequest
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
name|TagApi
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
name|AuthException
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
name|BadRequestException
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
name|MethodNotAllowedException
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|TagsIT
specifier|public
class|class
name|TagsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|testTags
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|testTags
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"tag-A"
argument_list|,
literal|"tag-B"
argument_list|,
literal|"tag-C"
argument_list|,
literal|"tag-D"
argument_list|,
literal|"tag-E"
argument_list|,
literal|"tag-F"
argument_list|,
literal|"tag-G"
argument_list|,
literal|"tag-H"
argument_list|)
decl_stmt|;
DECL|field|SIGNED_ANNOTATION
specifier|private
specifier|static
specifier|final
name|String
name|SIGNED_ANNOTATION
init|=
literal|"annotation\n"
operator|+
literal|"-----BEGIN PGP SIGNATURE-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"iQEcBAABAgAGBQJVeGg5AAoJEPfTicJkUdPkUggH/RKAeI9/i/LduuiqrL/SSdIa\n"
operator|+
literal|"9tYaSqJKLbXz63M/AW4Sp+4u+dVCQvnAt/a35CVEnpZz6hN4Kn/tiswOWVJf4CO7\n"
operator|+
literal|"htNubGs5ZMwvD6sLYqKAnrM3WxV/2TbbjzjZW6Jkidz3jz/WRT4SmjGYiEO7aA+V\n"
operator|+
literal|"4ZdIS9f7sW5VsHHYlNThCA7vH8Uu48bUovFXyQlPTX0pToSgrWV3JnTxDNxfn3iG\n"
operator|+
literal|"IL0zTY/qwVCdXgFownLcs6J050xrrBWIKqfcWr3u4D2aCLyR0v+S/KArr7ulZygY\n"
operator|+
literal|"+SOklImn8TAZiNxhWtA6ens66IiammUkZYFv7SSzoPLFZT4dC84SmGPWgf94NoQ=\n"
operator|+
literal|"=XFeC\n"
operator|+
literal|"-----END PGP SIGNATURE-----"
decl_stmt|;
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
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTagOfNonExistingProject ()
specifier|public
name|void
name|getTagOfNonExistingProject
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
literal|"refs/*"
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
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
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTagOfNonVisibleProject ()
specifier|public
name|void
name|getTagOfNonVisibleProject
parameter_list|()
throws|throws
name|Exception
block|{
name|blockRead
argument_list|(
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
name|createTags
argument_list|()
expr_stmt|;
comment|// No options
name|List
argument_list|<
name|TagInfo
argument_list|>
name|result
init|=
name|getTags
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|testTags
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// With start option
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withStart
argument_list|(
literal|1
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|testTags
argument_list|)
operator|.
name|skip
argument_list|(
literal|1
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// With limit option
name|int
name|limit
init|=
name|testTags
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withLimit
argument_list|(
name|limit
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|testTags
argument_list|)
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// With both start and limit
name|limit
operator|=
name|testTags
operator|.
name|size
argument_list|()
operator|-
literal|3
expr_stmt|;
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withStart
argument_list|(
literal|1
argument_list|)
operator|.
name|withLimit
argument_list|(
name|limit
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|testTags
argument_list|)
operator|.
name|skip
argument_list|(
literal|1
argument_list|)
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// With regular expression filter
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withRegex
argument_list|(
literal|"^tag-[C|D]$"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"tag-C"
argument_list|,
literal|"tag-D"
argument_list|)
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withRegex
argument_list|(
literal|"^tag-[c|d]$"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// With substring filter
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"tag-"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|testTags
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|getTags
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"ag-B"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertTagList
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"tag-B"
argument_list|)
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// With conflicting options
name|assertBadRequest
argument_list|(
name|getTags
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"ag-B"
argument_list|)
operator|.
name|withRegex
argument_list|(
literal|"^tag-[c|d]$"
argument_list|)
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
name|grantTagPermissions
argument_list|()
expr_stmt|;
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
name|PushOneCommit
operator|.
name|Result
name|r1
init|=
name|push1
operator|.
name|to
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|TagInput
name|tag1
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|tag1
operator|.
name|ref
operator|=
literal|"v1.0"
expr_stmt|;
name|tag1
operator|.
name|revision
operator|=
name|r1
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|TagInfo
name|result
init|=
name|tag
argument_list|(
name|tag1
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|tag1
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
name|tag1
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tag1
operator|.
name|revision
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/hidden"
argument_list|)
expr_stmt|;
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
name|PushOneCommit
operator|.
name|Result
name|r2
init|=
name|push2
operator|.
name|to
argument_list|(
literal|"refs/heads/hidden"
argument_list|)
decl_stmt|;
name|r2
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|TagInput
name|tag2
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|tag2
operator|.
name|ref
operator|=
literal|"v2.0"
expr_stmt|;
name|tag2
operator|.
name|revision
operator|=
name|r2
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|result
operator|=
name|tag
argument_list|(
name|tag2
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|tag2
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
name|tag2
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tag2
operator|.
name|revision
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TagInfo
argument_list|>
name|tags
init|=
name|getTags
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|tags
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tags
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
name|R_TAGS
operator|+
name|tag1
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tags
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
name|tag1
operator|.
name|revision
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tags
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
name|R_TAGS
operator|+
name|tag2
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tags
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
name|tag2
operator|.
name|revision
argument_list|)
expr_stmt|;
name|blockRead
argument_list|(
literal|"refs/heads/hidden"
argument_list|)
expr_stmt|;
name|tags
operator|=
name|getTags
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|tags
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tags
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
name|R_TAGS
operator|+
name|tag1
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tags
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
name|tag1
operator|.
name|revision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lightweightTag ()
specifier|public
name|void
name|lightweightTag
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
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
name|testRepo
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"v1.0"
expr_stmt|;
name|input
operator|.
name|revision
operator|=
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|TagInfo
name|result
init|=
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
name|input
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|revision
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|canDelete
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|created
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|timestamp
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"refs/tags/v2.0"
expr_stmt|;
name|result
operator|=
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|revision
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|canDelete
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|created
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|timestamp
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|result
operator|=
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|canDelete
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|result
operator|.
name|ref
argument_list|,
literal|null
argument_list|,
name|result
operator|.
name|revision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|annotatedTag ()
specifier|public
name|void
name|annotatedTag
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
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
name|testRepo
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"v1.0"
expr_stmt|;
name|input
operator|.
name|revision
operator|=
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|input
operator|.
name|message
operator|=
literal|"annotation message"
expr_stmt|;
name|TagInfo
name|result
init|=
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
name|input
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|object
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|revision
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|tagger
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|fullName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|tagger
operator|.
name|email
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|email
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|created
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|result
operator|.
name|tagger
operator|.
name|date
argument_list|)
expr_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|result
operator|.
name|ref
argument_list|,
literal|null
argument_list|,
name|result
operator|.
name|revision
argument_list|)
expr_stmt|;
comment|// A second tag pushed on the same ref should have the same ref
name|TagInput
name|input2
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input2
operator|.
name|ref
operator|=
literal|"refs/tags/v2.0"
expr_stmt|;
name|input2
operator|.
name|revision
operator|=
name|input
operator|.
name|revision
expr_stmt|;
name|input2
operator|.
name|message
operator|=
literal|"second annotation message"
expr_stmt|;
name|TagInfo
name|result2
init|=
name|tag
argument_list|(
name|input2
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input2
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input2
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|object
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input2
operator|.
name|revision
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input2
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|tagger
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|fullName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|tagger
operator|.
name|email
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|email
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|created
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|result2
operator|.
name|tagger
operator|.
name|date
argument_list|)
expr_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|result2
operator|.
name|ref
argument_list|,
literal|null
argument_list|,
name|result2
operator|.
name|revision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createExistingTag ()
specifier|public
name|void
name|createExistingTag
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"test"
expr_stmt|;
name|TagInfo
name|result
init|=
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
literal|"test"
argument_list|)
expr_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"refs/tags/test"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"tag \""
operator|+
name|R_TAGS
operator|+
literal|"test\" already exists"
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createTagNotAllowed ()
specifier|public
name|void
name|createTagNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|block
argument_list|(
name|R_TAGS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"test"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"not permitted: create"
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAnnotatedTagNotAllowed ()
specifier|public
name|void
name|createAnnotatedTagNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|block
argument_list|(
name|R_TAGS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE_TAG
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"test"
expr_stmt|;
name|input
operator|.
name|message
operator|=
literal|"annotation"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Cannot create annotated tag \""
operator|+
name|R_TAGS
operator|+
literal|"test\""
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSignedTagNotSupported ()
specifier|public
name|void
name|createSignedTagNotSupported
parameter_list|()
throws|throws
name|Exception
block|{
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"test"
expr_stmt|;
name|input
operator|.
name|message
operator|=
name|SIGNED_ANNOTATION
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Cannot create signed tag \""
operator|+
name|R_TAGS
operator|+
literal|"test\""
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mismatchedInput ()
specifier|public
name|void
name|mismatchedInput
parameter_list|()
throws|throws
name|Exception
block|{
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"test"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"ref must match URL"
argument_list|)
expr_stmt|;
name|tag
argument_list|(
literal|"TEST"
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidTagName ()
specifier|public
name|void
name|invalidTagName
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"refs/heads/test"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"invalid tag name \""
operator|+
name|input
operator|.
name|ref
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidTagNameOnlySlashes ()
specifier|public
name|void
name|invalidTagNameOnlySlashes
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"//"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"invalid tag name \"refs/tags/\""
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidBaseRevision ()
specifier|public
name|void
name|invalidBaseRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
expr_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"test"
expr_stmt|;
name|input
operator|.
name|revision
operator|=
literal|"abcdefg"
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Invalid base revision"
argument_list|)
expr_stmt|;
name|tag
argument_list|(
name|input
operator|.
name|ref
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
DECL|method|assertTagList (FluentIterable<String> expected, List<TagInfo> actual)
specifier|private
name|void
name|assertTagList
parameter_list|(
name|FluentIterable
argument_list|<
name|String
argument_list|>
name|expected
parameter_list|,
name|List
argument_list|<
name|TagInfo
argument_list|>
name|actual
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|actual
argument_list|)
operator|.
name|hasSize
argument_list|(
name|expected
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expected
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|TagInfo
name|info
init|=
name|actual
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|created
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
name|expected
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createTags ()
specifier|private
name|void
name|createTags
parameter_list|()
throws|throws
name|Exception
block|{
name|grantTagPermissions
argument_list|()
expr_stmt|;
name|String
name|revision
init|=
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|TagInput
name|input
init|=
operator|new
name|TagInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
for|for
control|(
name|String
name|tagname
range|:
name|testTags
control|)
block|{
name|TagInfo
name|result
init|=
name|tag
argument_list|(
name|tagname
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|revision
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|R_TAGS
operator|+
name|tagname
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTags ()
specifier|private
name|ListRefsRequest
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
return|;
block|}
DECL|method|tag (String tagname)
specifier|private
name|TagApi
name|tag
parameter_list|(
name|String
name|tagname
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
name|tagname
argument_list|)
return|;
block|}
DECL|method|timestamp (PushOneCommit.Result r)
specifier|private
name|Timestamp
name|timestamp
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|)
block|{
return|return
operator|new
name|Timestamp
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|getCommitterIdent
argument_list|()
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertBadRequest (ListRefsRequest<TagInfo> req)
specifier|private
name|void
name|assertBadRequest
parameter_list|(
name|ListRefsRequest
argument_list|<
name|TagInfo
argument_list|>
name|req
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|req
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected BadRequestException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadRequestException
name|e
parameter_list|)
block|{
comment|// Expected
block|}
block|}
DECL|method|grantTagPermissions ()
specifier|private
name|void
name|grantTagPermissions
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
name|R_TAGS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
name|R_TAGS
operator|+
literal|""
argument_list|,
name|Permission
operator|.
name|DELETE
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
name|R_TAGS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE_TAG
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
name|R_TAGS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE_SIGNED_TAG
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

