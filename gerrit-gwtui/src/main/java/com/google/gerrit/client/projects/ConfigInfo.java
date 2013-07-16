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
DECL|package|com.google.gerrit.client.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|projects
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|Project
operator|.
name|InheritableBoolean
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
name|Project
operator|.
name|SubmitType
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
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|FindReplace
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|LinkFindReplace
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|RawFindReplace
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
DECL|class|ConfigInfo
specifier|public
class|class
name|ConfigInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|description ()
specifier|public
specifier|final
specifier|native
name|String
name|description
parameter_list|()
comment|/*-{ return this.description }-*/
function_decl|;
DECL|method|require_change_id ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|require_change_id
parameter_list|()
comment|/*-{ return this.require_change_id; }-*/
function_decl|;
DECL|method|use_content_merge ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|use_content_merge
parameter_list|()
comment|/*-{ return this.use_content_merge; }-*/
function_decl|;
DECL|method|use_contributor_agreements ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|use_contributor_agreements
parameter_list|()
comment|/*-{ return this.use_contributor_agreements; }-*/
function_decl|;
DECL|method|use_signed_off_by ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|use_signed_off_by
parameter_list|()
comment|/*-{ return this.use_signed_off_by; }-*/
function_decl|;
DECL|method|submit_type ()
specifier|public
specifier|final
name|SubmitType
name|submit_type
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|submit_typeRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|submit_typeRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|submit_typeRaw
parameter_list|()
comment|/*-{ return this.submit_type }-*/
function_decl|;
DECL|method|state ()
specifier|public
specifier|final
name|Project
operator|.
name|State
name|state
parameter_list|()
block|{
if|if
condition|(
name|stateRaw
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Project
operator|.
name|State
operator|.
name|ACTIVE
return|;
block|}
return|return
name|Project
operator|.
name|State
operator|.
name|valueOf
argument_list|(
name|stateRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|stateRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|stateRaw
parameter_list|()
comment|/*-{ return this.state }-*/
function_decl|;
DECL|method|max_object_size_limit ()
specifier|public
specifier|final
specifier|native
name|MaxObjectSizeLimitInfo
name|max_object_size_limit
parameter_list|()
comment|/*-{ return this.max_object_size_limit; }-*/
function_decl|;
DECL|method|commentlinks0 ()
specifier|private
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|CommentLinkInfo
argument_list|>
name|commentlinks0
parameter_list|()
comment|/*-{ return this.commentlinks; }-*/
function_decl|;
DECL|method|commentlinks ()
specifier|final
name|List
argument_list|<
name|FindReplace
argument_list|>
name|commentlinks
parameter_list|()
block|{
name|JsArray
argument_list|<
name|CommentLinkInfo
argument_list|>
name|cls
init|=
name|commentlinks0
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|FindReplace
argument_list|>
name|commentLinks
init|=
operator|new
name|ArrayList
argument_list|<
name|FindReplace
argument_list|>
argument_list|(
name|cls
operator|.
name|length
argument_list|()
argument_list|)
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
name|cls
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|CommentLinkInfo
name|cl
init|=
name|cls
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cl
operator|.
name|enabled
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|cl
operator|.
name|link
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commentLinks
operator|.
name|add
argument_list|(
operator|new
name|LinkFindReplace
argument_list|(
name|cl
operator|.
name|match
argument_list|()
argument_list|,
name|cl
operator|.
name|link
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|commentLinks
operator|.
name|add
argument_list|(
operator|new
name|RawFindReplace
argument_list|(
name|cl
operator|.
name|match
argument_list|()
argument_list|,
name|cl
operator|.
name|html
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|commentLinks
return|;
block|}
DECL|method|theme ()
specifier|final
specifier|native
name|ThemeInfo
name|theme
parameter_list|()
comment|/*-{ return this.theme; }-*/
function_decl|;
DECL|method|ConfigInfo ()
specifier|protected
name|ConfigInfo
parameter_list|()
block|{   }
DECL|class|CommentLinkInfo
specifier|static
class|class
name|CommentLinkInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|match ()
specifier|final
specifier|native
name|String
name|match
parameter_list|()
comment|/*-{ return this.match; }-*/
function_decl|;
DECL|method|link ()
specifier|final
specifier|native
name|String
name|link
parameter_list|()
comment|/*-{ return this.link; }-*/
function_decl|;
DECL|method|html ()
specifier|final
specifier|native
name|String
name|html
parameter_list|()
comment|/*-{ return this.html; }-*/
function_decl|;
DECL|method|enabled ()
specifier|final
specifier|native
name|boolean
name|enabled
parameter_list|()
comment|/*-{       return !this.hasOwnProperty('enabled') || this.enabled;     }-*/
function_decl|;
DECL|method|CommentLinkInfo ()
specifier|protected
name|CommentLinkInfo
parameter_list|()
block|{     }
block|}
DECL|class|InheritedBooleanInfo
specifier|public
specifier|static
class|class
name|InheritedBooleanInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|InheritedBooleanInfo
name|create
parameter_list|()
block|{
return|return
operator|(
name|InheritedBooleanInfo
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|value ()
specifier|public
specifier|final
specifier|native
name|boolean
name|value
parameter_list|()
comment|/*-{ return this.value ? true : false; }-*/
function_decl|;
DECL|method|inherited_value ()
specifier|public
specifier|final
specifier|native
name|boolean
name|inherited_value
parameter_list|()
comment|/*-{ return this.inherited_value ? true : false; }-*/
function_decl|;
DECL|method|configured_value ()
specifier|public
specifier|final
name|InheritableBoolean
name|configured_value
parameter_list|()
block|{
return|return
name|InheritableBoolean
operator|.
name|valueOf
argument_list|(
name|configured_valueRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|configured_valueRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|configured_valueRaw
parameter_list|()
comment|/*-{ return this.configured_value }-*/
function_decl|;
DECL|method|setConfiguredValue (InheritableBoolean v)
specifier|public
specifier|final
name|void
name|setConfiguredValue
parameter_list|(
name|InheritableBoolean
name|v
parameter_list|)
block|{
name|setConfiguredValueRaw
argument_list|(
name|v
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setConfiguredValueRaw (String v)
specifier|public
specifier|final
specifier|native
name|void
name|setConfiguredValueRaw
parameter_list|(
name|String
name|v
parameter_list|)
comment|/*-{ if(v)this.configured_value=v; }-*/
function_decl|;
DECL|method|InheritedBooleanInfo ()
specifier|protected
name|InheritedBooleanInfo
parameter_list|()
block|{     }
block|}
DECL|class|MaxObjectSizeLimitInfo
specifier|public
specifier|static
class|class
name|MaxObjectSizeLimitInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|value ()
specifier|public
specifier|final
specifier|native
name|String
name|value
parameter_list|()
comment|/*-{ return this.value; }-*/
function_decl|;
DECL|method|inherited_value ()
specifier|public
specifier|final
specifier|native
name|String
name|inherited_value
parameter_list|()
comment|/*-{ return this.inherited_value; }-*/
function_decl|;
DECL|method|configured_value ()
specifier|public
specifier|final
specifier|native
name|String
name|configured_value
parameter_list|()
comment|/*-{ return this.configured_value }-*/
function_decl|;
DECL|method|MaxObjectSizeLimitInfo ()
specifier|protected
name|MaxObjectSizeLimitInfo
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

