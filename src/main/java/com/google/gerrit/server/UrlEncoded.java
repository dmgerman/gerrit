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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

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
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
DECL|class|UrlEncoded
specifier|public
class|class
name|UrlEncoded
extends|extends
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|method|UrlEncoded ()
specifier|public
name|UrlEncoded
parameter_list|()
block|{   }
DECL|method|UrlEncoded (final String url)
specifier|public
name|UrlEncoded
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|char
name|separator
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|separator
operator|=
literal|'?'
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|separator
operator|!=
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|separator
operator|=
literal|'&'
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|encode (final String str)
specifier|private
specifier|static
name|String
name|encode
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
try|try
block|{
return|return
name|URLEncoder
operator|.
name|encode
argument_list|(
name|str
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

