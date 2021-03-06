begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitUtil
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
name|config
operator|.
name|GerritServerConfig
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Opens the user's web browser to the web UI. */
end_comment

begin_class
DECL|class|Browser
specifier|public
class|class
name|Browser
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|Browser (@erritServerConfig Config cfg)
name|Browser
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
DECL|method|open ()
specifier|public
name|void
name|open
parameter_list|()
throws|throws
name|Exception
block|{
name|open
argument_list|(
literal|null
comment|/* root page */
argument_list|)
expr_stmt|;
block|}
DECL|method|open (String link)
specifier|public
name|void
name|open
parameter_list|(
name|String
name|link
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalWebUrl"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"httpd"
argument_list|,
literal|null
argument_list|,
literal|"listenUrl"
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"proxy-"
argument_list|)
condition|)
block|{
name|url
operator|=
name|url
operator|.
name|substring
argument_list|(
literal|"proxy-"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|URI
name|uri
decl_stmt|;
try|try
block|{
name|uri
operator|=
name|InitUtil
operator|.
name|toURI
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: invalid httpd.listenUrl: "
operator|+
name|url
argument_list|)
expr_stmt|;
return|return;
block|}
name|waitForServer
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|openBrowser
argument_list|(
name|uri
argument_list|,
name|link
argument_list|)
expr_stmt|;
block|}
DECL|method|waitForServer (URI uri)
specifier|private
name|void
name|waitForServer
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|host
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|InitUtil
operator|.
name|portOf
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|format
argument_list|(
literal|"Waiting for server on %s:%d ... "
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|Socket
name|s
decl_stmt|;
try|try
block|{
name|s
operator|=
operator|new
name|Socket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
comment|// Ignored
block|}
continue|continue;
block|}
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
break|break;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"OK"
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveUrl (URI uri, String link)
specifier|private
name|String
name|resolveUrl
parameter_list|(
name|URI
name|uri
parameter_list|,
name|String
name|link
parameter_list|)
block|{
name|String
name|url
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalWebUrl"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|url
operator|=
name|uri
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|link
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"#"
operator|+
name|link
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
DECL|method|openBrowser (URI uri, String link)
specifier|private
name|void
name|openBrowser
parameter_list|(
name|URI
name|uri
parameter_list|,
name|String
name|link
parameter_list|)
block|{
name|String
name|url
init|=
name|resolveUrl
argument_list|(
name|uri
argument_list|,
name|link
argument_list|)
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|format
argument_list|(
literal|"Opening %s ..."
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
name|org
operator|.
name|h2
operator|.
name|tools
operator|.
name|Server
operator|.
name|openBrowser
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"OK"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"FAILED"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Open Gerrit with a JavaScript capable browser:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

