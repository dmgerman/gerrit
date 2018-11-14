begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|LabelType
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
name|LabelValue
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
name|UsedAt
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
name|notedb
operator|.
name|Sequences
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|AllProjectsInput
specifier|public
specifier|abstract
class|class
name|AllProjectsInput
block|{
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|getDefaultCodeReviewLabel ()
specifier|public
specifier|static
name|LabelType
name|getDefaultCodeReviewLabel
parameter_list|()
block|{
name|LabelType
name|type
init|=
operator|new
name|LabelType
argument_list|(
literal|"Code-Review"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
literal|"I would prefer this is not merged as is"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|2
argument_list|,
literal|"This shall not be merged"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|type
operator|.
name|setCopyMinScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|type
operator|.
name|setCopyAllScoresOnTrivialRebase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
comment|/** The administrator group which gets default permissions granted. */
DECL|method|administratorsGroup ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|GroupReference
argument_list|>
name|administratorsGroup
parameter_list|()
function_decl|;
comment|/** The group which gets stream-events permission granted and appropriate properties set. */
DECL|method|batchUsersGroup ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|GroupReference
argument_list|>
name|batchUsersGroup
parameter_list|()
function_decl|;
comment|/** The commit message used when commit the project config change. */
DECL|method|commitMessage ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|commitMessage
parameter_list|()
function_decl|;
comment|/** The first change-id used in this host. */
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|firstChangeIdForNoteDb ()
specifier|public
specifier|abstract
name|int
name|firstChangeIdForNoteDb
parameter_list|()
function_decl|;
comment|/** The "Code-Review" label to be defined in All-Projects. */
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|codeReviewLabel ()
specifier|public
specifier|abstract
name|LabelType
name|codeReviewLabel
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_AllProjectsInput
operator|.
name|Builder
argument_list|()
operator|.
name|codeReviewLabel
argument_list|(
name|getDefaultCodeReviewLabel
argument_list|()
argument_list|)
operator|.
name|firstChangeIdForNoteDb
argument_list|(
name|Sequences
operator|.
name|FIRST_CHANGE_ID
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|administratorsGroup (GroupReference adminGroup)
specifier|public
specifier|abstract
name|Builder
name|administratorsGroup
parameter_list|(
name|GroupReference
name|adminGroup
parameter_list|)
function_decl|;
DECL|method|batchUsersGroup (GroupReference batchGroup)
specifier|public
specifier|abstract
name|Builder
name|batchUsersGroup
parameter_list|(
name|GroupReference
name|batchGroup
parameter_list|)
function_decl|;
DECL|method|commitMessage (String commitMessage)
specifier|public
specifier|abstract
name|Builder
name|commitMessage
parameter_list|(
name|String
name|commitMessage
parameter_list|)
function_decl|;
DECL|method|firstChangeIdForNoteDb (int firstChangeId)
specifier|public
specifier|abstract
name|Builder
name|firstChangeIdForNoteDb
parameter_list|(
name|int
name|firstChangeId
parameter_list|)
function_decl|;
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|codeReviewLabel (LabelType codeReviewLabel)
specifier|public
specifier|abstract
name|Builder
name|codeReviewLabel
parameter_list|(
name|LabelType
name|codeReviewLabel
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|AllProjectsInput
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

