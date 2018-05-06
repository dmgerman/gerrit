begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.info
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|info
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
name|client
operator|.
name|rpc
operator|.
name|NativeString
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
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|DownloadInfo
specifier|public
class|class
name|DownloadInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|schemes ()
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|schemes
parameter_list|()
block|{
return|return
name|_schemes
argument_list|()
operator|.
name|sortedKeys
argument_list|()
return|;
block|}
DECL|method|archives ()
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|archives
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|archives
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|archives
operator|.
name|addAll
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|_archives
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|archives
return|;
block|}
DECL|method|scheme (String n)
specifier|public
specifier|final
specifier|native
name|DownloadSchemeInfo
name|scheme
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.schemes[n]; }-*/
function_decl|;
DECL|method|_schemes ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|DownloadSchemeInfo
argument_list|>
name|_schemes
parameter_list|()
comment|/*-{ return this.schemes; }-*/
function_decl|;
DECL|method|_archives ()
specifier|private
specifier|native
name|JsArrayString
name|_archives
parameter_list|()
comment|/*-{ return this.archives; }-*/
function_decl|;
DECL|method|DownloadInfo ()
specifier|protected
name|DownloadInfo
parameter_list|()
block|{}
DECL|class|DownloadSchemeInfo
specifier|public
specifier|static
class|class
name|DownloadSchemeInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|commandNames ()
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|commandNames
parameter_list|()
block|{
return|return
name|_commands
argument_list|()
operator|.
name|sortedKeys
argument_list|()
return|;
block|}
DECL|method|commands (String project)
specifier|public
specifier|final
name|Set
argument_list|<
name|DownloadCommandInfo
argument_list|>
name|commands
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|Set
argument_list|<
name|DownloadCommandInfo
argument_list|>
name|commands
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|commandName
range|:
name|commandNames
argument_list|()
control|)
block|{
name|commands
operator|.
name|add
argument_list|(
operator|new
name|DownloadCommandInfo
argument_list|(
name|commandName
argument_list|,
name|command
argument_list|(
name|commandName
argument_list|,
name|project
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|commands
return|;
block|}
DECL|method|command (String commandName, String project)
specifier|public
specifier|final
name|String
name|command
parameter_list|(
name|String
name|commandName
parameter_list|,
name|String
name|project
parameter_list|)
block|{
return|return
name|command
argument_list|(
name|commandName
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{project\\}"
argument_list|,
name|project
argument_list|)
return|;
block|}
DECL|method|projectBaseName (String project)
specifier|private
specifier|static
name|String
name|projectBaseName
parameter_list|(
name|String
name|project
parameter_list|)
block|{
return|return
name|project
operator|.
name|substring
argument_list|(
name|project
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
DECL|method|cloneCommandNames ()
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|cloneCommandNames
parameter_list|()
block|{
return|return
name|_cloneCommands
argument_list|()
operator|.
name|sortedKeys
argument_list|()
return|;
block|}
DECL|method|cloneCommands (String project)
specifier|public
specifier|final
name|List
argument_list|<
name|DownloadCommandInfo
argument_list|>
name|cloneCommands
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|commandNames
init|=
name|cloneCommandNames
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DownloadCommandInfo
argument_list|>
name|commands
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|commandNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|commandName
range|:
name|commandNames
control|)
block|{
name|commands
operator|.
name|add
argument_list|(
operator|new
name|DownloadCommandInfo
argument_list|(
name|commandName
argument_list|,
name|cloneCommand
argument_list|(
name|commandName
argument_list|,
name|project
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|commands
return|;
block|}
DECL|method|cloneCommand (String commandName, String project)
specifier|public
specifier|final
name|String
name|cloneCommand
parameter_list|(
name|String
name|commandName
parameter_list|,
name|String
name|project
parameter_list|)
block|{
return|return
name|cloneCommand
argument_list|(
name|commandName
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{project\\}"
argument_list|,
name|project
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{project-base-name\\}"
argument_list|,
name|projectBaseName
argument_list|(
name|project
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getUrl (String project)
specifier|public
specifier|final
name|String
name|getUrl
parameter_list|(
name|String
name|project
parameter_list|)
block|{
return|return
name|url
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{project\\}"
argument_list|,
name|project
argument_list|)
return|;
block|}
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|url ()
specifier|public
specifier|final
specifier|native
name|String
name|url
parameter_list|()
comment|/*-{ return this.url; }-*/
function_decl|;
DECL|method|isAuthRequired ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isAuthRequired
parameter_list|()
comment|/*-{ return this.is_auth_required || false; }-*/
function_decl|;
DECL|method|isAuthSupported ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isAuthSupported
parameter_list|()
comment|/*-{ return this.is_auth_supported || false; }-*/
function_decl|;
DECL|method|command (String n)
specifier|public
specifier|final
specifier|native
name|String
name|command
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.commands[n]; }-*/
function_decl|;
DECL|method|cloneCommand (String n)
specifier|public
specifier|final
specifier|native
name|String
name|cloneCommand
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.clone_commands[n]; }-*/
function_decl|;
DECL|method|_commands ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|NativeString
argument_list|>
name|_commands
parameter_list|()
comment|/*-{ return this.commands; }-*/
function_decl|;
DECL|method|_cloneCommands ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|NativeString
argument_list|>
name|_cloneCommands
parameter_list|()
comment|/*-{ return this.clone_commands; }-*/
function_decl|;
DECL|method|DownloadSchemeInfo ()
specifier|protected
name|DownloadSchemeInfo
parameter_list|()
block|{}
block|}
DECL|class|DownloadCommandInfo
specifier|public
specifier|static
class|class
name|DownloadCommandInfo
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|command
specifier|private
specifier|final
name|String
name|command
decl_stmt|;
DECL|method|DownloadCommandInfo (String name, String command)
name|DownloadCommandInfo
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|command ()
specifier|public
name|String
name|command
parameter_list|()
block|{
return|return
name|command
return|;
block|}
block|}
block|}
end_class

end_unit

