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
comment|// limitations under the License.package com.google.gerrit.server.git;
end_comment

begin_package
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|gerrit
operator|.
name|client
operator|.
name|projects
operator|.
name|ThemeInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|dom
operator|.
name|client
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|dom
operator|.
name|client
operator|.
name|StyleElement
import|;
end_import

begin_class
DECL|class|Themer
specifier|public
class|class
name|Themer
block|{
DECL|class|ThemerIE
specifier|public
specifier|static
class|class
name|ThemerIE
extends|extends
name|Themer
block|{
DECL|method|ThemerIE ()
specifier|protected
name|ThemerIE
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getCssText (StyleElement el)
specifier|protected
name|String
name|getCssText
parameter_list|(
name|StyleElement
name|el
parameter_list|)
block|{
return|return
name|el
operator|.
name|getCssText
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setCssText (StyleElement el, String css)
specifier|protected
name|void
name|setCssText
parameter_list|(
name|StyleElement
name|el
parameter_list|,
name|String
name|css
parameter_list|)
block|{
name|el
operator|.
name|setCssText
argument_list|(
name|css
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|cssElement
specifier|protected
name|StyleElement
name|cssElement
decl_stmt|;
DECL|field|headerElement
specifier|protected
name|Element
name|headerElement
decl_stmt|;
DECL|field|footerElement
specifier|protected
name|Element
name|footerElement
decl_stmt|;
DECL|field|cssText
specifier|protected
name|String
name|cssText
decl_stmt|;
DECL|field|headerHtml
specifier|protected
name|String
name|headerHtml
decl_stmt|;
DECL|field|footerHtml
specifier|protected
name|String
name|footerHtml
decl_stmt|;
DECL|method|Themer ()
specifier|protected
name|Themer
parameter_list|()
block|{   }
DECL|method|set (ThemeInfo theme)
specifier|public
name|void
name|set
parameter_list|(
name|ThemeInfo
name|theme
parameter_list|)
block|{
name|set
argument_list|(
name|theme
operator|.
name|css
argument_list|()
operator|!=
literal|null
condition|?
name|theme
operator|.
name|css
argument_list|()
else|:
name|cssText
argument_list|,
name|theme
operator|.
name|header
argument_list|()
operator|!=
literal|null
condition|?
name|theme
operator|.
name|header
argument_list|()
else|:
name|headerHtml
argument_list|,
name|theme
operator|.
name|footer
argument_list|()
operator|!=
literal|null
condition|?
name|theme
operator|.
name|footer
argument_list|()
else|:
name|footerHtml
argument_list|)
expr_stmt|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|set
argument_list|(
name|cssText
argument_list|,
name|headerHtml
argument_list|,
name|footerHtml
argument_list|)
expr_stmt|;
block|}
DECL|method|init (Element css, Element header, Element footer)
name|void
name|init
parameter_list|(
name|Element
name|css
parameter_list|,
name|Element
name|header
parameter_list|,
name|Element
name|footer
parameter_list|)
block|{
name|cssElement
operator|=
name|StyleElement
operator|.
name|as
argument_list|(
name|css
argument_list|)
expr_stmt|;
name|headerElement
operator|=
name|header
expr_stmt|;
name|footerElement
operator|=
name|footer
expr_stmt|;
name|cssText
operator|=
name|getCssText
argument_list|(
name|this
operator|.
name|cssElement
argument_list|)
expr_stmt|;
name|headerHtml
operator|=
name|header
operator|.
name|getInnerHTML
argument_list|()
expr_stmt|;
name|footerHtml
operator|=
name|footer
operator|.
name|getInnerHTML
argument_list|()
expr_stmt|;
block|}
DECL|method|getCssText (StyleElement el)
specifier|protected
name|String
name|getCssText
parameter_list|(
name|StyleElement
name|el
parameter_list|)
block|{
return|return
name|el
operator|.
name|getInnerHTML
argument_list|()
return|;
block|}
DECL|method|setCssText (StyleElement el, String css)
specifier|protected
name|void
name|setCssText
parameter_list|(
name|StyleElement
name|el
parameter_list|,
name|String
name|css
parameter_list|)
block|{
name|el
operator|.
name|setInnerHTML
argument_list|(
name|css
argument_list|)
expr_stmt|;
block|}
DECL|method|set (String css, String header, String footer)
specifier|private
name|void
name|set
parameter_list|(
name|String
name|css
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|footer
parameter_list|)
block|{
name|setCssText
argument_list|(
name|cssElement
argument_list|,
name|css
argument_list|)
expr_stmt|;
name|headerElement
operator|.
name|setInnerHTML
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|footerElement
operator|.
name|setInnerHTML
argument_list|(
name|footer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

