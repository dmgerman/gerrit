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
DECL|package|com.google.gerrit.server.fixes
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
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|entities
operator|.
name|Comment
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
name|FixReplacement
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
name|restapi
operator|.
name|ResourceConflictException
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
name|jgit
operator|.
name|diff
operator|.
name|ReplaceEdit
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
name|patch
operator|.
name|Text
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * Produces final version of an input content with all fixes applied together with list of edits.  */
end_comment

begin_class
DECL|class|FixCalculator
specifier|public
class|class
name|FixCalculator
block|{
DECL|field|ASC_RANGE_FIX_REPLACEMENT_COMPARATOR
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|FixReplacement
argument_list|>
name|ASC_RANGE_FIX_REPLACEMENT_COMPARATOR
init|=
name|Comparator
operator|.
name|comparing
argument_list|(
name|fixReplacement
lambda|->
name|fixReplacement
operator|.
name|range
argument_list|)
decl_stmt|;
DECL|method|FixCalculator ()
specifier|private
name|FixCalculator
parameter_list|()
block|{}
comment|/**    * Returns a result of applying fixes to an original content.    *    * @param originalContent is a text to which fixes must be applied    * @param fixReplacements is a list of fixes to be applied    * @throws ResourceConflictException if the fixReplacements contains invalid data (for example, if    *     an item points to an invalid range or if some ranges are intersected).    */
DECL|method|getNewFileContent ( String originalContent, List<FixReplacement> fixReplacements)
specifier|public
specifier|static
name|String
name|getNewFileContent
parameter_list|(
name|String
name|originalContent
parameter_list|,
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|fixReplacements
parameter_list|)
throws|throws
name|ResourceConflictException
block|{
name|FixResult
name|fixResult
init|=
name|calculateFix
argument_list|(
operator|new
name|Text
argument_list|(
name|originalContent
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
argument_list|,
name|fixReplacements
argument_list|)
decl_stmt|;
return|return
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
return|;
block|}
comment|/**    * Returns a result of applying fixes to an original content and list of applied edits.    *    * @param originalText is a text to which fixes must be applied    * @param fixReplacements is a list of fixes to be applied    * @return {@link FixResult}    * @throws ResourceConflictException if the fixReplacements contains invalid data (for example, if    *     an item points to an invalid range or if some ranges are intersected).    */
DECL|method|calculateFix (Text originalText, List<FixReplacement> fixReplacements)
specifier|public
specifier|static
name|FixResult
name|calculateFix
parameter_list|(
name|Text
name|originalText
parameter_list|,
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|fixReplacements
parameter_list|)
throws|throws
name|ResourceConflictException
block|{
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|sortedReplacements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|fixReplacements
argument_list|)
decl_stmt|;
name|sortedReplacements
operator|.
name|sort
argument_list|(
name|ASC_RANGE_FIX_REPLACEMENT_COMPARATOR
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sortedReplacements
operator|.
name|isEmpty
argument_list|()
operator|&&
name|sortedReplacements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|range
operator|.
name|startLine
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot calculate fix replacement for range %s"
argument_list|,
name|toString
argument_list|(
name|sortedReplacements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|range
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
name|ContentBuilder
name|builder
init|=
operator|new
name|ContentBuilder
argument_list|(
name|originalText
argument_list|)
decl_stmt|;
for|for
control|(
name|FixReplacement
name|fixReplacement
range|:
name|sortedReplacements
control|)
block|{
try|try
block|{
name|builder
operator|.
name|addReplacement
argument_list|(
name|fixReplacement
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot calculate fix replacement for range %s"
argument_list|,
name|toString
argument_list|(
name|fixReplacement
operator|.
name|range
argument_list|)
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|toString (Comment.Range range)
specifier|private
specifier|static
name|String
name|toString
parameter_list|(
name|Comment
operator|.
name|Range
name|range
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"(%s:%s - %s:%s)"
argument_list|,
name|range
operator|.
name|startLine
argument_list|,
name|range
operator|.
name|startChar
argument_list|,
name|range
operator|.
name|endLine
argument_list|,
name|range
operator|.
name|endChar
argument_list|)
return|;
block|}
DECL|class|ContentBuilder
specifier|private
specifier|static
class|class
name|ContentBuilder
block|{
DECL|class|FixRegion
specifier|private
specifier|static
class|class
name|FixRegion
block|{
DECL|field|startSrcLine
name|int
name|startSrcLine
decl_stmt|;
DECL|field|startDstLine
name|int
name|startDstLine
decl_stmt|;
DECL|field|startSrcPos
name|int
name|startSrcPos
decl_stmt|;
DECL|field|startDstPos
name|int
name|startDstPos
decl_stmt|;
DECL|field|internalEdits
name|List
argument_list|<
name|Edit
argument_list|>
name|internalEdits
decl_stmt|;
DECL|method|FixRegion ()
name|FixRegion
parameter_list|()
block|{
name|this
operator|.
name|internalEdits
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
block|}
DECL|field|contentProcessor
specifier|private
specifier|final
name|ContentProcessor
name|contentProcessor
decl_stmt|;
DECL|field|edits
specifier|final
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
DECL|field|currentRegion
name|FixRegion
name|currentRegion
decl_stmt|;
DECL|method|ContentBuilder (Text src)
name|ContentBuilder
parameter_list|(
name|Text
name|src
parameter_list|)
block|{
name|this
operator|.
name|contentProcessor
operator|=
operator|new
name|ContentProcessor
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|this
operator|.
name|edits
operator|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|addReplacement (FixReplacement replacement)
name|void
name|addReplacement
parameter_list|(
name|FixReplacement
name|replacement
parameter_list|)
block|{
if|if
condition|(
name|shouldStartNewEdit
argument_list|(
name|replacement
argument_list|)
condition|)
block|{
name|finishExistingEdit
argument_list|()
expr_stmt|;
block|}
comment|// processSrcContent expects that line number is 0-based,
comment|// but replacement.range.startLine is 1-based, so subtract 1
name|processSrcContent
argument_list|(
name|replacement
operator|.
name|range
operator|.
name|startLine
operator|-
literal|1
argument_list|,
name|replacement
operator|.
name|range
operator|.
name|startChar
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|processReplacement
argument_list|(
name|replacement
argument_list|)
expr_stmt|;
block|}
DECL|method|getNewText ()
name|Text
name|getNewText
parameter_list|()
block|{
return|return
operator|new
name|Text
argument_list|(
name|contentProcessor
operator|.
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
return|;
block|}
DECL|method|finish ()
name|void
name|finish
parameter_list|()
block|{
name|finishExistingEdit
argument_list|()
expr_stmt|;
if|if
condition|(
name|contentProcessor
operator|.
name|hasMoreLines
argument_list|()
condition|)
block|{
name|contentProcessor
operator|.
name|appendLinesToEndOfContent
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|build ()
specifier|public
name|FixResult
name|build
parameter_list|()
block|{
name|finish
argument_list|()
expr_stmt|;
return|return
operator|new
name|FixResult
argument_list|(
name|edits
operator|.
name|build
argument_list|()
argument_list|,
name|this
operator|.
name|getNewText
argument_list|()
argument_list|)
return|;
block|}
DECL|method|finishExistingEdit ()
specifier|private
name|void
name|finishExistingEdit
parameter_list|()
block|{
if|if
condition|(
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|column
operator|>
literal|0
operator|||
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|column
operator|>
literal|0
condition|)
block|{
name|contentProcessor
operator|.
name|processToEndOfLine
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|currentRegion
operator|!=
literal|null
condition|)
block|{
name|int
name|endSrc
init|=
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|line
decl_stmt|;
if|if
condition|(
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|column
operator|>
literal|0
condition|)
block|{
name|endSrc
operator|++
expr_stmt|;
block|}
name|int
name|endDst
init|=
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|line
decl_stmt|;
if|if
condition|(
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|column
operator|>
literal|0
condition|)
block|{
name|endDst
operator|++
expr_stmt|;
block|}
name|ReplaceEdit
name|edit
init|=
operator|new
name|ReplaceEdit
argument_list|(
name|currentRegion
operator|.
name|startSrcLine
argument_list|,
name|endSrc
argument_list|,
name|currentRegion
operator|.
name|startDstLine
argument_list|,
name|endDst
argument_list|,
name|currentRegion
operator|.
name|internalEdits
argument_list|)
decl_stmt|;
name|currentRegion
operator|=
literal|null
expr_stmt|;
name|edits
operator|.
name|add
argument_list|(
name|edit
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shouldStartNewEdit (FixReplacement replacement)
specifier|private
name|boolean
name|shouldStartNewEdit
parameter_list|(
name|FixReplacement
name|replacement
parameter_list|)
block|{
if|if
condition|(
name|currentRegion
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// New edit must be started if there is at least one unchanged line after the last edit
comment|// Subtract 1 from replacement.range.startLine because it is a 1-based line number,
comment|// and contentProcessor.srcPosition.line is a 0-based line number
return|return
name|replacement
operator|.
name|range
operator|.
name|startLine
operator|-
literal|1
operator|>
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|line
operator|+
literal|1
return|;
block|}
DECL|method|processSrcContent (int toLine, int toColumn, boolean append)
specifier|private
name|void
name|processSrcContent
parameter_list|(
name|int
name|toLine
parameter_list|,
name|int
name|toColumn
parameter_list|,
name|boolean
name|append
parameter_list|)
throws|throws
name|IndexOutOfBoundsException
block|{
comment|// toLine>= currentSrcLineIndex
if|if
condition|(
name|toLine
operator|==
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|line
condition|)
block|{
name|contentProcessor
operator|.
name|processLineToColumn
argument_list|(
name|toColumn
argument_list|,
name|append
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|contentProcessor
operator|.
name|processToEndOfLine
argument_list|(
name|append
argument_list|)
expr_stmt|;
name|contentProcessor
operator|.
name|processMultiline
argument_list|(
name|toLine
argument_list|,
name|append
argument_list|)
expr_stmt|;
name|contentProcessor
operator|.
name|processLineToColumn
argument_list|(
name|toColumn
argument_list|,
name|append
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processReplacement (FixReplacement fix)
specifier|private
name|void
name|processReplacement
parameter_list|(
name|FixReplacement
name|fix
parameter_list|)
block|{
if|if
condition|(
name|currentRegion
operator|==
literal|null
condition|)
block|{
name|currentRegion
operator|=
operator|new
name|FixRegion
argument_list|()
expr_stmt|;
name|currentRegion
operator|.
name|startSrcLine
operator|=
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|line
expr_stmt|;
name|currentRegion
operator|.
name|startSrcPos
operator|=
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|getLineStartPos
argument_list|()
expr_stmt|;
name|currentRegion
operator|.
name|startDstLine
operator|=
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|line
expr_stmt|;
name|currentRegion
operator|.
name|startDstPos
operator|=
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|getLineStartPos
argument_list|()
expr_stmt|;
block|}
name|int
name|srcStartPos
init|=
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|textPos
decl_stmt|;
name|int
name|dstStartPos
init|=
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|textPos
decl_stmt|;
name|contentProcessor
operator|.
name|appendReplacement
argument_list|(
name|fix
operator|.
name|replacement
argument_list|)
expr_stmt|;
name|processSrcContent
argument_list|(
name|fix
operator|.
name|range
operator|.
name|endLine
operator|-
literal|1
argument_list|,
name|fix
operator|.
name|range
operator|.
name|endChar
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|currentRegion
operator|.
name|internalEdits
operator|.
name|add
argument_list|(
operator|new
name|Edit
argument_list|(
name|srcStartPos
operator|-
name|currentRegion
operator|.
name|startSrcPos
argument_list|,
name|contentProcessor
operator|.
name|srcPosition
operator|.
name|textPos
operator|-
name|currentRegion
operator|.
name|startSrcPos
argument_list|,
name|dstStartPos
operator|-
name|currentRegion
operator|.
name|startDstPos
argument_list|,
name|contentProcessor
operator|.
name|dstPosition
operator|.
name|textPos
operator|-
name|currentRegion
operator|.
name|startDstPos
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ContentProcessor
specifier|private
specifier|static
class|class
name|ContentProcessor
block|{
DECL|class|ContentPosition
specifier|static
class|class
name|ContentPosition
block|{
DECL|field|line
name|int
name|line
decl_stmt|;
DECL|field|column
name|int
name|column
decl_stmt|;
DECL|field|textPos
name|int
name|textPos
decl_stmt|;
DECL|method|appendMultilineContent (int lineCount, int charCount)
name|void
name|appendMultilineContent
parameter_list|(
name|int
name|lineCount
parameter_list|,
name|int
name|charCount
parameter_list|)
block|{
name|line
operator|+=
name|lineCount
expr_stmt|;
name|column
operator|=
literal|0
expr_stmt|;
name|textPos
operator|+=
name|charCount
expr_stmt|;
block|}
DECL|method|appendLineEndedWithEOLMark (int charCount)
name|void
name|appendLineEndedWithEOLMark
parameter_list|(
name|int
name|charCount
parameter_list|)
block|{
name|textPos
operator|+=
name|charCount
expr_stmt|;
name|line
operator|++
expr_stmt|;
name|column
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|appendStringWithoutEOLMark (int charCount)
name|void
name|appendStringWithoutEOLMark
parameter_list|(
name|int
name|charCount
parameter_list|)
block|{
name|textPos
operator|+=
name|charCount
expr_stmt|;
name|column
operator|+=
name|charCount
expr_stmt|;
block|}
DECL|method|getLineStartPos ()
name|int
name|getLineStartPos
parameter_list|()
block|{
return|return
name|textPos
operator|-
name|column
return|;
block|}
block|}
DECL|field|sb
specifier|private
specifier|final
name|StringBuilder
name|sb
decl_stmt|;
DECL|field|srcPosition
specifier|final
name|ContentPosition
name|srcPosition
decl_stmt|;
DECL|field|dstPosition
specifier|final
name|ContentPosition
name|dstPosition
decl_stmt|;
DECL|field|currentSrcLine
name|String
name|currentSrcLine
decl_stmt|;
DECL|field|src
name|Text
name|src
decl_stmt|;
DECL|field|endOfSource
name|boolean
name|endOfSource
decl_stmt|;
DECL|method|ContentProcessor (Text src)
name|ContentProcessor
parameter_list|(
name|Text
name|src
parameter_list|)
block|{
name|this
operator|.
name|src
operator|=
name|src
expr_stmt|;
name|sb
operator|=
operator|new
name|StringBuilder
argument_list|(
name|src
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|srcPosition
operator|=
operator|new
name|ContentPosition
argument_list|()
expr_stmt|;
name|dstPosition
operator|=
operator|new
name|ContentPosition
argument_list|()
expr_stmt|;
name|endOfSource
operator|=
name|src
operator|.
name|size
argument_list|()
operator|==
literal|0
expr_stmt|;
block|}
DECL|method|processMultiline (int toLine, boolean append)
name|void
name|processMultiline
parameter_list|(
name|int
name|toLine
parameter_list|,
name|boolean
name|append
parameter_list|)
block|{
if|if
condition|(
name|endOfSource
operator|||
name|toLine
operator|<=
name|srcPosition
operator|.
name|line
condition|)
block|{
return|return;
block|}
name|int
name|fromLine
init|=
name|srcPosition
operator|.
name|line
decl_stmt|;
name|String
name|lines
init|=
name|src
operator|.
name|getString
argument_list|(
name|fromLine
argument_list|,
name|toLine
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|int
name|lineCount
init|=
name|toLine
operator|-
name|fromLine
decl_stmt|;
name|int
name|charCount
init|=
name|lines
operator|.
name|length
argument_list|()
decl_stmt|;
name|srcPosition
operator|.
name|appendMultilineContent
argument_list|(
name|lineCount
argument_list|,
name|charCount
argument_list|)
expr_stmt|;
if|if
condition|(
name|append
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|lines
argument_list|)
expr_stmt|;
name|dstPosition
operator|.
name|appendMultilineContent
argument_list|(
name|lineCount
argument_list|,
name|charCount
argument_list|)
expr_stmt|;
block|}
name|currentSrcLine
operator|=
literal|null
expr_stmt|;
name|endOfSource
operator|=
name|srcPosition
operator|.
name|line
operator|>=
name|src
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
DECL|method|processToEndOfLine (boolean append)
name|void
name|processToEndOfLine
parameter_list|(
name|boolean
name|append
parameter_list|)
block|{
if|if
condition|(
name|endOfSource
condition|)
block|{
return|return;
block|}
name|String
name|srcLine
init|=
name|getCurrentSrcLine
argument_list|()
decl_stmt|;
name|int
name|from
init|=
name|srcPosition
operator|.
name|column
decl_stmt|;
name|int
name|charCount
init|=
name|srcLine
operator|.
name|length
argument_list|()
operator|-
name|from
decl_stmt|;
name|boolean
name|lastLineNoEOLMark
init|=
name|srcPosition
operator|.
name|line
operator|>=
name|src
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|&&
name|src
operator|.
name|isMissingNewlineAtEnd
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|lastLineNoEOLMark
condition|)
block|{
name|srcPosition
operator|.
name|appendLineEndedWithEOLMark
argument_list|(
name|charCount
argument_list|)
expr_stmt|;
name|endOfSource
operator|=
name|srcPosition
operator|.
name|line
operator|>=
name|src
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|srcPosition
operator|.
name|appendStringWithoutEOLMark
argument_list|(
name|charCount
argument_list|)
expr_stmt|;
name|endOfSource
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|append
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|srcLine
argument_list|,
name|from
argument_list|,
name|srcLine
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|lastLineNoEOLMark
condition|)
block|{
name|dstPosition
operator|.
name|appendLineEndedWithEOLMark
argument_list|(
name|charCount
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dstPosition
operator|.
name|appendStringWithoutEOLMark
argument_list|(
name|charCount
argument_list|)
expr_stmt|;
block|}
block|}
name|currentSrcLine
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|processLineToColumn (int to, boolean append)
name|void
name|processLineToColumn
parameter_list|(
name|int
name|to
parameter_list|,
name|boolean
name|append
parameter_list|)
throws|throws
name|IndexOutOfBoundsException
block|{
if|if
condition|(
name|to
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|String
name|srcLine
init|=
name|getCurrentSrcLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|to
operator|>
name|srcLine
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Parameter to is out of string"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|to
operator|==
name|srcLine
operator|.
name|length
argument_list|()
condition|)
block|{
if|if
condition|(
name|srcPosition
operator|.
name|line
operator|<
name|src
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|||
operator|!
name|src
operator|.
name|isMissingNewlineAtEnd
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"The processLineToColumn shouldn't add end of line"
argument_list|)
throw|;
block|}
block|}
name|int
name|from
init|=
name|srcPosition
operator|.
name|column
decl_stmt|;
name|int
name|charCount
init|=
name|to
operator|-
name|from
decl_stmt|;
name|srcPosition
operator|.
name|appendStringWithoutEOLMark
argument_list|(
name|charCount
argument_list|)
expr_stmt|;
if|if
condition|(
name|append
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|srcLine
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|dstPosition
operator|.
name|appendStringWithoutEOLMark
argument_list|(
name|charCount
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|appendLinesToEndOfContent ()
name|void
name|appendLinesToEndOfContent
parameter_list|()
block|{
name|processMultiline
argument_list|(
name|src
operator|.
name|size
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|appendReplacement (String replacement)
name|void
name|appendReplacement
parameter_list|(
name|String
name|replacement
parameter_list|)
block|{
if|if
condition|(
name|replacement
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|sb
operator|.
name|append
argument_list|(
name|replacement
argument_list|)
expr_stmt|;
name|int
name|lastNewLinePos
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|newLineMarkCount
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|index
init|=
name|replacement
operator|.
name|indexOf
argument_list|(
literal|'\n'
argument_list|,
name|lastNewLinePos
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|lastNewLinePos
operator|=
name|index
expr_stmt|;
name|newLineMarkCount
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|newLineMarkCount
operator|>
literal|0
condition|)
block|{
name|dstPosition
operator|.
name|appendMultilineContent
argument_list|(
name|newLineMarkCount
argument_list|,
name|lastNewLinePos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|dstPosition
operator|.
name|appendStringWithoutEOLMark
argument_list|(
name|replacement
operator|.
name|length
argument_list|()
operator|-
name|lastNewLinePos
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|hasMoreLines ()
name|boolean
name|hasMoreLines
parameter_list|()
block|{
return|return
operator|!
name|endOfSource
return|;
block|}
DECL|method|getCurrentSrcLine ()
specifier|private
name|String
name|getCurrentSrcLine
parameter_list|()
block|{
if|if
condition|(
name|currentSrcLine
operator|==
literal|null
condition|)
block|{
name|currentSrcLine
operator|=
name|src
operator|.
name|getString
argument_list|(
name|srcPosition
operator|.
name|line
argument_list|,
name|srcPosition
operator|.
name|line
operator|+
literal|1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|currentSrcLine
return|;
block|}
block|}
comment|/** The result of applying fix to a file content */
DECL|class|FixResult
specifier|public
specifier|static
class|class
name|FixResult
block|{
comment|/** List of edits to transform an original text to a final text (with all fixes applied) */
DECL|field|edits
specifier|public
specifier|final
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
comment|/** Final text with all applied fixes */
DECL|field|text
specifier|public
specifier|final
name|Text
name|text
decl_stmt|;
DECL|method|FixResult (ImmutableList<Edit> edits, Text text)
name|FixResult
parameter_list|(
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|,
name|Text
name|text
parameter_list|)
block|{
name|this
operator|.
name|edits
operator|=
name|edits
expr_stmt|;
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

