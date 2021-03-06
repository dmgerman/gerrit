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
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
operator|.
name|isExecutable
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
operator|.
name|isRegularFile
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
name|flogger
operator|.
name|FluentLogger
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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

begin_class
annotation|@
name|Singleton
DECL|class|GitwebCgiConfig
specifier|public
class|class
name|GitwebCgiConfig
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|method|disabled ()
specifier|public
name|GitwebCgiConfig
name|disabled
parameter_list|()
block|{
return|return
operator|new
name|GitwebCgiConfig
argument_list|()
return|;
block|}
DECL|field|cgi
specifier|private
specifier|final
name|Path
name|cgi
decl_stmt|;
DECL|field|css
specifier|private
specifier|final
name|Path
name|css
decl_stmt|;
DECL|field|js
specifier|private
specifier|final
name|Path
name|js
decl_stmt|;
DECL|field|logoPng
specifier|private
specifier|final
name|Path
name|logoPng
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitwebCgiConfig (SitePaths sitePaths, @GerritServerConfig Config cfg)
name|GitwebCgiConfig
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
if|if
condition|(
name|GitwebConfig
operator|.
name|isDisabled
argument_list|(
name|cfg
argument_list|)
condition|)
block|{
name|cgi
operator|=
literal|null
expr_stmt|;
name|css
operator|=
literal|null
expr_stmt|;
name|js
operator|=
literal|null
expr_stmt|;
name|logoPng
operator|=
literal|null
expr_stmt|;
return|return;
block|}
name|String
name|cfgCgi
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gitweb"
argument_list|,
literal|null
argument_list|,
literal|"cgi"
argument_list|)
decl_stmt|;
name|Path
name|pkgCgi
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/usr/lib/cgi-bin/gitweb.cgi"
argument_list|)
decl_stmt|;
name|String
index|[]
name|resourcePaths
init|=
block|{
literal|"/usr/share/gitweb/static"
block|,
literal|"/usr/share/gitweb"
block|,
literal|"/var/www/static"
block|,
literal|"/var/www"
block|,     }
decl_stmt|;
name|Path
name|cgi
decl_stmt|;
if|if
condition|(
name|cfgCgi
operator|!=
literal|null
condition|)
block|{
comment|// Use the CGI script configured by the administrator, failing if it
comment|// cannot be used as specified.
comment|//
name|cgi
operator|=
name|sitePaths
operator|.
name|resolve
argument_list|(
name|cfgCgi
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isRegularFile
argument_list|(
name|cgi
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find gitweb.cgi: "
operator|+
name|cgi
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isExecutable
argument_list|(
name|cgi
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot execute gitweb.cgi: "
operator|+
name|cgi
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|cgi
operator|.
name|equals
argument_list|(
name|pkgCgi
argument_list|)
condition|)
block|{
comment|// Assume the administrator pointed us to the distribution,
comment|// which also has the corresponding CSS and logo file.
comment|//
name|String
name|absPath
init|=
name|cgi
operator|.
name|getParent
argument_list|()
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|resourcePaths
operator|=
operator|new
name|String
index|[]
block|{
name|absPath
operator|+
literal|"/static"
block|,
name|absPath
block|}
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"gitweb"
argument_list|,
literal|null
argument_list|,
literal|"url"
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// Use an externally managed gitweb instance, and not an internal one.
comment|//
name|cgi
operator|=
literal|null
expr_stmt|;
name|resourcePaths
operator|=
operator|new
name|String
index|[]
block|{}
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isRegularFile
argument_list|(
name|pkgCgi
argument_list|)
operator|&&
name|isExecutable
argument_list|(
name|pkgCgi
argument_list|)
condition|)
block|{
comment|// Use the OS packaged CGI.
comment|//
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Assuming gitweb at %s"
argument_list|,
name|pkgCgi
argument_list|)
expr_stmt|;
name|cgi
operator|=
name|pkgCgi
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"gitweb not installed (no %s found)"
argument_list|,
name|pkgCgi
argument_list|)
expr_stmt|;
name|cgi
operator|=
literal|null
expr_stmt|;
name|resourcePaths
operator|=
operator|new
name|String
index|[]
block|{}
expr_stmt|;
block|}
name|Path
name|css
init|=
literal|null
decl_stmt|;
name|Path
name|js
init|=
literal|null
decl_stmt|;
name|Path
name|logo
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|resourcePaths
control|)
block|{
name|Path
name|dir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|css
operator|=
name|dir
operator|.
name|resolve
argument_list|(
literal|"gitweb.css"
argument_list|)
expr_stmt|;
name|js
operator|=
name|dir
operator|.
name|resolve
argument_list|(
literal|"gitweb.js"
argument_list|)
expr_stmt|;
name|logo
operator|=
name|dir
operator|.
name|resolve
argument_list|(
literal|"git-logo.png"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRegularFile
argument_list|(
name|css
argument_list|)
operator|&&
name|isRegularFile
argument_list|(
name|logo
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|this
operator|.
name|cgi
operator|=
name|cgi
expr_stmt|;
name|this
operator|.
name|css
operator|=
name|css
expr_stmt|;
name|this
operator|.
name|js
operator|=
name|js
expr_stmt|;
name|this
operator|.
name|logoPng
operator|=
name|logo
expr_stmt|;
block|}
DECL|method|GitwebCgiConfig ()
specifier|private
name|GitwebCgiConfig
parameter_list|()
block|{
name|this
operator|.
name|cgi
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|css
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|js
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|logoPng
operator|=
literal|null
expr_stmt|;
block|}
comment|/** @return local path to the CGI executable; null if we shouldn't execute. */
DECL|method|getGitwebCgi ()
specifier|public
name|Path
name|getGitwebCgi
parameter_list|()
block|{
return|return
name|cgi
return|;
block|}
comment|/** @return local path of the {@code gitweb.css} matching the CGI. */
DECL|method|getGitwebCss ()
specifier|public
name|Path
name|getGitwebCss
parameter_list|()
block|{
return|return
name|css
return|;
block|}
comment|/** @return local path of the {@code gitweb.js} for the CGI. */
DECL|method|getGitwebJs ()
specifier|public
name|Path
name|getGitwebJs
parameter_list|()
block|{
return|return
name|js
return|;
block|}
comment|/** @return local path of the {@code git-logo.png} for the CGI. */
DECL|method|getGitLogoPng ()
specifier|public
name|Path
name|getGitLogoPng
parameter_list|()
block|{
return|return
name|logoPng
return|;
block|}
block|}
end_class

end_unit

