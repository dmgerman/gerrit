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
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|CallbackGroup
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
name|core
operator|.
name|client
operator|.
name|Callback
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
name|core
operator|.
name|client
operator|.
name|ScriptInjector
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
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|Loader
specifier|public
class|class
name|Loader
block|{
DECL|method|isLibLoaded ()
specifier|private
specifier|static
specifier|native
name|boolean
name|isLibLoaded
parameter_list|()
comment|/*-{ return $wnd.hasOwnProperty('CodeMirror'); }-*/
function_decl|;
DECL|method|initLibrary (final AsyncCallback<Void> cb)
specifier|static
name|void
name|initLibrary
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|isLibLoaded
argument_list|()
condition|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|CallbackGroup
name|group
init|=
operator|new
name|CallbackGroup
argument_list|()
decl_stmt|;
name|injectCss
argument_list|(
name|Lib
operator|.
name|I
operator|.
name|css
argument_list|()
argument_list|,
name|group
operator|.
expr|<
name|Void
operator|>
name|addEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|injectScript
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
argument_list|,
name|group
operator|.
name|add
argument_list|(
operator|new
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|result
parameter_list|)
block|{
name|Vim
operator|.
name|initKeyMap
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{}
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|group
operator|.
name|addListener
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|group
operator|.
name|done
argument_list|()
expr_stmt|;
block|}
DECL|method|injectCss (ExternalTextResource css, final AsyncCallback<Void> cb)
specifier|private
specifier|static
name|void
name|injectCss
parameter_list|(
name|ExternalTextResource
name|css
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|cb
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
name|Lib
operator|.
name|I
operator|.
name|style
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
name|cb
operator|.
name|onSuccess
argument_list|(
literal|null
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
name|cb
operator|.
name|onFailure
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
name|cb
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|injectScript (SafeUri js, final AsyncCallback<Void> callback)
specifier|public
specifier|static
name|void
name|injectScript
parameter_list|(
name|SafeUri
name|js
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|callback
parameter_list|)
block|{
specifier|final
name|ScriptElement
index|[]
name|script
init|=
operator|new
name|ScriptElement
index|[
literal|1
index|]
decl_stmt|;
name|script
index|[
literal|0
index|]
operator|=
name|ScriptInjector
operator|.
name|fromUrl
argument_list|(
name|js
operator|.
name|asString
argument_list|()
argument_list|)
operator|.
name|setWindow
argument_list|(
name|ScriptInjector
operator|.
name|TOP_WINDOW
argument_list|)
operator|.
name|setCallback
argument_list|(
operator|new
name|Callback
argument_list|<
name|Void
argument_list|,
name|Exception
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|result
parameter_list|)
block|{
name|script
index|[
literal|0
index|]
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|callback
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exception
name|reason
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|reason
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|inject
argument_list|()
operator|.
name|cast
argument_list|()
expr_stmt|;
block|}
DECL|method|Loader ()
specifier|private
name|Loader
parameter_list|()
block|{}
block|}
end_class

end_unit

