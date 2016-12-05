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
name|assertAbout
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
name|FailureStrategy
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
name|StringSubject
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
name|common
operator|.
name|truth
operator|.
name|SubjectFactory
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
name|Truth
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
name|ListSubject
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
name|FixReplacementInfo
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

begin_class
DECL|class|FixSuggestionInfoSubject
specifier|public
class|class
name|FixSuggestionInfoSubject
extends|extends
name|Subject
argument_list|<
name|FixSuggestionInfoSubject
argument_list|,
name|FixSuggestionInfo
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|SubjectFactory
argument_list|<
name|FixSuggestionInfoSubject
argument_list|,
DECL|field|FIX_SUGGESTION_INFO_SUBJECT_FACTORY
name|FixSuggestionInfo
argument_list|>
name|FIX_SUGGESTION_INFO_SUBJECT_FACTORY
init|=
operator|new
name|SubjectFactory
argument_list|<
name|FixSuggestionInfoSubject
argument_list|,
name|FixSuggestionInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|FixSuggestionInfoSubject
name|getSubject
parameter_list|(
name|FailureStrategy
name|failureStrategy
parameter_list|,
name|FixSuggestionInfo
name|fixSuggestionInfo
parameter_list|)
block|{
return|return
operator|new
name|FixSuggestionInfoSubject
argument_list|(
name|failureStrategy
argument_list|,
name|fixSuggestionInfo
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|assertThat ( FixSuggestionInfo fixSuggestionInfo)
specifier|public
specifier|static
name|FixSuggestionInfoSubject
name|assertThat
parameter_list|(
name|FixSuggestionInfo
name|fixSuggestionInfo
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|FIX_SUGGESTION_INFO_SUBJECT_FACTORY
argument_list|)
operator|.
name|that
argument_list|(
name|fixSuggestionInfo
argument_list|)
return|;
block|}
DECL|method|FixSuggestionInfoSubject (FailureStrategy failureStrategy, FixSuggestionInfo fixSuggestionInfo)
specifier|private
name|FixSuggestionInfoSubject
parameter_list|(
name|FailureStrategy
name|failureStrategy
parameter_list|,
name|FixSuggestionInfo
name|fixSuggestionInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureStrategy
argument_list|,
name|fixSuggestionInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|fixId ()
specifier|public
name|StringSubject
name|fixId
parameter_list|()
block|{
return|return
name|Truth
operator|.
name|assertThat
argument_list|(
name|actual
argument_list|()
operator|.
name|fixId
argument_list|)
operator|.
name|named
argument_list|(
literal|"fixId"
argument_list|)
return|;
block|}
specifier|public
name|ListSubject
argument_list|<
name|FixReplacementInfoSubject
argument_list|,
DECL|method|replacements ()
name|FixReplacementInfo
argument_list|>
name|replacements
parameter_list|()
block|{
return|return
name|ListSubject
operator|.
name|assertThat
argument_list|(
name|actual
argument_list|()
operator|.
name|replacements
argument_list|,
name|FixReplacementInfoSubject
operator|::
name|assertThat
argument_list|)
operator|.
name|named
argument_list|(
literal|"replacements"
argument_list|)
return|;
block|}
DECL|method|onlyReplacement ()
specifier|public
name|FixReplacementInfoSubject
name|onlyReplacement
parameter_list|()
block|{
return|return
name|replacements
argument_list|()
operator|.
name|onlyElement
argument_list|()
return|;
block|}
DECL|method|description ()
specifier|public
name|StringSubject
name|description
parameter_list|()
block|{
return|return
name|Truth
operator|.
name|assertThat
argument_list|(
name|actual
argument_list|()
operator|.
name|description
argument_list|)
operator|.
name|named
argument_list|(
literal|"description"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

