begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|server
operator|.
name|OutputFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|Map
import|;
end_import

begin_comment
comment|/** List the installed plugins. */
end_comment

begin_class
DECL|class|ListPlugins
specifier|public
class|class
name|ListPlugins
block|{
DECL|field|pluginLoader
specifier|private
specifier|final
name|PluginLoader
name|pluginLoader
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--format"
argument_list|,
name|metaVar
operator|=
literal|"FMT"
argument_list|,
name|usage
operator|=
literal|"Output display format"
argument_list|)
DECL|field|format
specifier|private
name|OutputFormat
name|format
init|=
name|OutputFormat
operator|.
name|TEXT
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--all"
argument_list|,
name|aliases
operator|=
block|{
literal|"-a"
block|}
argument_list|,
name|usage
operator|=
literal|"List all plugins, including disabled plugins"
argument_list|)
DECL|field|all
specifier|private
name|boolean
name|all
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListPlugins (PluginLoader pluginLoader)
specifier|protected
name|ListPlugins
parameter_list|(
name|PluginLoader
name|pluginLoader
parameter_list|)
block|{
name|this
operator|.
name|pluginLoader
operator|=
name|pluginLoader
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|OutputFormat
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setFormat (OutputFormat fmt)
specifier|public
name|ListPlugins
name|setFormat
parameter_list|(
name|OutputFormat
name|fmt
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|fmt
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|display (OutputStream out)
specifier|public
name|void
name|display
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
specifier|final
name|PrintWriter
name|stdout
decl_stmt|;
try|try
block|{
name|stdout
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// Our encoding is required by the specifications for the runtime.
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"JVM lacks UTF-8 encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
name|output
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Plugin
argument_list|>
name|plugins
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|pluginLoader
operator|.
name|getPlugins
argument_list|(
name|all
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|plugins
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Plugin
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Plugin
name|a
parameter_list|,
name|Plugin
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|format
operator|.
name|isJson
argument_list|()
condition|)
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"%-30s %-10s %-8s\n"
argument_list|,
literal|"Name"
argument_list|,
literal|"Version"
argument_list|,
literal|"Status"
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|"-------------------------------------------------------------------------------\n"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Plugin
name|p
range|:
name|plugins
control|)
block|{
name|PluginInfo
name|info
init|=
operator|new
name|PluginInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|version
operator|=
name|p
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|info
operator|.
name|disabled
operator|=
name|p
operator|.
name|isDisabled
argument_list|()
condition|?
literal|true
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|format
operator|.
name|isJson
argument_list|()
condition|)
block|{
name|output
operator|.
name|put
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"%-30s %-10s %-8s\n"
argument_list|,
name|p
operator|.
name|getName
argument_list|()
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|info
operator|.
name|version
argument_list|)
argument_list|,
name|p
operator|.
name|isDisabled
argument_list|()
condition|?
literal|"DISABLED"
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|format
operator|.
name|isJson
argument_list|()
condition|)
block|{
name|format
operator|.
name|newGson
argument_list|()
operator|.
name|toJson
argument_list|(
name|output
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|,
name|stdout
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|class|PluginInfo
specifier|private
specifier|static
class|class
name|PluginInfo
block|{
DECL|field|version
name|String
name|version
decl_stmt|;
comment|// disabled is only read via reflection when building the json output.  We
comment|// do not want to show a compiler error that it isn't used.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|field|disabled
name|Boolean
name|disabled
decl_stmt|;
block|}
block|}
end_class

end_unit

