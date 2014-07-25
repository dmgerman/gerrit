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
name|client
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
name|LinkedHashMap
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
name|CommentInput
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
comment|/** Delete pending drafts on this revision only. */
DECL|enumConstant|DELETE
name|DELETE
block|,
comment|/** Publish pending drafts on this revision only. */
DECL|enumConstant|PUBLISH
name|PUBLISH
block|,
comment|/** Leave pending drafts alone. */
DECL|enumConstant|KEEP
name|KEEP
block|,
comment|/** Publish pending drafts on all revisions. */
DECL|enumConstant|PUBLISH_ALL_REVISIONS
name|PUBLISH_ALL_REVISIONS
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
block|}
DECL|class|CommentInput
specifier|public
specifier|static
class|class
name|CommentInput
extends|extends
name|Comment
block|{   }
DECL|method|message (String msg)
specifier|public
name|ReviewInput
name|message
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|message
operator|=
name|msg
operator|!=
literal|null
operator|&&
operator|!
name|msg
operator|.
name|isEmpty
argument_list|()
condition|?
name|msg
else|:
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|label (String name, short value)
specifier|public
name|ReviewInput
name|label
parameter_list|(
name|String
name|name
parameter_list|,
name|short
name|value
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
if|if
condition|(
name|labels
operator|==
literal|null
condition|)
block|{
name|labels
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
name|labels
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|label (String name, int value)
specifier|public
name|ReviewInput
name|label
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
argument_list|<
name|Short
operator|.
name|MIN_VALUE
operator|||
name|value
argument_list|>
name|Short
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
return|return
name|label
argument_list|(
name|name
argument_list|,
operator|(
name|short
operator|)
name|value
argument_list|)
return|;
block|}
DECL|method|label (String name)
specifier|public
name|ReviewInput
name|label
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|label
argument_list|(
name|name
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
return|;
block|}
DECL|method|recommend ()
specifier|public
specifier|static
name|ReviewInput
name|recommend
parameter_list|()
block|{
return|return
operator|new
name|ReviewInput
argument_list|()
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|,
literal|1
argument_list|)
return|;
block|}
DECL|method|dislike ()
specifier|public
specifier|static
name|ReviewInput
name|dislike
parameter_list|()
block|{
return|return
operator|new
name|ReviewInput
argument_list|()
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|approve ()
specifier|public
specifier|static
name|ReviewInput
name|approve
parameter_list|()
block|{
return|return
operator|new
name|ReviewInput
argument_list|()
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|,
literal|2
argument_list|)
return|;
block|}
DECL|method|reject ()
specifier|public
specifier|static
name|ReviewInput
name|reject
parameter_list|()
block|{
return|return
operator|new
name|ReviewInput
argument_list|()
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|,
operator|-
literal|2
argument_list|)
return|;
block|}
block|}
end_class

end_unit

