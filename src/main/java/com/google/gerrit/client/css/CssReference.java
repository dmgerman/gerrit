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
DECL|package|com.google.gerrit.client.css
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|css
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
name|Document
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
name|LinkElement
import|;
end_import

begin_comment
comment|/** Reference to a CSS file under the module base. */
end_comment

begin_class
DECL|class|CssReference
specifier|public
class|class
name|CssReference
block|{
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
comment|/**    * Create a new reference to a CSS file.    *     * @param name name of the CSS file, under the module base. Typically this is    *        a MD-5 or SHA-1 hash of the file content, with    *<code>.cache.css</code> suffix, to permit long-lived caching of the    *        resource by browers and edge caches.    */
DECL|method|CssReference (final String name)
specifier|public
name|CssReference
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|url
operator|=
name|GWT
operator|.
name|getModuleBaseURL
argument_list|()
operator|+
name|name
expr_stmt|;
block|}
comment|/** Inject this resource into the document as a style sheet. */
DECL|method|inject ()
specifier|public
name|void
name|inject
parameter_list|()
block|{
specifier|final
name|LinkElement
name|style
init|=
name|Document
operator|.
name|get
argument_list|()
operator|.
name|createLinkElement
argument_list|()
decl_stmt|;
name|style
operator|.
name|setRel
argument_list|(
literal|"stylesheet"
argument_list|)
expr_stmt|;
name|style
operator|.
name|setHref
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|Document
operator|.
name|get
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"head"
argument_list|)
operator|.
name|getItem
argument_list|(
literal|0
argument_list|)
operator|.
name|appendChild
argument_list|(
name|style
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

