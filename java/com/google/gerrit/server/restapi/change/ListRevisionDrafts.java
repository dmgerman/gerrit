begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|change
package|;
end_package

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
name|entities
operator|.
name|Comment
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
name|common
operator|.
name|CommentInfo
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
name|Response
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
name|RestReadView
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
name|CommentsUtil
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
name|change
operator|.
name|RevisionResource
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
name|permissions
operator|.
name|PermissionBackendException
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
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|Singleton
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ListRevisionDrafts
specifier|public
class|class
name|ListRevisionDrafts
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|commentJson
specifier|protected
specifier|final
name|Provider
argument_list|<
name|CommentJson
argument_list|>
name|commentJson
decl_stmt|;
DECL|field|commentsUtil
specifier|protected
specifier|final
name|CommentsUtil
name|commentsUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListRevisionDrafts (Provider<CommentJson> commentJson, CommentsUtil commentsUtil)
name|ListRevisionDrafts
parameter_list|(
name|Provider
argument_list|<
name|CommentJson
argument_list|>
name|commentJson
parameter_list|,
name|CommentsUtil
name|commentsUtil
parameter_list|)
block|{
name|this
operator|.
name|commentJson
operator|=
name|commentJson
expr_stmt|;
name|this
operator|.
name|commentsUtil
operator|=
name|commentsUtil
expr_stmt|;
block|}
DECL|method|listComments (RevisionResource rsrc)
specifier|protected
name|Iterable
argument_list|<
name|Comment
argument_list|>
name|listComments
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
block|{
return|return
name|commentsUtil
operator|.
name|draftByPatchSetAuthor
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|id
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
return|;
block|}
DECL|method|includeAuthorInfo ()
specifier|protected
name|boolean
name|includeAuthorInfo
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc)
specifier|public
name|Response
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|commentJson
operator|.
name|get
argument_list|()
operator|.
name|setFillAccounts
argument_list|(
name|includeAuthorInfo
argument_list|()
argument_list|)
operator|.
name|newCommentFormatter
argument_list|()
operator|.
name|format
argument_list|(
name|listComments
argument_list|(
name|rsrc
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getComments (RevisionResource rsrc)
specifier|public
name|ImmutableList
argument_list|<
name|CommentInfo
argument_list|>
name|getComments
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
return|return
name|commentJson
operator|.
name|get
argument_list|()
operator|.
name|setFillAccounts
argument_list|(
name|includeAuthorInfo
argument_list|()
argument_list|)
operator|.
name|newCommentFormatter
argument_list|()
operator|.
name|formatAsList
argument_list|(
name|listComments
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

