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
DECL|package|com.google.gerrit.server.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|changes
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
name|api
operator|.
name|ApiUtil
operator|.
name|asRestApiException
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
name|changes
operator|.
name|CommentApi
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
name|changes
operator|.
name|DeleteCommentInput
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
name|RestApiException
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
name|CommentResource
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
name|restapi
operator|.
name|change
operator|.
name|DeleteComment
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
name|restapi
operator|.
name|change
operator|.
name|GetComment
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_class
DECL|class|CommentApiImpl
class|class
name|CommentApiImpl
implements|implements
name|CommentApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (CommentResource c)
name|CommentApiImpl
name|create
parameter_list|(
name|CommentResource
name|c
parameter_list|)
function_decl|;
block|}
DECL|field|getComment
specifier|private
specifier|final
name|GetComment
name|getComment
decl_stmt|;
DECL|field|deleteComment
specifier|private
specifier|final
name|DeleteComment
name|deleteComment
decl_stmt|;
DECL|field|comment
specifier|private
specifier|final
name|CommentResource
name|comment
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommentApiImpl ( GetComment getComment, DeleteComment deleteComment, @Assisted CommentResource comment)
name|CommentApiImpl
parameter_list|(
name|GetComment
name|getComment
parameter_list|,
name|DeleteComment
name|deleteComment
parameter_list|,
annotation|@
name|Assisted
name|CommentResource
name|comment
parameter_list|)
block|{
name|this
operator|.
name|getComment
operator|=
name|getComment
expr_stmt|;
name|this
operator|.
name|deleteComment
operator|=
name|deleteComment
expr_stmt|;
name|this
operator|.
name|comment
operator|=
name|comment
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|CommentInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|getComment
operator|.
name|apply
argument_list|(
name|comment
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot retrieve comment"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|delete (DeleteCommentInput input)
specifier|public
name|CommentInfo
name|delete
parameter_list|(
name|DeleteCommentInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|deleteComment
operator|.
name|apply
argument_list|(
name|comment
argument_list|,
name|input
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot delete comment"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

