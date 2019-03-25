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
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|testing
operator|.
name|RangeSubject
operator|.
name|ranges
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

begin_class
DECL|class|FixReplacementInfoSubject
specifier|public
class|class
name|FixReplacementInfoSubject
extends|extends
name|Subject
argument_list|<
name|FixReplacementInfoSubject
argument_list|,
name|FixReplacementInfo
argument_list|>
block|{
DECL|method|assertThat (FixReplacementInfo fixReplacementInfo)
specifier|public
specifier|static
name|FixReplacementInfoSubject
name|assertThat
parameter_list|(
name|FixReplacementInfo
name|fixReplacementInfo
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|fixReplacements
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|fixReplacementInfo
argument_list|)
return|;
block|}
DECL|method|fixReplacements ()
specifier|public
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|FixReplacementInfoSubject
argument_list|,
name|FixReplacementInfo
argument_list|>
name|fixReplacements
parameter_list|()
block|{
return|return
name|FixReplacementInfoSubject
operator|::
operator|new
return|;
block|}
DECL|method|FixReplacementInfoSubject ( FailureMetadata failureMetadata, FixReplacementInfo fixReplacementInfo)
specifier|private
name|FixReplacementInfoSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|FixReplacementInfo
name|fixReplacementInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|fixReplacementInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|path ()
specifier|public
name|StringSubject
name|path
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"path()"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|path
argument_list|)
return|;
block|}
DECL|method|range ()
specifier|public
name|RangeSubject
name|range
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"range()"
argument_list|)
operator|.
name|about
argument_list|(
name|ranges
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|range
argument_list|)
return|;
block|}
DECL|method|replacement ()
specifier|public
name|StringSubject
name|replacement
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"replacement()"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|replacement
argument_list|)
return|;
block|}
block|}
end_class

end_unit

