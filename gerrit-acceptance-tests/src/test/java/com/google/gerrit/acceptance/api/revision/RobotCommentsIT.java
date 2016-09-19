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
DECL|package|com.google.gerrit.acceptance.api.revision
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|revision
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
name|TruthJUnit
operator|.
name|assume
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
name|PushOneCommit
operator|.
name|FILE_NAME
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ReviewInput
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
name|ReviewInput
operator|.
name|RobotCommentInput
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
name|MethodNotAllowedException
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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|RobotCommentsIT
specifier|public
class|class
name|RobotCommentsIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|comments ()
specifier|public
name|void
name|comments
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|enabled
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|RobotCommentInput
name|in
init|=
name|createRobotCommentInput
argument_list|()
decl_stmt|;
name|ReviewInput
name|reviewInput
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RobotCommentInput
argument_list|>
argument_list|>
name|robotComments
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|robotComments
operator|.
name|put
argument_list|(
name|in
operator|.
name|path
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|reviewInput
operator|.
name|robotComments
operator|=
name|robotComments
expr_stmt|;
name|reviewInput
operator|.
name|message
operator|=
literal|"comment test"
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|reviewInput
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RobotCommentInfo
argument_list|>
argument_list|>
name|out
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|robotComments
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|out
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|RobotCommentInfo
name|comment
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|out
operator|.
name|get
argument_list|(
name|in
operator|.
name|path
argument_list|)
argument_list|)
decl_stmt|;
name|assertRobotComment
argument_list|(
name|comment
argument_list|,
name|in
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RobotCommentInfo
argument_list|>
name|list
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|robotCommentsAsList
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|list
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|RobotCommentInfo
name|comment2
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertRobotComment
argument_list|(
name|comment2
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|RobotCommentInfo
name|comment3
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|robotComment
argument_list|(
name|comment
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertRobotComment
argument_list|(
name|comment3
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|robotCommentsNotSupported ()
specifier|public
name|void
name|robotCommentsNotSupported
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|enabled
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|RobotCommentInput
name|in
init|=
name|createRobotCommentInput
argument_list|()
decl_stmt|;
name|ReviewInput
name|reviewInput
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RobotCommentInput
argument_list|>
argument_list|>
name|robotComments
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|robotComments
operator|.
name|put
argument_list|(
name|FILE_NAME
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|reviewInput
operator|.
name|robotComments
operator|=
name|robotComments
expr_stmt|;
name|reviewInput
operator|.
name|message
operator|=
literal|"comment test"
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
literal|"robot comments not supported"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|reviewInput
argument_list|)
expr_stmt|;
block|}
DECL|method|createRobotCommentInput ()
specifier|private
name|RobotCommentInput
name|createRobotCommentInput
parameter_list|()
block|{
name|RobotCommentInput
name|in
init|=
operator|new
name|RobotCommentInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|robotId
operator|=
literal|"happyRobot"
expr_stmt|;
name|in
operator|.
name|robotRunId
operator|=
literal|"1"
expr_stmt|;
name|in
operator|.
name|url
operator|=
literal|"http://www.happy-robot.com"
expr_stmt|;
name|in
operator|.
name|line
operator|=
literal|1
expr_stmt|;
name|in
operator|.
name|message
operator|=
literal|"nit: trailing whitespace"
expr_stmt|;
name|in
operator|.
name|path
operator|=
name|FILE_NAME
expr_stmt|;
return|return
name|in
return|;
block|}
DECL|method|assertRobotComment (RobotCommentInfo c, RobotCommentInput expected)
specifier|private
name|void
name|assertRobotComment
parameter_list|(
name|RobotCommentInfo
name|c
parameter_list|,
name|RobotCommentInput
name|expected
parameter_list|)
block|{
name|assertRobotComment
argument_list|(
name|c
argument_list|,
name|expected
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRobotComment (RobotCommentInfo c, RobotCommentInput expected, boolean expectPath)
specifier|private
name|void
name|assertRobotComment
parameter_list|(
name|RobotCommentInfo
name|c
parameter_list|,
name|RobotCommentInput
name|expected
parameter_list|,
name|boolean
name|expectPath
parameter_list|)
block|{
name|assertThat
argument_list|(
name|c
operator|.
name|robotId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|robotId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|robotRunId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|robotRunId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|url
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|url
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|line
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|line
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|author
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
if|if
condition|(
name|expectPath
condition|)
block|{
name|assertThat
argument_list|(
name|c
operator|.
name|path
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|c
operator|.
name|path
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

