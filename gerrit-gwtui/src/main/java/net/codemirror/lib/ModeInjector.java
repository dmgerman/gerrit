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
name|gwt
operator|.
name|resources
operator|.
name|client
operator|.
name|DataResource
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

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|mode
operator|.
name|Modes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
DECL|class|ModeInjector
specifier|public
class|class
name|ModeInjector
block|{
comment|/** Map of server content type to CodeMiror mode or content type. */
DECL|field|mimeAlias
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mimeAlias
decl_stmt|;
comment|/** Map of content type "text/x-java" to mode name "clike". */
DECL|field|mimeModes
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mimeModes
decl_stmt|;
comment|/** Map of names such as "clike" to URI for code download. */
DECL|field|modeUris
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SafeUri
argument_list|>
name|modeUris
decl_stmt|;
static|static
block|{
name|DataResource
index|[]
name|all
init|=
block|{
name|Modes
operator|.
name|I
operator|.
name|clike
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|css
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|go
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|htmlmixed
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|javascript
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|perl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|properties
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|python
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ruby
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|shell
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|sql
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|velocity
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|xml
argument_list|()
block|,     }
decl_stmt|;
name|mimeAlias
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|mimeModes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|modeUris
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SafeUri
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|DataResource
name|m
range|:
name|all
control|)
block|{
name|modeUris
operator|.
name|put
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getSafeUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|parseModeMap
argument_list|()
expr_stmt|;
block|}
DECL|method|parseModeMap ()
specifier|private
specifier|static
name|void
name|parseModeMap
parameter_list|()
block|{
name|String
name|mode
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|Modes
operator|.
name|I
operator|.
name|mode_map
argument_list|()
operator|.
name|getText
argument_list|()
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
name|int
name|eq
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|eq
condition|)
block|{
name|mimeAlias
operator|.
name|put
argument_list|(
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|eq
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|,
name|line
operator|.
name|substring
argument_list|(
name|eq
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|endsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|String
name|n
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|line
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|modeUris
operator|.
name|containsKey
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|mode
operator|=
name|n
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|mode
operator|!=
literal|null
operator|&&
name|line
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|mimeModes
operator|.
name|put
argument_list|(
name|line
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mode
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|getContentType (String mode)
specifier|public
specifier|static
name|String
name|getContentType
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|String
name|real
init|=
name|mode
operator|!=
literal|null
condition|?
name|mimeAlias
operator|.
name|get
argument_list|(
name|mode
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
name|real
operator|!=
literal|null
condition|?
name|real
else|:
name|mode
return|;
block|}
DECL|method|isModeLoaded (String n)
specifier|private
specifier|static
specifier|native
name|boolean
name|isModeLoaded
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return $wnd.CodeMirror.modes.hasOwnProperty(n); }-*/
function_decl|;
DECL|method|isMimeLoaded (String n)
specifier|private
specifier|static
specifier|native
name|boolean
name|isMimeLoaded
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return $wnd.CodeMirror.mimeModes.hasOwnProperty(n); }-*/
function_decl|;
DECL|method|getDependencies (String n)
specifier|private
specifier|static
specifier|native
name|JsArrayString
name|getDependencies
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return $wnd.CodeMirror.modes[n].dependencies || []; }-*/
function_decl|;
DECL|field|loading
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|loading
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
DECL|field|pending
specifier|private
name|int
name|pending
decl_stmt|;
DECL|field|appCallback
specifier|private
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|appCallback
decl_stmt|;
DECL|method|add (String name)
specifier|public
name|ModeInjector
name|add
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|isModeLoaded
argument_list|(
name|name
argument_list|)
operator|||
name|isMimeLoaded
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|this
return|;
block|}
name|String
name|mode
init|=
name|mimeModes
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|==
literal|null
condition|)
block|{
name|mode
operator|=
name|name
expr_stmt|;
block|}
name|SafeUri
name|uri
init|=
name|modeUris
operator|.
name|get
argument_list|(
name|mode
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"net.codemirror"
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"CodeMirror mode "
operator|+
name|mode
operator|+
literal|" not configured."
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|loading
operator|.
name|add
argument_list|(
name|mode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|inject (AsyncCallback<Void> appCallback)
specifier|public
name|void
name|inject
parameter_list|(
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|appCallback
parameter_list|)
block|{
name|this
operator|.
name|appCallback
operator|=
name|appCallback
expr_stmt|;
for|for
control|(
name|String
name|mode
range|:
name|loading
control|)
block|{
name|beginLoading
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pending
operator|==
literal|0
condition|)
block|{
name|appCallback
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|beginLoading (final String mode)
specifier|private
name|void
name|beginLoading
parameter_list|(
specifier|final
name|String
name|mode
parameter_list|)
block|{
name|pending
operator|++
expr_stmt|;
name|Loader
operator|.
name|injectScript
argument_list|(
name|modeUris
operator|.
name|get
argument_list|(
name|mode
argument_list|)
argument_list|,
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
name|pending
operator|--
expr_stmt|;
name|ensureDependenciesAreLoaded
argument_list|(
name|mode
argument_list|)
expr_stmt|;
if|if
condition|(
name|pending
operator|==
literal|0
condition|)
block|{
name|appCallback
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
block|{
if|if
condition|(
operator|--
name|pending
operator|==
literal|0
condition|)
block|{
name|appCallback
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|ensureDependenciesAreLoaded (String mode)
specifier|private
name|void
name|ensureDependenciesAreLoaded
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|JsArrayString
name|deps
init|=
name|getDependencies
argument_list|(
name|mode
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
name|deps
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|d
init|=
name|deps
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|loading
operator|.
name|contains
argument_list|(
name|d
argument_list|)
operator|||
name|isModeLoaded
argument_list|(
name|d
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|SafeUri
name|uri
init|=
name|modeUris
operator|.
name|get
argument_list|(
name|d
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"net.codemirror"
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"CodeMirror mode "
operator|+
name|mode
operator|+
literal|" needs "
operator|+
name|d
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|loading
operator|.
name|add
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|beginLoading
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

