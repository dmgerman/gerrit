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
DECL|package|com.google.gerrit.prettify.common
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
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
operator|.
name|Range
import|;
end_import

begin_comment
comment|/**  * A builder for creating immutable {@link SparseFileContent}. Lines can be only be added in  * sequential (increased) order  */
end_comment

begin_class
DECL|class|SparseFileContentBuilder
specifier|public
class|class
name|SparseFileContentBuilder
block|{
DECL|field|ranges
specifier|private
specifier|final
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Range
argument_list|>
name|ranges
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
name|int
name|size
decl_stmt|;
DECL|field|lastRangeBase
specifier|private
name|int
name|lastRangeBase
decl_stmt|;
DECL|field|lastRangeEnd
specifier|private
name|int
name|lastRangeEnd
decl_stmt|;
DECL|field|lastRangeLines
specifier|private
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|lastRangeLines
decl_stmt|;
DECL|method|SparseFileContentBuilder (int size)
specifier|public
name|SparseFileContentBuilder
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|ranges
operator|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<>
argument_list|()
expr_stmt|;
name|startNextRange
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
DECL|method|addLine (int lineNumber, String content)
specifier|public
name|void
name|addLine
parameter_list|(
name|int
name|lineNumber
parameter_list|,
name|String
name|content
parameter_list|)
block|{
if|if
condition|(
name|lineNumber
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Line number must be non-negative"
argument_list|)
throw|;
block|}
comment|//    if (lineNumber>= size) {
comment|//     The following 4 tests are failed if you uncomment this condition:
comment|//
comment|//
comment|// diffOfFileWithMultilineRebaseHunkRemovingNewlineAtEndOfFileAndWithCommentReturnsFileContents
comment|//
comment|// diffOfFileWithMultilineRebaseHunkAddingNewlineAtEndOfFileAndWithCommentReturnsFileContents
comment|//
comment|//
comment|// diffOfFileWithMultilineRebaseHunkRemovingNewlineAtEndOfFileAndWithCommentReturnsFileContents
comment|//
comment|// diffOfFileWithMultilineRebaseHunkAddingNewlineAtEndOfFileAndWithCommentReturnsFileContents
comment|//     Tests are failed because there are some bug with diff calculation.
comment|//     The condition must be uncommented after all these bugs are fixed.
comment|//     Also don't forget to remove ignore from for SparseFileContentBuilder
comment|//      throw new IllegalArgumentException(String.format("The zero-based line number %d is after
comment|// the end of file. The file size is %d line(s).", lineNumber, size));
comment|//    }
if|if
condition|(
name|lineNumber
operator|<
name|lastRangeEnd
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid line number %d. You are trying to add a line before an already added line"
operator|+
literal|" %d"
argument_list|,
name|lineNumber
argument_list|,
name|lastRangeEnd
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|lineNumber
operator|>
name|lastRangeEnd
condition|)
block|{
name|finishLastRange
argument_list|()
expr_stmt|;
name|startNextRange
argument_list|(
name|lineNumber
argument_list|)
expr_stmt|;
block|}
name|lastRangeLines
operator|.
name|add
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|lastRangeEnd
operator|++
expr_stmt|;
block|}
DECL|method|startNextRange (int base)
specifier|private
name|void
name|startNextRange
parameter_list|(
name|int
name|base
parameter_list|)
block|{
name|lastRangeLines
operator|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<>
argument_list|()
expr_stmt|;
name|lastRangeBase
operator|=
name|lastRangeEnd
operator|=
name|base
expr_stmt|;
block|}
DECL|method|finishLastRange ()
specifier|private
name|void
name|finishLastRange
parameter_list|()
block|{
if|if
condition|(
name|lastRangeEnd
operator|>
name|lastRangeBase
condition|)
block|{
name|ranges
operator|.
name|add
argument_list|(
name|Range
operator|.
name|create
argument_list|(
name|lastRangeBase
argument_list|,
name|lastRangeLines
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|lastRangeLines
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|build ()
specifier|public
name|SparseFileContent
name|build
parameter_list|()
block|{
name|finishLastRange
argument_list|()
expr_stmt|;
return|return
name|SparseFileContent
operator|.
name|create
argument_list|(
name|ranges
operator|.
name|build
argument_list|()
argument_list|,
name|size
argument_list|)
return|;
block|}
block|}
end_class

end_unit

