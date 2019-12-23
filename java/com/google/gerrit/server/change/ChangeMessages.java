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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|nls
operator|.
name|NLS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|nls
operator|.
name|TranslationBundle
import|;
end_import

begin_class
DECL|class|ChangeMessages
specifier|public
class|class
name|ChangeMessages
extends|extends
name|TranslationBundle
block|{
DECL|method|get ()
specifier|public
specifier|static
name|ChangeMessages
name|get
parameter_list|()
block|{
return|return
name|NLS
operator|.
name|getBundleFor
argument_list|(
name|ChangeMessages
operator|.
name|class
argument_list|)
return|;
block|}
DECL|field|revertChangeDefaultMessage
specifier|public
name|String
name|revertChangeDefaultMessage
decl_stmt|;
DECL|field|revertSubmissionDefaultMessage
specifier|public
name|String
name|revertSubmissionDefaultMessage
decl_stmt|;
DECL|field|revertSubmissionUserMessage
specifier|public
name|String
name|revertSubmissionUserMessage
decl_stmt|;
DECL|field|revertSubmissionOfRevertSubmissionUserMessage
specifier|public
name|String
name|revertSubmissionOfRevertSubmissionUserMessage
decl_stmt|;
DECL|field|reviewerCantSeeChange
specifier|public
name|String
name|reviewerCantSeeChange
decl_stmt|;
DECL|field|reviewerInvalid
specifier|public
name|String
name|reviewerInvalid
decl_stmt|;
DECL|field|reviewerNotFoundUserOrGroup
specifier|public
name|String
name|reviewerNotFoundUserOrGroup
decl_stmt|;
DECL|field|groupIsNotAllowed
specifier|public
name|String
name|groupIsNotAllowed
decl_stmt|;
DECL|field|groupHasTooManyMembers
specifier|public
name|String
name|groupHasTooManyMembers
decl_stmt|;
DECL|field|groupManyMembersConfirmation
specifier|public
name|String
name|groupManyMembersConfirmation
decl_stmt|;
block|}
end_class

end_unit

