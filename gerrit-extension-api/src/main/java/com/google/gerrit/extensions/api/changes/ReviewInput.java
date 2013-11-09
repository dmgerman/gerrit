begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
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
name|restapi
operator|.
name|DefaultInput
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

begin_comment
comment|/** Input passed to {@code POST /changes/{id}/revisions/{id}/review}. */
end_comment

begin_class
DECL|class|ReviewInput
specifier|public
class|class
name|ReviewInput
block|{
annotation|@
name|DefaultInput
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
DECL|field|labels
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|labels
decl_stmt|;
DECL|field|comments
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Comment
argument_list|>
argument_list|>
name|comments
decl_stmt|;
comment|/**    * If true require all labels to be within the user's permitted ranges based    * on access controls, attempting to use a label not granted to the user    * will fail the entire modify operation early. If false the operation will    * execute anyway, but the proposed labels given by the user will be    * modified to be the "best" value allowed by the access controls, or    * ignored if the label does not exist.    */
DECL|field|strictLabels
specifier|public
name|boolean
name|strictLabels
init|=
literal|true
decl_stmt|;
comment|/**    * How to process draft comments already in the database that were not also    * described in this input request.    */
DECL|field|drafts
specifier|public
name|DraftHandling
name|drafts
init|=
name|DraftHandling
operator|.
name|DELETE
decl_stmt|;
comment|/** Who to send email notifications to after review is stored. */
DECL|field|notify
specifier|public
name|NotifyHandling
name|notify
init|=
name|NotifyHandling
operator|.
name|ALL
decl_stmt|;
comment|/**    * Account ID, name, email address or username of another user. The review    * will be posted/updated on behalf of this named user instead of the    * caller. Caller must have the labelAs-$NAME permission granted for each    * label that appears in {@link #labels}. This is in addition to the named    * user also needing to have permission to use the labels.    *<p>    * {@link #strictLabels} impacts how labels is processed for the named user,    * not the caller.    */
DECL|field|onBehalfOf
specifier|public
name|String
name|onBehalfOf
decl_stmt|;
DECL|enum|DraftHandling
specifier|public
specifier|static
enum|enum
name|DraftHandling
block|{
DECL|enumConstant|DELETE
DECL|enumConstant|PUBLISH
DECL|enumConstant|KEEP
name|DELETE
block|,
name|PUBLISH
block|,
name|KEEP
block|;   }
DECL|enum|NotifyHandling
specifier|public
specifier|static
enum|enum
name|NotifyHandling
block|{
DECL|enumConstant|NONE
DECL|enumConstant|OWNER
DECL|enumConstant|OWNER_REVIEWERS
DECL|enumConstant|ALL
name|NONE
block|,
name|OWNER
block|,
name|OWNER_REVIEWERS
block|,
name|ALL
block|;   }
DECL|enum|Side
specifier|public
specifier|static
enum|enum
name|Side
block|{
DECL|enumConstant|PARENT
DECL|enumConstant|REVISION
name|PARENT
block|,
name|REVISION
block|;   }
DECL|class|Comment
specifier|public
specifier|static
class|class
name|Comment
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|side
specifier|public
name|Side
name|side
decl_stmt|;
DECL|field|line
specifier|public
name|int
name|line
decl_stmt|;
DECL|field|inReplyTo
specifier|public
name|String
name|inReplyTo
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
DECL|field|range
specifier|public
name|Range
name|range
decl_stmt|;
DECL|class|Range
specifier|public
specifier|static
class|class
name|Range
block|{
DECL|field|startLine
specifier|public
name|int
name|startLine
decl_stmt|;
DECL|field|startCharacter
specifier|public
name|int
name|startCharacter
decl_stmt|;
DECL|field|endLine
specifier|public
name|int
name|endLine
decl_stmt|;
DECL|field|endCharacter
specifier|public
name|int
name|endCharacter
decl_stmt|;
block|}
block|}
block|}
end_class

end_unit

