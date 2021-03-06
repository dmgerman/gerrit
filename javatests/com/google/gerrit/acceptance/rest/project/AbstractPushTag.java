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
name|assertWithMessage
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|createAnnotatedTag
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|deleteRef
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|updateAnnotatedTag
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
name|acceptance
operator|.
name|rest
operator|.
name|project
operator|.
name|AbstractPushTag
operator|.
name|TagType
operator|.
name|ANNOTATED
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
name|acceptance
operator|.
name|rest
operator|.
name|project
operator|.
name|AbstractPushTag
operator|.
name|TagType
operator|.
name|LIGHTWEIGHT
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|permissionKey
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
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
name|GitUtil
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
name|testsuite
operator|.
name|project
operator|.
name|ProjectOperations
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
name|entities
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
name|revwalk
operator|.
name|RevCommit
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
name|transport
operator|.
name|PushResult
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
name|transport
operator|.
name|RemoteRefUpdate
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
name|transport
operator|.
name|RemoteRefUpdate
operator|.
name|Status
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
DECL|class|AbstractPushTag
specifier|public
specifier|abstract
class|class
name|AbstractPushTag
extends|extends
name|AbstractDaemonTest
block|{
DECL|enum|TagType
enum|enum
name|TagType
block|{
DECL|enumConstant|LIGHTWEIGHT
name|LIGHTWEIGHT
parameter_list|(
name|Permission
operator|.
name|CREATE
parameter_list|)
operator|,
DECL|enumConstant|ANNOTATED
constructor|ANNOTATED(Permission.CREATE_TAG
block|)
enum|;
DECL|field|createPermission
specifier|final
name|String
name|createPermission
decl_stmt|;
DECL|method|TagType (String createPermission)
name|TagType
parameter_list|(
name|String
name|createPermission
parameter_list|)
block|{
name|this
operator|.
name|createPermission
operator|=
name|createPermission
expr_stmt|;
block|}
block|}
end_class

begin_decl_stmt
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|initialHead
specifier|private
name|RevCommit
name|initialHead
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|tagType
specifier|private
name|TagType
name|tagType
decl_stmt|;
end_decl_stmt

begin_function
annotation|@
name|Before
DECL|method|setUpTestEnvironment ()
specifier|public
name|void
name|setUpTestEnvironment
parameter_list|()
throws|throws
name|Exception
block|{
comment|// clone with user to avoid inherited tag permissions of admin user
name|testRepo
operator|=
name|cloneProject
argument_list|(
name|project
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|initialHead
operator|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
expr_stmt|;
name|tagType
operator|=
name|getTagType
argument_list|()
expr_stmt|;
block|}
end_function

begin_function_decl
DECL|method|getTagType ()
specifier|protected
specifier|abstract
name|TagType
name|getTagType
parameter_list|()
function_decl|;
end_function_decl

begin_function
annotation|@
name|Test
DECL|method|createTagForExistingCommit ()
specifier|public
name|void
name|createTagForExistingCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowTagCreation
argument_list|()
expr_stmt|;
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|allowPushOnRefsTags
argument_list|()
expr_stmt|;
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|createTagForNewCommit ()
specifier|public
name|void
name|createTagForNewCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|pushTagForNewCommit
argument_list|(
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowTagCreation
argument_list|()
expr_stmt|;
name|pushTagForNewCommit
argument_list|(
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowPushOnRefsTags
argument_list|()
expr_stmt|;
name|pushTagForNewCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|fastForward ()
specifier|public
name|void
name|fastForward
parameter_list|()
throws|throws
name|Exception
block|{
name|allowTagCreation
argument_list|()
expr_stmt|;
name|String
name|tagName
init|=
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
decl_stmt|;
name|fastForwardTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|fastForwardTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowTagDeletion
argument_list|()
expr_stmt|;
name|fastForwardTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|fastForwardTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowPushOnRefsTags
argument_list|()
expr_stmt|;
name|Status
name|expectedStatus
init|=
name|tagType
operator|==
name|ANNOTATED
condition|?
name|Status
operator|.
name|REJECTED_OTHER_REASON
else|:
name|Status
operator|.
name|OK
decl_stmt|;
name|fastForwardTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|expectedStatus
argument_list|)
expr_stmt|;
name|fastForwardTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|expectedStatus
argument_list|)
expr_stmt|;
name|allowForcePushOnRefsTags
argument_list|()
expr_stmt|;
name|fastForwardTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|fastForwardTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|forceUpdate ()
specifier|public
name|void
name|forceUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|allowTagCreation
argument_list|()
expr_stmt|;
name|String
name|tagName
init|=
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
decl_stmt|;
name|forceUpdateTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|forceUpdateTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowPushOnRefsTags
argument_list|()
expr_stmt|;
name|forceUpdateTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|forceUpdateTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowTagDeletion
argument_list|()
expr_stmt|;
name|forceUpdateTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|forceUpdateTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowForcePushOnRefsTags
argument_list|()
expr_stmt|;
name|forceUpdateTagToExistingCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|forceUpdateTagToNewCommit
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|delete ()
specifier|public
name|void
name|delete
parameter_list|()
throws|throws
name|Exception
block|{
name|allowTagCreation
argument_list|()
expr_stmt|;
name|String
name|tagName
init|=
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
decl_stmt|;
name|pushTagDeletion
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowPushOnRefsTags
argument_list|()
expr_stmt|;
name|pushTagDeletion
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|allowForcePushOnRefsTags
argument_list|()
expr_stmt|;
name|tagName
operator|=
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|pushTagDeletion
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
name|allowTagDeletion
argument_list|()
expr_stmt|;
name|tagName
operator|=
name|pushTagForExistingCommit
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
name|pushTagDeletion
argument_list|(
name|tagName
argument_list|,
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|pushTagForExistingCommit (Status expectedStatus)
specifier|private
name|String
name|pushTagForExistingCommit
parameter_list|(
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|pushTag
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|expectedStatus
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|pushTagForNewCommit (Status expectedStatus)
specifier|private
name|String
name|pushTagForNewCommit
parameter_list|(
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|pushTag
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|expectedStatus
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|fastForwardTagToExistingCommit (String tagName, Status expectedStatus)
specifier|private
name|void
name|fastForwardTagToExistingCommit
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|pushTag
argument_list|(
name|tagName
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|expectedStatus
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|fastForwardTagToNewCommit (String tagName, Status expectedStatus)
specifier|private
name|void
name|fastForwardTagToNewCommit
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|pushTag
argument_list|(
name|tagName
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|expectedStatus
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|forceUpdateTagToExistingCommit (String tagName, Status expectedStatus)
specifier|private
name|void
name|forceUpdateTagToExistingCommit
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|pushTag
argument_list|(
name|tagName
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|expectedStatus
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|forceUpdateTagToNewCommit (String tagName, Status expectedStatus)
specifier|private
name|void
name|forceUpdateTagToNewCommit
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|pushTag
argument_list|(
name|tagName
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|expectedStatus
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|pushTag (String tagName, boolean newCommit, boolean force, Status expectedStatus)
specifier|private
name|String
name|pushTag
parameter_list|(
name|String
name|tagName
parameter_list|,
name|boolean
name|newCommit
parameter_list|,
name|boolean
name|force
parameter_list|,
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|force
condition|)
block|{
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
block|}
name|commit
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
literal|"subject"
argument_list|)
expr_stmt|;
name|boolean
name|createTag
init|=
name|tagName
operator|==
literal|null
decl_stmt|;
name|tagName
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|tagName
argument_list|,
literal|"v1_"
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|tagType
condition|)
block|{
case|case
name|LIGHTWEIGHT
case|:
break|break;
case|case
name|ANNOTATED
case|:
if|if
condition|(
name|createTag
condition|)
block|{
name|createAnnotatedTag
argument_list|(
name|testRepo
argument_list|,
name|tagName
argument_list|,
name|user
operator|.
name|newIdent
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateAnnotatedTag
argument_list|(
name|testRepo
argument_list|,
name|tagName
argument_list|,
name|user
operator|.
name|newIdent
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unexpected tag type: "
operator|+
name|tagType
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|newCommit
condition|)
block|{
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/for/refs/heads/master"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master%submit"
argument_list|)
expr_stmt|;
block|}
name|String
name|tagRef
init|=
name|tagRef
argument_list|(
name|tagName
argument_list|)
decl_stmt|;
name|PushResult
name|r
init|=
name|tagType
operator|==
name|LIGHTWEIGHT
condition|?
name|pushHead
argument_list|(
name|testRepo
argument_list|,
name|tagRef
argument_list|,
literal|false
argument_list|,
name|force
argument_list|)
else|:
name|GitUtil
operator|.
name|pushTag
argument_list|(
name|testRepo
argument_list|,
name|tagName
argument_list|,
operator|!
name|createTag
argument_list|)
decl_stmt|;
name|RemoteRefUpdate
name|refUpdate
init|=
name|r
operator|.
name|getRemoteUpdate
argument_list|(
name|tagRef
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
name|tagType
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|refUpdate
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedStatus
argument_list|)
expr_stmt|;
return|return
name|tagName
return|;
block|}
end_function

begin_function
DECL|method|pushTagDeletion (String tagName, Status expectedStatus)
specifier|private
name|void
name|pushTagDeletion
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Status
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|tagRef
init|=
name|tagRef
argument_list|(
name|tagName
argument_list|)
decl_stmt|;
name|PushResult
name|r
init|=
name|deleteRef
argument_list|(
name|testRepo
argument_list|,
name|tagRef
argument_list|)
decl_stmt|;
name|RemoteRefUpdate
name|refUpdate
init|=
name|r
operator|.
name|getRemoteUpdate
argument_list|(
name|tagRef
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
name|tagType
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|refUpdate
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedStatus
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|allowTagCreation ()
specifier|private
name|void
name|allowTagCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
argument_list|(
name|tagType
operator|.
name|createPermission
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/tags/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|allowPushOnRefsTags ()
specifier|private
name|void
name|allowPushOnRefsTags
parameter_list|()
throws|throws
name|Exception
block|{
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/tags/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|allowForcePushOnRefsTags ()
specifier|private
name|void
name|allowForcePushOnRefsTags
parameter_list|()
throws|throws
name|Exception
block|{
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/tags/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|force
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|allowTagDeletion ()
specifier|private
name|void
name|allowTagDeletion
parameter_list|()
throws|throws
name|Exception
block|{
name|removePushFromRefsTags
argument_list|()
expr_stmt|;
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
argument_list|(
name|Permission
operator|.
name|DELETE
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/tags/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|force
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|removePushFromRefsTags ()
specifier|private
name|void
name|removePushFromRefsTags
parameter_list|()
throws|throws
name|Exception
block|{
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|remove
argument_list|(
name|permissionKey
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/tags/*"
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|commit (PersonIdent ident, String subject)
specifier|private
name|void
name|commit
parameter_list|(
name|PersonIdent
name|ident
parameter_list|,
name|String
name|subject
parameter_list|)
throws|throws
name|Exception
block|{
name|commitBuilder
argument_list|()
operator|.
name|ident
argument_list|(
name|ident
argument_list|)
operator|.
name|message
argument_list|(
name|subject
operator|+
literal|" ("
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
literal|")"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|tagRef (String tagName)
specifier|private
specifier|static
name|String
name|tagRef
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
return|return
name|RefNames
operator|.
name|REFS_TAGS
operator|+
name|tagName
return|;
block|}
end_function

unit|}
end_unit

