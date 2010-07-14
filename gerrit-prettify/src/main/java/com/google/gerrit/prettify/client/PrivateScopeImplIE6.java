begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.prettify.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_comment
comment|/** IE6 requires us to initialize the document before we can use it. */
end_comment

begin_class
DECL|class|PrivateScopeImplIE6
specifier|public
class|class
name|PrivateScopeImplIE6
extends|extends
name|PrivateScopeImpl
block|{
DECL|field|context
specifier|private
name|JavaScriptObject
name|context
decl_stmt|;
annotation|@
name|Override
DECL|method|onAttach ()
specifier|protected
name|void
name|onAttach
parameter_list|()
block|{
name|super
operator|.
name|onAttach
argument_list|()
expr_stmt|;
name|context
operator|=
name|nativeInitContext
argument_list|(
name|scopeName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getContext ()
specifier|public
name|JavaScriptObject
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|nativeInitContext (String scopeName)
specifier|private
specifier|static
specifier|native
name|JavaScriptObject
name|nativeInitContext
parameter_list|(
name|String
name|scopeName
parameter_list|)
comment|/*-{     var fe = $wnd[scopeName];     fe.document.write(         '<script>'       + 'parent._PrivateScopeNewChild = this;'       + '</' + 'script>'     );     var ctx = $wnd._PrivateScopeNewChild;     $wnd._PrivateScopeNewChild = undefined;     return ctx;   }-*/
function_decl|;
block|}
end_class

end_unit

