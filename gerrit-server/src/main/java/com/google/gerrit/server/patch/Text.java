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
name|org
operator|.
name|eclipse
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
name|eclipse
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
name|mozilla
operator|.
name|universalchardet
operator|.
name|UniversalDetector
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

begin_class
DECL|class|Text
specifier|public
class|class
name|Text
extends|extends
name|RawText
block|{
DECL|field|NO_BYTES
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|NO_BYTES
init|=
block|{}
decl_stmt|;
DECL|field|EMPTY
specifier|public
specifier|static
specifier|final
name|Text
name|EMPTY
init|=
operator|new
name|Text
argument_list|(
name|NO_BYTES
argument_list|)
decl_stmt|;
DECL|method|asString (byte[] content, String encoding)
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|byte
index|[]
name|content
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|UniversalDetector
name|d
init|=
operator|new
name|UniversalDetector
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|d
operator|.
name|handleData
argument_list|(
name|content
argument_list|,
literal|0
argument_list|,
name|content
operator|.
name|length
argument_list|)
expr_stmt|;
name|d
operator|.
name|dataEnd
argument_list|()
expr_stmt|;
name|encoding
operator|=
name|d
operator|.
name|getDetectedCharset
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
literal|"ISO-8859-1"
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|content
argument_list|,
name|encoding
argument_list|)
return|;
block|}
DECL|method|Text (final byte[] r)
specifier|public
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
specifier|public
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
specifier|public
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
block|}
end_class

end_unit

