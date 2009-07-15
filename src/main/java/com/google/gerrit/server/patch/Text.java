begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|SparseFileContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|diff
operator|.
name|RawText
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_class
DECL|class|Text
class|class
name|Text
extends|extends
name|RawText
block|{
DECL|field|EMPTY
specifier|static
specifier|final
name|Text
name|EMPTY
init|=
operator|new
name|Text
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
DECL|method|Text (final byte[] r)
name|Text
parameter_list|(
specifier|final
name|byte
index|[]
name|r
parameter_list|)
block|{
name|super
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|getContent ()
name|byte
index|[]
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
DECL|method|getLine (final int i)
name|String
name|getLine
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
specifier|final
name|int
name|s
init|=
name|lines
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|e
init|=
name|lines
operator|.
name|get
argument_list|(
name|i
operator|+
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
index|[
name|e
operator|-
literal|1
index|]
operator|==
literal|'\n'
condition|)
block|{
name|e
operator|--
expr_stmt|;
block|}
return|return
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|Constants
operator|.
name|CHARSET
argument_list|,
name|content
argument_list|,
name|s
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|addLineTo (final SparseFileContent out, final int i)
name|void
name|addLineTo
parameter_list|(
specifier|final
name|SparseFileContent
name|out
parameter_list|,
specifier|final
name|int
name|i
parameter_list|)
block|{
name|out
operator|.
name|addLine
argument_list|(
name|i
argument_list|,
name|getLine
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

