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
name|RobotCommentApi
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
name|RobotCommentInfo
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
name|RobotCommentResource
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
name|GetRobotComment
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
DECL|class|RobotCommentApiImpl
specifier|public
class|class
name|RobotCommentApiImpl
implements|implements
name|RobotCommentApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (RobotCommentResource c)
name|RobotCommentApiImpl
name|create
parameter_list|(
name|RobotCommentResource
name|c
parameter_list|)
function_decl|;
block|}
DECL|field|getComment
specifier|private
specifier|final
name|GetRobotComment
name|getComment
decl_stmt|;
DECL|field|comment
specifier|private
specifier|final
name|RobotCommentResource
name|comment
decl_stmt|;
annotation|@
name|Inject
DECL|method|RobotCommentApiImpl (GetRobotComment getComment, @Assisted RobotCommentResource comment)
name|RobotCommentApiImpl
parameter_list|(
name|GetRobotComment
name|getComment
parameter_list|,
annotation|@
name|Assisted
name|RobotCommentResource
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
name|comment
operator|=
name|comment
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|RobotCommentInfo
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
literal|"Cannot retrieve robot comment"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

