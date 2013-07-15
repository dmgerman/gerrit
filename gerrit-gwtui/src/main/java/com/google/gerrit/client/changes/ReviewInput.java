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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
DECL|class|ReviewInput
specifier|public
class|class
name|ReviewInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|ReviewInput
name|create
parameter_list|()
block|{
name|ReviewInput
name|r
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|r
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|message (String m)
specifier|public
specifier|final
specifier|native
name|void
name|message
parameter_list|(
name|String
name|m
parameter_list|)
comment|/*-{ if(m)this.message=m; }-*/
function_decl|;
DECL|method|label (String n, short v)
specifier|public
specifier|final
specifier|native
name|void
name|label
parameter_list|(
name|String
name|n
parameter_list|,
name|short
name|v
parameter_list|)
comment|/*-{ this.labels[n]=v; }-*/
function_decl|;
DECL|method|init ()
specifier|private
specifier|final
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{     this.labels = {};     this.strict_labels = true;     this.drafts = 'PUBLISH';   }-*/
function_decl|;
DECL|method|ReviewInput ()
specifier|protected
name|ReviewInput
parameter_list|()
block|{   }
block|}
end_class

end_unit

