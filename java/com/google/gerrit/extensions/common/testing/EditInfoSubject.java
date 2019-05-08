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
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|testing
operator|.
name|CommitInfoSubject
operator|.
name|commits
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
name|EditInfo
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
name|OptionalSubject
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

begin_class
DECL|class|EditInfoSubject
specifier|public
class|class
name|EditInfoSubject
extends|extends
name|Subject
argument_list|<
name|EditInfoSubject
argument_list|,
name|EditInfo
argument_list|>
block|{
DECL|method|assertThat (EditInfo editInfo)
specifier|public
specifier|static
name|EditInfoSubject
name|assertThat
parameter_list|(
name|EditInfo
name|editInfo
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|edits
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|editInfo
argument_list|)
return|;
block|}
DECL|method|edits ()
specifier|private
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|EditInfoSubject
argument_list|,
name|EditInfo
argument_list|>
name|edits
parameter_list|()
block|{
return|return
name|EditInfoSubject
operator|::
operator|new
return|;
block|}
DECL|method|assertThat ( Optional<EditInfo> editInfoOptional)
specifier|public
specifier|static
name|OptionalSubject
argument_list|<
name|EditInfoSubject
argument_list|,
name|EditInfo
argument_list|>
name|assertThat
parameter_list|(
name|Optional
argument_list|<
name|EditInfo
argument_list|>
name|editInfoOptional
parameter_list|)
block|{
return|return
name|OptionalSubject
operator|.
name|assertThat
argument_list|(
name|editInfoOptional
argument_list|,
name|edits
argument_list|()
argument_list|)
return|;
block|}
DECL|field|editInfo
specifier|private
specifier|final
name|EditInfo
name|editInfo
decl_stmt|;
DECL|method|EditInfoSubject (FailureMetadata failureMetadata, EditInfo editInfo)
specifier|private
name|EditInfoSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|EditInfo
name|editInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|editInfo
argument_list|)
expr_stmt|;
name|this
operator|.
name|editInfo
operator|=
name|editInfo
expr_stmt|;
block|}
DECL|method|commit ()
specifier|public
name|CommitInfoSubject
name|commit
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"commit"
argument_list|)
operator|.
name|about
argument_list|(
name|commits
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|editInfo
operator|.
name|commit
argument_list|)
return|;
block|}
DECL|method|baseRevision ()
specifier|public
name|StringSubject
name|baseRevision
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"baseRevision"
argument_list|)
operator|.
name|that
argument_list|(
name|editInfo
operator|.
name|baseRevision
argument_list|)
return|;
block|}
block|}
end_class

end_unit

