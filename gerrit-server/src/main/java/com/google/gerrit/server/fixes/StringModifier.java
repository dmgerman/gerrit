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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_comment
comment|/**  * A modifier of a string. It allows to replace multiple parts of a string by indicating those parts  * with indices based on the unmodified string. There is one limitation though: Replacements which  * affect lower indices of the string must be specified before replacements for higher indices.  */
end_comment

begin_class
DECL|class|StringModifier
class|class
name|StringModifier
block|{
DECL|field|stringBuilder
specifier|private
specifier|final
name|StringBuilder
name|stringBuilder
decl_stmt|;
DECL|field|characterShift
specifier|private
name|int
name|characterShift
init|=
literal|0
decl_stmt|;
DECL|field|previousEndOffset
specifier|private
name|int
name|previousEndOffset
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
DECL|method|StringModifier (String string)
name|StringModifier
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|,
literal|"string must not be null"
argument_list|)
expr_stmt|;
name|stringBuilder
operator|=
operator|new
name|StringBuilder
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**    * Replaces part of the string with another content. When called multiple times, the calls must be    * ordered according to increasing start indices. Overlapping replacement regions aren't    * supported.    *    * @param startIndex the beginning index in the unmodified string (inclusive)    * @param endIndex the ending index in the unmodified string (exclusive)    * @param replacement the string which should be used instead of the original content    * @throws StringIndexOutOfBoundsException if the start index is smaller than the end index of a    *     previous call of this method    */
DECL|method|replace (int startIndex, int endIndex, String replacement)
specifier|public
name|void
name|replace
parameter_list|(
name|int
name|startIndex
parameter_list|,
name|int
name|endIndex
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|replacement
argument_list|,
literal|"replacement string must not be null"
argument_list|)
expr_stmt|;
if|if
condition|(
name|previousEndOffset
operator|>
name|startIndex
condition|)
block|{
throw|throw
operator|new
name|StringIndexOutOfBoundsException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Not supported to replace the content starting at index %s after previous "
operator|+
literal|"replacement which ended at index %s"
argument_list|,
name|startIndex
argument_list|,
name|previousEndOffset
argument_list|)
argument_list|)
throw|;
block|}
name|int
name|shiftedStartIndex
init|=
name|startIndex
operator|+
name|characterShift
decl_stmt|;
name|int
name|shiftedEndIndex
init|=
name|endIndex
operator|+
name|characterShift
decl_stmt|;
if|if
condition|(
name|shiftedEndIndex
operator|>
name|stringBuilder
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|StringIndexOutOfBoundsException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"end %s> length %s"
argument_list|,
name|shiftedEndIndex
argument_list|,
name|stringBuilder
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|stringBuilder
operator|.
name|replace
argument_list|(
name|shiftedStartIndex
argument_list|,
name|shiftedEndIndex
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
name|int
name|replacedContentLength
init|=
name|endIndex
operator|-
name|startIndex
decl_stmt|;
name|characterShift
operator|+=
name|replacement
operator|.
name|length
argument_list|()
operator|-
name|replacedContentLength
expr_stmt|;
name|previousEndOffset
operator|=
name|endIndex
expr_stmt|;
block|}
comment|/**    * Returns the modified string including all specified replacements.    *    * @return the modified string    */
DECL|method|getResult ()
specifier|public
name|String
name|getResult
parameter_list|()
block|{
return|return
name|stringBuilder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

