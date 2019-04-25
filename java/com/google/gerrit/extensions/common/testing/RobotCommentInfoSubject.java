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
DECL|package|com.google.gerrit.extensions.common.testing
package|package
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
name|testing
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
name|assertAbout
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
name|truth
operator|.
name|ListSubject
operator|.
name|elements
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
name|truth
operator|.
name|FailureMetadata
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
name|truth
operator|.
name|Subject
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
name|FixSuggestionInfo
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
name|truth
operator|.
name|ListSubject
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
DECL|class|RobotCommentInfoSubject
specifier|public
class|class
name|RobotCommentInfoSubject
extends|extends
name|Subject
argument_list|<
name|RobotCommentInfoSubject
argument_list|,
name|RobotCommentInfo
argument_list|>
block|{
DECL|method|assertThatList ( List<RobotCommentInfo> robotCommentInfos)
specifier|public
specifier|static
name|ListSubject
argument_list|<
name|RobotCommentInfoSubject
argument_list|,
name|RobotCommentInfo
argument_list|>
name|assertThatList
parameter_list|(
name|List
argument_list|<
name|RobotCommentInfo
argument_list|>
name|robotCommentInfos
parameter_list|)
block|{
return|return
name|ListSubject
operator|.
name|assertThat
argument_list|(
name|robotCommentInfos
argument_list|,
name|robotComments
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertThat (RobotCommentInfo robotCommentInfo)
specifier|public
specifier|static
name|RobotCommentInfoSubject
name|assertThat
parameter_list|(
name|RobotCommentInfo
name|robotCommentInfo
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|robotComments
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|robotCommentInfo
argument_list|)
return|;
block|}
DECL|method|robotComments ()
specifier|private
specifier|static
name|Factory
argument_list|<
name|RobotCommentInfoSubject
argument_list|,
name|RobotCommentInfo
argument_list|>
name|robotComments
parameter_list|()
block|{
return|return
name|RobotCommentInfoSubject
operator|::
operator|new
return|;
block|}
DECL|method|RobotCommentInfoSubject ( FailureMetadata failureMetadata, RobotCommentInfo robotCommentInfo)
specifier|private
name|RobotCommentInfoSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|RobotCommentInfo
name|robotCommentInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|robotCommentInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|fixSuggestions ()
specifier|public
name|ListSubject
argument_list|<
name|FixSuggestionInfoSubject
argument_list|,
name|FixSuggestionInfo
argument_list|>
name|fixSuggestions
parameter_list|()
block|{
return|return
name|check
argument_list|(
literal|"fixSuggestions()"
argument_list|)
operator|.
name|about
argument_list|(
name|elements
argument_list|()
argument_list|)
operator|.
name|thatCustom
argument_list|(
name|actual
argument_list|()
operator|.
name|fixSuggestions
argument_list|,
name|FixSuggestionInfoSubject
operator|.
name|fixSuggestions
argument_list|()
argument_list|)
return|;
block|}
DECL|method|onlyFixSuggestion ()
specifier|public
name|FixSuggestionInfoSubject
name|onlyFixSuggestion
parameter_list|()
block|{
return|return
name|fixSuggestions
argument_list|()
operator|.
name|onlyElement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

