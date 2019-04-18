begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|ChangeResource
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
DECL|class|ListChangeComments
specifier|public
class|class
name|ListChangeComments
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|commentJson
specifier|private
specifier|final
name|Provider
argument_list|<
name|CommentJson
argument_list|>
name|commentJson
decl_stmt|;
DECL|field|commentsUtil
specifier|private
specifier|final
name|CommentsUtil
name|commentsUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListChangeComments ( ChangeData.Factory changeDataFactory, Provider<CommentJson> commentJson, CommentsUtil commentsUtil)
name|ListChangeComments
parameter_list|(
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
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
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
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
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|commentJson
operator|.
name|get
argument_list|()
operator|.
name|setFillAccounts
argument_list|(
literal|true
argument_list|)
operator|.
name|setFillPatchSet
argument_list|(
literal|true
argument_list|)
operator|.
name|newCommentFormatter
argument_list|()
operator|.
name|format
argument_list|(
name|commentsUtil
operator|.
name|publishedByChange
argument_list|(
name|cd
operator|.
name|notes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

