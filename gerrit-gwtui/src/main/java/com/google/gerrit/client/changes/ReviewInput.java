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
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|NativeMap
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
name|JavaScriptObject
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
name|JsArray
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
DECL|enum|NotifyHandling
specifier|public
specifier|static
enum|enum
name|NotifyHandling
block|{
DECL|enumConstant|NONE
DECL|enumConstant|OWNER
DECL|enumConstant|OWNER_REVIEWERS
DECL|enumConstant|ALL
name|NONE
block|,
name|OWNER
block|,
name|OWNER_REVIEWERS
block|,
name|ALL
block|}
DECL|enum|DraftHandling
specifier|public
specifier|static
enum|enum
name|DraftHandling
block|{
DECL|enumConstant|DELETE
DECL|enumConstant|PUBLISH
DECL|enumConstant|KEEP
name|DELETE
block|,
name|PUBLISH
block|,
name|KEEP
block|}
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
name|r
operator|.
name|drafts
argument_list|(
name|DraftHandling
operator|.
name|PUBLISH
argument_list|)
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
DECL|method|label (String n)
specifier|public
specifier|final
specifier|native
name|short
name|label
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.labels[n]||0 }-*/
function_decl|;
DECL|method|comments (NativeMap<JsArray<CommentInfo>> m)
specifier|public
specifier|final
specifier|native
name|void
name|comments
parameter_list|(
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|m
parameter_list|)
comment|/*-{ this.comments=m }-*/
function_decl|;
DECL|method|notify (NotifyHandling e)
specifier|public
specifier|final
name|void
name|notify
parameter_list|(
name|NotifyHandling
name|e
parameter_list|)
block|{
name|_notify
argument_list|(
name|e
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|_notify (String n)
specifier|private
specifier|final
specifier|native
name|void
name|_notify
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.notify=n; }-*/
function_decl|;
DECL|method|drafts (DraftHandling e)
specifier|public
specifier|final
name|void
name|drafts
parameter_list|(
name|DraftHandling
name|e
parameter_list|)
block|{
name|_drafts
argument_list|(
name|e
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|_drafts (String n)
specifier|private
specifier|final
specifier|native
name|void
name|_drafts
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.drafts=n; }-*/
function_decl|;
DECL|method|init ()
specifier|private
specifier|final
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{     this.labels = {};     this.strict_labels = true;   }-*/
function_decl|;
DECL|method|prePost ()
specifier|public
specifier|final
specifier|native
name|void
name|prePost
parameter_list|()
comment|/*-{     var m=this.comments;     if (m) {       for (var p in m) {         var l=m[p];         for (var i=0;i<l.length;i++) {           var c=l[i];           delete c['kind'];           delete c['path'];           delete c['updated'];         }       }     }   }-*/
function_decl|;
DECL|method|ReviewInput ()
specifier|protected
name|ReviewInput
parameter_list|()
block|{   }
block|}
end_class

end_unit

