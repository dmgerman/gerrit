begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|Change
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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Formats URLs to different parts of the Gerrit API and UI.  *  *<p>By default, these gerrit URLs are formed by adding suffixes to the web URL. The interface  * centralizes these conventions, and also allows introducing different, custom URL schemes.  *  *<p>Unfortunately, Gerrit operates in modes for which there is no canonical URL. This can be in  * standalone utilities that have no HTTP server (eg. index upgrade commands), in servers that run  * SSH only, or in a HTTP/SSH server that is accessed over SSH without canonical web URL configured.  */
end_comment

begin_interface
DECL|interface|UrlFormatter
specifier|public
interface|interface
name|UrlFormatter
block|{
comment|/**    * The canonical base URL where this Gerrit installation can be reached.    *    *<p>For the default implementations below to work, it must end in "/".    */
DECL|method|getWebUrl ()
name|Optional
argument_list|<
name|String
argument_list|>
name|getWebUrl
parameter_list|()
function_decl|;
comment|/** Returns the URL for viewing a change. */
DECL|method|getChangeViewUrl (Project.NameKey project, Change.Id id)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getChangeViewUrl
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
comment|// In the PolyGerrit URL (contrary to REST URLs) there is no need to URL-escape strings, since
comment|// the /+/ separator unambiguously defines how to parse the path.
return|return
name|getWebUrl
argument_list|()
operator|.
name|map
argument_list|(
name|url
lambda|->
name|url
operator|+
literal|"c/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/+/"
operator|+
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|/** Returns the URL for viewing a file in a given patch set of a change. */
DECL|method|getPatchFileView (Change change, int patchsetId, String filename)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getPatchFileView
parameter_list|(
name|Change
name|change
parameter_list|,
name|int
name|patchsetId
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
return|return
name|getChangeViewUrl
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|url
lambda|->
name|url
operator|+
literal|"/"
operator|+
name|patchsetId
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
return|;
block|}
comment|/** Returns the URL for viewing a comment in a file in a given patch set of a change. */
DECL|method|getInlineCommentView ( Change change, int patchsetId, String filename, short side, int startLine)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getInlineCommentView
parameter_list|(
name|Change
name|change
parameter_list|,
name|int
name|patchsetId
parameter_list|,
name|String
name|filename
parameter_list|,
name|short
name|side
parameter_list|,
name|int
name|startLine
parameter_list|)
block|{
return|return
name|getPatchFileView
argument_list|(
name|change
argument_list|,
name|patchsetId
argument_list|,
name|filename
argument_list|)
operator|.
name|map
argument_list|(
name|url
lambda|->
name|url
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"@%s%d"
argument_list|,
name|side
operator|==
literal|0
condition|?
literal|"a"
else|:
literal|""
argument_list|,
name|startLine
argument_list|)
argument_list|)
return|;
block|}
comment|/** Returns a URL pointing to a section of the settings page. */
DECL|method|getSettingsUrl (@ullable String section)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getSettingsUrl
parameter_list|(
annotation|@
name|Nullable
name|String
name|section
parameter_list|)
block|{
return|return
name|getWebUrl
argument_list|()
operator|.
name|map
argument_list|(
name|url
lambda|->
name|url
operator|+
literal|"settings"
operator|+
operator|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|section
argument_list|)
condition|?
literal|""
else|:
literal|"#"
operator|+
name|section
operator|)
argument_list|)
return|;
block|}
comment|/** Returns a URL pointing to a documentation page, at a given named anchor. */
DECL|method|getDocUrl (String page, String anchor)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getDocUrl
parameter_list|(
name|String
name|page
parameter_list|,
name|String
name|anchor
parameter_list|)
block|{
return|return
name|getWebUrl
argument_list|()
operator|.
name|map
argument_list|(
name|url
lambda|->
name|url
operator|+
literal|"Documentation/"
operator|+
name|page
operator|+
literal|"#"
operator|+
name|anchor
argument_list|)
return|;
block|}
comment|/** Returns a REST API URL for a given suffix (eg. "accounts/self/details") */
DECL|method|getRestUrl (String suffix)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getRestUrl
parameter_list|(
name|String
name|suffix
parameter_list|)
block|{
return|return
name|getWebUrl
argument_list|()
operator|.
name|map
argument_list|(
name|url
lambda|->
name|url
operator|+
name|suffix
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

