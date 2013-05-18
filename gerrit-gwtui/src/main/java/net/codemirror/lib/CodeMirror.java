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
DECL|package|net.codemirror.lib
package|package
name|net
operator|.
name|codemirror
operator|.
name|lib
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
name|ScriptElement
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
name|StyleInjector
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
name|resources
operator|.
name|client
operator|.
name|ExternalTextResource
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
name|resources
operator|.
name|client
operator|.
name|ResourceCallback
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
name|resources
operator|.
name|client
operator|.
name|ResourceException
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
name|resources
operator|.
name|client
operator|.
name|TextResource
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
name|safehtml
operator|.
name|shared
operator|.
name|SafeUri
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_class
DECL|class|CodeMirror
specifier|public
class|class
name|CodeMirror
block|{
DECL|method|install ()
specifier|public
specifier|static
name|void
name|install
parameter_list|()
block|{
name|asyncInjectCss
argument_list|(
name|Lib
operator|.
name|I
operator|.
name|css
argument_list|()
argument_list|)
expr_stmt|;
name|asyncInjectScript
argument_list|(
name|Lib
operator|.
name|I
operator|.
name|js
argument_list|()
operator|.
name|getSafeUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|asyncInjectCss (ExternalTextResource css)
specifier|private
specifier|static
name|void
name|asyncInjectCss
parameter_list|(
name|ExternalTextResource
name|css
parameter_list|)
block|{
try|try
block|{
name|css
operator|.
name|getText
argument_list|(
operator|new
name|ResourceCallback
argument_list|<
name|TextResource
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|TextResource
name|resource
parameter_list|)
block|{
name|StyleInjector
operator|.
name|inject
argument_list|(
name|resource
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|ResourceException
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceException
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|asyncInjectScript (SafeUri uri)
specifier|private
specifier|static
name|void
name|asyncInjectScript
parameter_list|(
name|SafeUri
name|uri
parameter_list|)
block|{
name|ScriptElement
name|script
init|=
name|Document
operator|.
name|get
argument_list|()
operator|.
name|createScriptElement
argument_list|()
decl_stmt|;
name|script
operator|.
name|setSrc
argument_list|(
name|uri
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
name|script
operator|.
name|setLang
argument_list|(
literal|"javascript"
argument_list|)
expr_stmt|;
name|script
operator|.
name|setType
argument_list|(
literal|"text/javascript"
argument_list|)
expr_stmt|;
name|Document
operator|.
name|get
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|appendChild
argument_list|(
name|script
argument_list|)
expr_stmt|;
block|}
DECL|method|error (ResourceException e)
specifier|private
specifier|static
name|void
name|error
parameter_list|(
name|ResourceException
name|e
parameter_list|)
block|{
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"net.codemirror"
argument_list|)
decl_stmt|;
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Cannot fetch CSS"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|CodeMirror ()
specifier|private
name|CodeMirror
parameter_list|()
block|{   }
block|}
end_class

end_unit

