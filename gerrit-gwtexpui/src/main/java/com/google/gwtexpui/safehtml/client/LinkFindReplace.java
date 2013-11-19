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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|regexp
operator|.
name|shared
operator|.
name|RegExp
import|;
end_import

begin_comment
comment|/**  * A Find/Replace pair whose replacement string is a link.  *<p>  * It is safe to pass arbitrary user-provided links to this class. Links are  * sanitized as follows:  *<ul>  *<li>Only http(s) and mailto links are supported; any other scheme results in  * an {@link IllegalArgumentException} from {@link #replace(String)}.  *<li>Special characters in the link after regex replacement are escaped with  * {@link SafeHtmlBuilder}.</li>  *</ul>  */
end_comment

begin_class
DECL|class|LinkFindReplace
specifier|public
class|class
name|LinkFindReplace
implements|implements
name|FindReplace
block|{
DECL|method|hasValidScheme (String link)
specifier|public
specifier|static
name|boolean
name|hasValidScheme
parameter_list|(
name|String
name|link
parameter_list|)
block|{
name|int
name|colon
init|=
name|link
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|colon
operator|<
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|scheme
init|=
name|link
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colon
argument_list|)
decl_stmt|;
return|return
literal|"http"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"https"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"mailto"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scheme
argument_list|)
return|;
block|}
DECL|field|pat
specifier|private
name|RegExp
name|pat
decl_stmt|;
DECL|field|link
specifier|private
name|String
name|link
decl_stmt|;
DECL|method|LinkFindReplace ()
specifier|protected
name|LinkFindReplace
parameter_list|()
block|{   }
comment|/**    * @param find regular expression pattern to match substrings with.    * @param link replacement link href. Capture groups within    *<code>find</code> can be referenced with<code>$<i>n</i></code>.    */
DECL|method|LinkFindReplace (String find, String link)
specifier|public
name|LinkFindReplace
parameter_list|(
name|String
name|find
parameter_list|,
name|String
name|link
parameter_list|)
block|{
name|this
operator|.
name|pat
operator|=
name|RegExp
operator|.
name|compile
argument_list|(
name|find
argument_list|)
expr_stmt|;
name|this
operator|.
name|link
operator|=
name|link
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pattern ()
specifier|public
name|RegExp
name|pattern
parameter_list|()
block|{
return|return
name|pat
return|;
block|}
annotation|@
name|Override
DECL|method|replace (String input)
specifier|public
name|String
name|replace
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|String
name|href
init|=
name|pat
operator|.
name|replace
argument_list|(
name|input
argument_list|,
name|link
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasValidScheme
argument_list|(
name|href
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid scheme ("
operator|+
name|toString
argument_list|()
operator|+
literal|"): "
operator|+
name|href
argument_list|)
throw|;
block|}
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|openAnchor
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
name|href
argument_list|)
operator|.
name|append
argument_list|(
name|SafeHtml
operator|.
name|asis
argument_list|(
name|input
argument_list|)
argument_list|)
operator|.
name|closeAnchor
argument_list|()
operator|.
name|asString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"find = "
operator|+
name|pat
operator|.
name|getSource
argument_list|()
operator|+
literal|", link = "
operator|+
name|link
return|;
block|}
block|}
end_class

end_unit

