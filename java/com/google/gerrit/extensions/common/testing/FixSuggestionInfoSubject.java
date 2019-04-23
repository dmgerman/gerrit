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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
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
name|extensions
operator|.
name|common
operator|.
name|testing
operator|.
name|FixReplacementInfoSubject
operator|.
name|fixReplacements
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
DECL|method|assertThat (FixSuggestionInfo fixSuggestionInfo)
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
name|fixSuggestions
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|fixSuggestionInfo
argument_list|)
return|;
block|}
DECL|method|fixSuggestions ()
specifier|public
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|FixSuggestionInfoSubject
argument_list|,
name|FixSuggestionInfo
argument_list|>
name|fixSuggestions
parameter_list|()
block|{
return|return
name|FixSuggestionInfoSubject
operator|::
operator|new
return|;
block|}
DECL|method|FixSuggestionInfoSubject ( FailureMetadata failureMetadata, FixSuggestionInfo fixSuggestionInfo)
specifier|private
name|FixSuggestionInfoSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|FixSuggestionInfo
name|fixSuggestionInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
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
name|assertWithMessage
argument_list|(
literal|"fixId"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|fixId
argument_list|)
return|;
block|}
DECL|method|replacements ()
specifier|public
name|ListSubject
argument_list|<
name|FixReplacementInfoSubject
argument_list|,
name|FixReplacementInfo
argument_list|>
name|replacements
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"replacements()"
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
name|replacements
argument_list|,
name|fixReplacements
argument_list|()
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
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"description()"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|description
argument_list|)
return|;
block|}
block|}
end_class

end_unit

