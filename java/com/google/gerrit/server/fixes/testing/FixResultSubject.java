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
DECL|package|com.google.gerrit.server.fixes.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|fixes
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
name|gerrit
operator|.
name|server
operator|.
name|fixes
operator|.
name|testing
operator|.
name|GitEditSubject
operator|.
name|gitEdits
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
name|server
operator|.
name|fixes
operator|.
name|FixCalculator
operator|.
name|FixResult
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
import|;
end_import

begin_class
DECL|class|FixResultSubject
specifier|public
class|class
name|FixResultSubject
extends|extends
name|Subject
block|{
DECL|method|assertThat (FixResult fixResult)
specifier|public
specifier|static
name|FixResultSubject
name|assertThat
parameter_list|(
name|FixResult
name|fixResult
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|FixResultSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|fixResult
argument_list|)
return|;
block|}
DECL|field|fixResult
specifier|private
specifier|final
name|FixResult
name|fixResult
decl_stmt|;
DECL|method|FixResultSubject (FailureMetadata failureMetadata, FixResult fixResult)
specifier|private
name|FixResultSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|FixResult
name|fixResult
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|fixResult
argument_list|)
expr_stmt|;
name|this
operator|.
name|fixResult
operator|=
name|fixResult
expr_stmt|;
block|}
DECL|method|text ()
specifier|public
name|StringSubject
name|text
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"text"
argument_list|)
operator|.
name|that
argument_list|(
name|fixResult
operator|.
name|text
operator|.
name|getString
argument_list|(
literal|0
argument_list|,
name|fixResult
operator|.
name|text
operator|.
name|size
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
DECL|method|edits ()
specifier|public
name|ListSubject
argument_list|<
name|GitEditSubject
argument_list|,
name|Edit
argument_list|>
name|edits
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"edits"
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
name|fixResult
operator|.
name|edits
argument_list|,
name|gitEdits
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

