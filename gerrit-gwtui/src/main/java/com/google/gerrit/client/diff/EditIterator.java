begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JsArrayString
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|LineCharacter
import|;
end_import

begin_comment
comment|/** An iterator for intraline edits */
end_comment

begin_class
DECL|class|EditIterator
class|class
name|EditIterator
block|{
DECL|field|lines
specifier|private
specifier|final
name|JsArrayString
name|lines
decl_stmt|;
DECL|field|startLine
specifier|private
specifier|final
name|int
name|startLine
decl_stmt|;
DECL|field|currLineIndex
specifier|private
name|int
name|currLineIndex
decl_stmt|;
DECL|field|currLineOffset
specifier|private
name|int
name|currLineOffset
decl_stmt|;
DECL|method|EditIterator (JsArrayString lineArray, int start)
name|EditIterator
parameter_list|(
name|JsArrayString
name|lineArray
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|lines
operator|=
name|lineArray
expr_stmt|;
name|startLine
operator|=
name|start
expr_stmt|;
block|}
DECL|method|advance (int numOfChar)
name|LineCharacter
name|advance
parameter_list|(
name|int
name|numOfChar
parameter_list|)
block|{
while|while
condition|(
name|currLineIndex
operator|<
name|lines
operator|.
name|length
argument_list|()
condition|)
block|{
name|int
name|lengthWithNewline
init|=
name|lines
operator|.
name|get
argument_list|(
name|currLineIndex
argument_list|)
operator|.
name|length
argument_list|()
operator|-
name|currLineOffset
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|numOfChar
operator|<
name|lengthWithNewline
condition|)
block|{
name|LineCharacter
name|at
init|=
name|LineCharacter
operator|.
name|create
argument_list|(
name|startLine
operator|+
name|currLineIndex
argument_list|,
name|numOfChar
operator|+
name|currLineOffset
argument_list|)
decl_stmt|;
name|currLineOffset
operator|+=
name|numOfChar
expr_stmt|;
return|return
name|at
return|;
block|}
name|numOfChar
operator|-=
name|lengthWithNewline
expr_stmt|;
name|advanceLine
argument_list|()
expr_stmt|;
if|if
condition|(
name|numOfChar
operator|==
literal|0
condition|)
block|{
return|return
name|LineCharacter
operator|.
name|create
argument_list|(
name|startLine
operator|+
name|currLineIndex
argument_list|,
literal|0
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"EditIterator index out of bound"
argument_list|)
throw|;
block|}
DECL|method|advanceLine ()
specifier|private
name|void
name|advanceLine
parameter_list|()
block|{
name|currLineIndex
operator|++
expr_stmt|;
name|currLineOffset
operator|=
literal|0
expr_stmt|;
block|}
block|}
end_class

end_unit

