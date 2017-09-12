begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
import|;
end_import

begin_interface
DECL|interface|ChangeReportFormatter
specifier|public
interface|interface
name|ChangeReportFormatter
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
DECL|field|draft
specifier|private
name|Boolean
name|draft
decl_stmt|;
DECL|field|edit
specifier|private
name|Boolean
name|edit
decl_stmt|;
DECL|field|isPrivate
specifier|private
name|Boolean
name|isPrivate
decl_stmt|;
DECL|field|wip
specifier|private
name|Boolean
name|wip
decl_stmt|;
DECL|method|Input (Change change)
specifier|public
name|Input
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
block|}
DECL|method|setPrivate (boolean isPrivate)
specifier|public
name|Input
name|setPrivate
parameter_list|(
name|boolean
name|isPrivate
parameter_list|)
block|{
name|this
operator|.
name|isPrivate
operator|=
name|isPrivate
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setDraft (boolean draft)
specifier|public
name|Input
name|setDraft
parameter_list|(
name|boolean
name|draft
parameter_list|)
block|{
name|this
operator|.
name|draft
operator|=
name|draft
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setEdit (boolean edit)
specifier|public
name|Input
name|setEdit
parameter_list|(
name|boolean
name|edit
parameter_list|)
block|{
name|this
operator|.
name|edit
operator|=
name|edit
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setWorkInProgress (boolean wip)
specifier|public
name|Input
name|setWorkInProgress
parameter_list|(
name|boolean
name|wip
parameter_list|)
block|{
name|this
operator|.
name|wip
operator|=
name|wip
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|Input
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
operator|==
literal|null
condition|?
name|change
operator|.
name|getSubject
argument_list|()
else|:
name|subject
return|;
block|}
DECL|method|isDraft ()
specifier|public
name|boolean
name|isDraft
parameter_list|()
block|{
return|return
name|draft
operator|==
literal|null
condition|?
name|Change
operator|.
name|Status
operator|.
name|DRAFT
operator|==
name|change
operator|.
name|getStatus
argument_list|()
else|:
name|draft
return|;
block|}
DECL|method|isEdit ()
specifier|public
name|boolean
name|isEdit
parameter_list|()
block|{
return|return
name|edit
operator|==
literal|null
condition|?
literal|false
else|:
name|edit
return|;
block|}
DECL|method|isPrivate ()
specifier|public
name|boolean
name|isPrivate
parameter_list|()
block|{
return|return
name|isPrivate
operator|==
literal|null
condition|?
name|change
operator|.
name|isPrivate
argument_list|()
else|:
name|isPrivate
return|;
block|}
DECL|method|isWorkInProgress ()
specifier|public
name|boolean
name|isWorkInProgress
parameter_list|()
block|{
return|return
name|wip
operator|==
literal|null
condition|?
name|change
operator|.
name|isWorkInProgress
argument_list|()
else|:
name|wip
return|;
block|}
block|}
DECL|method|newChange (Input input)
name|String
name|newChange
parameter_list|(
name|Input
name|input
parameter_list|)
function_decl|;
DECL|method|changeUpdated (Input input)
name|String
name|changeUpdated
parameter_list|(
name|Input
name|input
parameter_list|)
function_decl|;
DECL|method|changeClosed (Input input)
name|String
name|changeClosed
parameter_list|(
name|Input
name|input
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

