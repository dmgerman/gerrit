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
DECL|package|com.google.gerrit.client.extensions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|extensions
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

begin_class
DECL|class|TopMenuItem
specifier|public
class|class
name|TopMenuItem
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (String name, String url)
specifier|public
specifier|static
name|TopMenuItem
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|TopMenuItem
name|i
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|i
operator|.
name|name
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|i
operator|.
name|url
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|i
return|;
block|}
DECL|method|getName ()
specifier|public
specifier|final
specifier|native
name|String
name|getName
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|getUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|getUrl
parameter_list|()
comment|/*-{ return this.url; }-*/
function_decl|;
DECL|method|getTarget ()
specifier|public
specifier|final
specifier|native
name|String
name|getTarget
parameter_list|()
comment|/*-{ return this.target; }-*/
function_decl|;
DECL|method|getId ()
specifier|public
specifier|final
specifier|native
name|String
name|getId
parameter_list|()
comment|/*-{ return this.id; }-*/
function_decl|;
DECL|method|name (String n)
specifier|public
specifier|final
specifier|native
name|void
name|name
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.name = n }-*/
function_decl|;
DECL|method|url (String u)
specifier|public
specifier|final
specifier|native
name|void
name|url
parameter_list|(
name|String
name|u
parameter_list|)
comment|/*-{ this.url = u }-*/
function_decl|;
DECL|method|TopMenuItem ()
specifier|protected
name|TopMenuItem
parameter_list|()
block|{   }
block|}
end_class

end_unit

