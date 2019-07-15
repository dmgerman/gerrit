begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|METHOD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
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
name|client
operator|.
name|InheritableBoolean
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
name|client
operator|.
name|SubmitType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|TestProjectInput
specifier|public
annotation_defn|@interface
name|TestProjectInput
block|{
comment|// Fields from ProjectInput for creating the project.
DECL|method|parent ()
name|String
name|parent
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|createEmptyCommit ()
DECL|field|true
name|boolean
name|createEmptyCommit
parameter_list|()
default|default
literal|true
function_decl|;
DECL|method|description ()
name|String
name|description
parameter_list|()
default|default
literal|""
function_decl|;
comment|// These may be null in a ProjectInput, but annotations do not allow null
comment|// default values. Thus these defaults should match ProjectConfig.
DECL|method|submitType ()
DECL|field|SubmitType.MERGE_IF_NECESSARY
name|SubmitType
name|submitType
parameter_list|()
default|default
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
function_decl|;
DECL|method|useContributorAgreements ()
DECL|field|InheritableBoolean.INHERIT
name|InheritableBoolean
name|useContributorAgreements
parameter_list|()
default|default
name|InheritableBoolean
operator|.
name|INHERIT
function_decl|;
DECL|method|useSignedOffBy ()
DECL|field|InheritableBoolean.INHERIT
name|InheritableBoolean
name|useSignedOffBy
parameter_list|()
default|default
name|InheritableBoolean
operator|.
name|INHERIT
function_decl|;
DECL|method|useContentMerge ()
DECL|field|InheritableBoolean.INHERIT
name|InheritableBoolean
name|useContentMerge
parameter_list|()
default|default
name|InheritableBoolean
operator|.
name|INHERIT
function_decl|;
DECL|method|rejectEmptyCommit ()
DECL|field|InheritableBoolean.INHERIT
name|InheritableBoolean
name|rejectEmptyCommit
parameter_list|()
default|default
name|InheritableBoolean
operator|.
name|INHERIT
function_decl|;
DECL|method|enableSignedPush ()
DECL|field|InheritableBoolean.INHERIT
name|InheritableBoolean
name|enableSignedPush
parameter_list|()
default|default
name|InheritableBoolean
operator|.
name|INHERIT
function_decl|;
DECL|method|requireSignedPush ()
DECL|field|InheritableBoolean.INHERIT
name|InheritableBoolean
name|requireSignedPush
parameter_list|()
default|default
name|InheritableBoolean
operator|.
name|INHERIT
function_decl|;
comment|// Fields specific to acceptance test behavior.
comment|/** Username to use for initial clone, passed to {@link AccountCreator}. */
DECL|method|cloneAs ()
name|String
name|cloneAs
parameter_list|()
default|default
literal|"admin"
function_decl|;
block|}
end_annotation_defn

end_unit

