begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|GWT
import|;
end_import

begin_comment
comment|/** Utilities for dealing with the DOM. */
end_comment

begin_class
DECL|class|DomUtil
specifier|public
specifier|abstract
class|class
name|DomUtil
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|Impl
name|INSTANCE
decl_stmt|;
static|static
block|{
if|if
condition|(
name|GWT
operator|.
name|isClient
argument_list|()
condition|)
name|INSTANCE
operator|=
operator|new
name|ClientImpl
argument_list|()
expr_stmt|;
else|else
name|INSTANCE
operator|=
operator|new
name|JavaImpl
argument_list|()
expr_stmt|;
block|}
comment|/** Escape XML/HTML special characters in the input string. */
DECL|method|escape (final String in)
specifier|public
specifier|static
name|String
name|escape
parameter_list|(
specifier|final
name|String
name|in
parameter_list|)
block|{
return|return
name|INSTANCE
operator|.
name|escape
argument_list|(
name|in
argument_list|)
return|;
block|}
DECL|method|DomUtil ()
specifier|private
name|DomUtil
parameter_list|()
block|{   }
DECL|class|Impl
specifier|private
specifier|static
specifier|abstract
class|class
name|Impl
block|{
DECL|method|escape (String in)
specifier|abstract
name|String
name|escape
parameter_list|(
name|String
name|in
parameter_list|)
function_decl|;
block|}
DECL|class|ClientImpl
specifier|private
specifier|static
class|class
name|ClientImpl
extends|extends
name|Impl
block|{
annotation|@
name|Override
DECL|method|escape (String src)
specifier|native
name|String
name|escape
parameter_list|(
name|String
name|src
parameter_list|)
comment|/*-{ return src.replace(/&/g,'&amp;').replace(/>/g,'&gt;').replace(/</g,'&lt;').replace(/"/g,'&quot;'); }-*/
function_decl|;
block|}
DECL|class|JavaImpl
specifier|private
specifier|static
class|class
name|JavaImpl
extends|extends
name|Impl
block|{
annotation|@
name|Override
DECL|method|escape (final String in)
name|String
name|escape
parameter_list|(
specifier|final
name|String
name|in
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|(
name|in
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|in
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|char
name|c
init|=
name|in
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'&'
case|:
name|r
operator|.
name|append
argument_list|(
literal|"&amp;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'>'
case|:
name|r
operator|.
name|append
argument_list|(
literal|"&gt;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'<'
case|:
name|r
operator|.
name|append
argument_list|(
literal|"&lt;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'"'
case|:
name|r
operator|.
name|append
argument_list|(
literal|"&quot;"
argument_list|)
expr_stmt|;
break|break;
default|default:
name|r
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

