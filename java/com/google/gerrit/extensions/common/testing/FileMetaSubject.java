begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|IntegerSubject
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
name|DiffInfo
operator|.
name|FileMeta
import|;
end_import

begin_class
DECL|class|FileMetaSubject
specifier|public
class|class
name|FileMetaSubject
extends|extends
name|Subject
argument_list|<
name|FileMetaSubject
argument_list|,
name|FileMeta
argument_list|>
block|{
DECL|method|assertThat (FileMeta fileMeta)
specifier|public
specifier|static
name|FileMetaSubject
name|assertThat
parameter_list|(
name|FileMeta
name|fileMeta
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|fileMetas
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|fileMeta
argument_list|)
return|;
block|}
DECL|method|fileMetas ()
specifier|public
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|FileMetaSubject
argument_list|,
name|FileMeta
argument_list|>
name|fileMetas
parameter_list|()
block|{
return|return
name|FileMetaSubject
operator|::
operator|new
return|;
block|}
DECL|method|FileMetaSubject (FailureMetadata failureMetadata, FileMeta fileMeta)
specifier|private
name|FileMetaSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|FileMeta
name|fileMeta
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|fileMeta
argument_list|)
expr_stmt|;
block|}
DECL|method|totalLineCount ()
specifier|public
name|IntegerSubject
name|totalLineCount
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|FileMeta
name|fileMeta
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|check
argument_list|(
literal|"totalLineCount()"
argument_list|)
operator|.
name|that
argument_list|(
name|fileMeta
operator|.
name|lines
argument_list|)
return|;
block|}
block|}
end_class

end_unit

