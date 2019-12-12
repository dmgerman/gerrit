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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|entities
operator|.
name|Change
import|;
end_import

begin_comment
comment|/** Formatter for git command-line progress messages. */
end_comment

begin_interface
DECL|interface|ChangeReportFormatter
specifier|public
interface|interface
name|ChangeReportFormatter
block|{
annotation|@
name|AutoValue
DECL|class|Input
specifier|public
specifier|abstract
specifier|static
class|class
name|Input
block|{
DECL|method|change ()
specifier|public
specifier|abstract
name|Change
name|change
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|subject ()
specifier|public
specifier|abstract
name|String
name|subject
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isEdit ()
specifier|public
specifier|abstract
name|Boolean
name|isEdit
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isPrivate ()
specifier|public
specifier|abstract
name|Boolean
name|isPrivate
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isWorkInProgress ()
specifier|public
specifier|abstract
name|Boolean
name|isWorkInProgress
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
name|AutoValue_ChangeReportFormatter_Input
operator|.
name|Builder
argument_list|()
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
DECL|method|setChange (Change val)
specifier|public
specifier|abstract
name|Builder
name|setChange
parameter_list|(
name|Change
name|val
parameter_list|)
function_decl|;
DECL|method|setSubject (String val)
specifier|public
specifier|abstract
name|Builder
name|setSubject
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
DECL|method|setIsEdit (Boolean val)
specifier|public
specifier|abstract
name|Builder
name|setIsEdit
parameter_list|(
name|Boolean
name|val
parameter_list|)
function_decl|;
DECL|method|setIsPrivate (Boolean val)
specifier|public
specifier|abstract
name|Builder
name|setIsPrivate
parameter_list|(
name|Boolean
name|val
parameter_list|)
function_decl|;
DECL|method|setIsWorkInProgress (Boolean val)
specifier|public
specifier|abstract
name|Builder
name|setIsWorkInProgress
parameter_list|(
name|Boolean
name|val
parameter_list|)
function_decl|;
DECL|method|change ()
specifier|abstract
name|Change
name|change
parameter_list|()
function_decl|;
DECL|method|subject ()
specifier|abstract
name|String
name|subject
parameter_list|()
function_decl|;
DECL|method|isEdit ()
specifier|abstract
name|Boolean
name|isEdit
parameter_list|()
function_decl|;
DECL|method|isPrivate ()
specifier|abstract
name|Boolean
name|isPrivate
parameter_list|()
function_decl|;
DECL|method|isWorkInProgress ()
specifier|abstract
name|Boolean
name|isWorkInProgress
parameter_list|()
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|Input
name|autoBuild
parameter_list|()
function_decl|;
DECL|method|build ()
specifier|public
name|Input
name|build
parameter_list|()
block|{
name|setChange
argument_list|(
name|change
argument_list|()
argument_list|)
expr_stmt|;
name|setSubject
argument_list|(
name|subject
argument_list|()
operator|==
literal|null
condition|?
name|change
argument_list|()
operator|.
name|getSubject
argument_list|()
else|:
name|subject
argument_list|()
argument_list|)
expr_stmt|;
name|setIsEdit
argument_list|(
name|isEdit
argument_list|()
operator|==
literal|null
condition|?
literal|false
else|:
name|isEdit
argument_list|()
argument_list|)
expr_stmt|;
name|setIsPrivate
argument_list|(
name|isPrivate
argument_list|()
operator|==
literal|null
condition|?
name|change
argument_list|()
operator|.
name|isPrivate
argument_list|()
else|:
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|setIsWorkInProgress
argument_list|(
name|isWorkInProgress
argument_list|()
operator|==
literal|null
condition|?
name|change
argument_list|()
operator|.
name|isWorkInProgress
argument_list|()
else|:
name|isWorkInProgress
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|autoBuild
argument_list|()
return|;
block|}
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

