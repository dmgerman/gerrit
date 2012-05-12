begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.documentation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|documentation
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|pegdown
operator|.
name|Extensions
operator|.
name|ALL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|pegdown
operator|.
name|Extensions
operator|.
name|HARDWRAPS
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
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|PegDownProcessor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_class
DECL|class|MarkdownFormatter
specifier|public
class|class
name|MarkdownFormatter
block|{
DECL|method|getHtmlFromMarkdown (byte[] data, String charEnc)
specifier|public
name|byte
index|[]
name|getHtmlFromMarkdown
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|String
name|charEnc
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
return|return
operator|new
name|PegDownProcessor
argument_list|(
name|ALL
operator|&
operator|~
operator|(
name|HARDWRAPS
operator|)
argument_list|)
operator|.
name|markdownToHtml
argument_list|(
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charEnc
argument_list|)
argument_list|,
name|data
argument_list|)
argument_list|)
operator|.
name|getBytes
argument_list|(
name|charEnc
argument_list|)
return|;
block|}
comment|// TODO: Add a cache
block|}
end_class

end_unit

