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
name|ErrorDialog
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
name|ActionInfo
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
name|extensions
operator|.
name|client
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
name|extensions
operator|.
name|client
operator|.
name|ProjectState
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
DECL|method|requireChangeId ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|requireChangeId
parameter_list|()
comment|/*-{ return this.require_change_id; }-*/
function_decl|;
DECL|method|useContentMerge ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|useContentMerge
parameter_list|()
comment|/*-{ return this.use_content_merge; }-*/
function_decl|;
DECL|method|useContributorAgreements ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|useContributorAgreements
parameter_list|()
comment|/*-{ return this.use_contributor_agreements; }-*/
function_decl|;
DECL|method|createNewChangeForAllNotInTarget ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|createNewChangeForAllNotInTarget
parameter_list|()
comment|/*-{ return this.create_new_change_for_all_not_in_target; }-*/
function_decl|;
DECL|method|useSignedOffBy ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|useSignedOffBy
parameter_list|()
comment|/*-{ return this.use_signed_off_by; }-*/
function_decl|;
DECL|method|enableSignedPush ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|enableSignedPush
parameter_list|()
comment|/*-{ return this.enable_signed_push; }-*/
function_decl|;
DECL|method|requireSignedPush ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|requireSignedPush
parameter_list|()
comment|/*-{ return this.require_signed_push; }-*/
function_decl|;
DECL|method|rejectImplicitMerges ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|rejectImplicitMerges
parameter_list|()
comment|/*-{ return this.reject_implicit_merges; }-*/
function_decl|;
DECL|method|privateByDefault ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|privateByDefault
parameter_list|()
comment|/*-{ return this.private_by_default; }-*/
function_decl|;
DECL|method|enableReviewerByEmail ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|enableReviewerByEmail
parameter_list|()
comment|/*-{ return this.enable_reviewer_by_email; }-*/
function_decl|;
DECL|method|matchAuthorToCommitterDate ()
specifier|public
specifier|final
specifier|native
name|InheritedBooleanInfo
name|matchAuthorToCommitterDate
parameter_list|()
comment|/*-{ return this.match_author_to_committer_date; }-*/
function_decl|;
DECL|method|submitType ()
specifier|public
specifier|final
name|SubmitType
name|submitType
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|submitTypeRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|defaultSubmitType ()
specifier|public
specifier|final
specifier|native
name|SubmitTypeInfo
name|defaultSubmitType
parameter_list|()
comment|/*-{ return this.default_submit_type; }-*/
function_decl|;
DECL|method|pluginConfig ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|NativeMap
argument_list|<
name|ConfigParameterInfo
argument_list|>
argument_list|>
name|pluginConfig
parameter_list|()
comment|/*-{ return this.plugin_config || {}; }-*/
function_decl|;
DECL|method|pluginConfig (String p)
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|ConfigParameterInfo
argument_list|>
name|pluginConfig
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{ return this.plugin_config[p]; }-*/
function_decl|;
DECL|method|actions ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|()
comment|/*-{ return this.actions; }-*/
function_decl|;
DECL|method|submitTypeRaw ()
specifier|private
specifier|native
name|String
name|submitTypeRaw
parameter_list|()
comment|/*-{ return this.submit_type }-*/
function_decl|;
DECL|method|state ()
specifier|public
specifier|final
name|ProjectState
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
name|ProjectState
operator|.
name|ACTIVE
return|;
block|}
return|return
name|ProjectState
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
specifier|native
name|String
name|stateRaw
parameter_list|()
comment|/*-{ return this.state }-*/
function_decl|;
DECL|method|maxObjectSizeLimit ()
specifier|public
specifier|final
specifier|native
name|MaxObjectSizeLimitInfo
name|maxObjectSizeLimit
parameter_list|()
comment|/*-{ return this.max_object_size_limit; }-*/
function_decl|;
DECL|method|commentlinks0 ()
specifier|private
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
argument_list|<>
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
try|try
block|{
name|FindReplace
name|fr
init|=
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
decl_stmt|;
name|commentLinks
operator|.
name|add
argument_list|(
name|fr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|int
name|index
init|=
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"at Object"
argument_list|)
decl_stmt|;
operator|new
name|ErrorDialog
argument_list|(
literal|"Invalid commentlink configuration: "
operator|+
operator|(
name|index
operator|==
operator|-
literal|1
condition|?
name|e
operator|.
name|getMessage
argument_list|()
else|:
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|)
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
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
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|JsArrayString
argument_list|>
DECL|method|extensionPanelNames ()
name|extensionPanelNames
parameter_list|()
comment|/*-{ return this.extension_panel_names; }-*/
function_decl|;
DECL|method|ConfigInfo ()
specifier|protected
name|ConfigInfo
parameter_list|()
block|{}
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
block|{}
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
DECL|method|inheritedValue ()
specifier|public
specifier|final
specifier|native
name|boolean
name|inheritedValue
parameter_list|()
comment|/*-{ return this.inherited_value ? true : false; }-*/
function_decl|;
DECL|method|configuredValue ()
specifier|public
specifier|final
name|InheritableBoolean
name|configuredValue
parameter_list|()
block|{
return|return
name|InheritableBoolean
operator|.
name|valueOf
argument_list|(
name|configuredValueRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|configuredValueRaw ()
specifier|private
specifier|native
name|String
name|configuredValueRaw
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
block|{}
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
DECL|method|inheritedValue ()
specifier|public
specifier|final
specifier|native
name|String
name|inheritedValue
parameter_list|()
comment|/*-{ return this.inherited_value; }-*/
function_decl|;
DECL|method|configuredValue ()
specifier|public
specifier|final
specifier|native
name|String
name|configuredValue
parameter_list|()
comment|/*-{ return this.configured_value }-*/
function_decl|;
DECL|method|MaxObjectSizeLimitInfo ()
specifier|protected
name|MaxObjectSizeLimitInfo
parameter_list|()
block|{}
block|}
DECL|class|ConfigParameterInfo
specifier|public
specifier|static
class|class
name|ConfigParameterInfo
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
DECL|method|displayName ()
specifier|public
specifier|final
specifier|native
name|String
name|displayName
parameter_list|()
comment|/*-{ return this.display_name; }-*/
function_decl|;
DECL|method|description ()
specifier|public
specifier|final
specifier|native
name|String
name|description
parameter_list|()
comment|/*-{ return this.description; }-*/
function_decl|;
DECL|method|warning ()
specifier|public
specifier|final
specifier|native
name|String
name|warning
parameter_list|()
comment|/*-{ return this.warning; }-*/
function_decl|;
DECL|method|type ()
specifier|public
specifier|final
specifier|native
name|String
name|type
parameter_list|()
comment|/*-{ return this.type; }-*/
function_decl|;
DECL|method|value ()
specifier|public
specifier|final
specifier|native
name|String
name|value
parameter_list|()
comment|/*-{ return this.value; }-*/
function_decl|;
DECL|method|editable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|editable
parameter_list|()
comment|/*-{ return this.editable ? true : false; }-*/
function_decl|;
DECL|method|inheritable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|inheritable
parameter_list|()
comment|/*-{ return this.inheritable ? true : false; }-*/
function_decl|;
DECL|method|configuredValue ()
specifier|public
specifier|final
specifier|native
name|String
name|configuredValue
parameter_list|()
comment|/*-{ return this.configured_value; }-*/
function_decl|;
DECL|method|inheritedValue ()
specifier|public
specifier|final
specifier|native
name|String
name|inheritedValue
parameter_list|()
comment|/*-{ return this.inherited_value; }-*/
function_decl|;
DECL|method|permittedValues ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|permittedValues
parameter_list|()
comment|/*-{ return this.permitted_values; }-*/
function_decl|;
DECL|method|values ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|values
parameter_list|()
comment|/*-{ return this.values; }-*/
function_decl|;
DECL|method|ConfigParameterInfo ()
specifier|protected
name|ConfigParameterInfo
parameter_list|()
block|{}
block|}
DECL|class|ConfigParameterValue
specifier|public
specifier|static
class|class
name|ConfigParameterValue
extends|extends
name|JavaScriptObject
block|{
DECL|method|init ()
specifier|final
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{ this.values = []; }-*/
function_decl|;
DECL|method|addValue (String v)
specifier|final
specifier|native
name|void
name|addValue
parameter_list|(
name|String
name|v
parameter_list|)
comment|/*-{ this.values.push(v); }-*/
function_decl|;
DECL|method|setValue (String v)
specifier|final
specifier|native
name|void
name|setValue
parameter_list|(
name|String
name|v
parameter_list|)
comment|/*-{ if(v)this.value = v; }-*/
function_decl|;
DECL|method|create ()
specifier|public
specifier|static
name|ConfigParameterValue
name|create
parameter_list|()
block|{
name|ConfigParameterValue
name|v
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
return|return
name|v
return|;
block|}
DECL|method|values (String[] values)
specifier|public
specifier|final
name|ConfigParameterValue
name|values
parameter_list|(
name|String
index|[]
name|values
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|v
range|:
name|values
control|)
block|{
name|addValue
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|value (String v)
specifier|public
specifier|final
name|ConfigParameterValue
name|value
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|setValue
argument_list|(
name|v
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|ConfigParameterValue ()
specifier|protected
name|ConfigParameterValue
parameter_list|()
block|{}
block|}
DECL|class|SubmitTypeInfo
specifier|public
specifier|static
class|class
name|SubmitTypeInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|value ()
specifier|public
specifier|final
name|SubmitType
name|value
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|valueRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|configuredValue ()
specifier|public
specifier|final
name|SubmitType
name|configuredValue
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|configuredValueRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|inheritedValue ()
specifier|public
specifier|final
name|SubmitType
name|inheritedValue
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|inheritedValueRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|valueRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|valueRaw
parameter_list|()
comment|/*-{ return this.value; }-*/
function_decl|;
DECL|method|configuredValueRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|configuredValueRaw
parameter_list|()
comment|/*-{ return this.configured_value; }-*/
function_decl|;
DECL|method|inheritedValueRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|inheritedValueRaw
parameter_list|()
comment|/*-{ return this.inherited_value; }-*/
function_decl|;
DECL|method|SubmitTypeInfo ()
specifier|protected
name|SubmitTypeInfo
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

