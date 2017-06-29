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
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
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
name|RestResponse
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
name|ResourceNotFoundException
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
DECL|class|DeleteTagIT
specifier|public
class|class
name|DeleteTagIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|TAG
specifier|private
specifier|final
name|String
name|TAG
init|=
literal|"refs/tags/test"
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
name|tag
argument_list|()
operator|.
name|create
argument_list|(
operator|new
name|TagInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTag_Forbidden ()
specifier|public
name|void
name|deleteTag_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertDeleteForbidden
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByAdmin ()
specifier|public
name|void
name|deleteTagByAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|assertDeleteSucceeds
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByProjectOwner ()
specifier|public
name|void
name|deleteTagByProjectOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|grantOwner
argument_list|()
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertDeleteSucceeds
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByAdminForcePushBlocked ()
specifier|public
name|void
name|deleteTagByAdminForcePushBlocked
parameter_list|()
throws|throws
name|Exception
block|{
name|blockForcePush
argument_list|()
expr_stmt|;
name|assertDeleteSucceeds
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByProjectOwnerForcePushBlocked_Forbidden ()
specifier|public
name|void
name|deleteTagByProjectOwnerForcePushBlocked_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|grantOwner
argument_list|()
expr_stmt|;
name|blockForcePush
argument_list|()
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertDeleteForbidden
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByUserWithForcePushPermission ()
specifier|public
name|void
name|deleteTagByUserWithForcePushPermission
parameter_list|()
throws|throws
name|Exception
block|{
name|grantForcePush
argument_list|()
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertDeleteSucceeds
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByUserWithDeletePermission ()
specifier|public
name|void
name|deleteTagByUserWithDeletePermission
parameter_list|()
throws|throws
name|Exception
block|{
name|grantDelete
argument_list|()
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertDeleteSucceeds
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTagByRestWithoutRefsTagsPrefix ()
specifier|public
name|void
name|deleteTagByRestWithoutRefsTagsPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|grantDelete
argument_list|()
expr_stmt|;
name|String
name|ref
init|=
name|TAG
operator|.
name|substring
argument_list|(
name|R_TAGS
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|delete
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/tags/"
operator|+
name|ref
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
block|}
DECL|method|blockForcePush ()
specifier|private
name|void
name|blockForcePush
parameter_list|()
throws|throws
name|Exception
block|{
name|block
argument_list|(
literal|"refs/tags/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|ANONYMOUS_USERS
argument_list|)
operator|.
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|grantForcePush ()
specifier|private
name|void
name|grantForcePush
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/tags/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|true
argument_list|,
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
block|}
DECL|method|grantDelete ()
specifier|private
name|void
name|grantDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|Permission
operator|.
name|DELETE
argument_list|,
name|ANONYMOUS_USERS
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
block|}
DECL|method|grantOwner ()
specifier|private
name|void
name|grantOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|Permission
operator|.
name|OWNER
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/tags/*"
argument_list|)
expr_stmt|;
block|}
DECL|method|tag ()
specifier|private
name|TagApi
name|tag
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
name|tag
argument_list|(
name|TAG
argument_list|)
return|;
block|}
DECL|method|assertDeleteSucceeds ()
specifier|private
name|void
name|assertDeleteSucceeds
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tagRev
init|=
name|tag
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|revision
decl_stmt|;
name|tag
argument_list|()
operator|.
name|delete
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
name|TAG
argument_list|,
literal|null
argument_list|,
name|tagRev
argument_list|,
name|tagRev
argument_list|,
literal|null
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
name|tag
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
DECL|method|assertDeleteForbidden ()
specifier|private
name|void
name|assertDeleteForbidden
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"delete not permitted"
argument_list|)
expr_stmt|;
name|tag
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

