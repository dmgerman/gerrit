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
DECL|package|com.google.gerrit.prettify.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
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
name|MapSubject
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
name|prettify
operator|.
name|common
operator|.
name|SparseFileContent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|SparseFileContentSubject
specifier|public
class|class
name|SparseFileContentSubject
extends|extends
name|Subject
block|{
DECL|method|assertThat (SparseFileContent sparseFileContent)
specifier|public
specifier|static
name|SparseFileContentSubject
name|assertThat
parameter_list|(
name|SparseFileContent
name|sparseFileContent
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|sparseFileContent
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|sparseFileContent
argument_list|)
return|;
block|}
DECL|field|sparseFileContent
specifier|private
specifier|final
name|SparseFileContent
name|sparseFileContent
decl_stmt|;
DECL|method|SparseFileContentSubject (FailureMetadata metadata, SparseFileContent actual)
specifier|private
name|SparseFileContentSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|SparseFileContent
name|actual
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|this
operator|.
name|sparseFileContent
operator|=
name|actual
expr_stmt|;
block|}
DECL|method|sparseFileContent ()
specifier|private
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|SparseFileContentSubject
argument_list|,
name|SparseFileContent
argument_list|>
name|sparseFileContent
parameter_list|()
block|{
return|return
name|SparseFileContentSubject
operator|::
operator|new
return|;
block|}
DECL|method|getSize ()
specifier|public
name|IntegerSubject
name|getSize
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"size()"
argument_list|)
operator|.
name|that
argument_list|(
name|sparseFileContent
operator|.
name|getSize
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRangesCount ()
specifier|public
name|IntegerSubject
name|getRangesCount
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"rangesCount()"
argument_list|)
operator|.
name|that
argument_list|(
name|sparseFileContent
operator|.
name|getRangesCount
argument_list|()
argument_list|)
return|;
block|}
DECL|method|lines ()
specifier|public
name|MapSubject
name|lines
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|lines
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|SparseFileContent
operator|.
name|Accessor
name|accessor
init|=
name|sparseFileContent
operator|.
name|createAccessor
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|accessor
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|accessor
operator|.
name|first
argument_list|()
decl_stmt|;
while|while
condition|(
name|idx
operator|<
name|size
condition|)
block|{
name|lines
operator|.
name|put
argument_list|(
name|idx
argument_list|,
name|accessor
operator|.
name|get
argument_list|(
name|idx
argument_list|)
argument_list|)
expr_stmt|;
name|idx
operator|=
name|accessor
operator|.
name|next
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|check
argument_list|(
literal|"lines()"
argument_list|)
operator|.
name|that
argument_list|(
name|lines
argument_list|)
return|;
block|}
block|}
end_class

end_unit

