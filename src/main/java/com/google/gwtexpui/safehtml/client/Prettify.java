begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_class
DECL|class|Prettify
specifier|public
class|class
name|Prettify
block|{
DECL|field|loaded
specifier|private
specifier|static
name|boolean
name|loaded
init|=
name|isLoaded
argument_list|()
decl_stmt|;
DECL|method|isLoaded ()
specifier|private
specifier|static
specifier|native
name|boolean
name|isLoaded
parameter_list|()
comment|/*-{ return $wnd['prettyPrintOne'] != null }-*/
function_decl|;
DECL|method|prettify (SafeHtml src, String srcType)
specifier|public
specifier|static
name|SafeHtml
name|prettify
parameter_list|(
name|SafeHtml
name|src
parameter_list|,
name|String
name|srcType
parameter_list|)
block|{
if|if
condition|(
name|loaded
condition|)
block|{
name|src
operator|=
name|src
operator|.
name|replaceAll
argument_list|(
literal|"&#39;"
argument_list|,
literal|"'"
argument_list|)
expr_stmt|;
name|src
operator|=
name|SafeHtml
operator|.
name|asis
argument_list|(
name|prettifyNative
argument_list|(
name|src
operator|.
name|asString
argument_list|()
argument_list|,
name|srcType
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|src
return|;
block|}
DECL|method|prettifyNative (String srcText, String srcType)
specifier|private
specifier|static
specifier|native
name|String
name|prettifyNative
parameter_list|(
name|String
name|srcText
parameter_list|,
name|String
name|srcType
parameter_list|)
comment|/*-{ return $wnd.prettyPrintOne(srcText, srcType); }-*/
function_decl|;
DECL|method|Prettify ()
specifier|private
name|Prettify
parameter_list|()
block|{   }
block|}
end_class

end_unit

