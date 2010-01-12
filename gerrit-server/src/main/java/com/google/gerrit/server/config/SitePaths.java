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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_comment
comment|/** Important paths within a {@link SitePath}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SitePaths
specifier|public
specifier|final
class|class
name|SitePaths
block|{
DECL|field|site_path
specifier|public
specifier|final
name|File
name|site_path
decl_stmt|;
DECL|field|bin_dir
specifier|public
specifier|final
name|File
name|bin_dir
decl_stmt|;
DECL|field|etc_dir
specifier|public
specifier|final
name|File
name|etc_dir
decl_stmt|;
DECL|field|lib_dir
specifier|public
specifier|final
name|File
name|lib_dir
decl_stmt|;
DECL|field|logs_dir
specifier|public
specifier|final
name|File
name|logs_dir
decl_stmt|;
DECL|field|hooks_dir
specifier|public
specifier|final
name|File
name|hooks_dir
decl_stmt|;
DECL|field|static_dir
specifier|public
specifier|final
name|File
name|static_dir
decl_stmt|;
DECL|field|gerrit_sh
specifier|public
specifier|final
name|File
name|gerrit_sh
decl_stmt|;
DECL|field|gerrit_war
specifier|public
specifier|final
name|File
name|gerrit_war
decl_stmt|;
DECL|field|gerrit_config
specifier|public
specifier|final
name|File
name|gerrit_config
decl_stmt|;
DECL|field|secure_config
specifier|public
specifier|final
name|File
name|secure_config
decl_stmt|;
DECL|field|replication_config
specifier|public
specifier|final
name|File
name|replication_config
decl_stmt|;
DECL|field|contact_information_pub
specifier|public
specifier|final
name|File
name|contact_information_pub
decl_stmt|;
DECL|field|ssl_keystore
specifier|public
specifier|final
name|File
name|ssl_keystore
decl_stmt|;
DECL|field|ssh_key
specifier|public
specifier|final
name|File
name|ssh_key
decl_stmt|;
DECL|field|ssh_rsa
specifier|public
specifier|final
name|File
name|ssh_rsa
decl_stmt|;
DECL|field|ssh_dsa
specifier|public
specifier|final
name|File
name|ssh_dsa
decl_stmt|;
DECL|field|site_css
specifier|public
specifier|final
name|File
name|site_css
decl_stmt|;
DECL|field|site_header
specifier|public
specifier|final
name|File
name|site_header
decl_stmt|;
DECL|field|site_footer
specifier|public
specifier|final
name|File
name|site_footer
decl_stmt|;
DECL|field|site_gitweb
specifier|public
specifier|final
name|File
name|site_gitweb
decl_stmt|;
comment|/** {@code true} if {@link #site_path} has not been initialized. */
DECL|field|isNew
specifier|public
specifier|final
name|boolean
name|isNew
decl_stmt|;
annotation|@
name|Inject
DECL|method|SitePaths (final @SitePath File sitePath)
specifier|public
name|SitePaths
parameter_list|(
specifier|final
annotation|@
name|SitePath
name|File
name|sitePath
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|site_path
operator|=
name|sitePath
expr_stmt|;
name|bin_dir
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
literal|"bin"
argument_list|)
expr_stmt|;
name|etc_dir
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
literal|"etc"
argument_list|)
expr_stmt|;
name|lib_dir
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
literal|"lib"
argument_list|)
expr_stmt|;
name|logs_dir
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
literal|"logs"
argument_list|)
expr_stmt|;
name|hooks_dir
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
literal|"hooks"
argument_list|)
expr_stmt|;
name|static_dir
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
literal|"static"
argument_list|)
expr_stmt|;
name|gerrit_sh
operator|=
operator|new
name|File
argument_list|(
name|bin_dir
argument_list|,
literal|"gerrit.sh"
argument_list|)
expr_stmt|;
name|gerrit_war
operator|=
operator|new
name|File
argument_list|(
name|bin_dir
argument_list|,
literal|"gerrit.war"
argument_list|)
expr_stmt|;
name|gerrit_config
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"gerrit.config"
argument_list|)
expr_stmt|;
name|secure_config
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"secure.config"
argument_list|)
expr_stmt|;
name|replication_config
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"replication.config"
argument_list|)
expr_stmt|;
name|contact_information_pub
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"contact_information.pub"
argument_list|)
expr_stmt|;
name|ssl_keystore
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"keystore"
argument_list|)
expr_stmt|;
name|ssh_key
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"ssh_host_key"
argument_list|)
expr_stmt|;
name|ssh_rsa
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"ssh_host_rsa_key"
argument_list|)
expr_stmt|;
name|ssh_dsa
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"ssh_host_dsa_key"
argument_list|)
expr_stmt|;
name|site_css
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"GerritSite.css"
argument_list|)
expr_stmt|;
name|site_header
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"GerritSiteHeader.html"
argument_list|)
expr_stmt|;
name|site_footer
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"GerritSiteFooter.html"
argument_list|)
expr_stmt|;
name|site_gitweb
operator|=
operator|new
name|File
argument_list|(
name|etc_dir
argument_list|,
literal|"gitweb_config.perl"
argument_list|)
expr_stmt|;
if|if
condition|(
name|site_path
operator|.
name|exists
argument_list|()
condition|)
block|{
specifier|final
name|String
index|[]
name|contents
init|=
name|site_path
operator|.
name|list
argument_list|()
decl_stmt|;
if|if
condition|(
name|contents
operator|!=
literal|null
condition|)
name|isNew
operator|=
name|contents
operator|.
name|length
operator|==
literal|0
expr_stmt|;
elseif|else
if|if
condition|(
name|site_path
operator|.
name|isDirectory
argument_list|()
condition|)
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Cannot access "
operator|+
name|site_path
argument_list|)
throw|;
else|else
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Not a directory: "
operator|+
name|site_path
argument_list|)
throw|;
block|}
else|else
block|{
name|isNew
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**    * Resolve an absolute or relative path.    *<p>    * Relative paths are resolved relative to the {@link #site_path}.    *    * @param path the path string to resolve. May be null.    * @return the resolved path; null if {@code path} was null or empty.    */
DECL|method|resolve (final String path)
specifier|public
name|File
name|resolve
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
operator|!
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|File
name|loc
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|loc
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|loc
operator|=
operator|new
name|File
argument_list|(
name|site_path
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
return|return
name|loc
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

