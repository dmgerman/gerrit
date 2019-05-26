begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
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
name|primitives
operator|.
name|Shorts
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/** An approval (or negative approval) on a patch set. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|PatchSetApproval
specifier|public
specifier|abstract
class|class
name|PatchSetApproval
block|{
DECL|method|key (PatchSet.Id patchSetId, Account.Id accountId, LabelId labelId)
specifier|public
specifier|static
name|Key
name|key
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|LabelId
name|labelId
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PatchSetApproval_Key
argument_list|(
name|patchSetId
argument_list|,
name|accountId
argument_list|,
name|labelId
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Key
specifier|public
specifier|abstract
specifier|static
class|class
name|Key
block|{
DECL|method|patchSetId ()
specifier|public
specifier|abstract
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|()
function_decl|;
DECL|method|accountId ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|accountId
parameter_list|()
function_decl|;
DECL|method|labelId ()
specifier|public
specifier|abstract
name|LabelId
name|labelId
parameter_list|()
function_decl|;
DECL|method|isLegacySubmit ()
specifier|public
name|boolean
name|isLegacySubmit
parameter_list|()
block|{
return|return
name|LabelId
operator|.
name|LEGACY_SUBMIT_NAME
operator|.
name|equals
argument_list|(
name|labelId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_PatchSetApproval
operator|.
name|Builder
argument_list|()
operator|.
name|postSubmit
argument_list|(
literal|false
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
DECL|method|key (Key key)
specifier|public
specifier|abstract
name|Builder
name|key
parameter_list|(
name|Key
name|key
parameter_list|)
function_decl|;
DECL|method|key ()
specifier|public
specifier|abstract
name|Key
name|key
parameter_list|()
function_decl|;
DECL|method|value (short value)
specifier|public
specifier|abstract
name|Builder
name|value
parameter_list|(
name|short
name|value
parameter_list|)
function_decl|;
DECL|method|value (int value)
specifier|public
name|Builder
name|value
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
name|value
argument_list|(
name|Shorts
operator|.
name|checkedCast
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|granted (Timestamp granted)
specifier|public
specifier|abstract
name|Builder
name|granted
parameter_list|(
name|Timestamp
name|granted
parameter_list|)
function_decl|;
DECL|method|granted (Date granted)
specifier|public
name|Builder
name|granted
parameter_list|(
name|Date
name|granted
parameter_list|)
block|{
return|return
name|granted
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|granted
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|tag (String tag)
specifier|public
specifier|abstract
name|Builder
name|tag
parameter_list|(
name|String
name|tag
parameter_list|)
function_decl|;
DECL|method|tag (Optional<String> tag)
specifier|public
specifier|abstract
name|Builder
name|tag
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|tag
parameter_list|)
function_decl|;
DECL|method|realAccountId (Account.Id realAccountId)
specifier|public
specifier|abstract
name|Builder
name|realAccountId
parameter_list|(
name|Account
operator|.
name|Id
name|realAccountId
parameter_list|)
function_decl|;
DECL|method|realAccountId ()
specifier|abstract
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|realAccountId
parameter_list|()
function_decl|;
DECL|method|postSubmit (boolean isPostSubmit)
specifier|public
specifier|abstract
name|Builder
name|postSubmit
parameter_list|(
name|boolean
name|isPostSubmit
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|PatchSetApproval
name|autoBuild
parameter_list|()
function_decl|;
DECL|method|build ()
specifier|public
name|PatchSetApproval
name|build
parameter_list|()
block|{
if|if
condition|(
operator|!
name|realAccountId
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|realAccountId
argument_list|(
name|key
argument_list|()
operator|.
name|accountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|autoBuild
argument_list|()
return|;
block|}
block|}
DECL|method|key ()
specifier|public
specifier|abstract
name|Key
name|key
parameter_list|()
function_decl|;
comment|/**    * Value assigned by the user.    *    *<p>The precise meaning of "value" is up to each category.    *    *<p>In general:    *    *<ul>    *<li><b>&lt; 0:</b> The approval is rejected/revoked.    *<li><b>= 0:</b> No indication either way is provided.    *<li><b>&gt; 0:</b> The approval is approved/positive.    *</ul>    *    * and in the negative and positive direction a magnitude can be assumed.The further from 0 the    * more assertive the approval.    */
DECL|method|value ()
specifier|public
specifier|abstract
name|short
name|value
parameter_list|()
function_decl|;
DECL|method|granted ()
specifier|public
specifier|abstract
name|Timestamp
name|granted
parameter_list|()
function_decl|;
DECL|method|tag ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|tag
parameter_list|()
function_decl|;
comment|/** Real user that made this approval on behalf of the user recorded in {@link Key#accountId}. */
DECL|method|realAccountId ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|realAccountId
parameter_list|()
function_decl|;
DECL|method|postSubmit ()
specifier|public
specifier|abstract
name|boolean
name|postSubmit
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|copyWithPatchSet (PatchSet.Id psId)
specifier|public
name|PatchSetApproval
name|copyWithPatchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
return|return
name|toBuilder
argument_list|()
operator|.
name|key
argument_list|(
name|key
argument_list|(
name|psId
argument_list|,
name|key
argument_list|()
operator|.
name|accountId
argument_list|()
argument_list|,
name|key
argument_list|()
operator|.
name|labelId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|patchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|()
block|{
return|return
name|key
argument_list|()
operator|.
name|patchSetId
argument_list|()
return|;
block|}
DECL|method|accountId ()
specifier|public
name|Account
operator|.
name|Id
name|accountId
parameter_list|()
block|{
return|return
name|key
argument_list|()
operator|.
name|accountId
argument_list|()
return|;
block|}
DECL|method|labelId ()
specifier|public
name|LabelId
name|labelId
parameter_list|()
block|{
return|return
name|key
argument_list|()
operator|.
name|labelId
argument_list|()
return|;
block|}
DECL|method|label ()
specifier|public
name|String
name|label
parameter_list|()
block|{
return|return
name|labelId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|isLegacySubmit ()
specifier|public
name|boolean
name|isLegacySubmit
parameter_list|()
block|{
return|return
name|key
argument_list|()
operator|.
name|isLegacySubmit
argument_list|()
return|;
block|}
block|}
end_class

end_unit

