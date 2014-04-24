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
DECL|field|line
specifier|private
name|int
name|line
decl_stmt|;
DECL|field|pos
specifier|private
name|int
name|pos
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
name|numOfChar
operator|=
name|adjustForNegativeDelta
argument_list|(
name|numOfChar
argument_list|)
expr_stmt|;
while|while
condition|(
name|line
operator|<
name|lines
operator|.
name|length
argument_list|()
condition|)
block|{
name|int
name|len
init|=
name|lines
operator|.
name|get
argument_list|(
name|line
argument_list|)
operator|.
name|length
argument_list|()
operator|-
name|pos
operator|+
literal|1
decl_stmt|;
comment|// + 1 for LF
if|if
condition|(
name|numOfChar
operator|<
name|len
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
name|line
argument_list|,
name|numOfChar
operator|+
name|pos
argument_list|)
decl_stmt|;
name|pos
operator|+=
name|numOfChar
expr_stmt|;
return|return
name|at
return|;
block|}
name|numOfChar
operator|-=
name|len
expr_stmt|;
name|line
operator|++
expr_stmt|;
name|pos
operator|=
literal|0
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
name|line
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
literal|"EditIterator index out of bounds"
argument_list|)
throw|;
block|}
DECL|method|adjustForNegativeDelta (int n)
specifier|private
name|int
name|adjustForNegativeDelta
parameter_list|(
name|int
name|n
parameter_list|)
block|{
while|while
condition|(
name|n
operator|<
literal|0
condition|)
block|{
if|if
condition|(
operator|-
name|n
operator|<=
name|pos
condition|)
block|{
name|pos
operator|+=
name|n
expr_stmt|;
return|return
literal|0
return|;
block|}
name|n
operator|+=
name|pos
expr_stmt|;
name|line
operator|--
expr_stmt|;
if|if
condition|(
name|line
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"EditIterator index out of bounds"
argument_list|)
throw|;
block|}
name|pos
operator|=
name|lines
operator|.
name|get
argument_list|(
name|line
argument_list|)
operator|.
name|length
argument_list|()
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
block|}
end_class

end_unit

