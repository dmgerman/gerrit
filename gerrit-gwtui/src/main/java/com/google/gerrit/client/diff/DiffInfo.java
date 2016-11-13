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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
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
name|DiffWebLinkInfo
import|;
end_import

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
name|info
operator|.
name|WebLinkInfo
import|;
end_import

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
name|Natives
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DiffView
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Patch
operator|.
name|ChangeType
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
name|JsArrayString
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|DiffInfo
specifier|public
class|class
name|DiffInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|metaA ()
specifier|public
specifier|final
specifier|native
name|FileMeta
name|metaA
parameter_list|()
comment|/*-{ return this.meta_a; }-*/
function_decl|;
DECL|method|metaB ()
specifier|public
specifier|final
specifier|native
name|FileMeta
name|metaB
parameter_list|()
comment|/*-{ return this.meta_b; }-*/
function_decl|;
DECL|method|diffHeader ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|diffHeader
parameter_list|()
comment|/*-{ return this.diff_header; }-*/
function_decl|;
DECL|method|content ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|Region
argument_list|>
name|content
parameter_list|()
comment|/*-{ return this.content; }-*/
function_decl|;
DECL|method|webLinks ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|DiffWebLinkInfo
argument_list|>
name|webLinks
parameter_list|()
comment|/*-{ return this.web_links; }-*/
function_decl|;
DECL|method|binary ()
specifier|public
specifier|final
specifier|native
name|boolean
name|binary
parameter_list|()
comment|/*-{ return this.binary || false; }-*/
function_decl|;
DECL|method|sideBySideWebLinks ()
specifier|public
specifier|final
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|sideBySideWebLinks
parameter_list|()
block|{
return|return
name|filterWebLinks
argument_list|(
name|DiffView
operator|.
name|SIDE_BY_SIDE
argument_list|)
return|;
block|}
DECL|method|unifiedWebLinks ()
specifier|public
specifier|final
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|unifiedWebLinks
parameter_list|()
block|{
return|return
name|filterWebLinks
argument_list|(
name|DiffView
operator|.
name|UNIFIED_DIFF
argument_list|)
return|;
block|}
DECL|method|filterWebLinks (DiffView diffView)
specifier|private
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|filterWebLinks
parameter_list|(
name|DiffView
name|diffView
parameter_list|)
block|{
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|filteredDiffWebLinks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DiffWebLinkInfo
argument_list|>
name|allDiffWebLinks
init|=
name|Natives
operator|.
name|asList
argument_list|(
name|webLinks
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|allDiffWebLinks
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DiffWebLinkInfo
name|webLink
range|:
name|allDiffWebLinks
control|)
block|{
if|if
condition|(
name|diffView
operator|==
name|DiffView
operator|.
name|SIDE_BY_SIDE
operator|&&
name|webLink
operator|.
name|showOnSideBySideDiffView
argument_list|()
condition|)
block|{
name|filteredDiffWebLinks
operator|.
name|add
argument_list|(
name|webLink
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|diffView
operator|==
name|DiffView
operator|.
name|UNIFIED_DIFF
operator|&&
name|webLink
operator|.
name|showOnUnifiedDiffView
argument_list|()
condition|)
block|{
name|filteredDiffWebLinks
operator|.
name|add
argument_list|(
name|webLink
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|filteredDiffWebLinks
return|;
block|}
DECL|method|changeType ()
specifier|public
specifier|final
name|ChangeType
name|changeType
parameter_list|()
block|{
return|return
name|ChangeType
operator|.
name|valueOf
argument_list|(
name|changeTypeRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|changeTypeRaw ()
specifier|private
specifier|native
name|String
name|changeTypeRaw
parameter_list|()
comment|/*-{ return this.change_type }-*/
function_decl|;
DECL|method|intralineStatus ()
specifier|public
specifier|final
name|IntraLineStatus
name|intralineStatus
parameter_list|()
block|{
name|String
name|s
init|=
name|intralineStatusRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|IntraLineStatus
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
name|IntraLineStatus
operator|.
name|OFF
return|;
block|}
DECL|method|intralineStatusRaw ()
specifier|private
specifier|native
name|String
name|intralineStatusRaw
parameter_list|()
comment|/*-{ return this.intraline_status }-*/
function_decl|;
DECL|method|hasSkip ()
specifier|public
specifier|final
name|boolean
name|hasSkip
parameter_list|()
block|{
name|JsArray
argument_list|<
name|Region
argument_list|>
name|c
init|=
name|content
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|c
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|c
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|skip
argument_list|()
operator|!=
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|textA ()
specifier|public
specifier|final
name|String
name|textA
parameter_list|()
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|JsArray
argument_list|<
name|Region
argument_list|>
name|c
init|=
name|content
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|c
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Region
name|r
init|=
name|c
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|ab
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|ab
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|r
operator|.
name|a
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|a
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO skip may need to be handled
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|textB ()
specifier|public
specifier|final
name|String
name|textB
parameter_list|()
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|JsArray
argument_list|<
name|Region
argument_list|>
name|c
init|=
name|content
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|c
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Region
name|r
init|=
name|c
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|ab
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|ab
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|r
operator|.
name|b
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|b
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO skip may need to be handled
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|textUnified ()
specifier|public
specifier|final
name|String
name|textUnified
parameter_list|()
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|JsArray
argument_list|<
name|Region
argument_list|>
name|c
init|=
name|content
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|c
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Region
name|r
init|=
name|c
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|ab
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|ab
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|r
operator|.
name|a
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|a
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|b
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|s
argument_list|,
name|r
operator|.
name|b
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO skip may need to be handled
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|append (StringBuilder s, JsArrayString lines)
specifier|private
specifier|static
name|void
name|append
parameter_list|(
name|StringBuilder
name|s
parameter_list|,
name|JsArrayString
name|lines
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lines
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|s
operator|.
name|append
argument_list|(
name|lines
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|DiffInfo ()
specifier|protected
name|DiffInfo
parameter_list|()
block|{}
DECL|enum|IntraLineStatus
specifier|public
enum|enum
name|IntraLineStatus
block|{
DECL|enumConstant|OFF
name|OFF
block|,
DECL|enumConstant|OK
name|OK
block|,
DECL|enumConstant|TIMEOUT
name|TIMEOUT
block|,
DECL|enumConstant|FAILURE
name|FAILURE
block|}
DECL|class|FileMeta
specifier|public
specifier|static
class|class
name|FileMeta
extends|extends
name|JavaScriptObject
block|{
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|contentType ()
specifier|public
specifier|final
specifier|native
name|String
name|contentType
parameter_list|()
comment|/*-{ return this.content_type; }-*/
function_decl|;
DECL|method|lines ()
specifier|public
specifier|final
specifier|native
name|int
name|lines
parameter_list|()
comment|/*-{ return this.lines || 0 }-*/
function_decl|;
DECL|method|webLinks ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
parameter_list|()
comment|/*-{ return this.web_links; }-*/
function_decl|;
DECL|method|FileMeta ()
specifier|protected
name|FileMeta
parameter_list|()
block|{}
block|}
DECL|class|Region
specifier|public
specifier|static
class|class
name|Region
extends|extends
name|JavaScriptObject
block|{
DECL|method|ab ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|ab
parameter_list|()
comment|/*-{ return this.ab; }-*/
function_decl|;
DECL|method|a ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|a
parameter_list|()
comment|/*-{ return this.a; }-*/
function_decl|;
DECL|method|b ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|b
parameter_list|()
comment|/*-{ return this.b; }-*/
function_decl|;
DECL|method|skip ()
specifier|public
specifier|final
specifier|native
name|int
name|skip
parameter_list|()
comment|/*-{ return this.skip || 0; }-*/
function_decl|;
DECL|method|common ()
specifier|public
specifier|final
specifier|native
name|boolean
name|common
parameter_list|()
comment|/*-{ return this.common || false; }-*/
function_decl|;
DECL|method|editA ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|Span
argument_list|>
name|editA
parameter_list|()
comment|/*-{ return this.edit_a }-*/
function_decl|;
DECL|method|editB ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|Span
argument_list|>
name|editB
parameter_list|()
comment|/*-{ return this.edit_b }-*/
function_decl|;
DECL|method|Region ()
specifier|protected
name|Region
parameter_list|()
block|{}
block|}
DECL|class|Span
specifier|public
specifier|static
class|class
name|Span
extends|extends
name|JavaScriptObject
block|{
DECL|method|skip ()
specifier|public
specifier|final
specifier|native
name|int
name|skip
parameter_list|()
comment|/*-{ return this[0]; }-*/
function_decl|;
DECL|method|mark ()
specifier|public
specifier|final
specifier|native
name|int
name|mark
parameter_list|()
comment|/*-{ return this[1]; }-*/
function_decl|;
DECL|method|Span ()
specifier|protected
name|Span
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

