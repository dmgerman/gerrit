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
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|io
operator|.
name|IOException
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
name|DirectoryStream
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
name|Files
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
name|NoSuchFileException
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
DECL|field|CSS_FILENAME
specifier|public
specifier|static
specifier|final
name|String
name|CSS_FILENAME
init|=
literal|"GerritSite.css"
decl_stmt|;
DECL|field|HEADER_FILENAME
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_FILENAME
init|=
literal|"GerritSiteHeader.html"
decl_stmt|;
DECL|field|FOOTER_FILENAME
specifier|public
specifier|static
specifier|final
name|String
name|FOOTER_FILENAME
init|=
literal|"GerritSiteFooter.html"
decl_stmt|;
DECL|field|THEME_FILENAME
specifier|public
specifier|static
specifier|final
name|String
name|THEME_FILENAME
init|=
literal|"gerrit-theme.html"
decl_stmt|;
DECL|field|site_path
specifier|public
specifier|final
name|Path
name|site_path
decl_stmt|;
DECL|field|bin_dir
specifier|public
specifier|final
name|Path
name|bin_dir
decl_stmt|;
DECL|field|etc_dir
specifier|public
specifier|final
name|Path
name|etc_dir
decl_stmt|;
DECL|field|lib_dir
specifier|public
specifier|final
name|Path
name|lib_dir
decl_stmt|;
DECL|field|tmp_dir
specifier|public
specifier|final
name|Path
name|tmp_dir
decl_stmt|;
DECL|field|logs_dir
specifier|public
specifier|final
name|Path
name|logs_dir
decl_stmt|;
DECL|field|plugins_dir
specifier|public
specifier|final
name|Path
name|plugins_dir
decl_stmt|;
DECL|field|db_dir
specifier|public
specifier|final
name|Path
name|db_dir
decl_stmt|;
DECL|field|data_dir
specifier|public
specifier|final
name|Path
name|data_dir
decl_stmt|;
DECL|field|mail_dir
specifier|public
specifier|final
name|Path
name|mail_dir
decl_stmt|;
DECL|field|hooks_dir
specifier|public
specifier|final
name|Path
name|hooks_dir
decl_stmt|;
DECL|field|static_dir
specifier|public
specifier|final
name|Path
name|static_dir
decl_stmt|;
DECL|field|index_dir
specifier|public
specifier|final
name|Path
name|index_dir
decl_stmt|;
DECL|field|gerrit_sh
specifier|public
specifier|final
name|Path
name|gerrit_sh
decl_stmt|;
DECL|field|gerrit_service
specifier|public
specifier|final
name|Path
name|gerrit_service
decl_stmt|;
DECL|field|gerrit_socket
specifier|public
specifier|final
name|Path
name|gerrit_socket
decl_stmt|;
DECL|field|gerrit_war
specifier|public
specifier|final
name|Path
name|gerrit_war
decl_stmt|;
DECL|field|gerrit_config
specifier|public
specifier|final
name|Path
name|gerrit_config
decl_stmt|;
DECL|field|secure_config
specifier|public
specifier|final
name|Path
name|secure_config
decl_stmt|;
DECL|field|notedb_config
specifier|public
specifier|final
name|Path
name|notedb_config
decl_stmt|;
DECL|field|jgit_config
specifier|public
specifier|final
name|Path
name|jgit_config
decl_stmt|;
DECL|field|ssl_keystore
specifier|public
specifier|final
name|Path
name|ssl_keystore
decl_stmt|;
DECL|field|ssh_key
specifier|public
specifier|final
name|Path
name|ssh_key
decl_stmt|;
DECL|field|ssh_rsa
specifier|public
specifier|final
name|Path
name|ssh_rsa
decl_stmt|;
DECL|field|ssh_ecdsa_256
specifier|public
specifier|final
name|Path
name|ssh_ecdsa_256
decl_stmt|;
DECL|field|ssh_ecdsa_384
specifier|public
specifier|final
name|Path
name|ssh_ecdsa_384
decl_stmt|;
DECL|field|ssh_ecdsa_521
specifier|public
specifier|final
name|Path
name|ssh_ecdsa_521
decl_stmt|;
DECL|field|ssh_ed25519
specifier|public
specifier|final
name|Path
name|ssh_ed25519
decl_stmt|;
DECL|field|peer_keys
specifier|public
specifier|final
name|Path
name|peer_keys
decl_stmt|;
DECL|field|site_css
specifier|public
specifier|final
name|Path
name|site_css
decl_stmt|;
DECL|field|site_header
specifier|public
specifier|final
name|Path
name|site_header
decl_stmt|;
DECL|field|site_footer
specifier|public
specifier|final
name|Path
name|site_footer
decl_stmt|;
DECL|field|site_theme
specifier|public
specifier|final
name|Path
name|site_theme
decl_stmt|;
comment|// For PolyGerrit UI only.
DECL|field|site_gitweb
specifier|public
specifier|final
name|Path
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
DECL|method|SitePaths (@itePath Path sitePath)
specifier|public
name|SitePaths
parameter_list|(
annotation|@
name|SitePath
name|Path
name|sitePath
parameter_list|)
throws|throws
name|IOException
block|{
name|site_path
operator|=
name|sitePath
expr_stmt|;
name|Path
name|p
init|=
name|sitePath
decl_stmt|;
name|bin_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"bin"
argument_list|)
expr_stmt|;
name|etc_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"etc"
argument_list|)
expr_stmt|;
name|lib_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"lib"
argument_list|)
expr_stmt|;
name|tmp_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"tmp"
argument_list|)
expr_stmt|;
name|plugins_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"plugins"
argument_list|)
expr_stmt|;
name|db_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"db"
argument_list|)
expr_stmt|;
name|data_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"data"
argument_list|)
expr_stmt|;
name|logs_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"logs"
argument_list|)
expr_stmt|;
name|mail_dir
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"mail"
argument_list|)
expr_stmt|;
name|hooks_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"hooks"
argument_list|)
expr_stmt|;
name|static_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"static"
argument_list|)
expr_stmt|;
name|index_dir
operator|=
name|p
operator|.
name|resolve
argument_list|(
literal|"index"
argument_list|)
expr_stmt|;
name|gerrit_sh
operator|=
name|bin_dir
operator|.
name|resolve
argument_list|(
literal|"gerrit.sh"
argument_list|)
expr_stmt|;
name|gerrit_service
operator|=
name|bin_dir
operator|.
name|resolve
argument_list|(
literal|"gerrit.service"
argument_list|)
expr_stmt|;
name|gerrit_socket
operator|=
name|bin_dir
operator|.
name|resolve
argument_list|(
literal|"gerrit.socket"
argument_list|)
expr_stmt|;
name|gerrit_war
operator|=
name|bin_dir
operator|.
name|resolve
argument_list|(
literal|"gerrit.war"
argument_list|)
expr_stmt|;
name|gerrit_config
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"gerrit.config"
argument_list|)
expr_stmt|;
name|secure_config
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"secure.config"
argument_list|)
expr_stmt|;
name|notedb_config
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"notedb.config"
argument_list|)
expr_stmt|;
name|jgit_config
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"jgit.config"
argument_list|)
expr_stmt|;
name|ssl_keystore
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"keystore"
argument_list|)
expr_stmt|;
name|ssh_key
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"ssh_host_key"
argument_list|)
expr_stmt|;
name|ssh_rsa
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"ssh_host_rsa_key"
argument_list|)
expr_stmt|;
name|ssh_ecdsa_256
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"ssh_host_ecdsa_key"
argument_list|)
expr_stmt|;
name|ssh_ecdsa_384
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"ssh_host_ecdsa_384_key"
argument_list|)
expr_stmt|;
name|ssh_ecdsa_521
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"ssh_host_ecdsa_521_key"
argument_list|)
expr_stmt|;
name|ssh_ed25519
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"ssh_host_ed25519_key"
argument_list|)
expr_stmt|;
name|peer_keys
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"peer_keys"
argument_list|)
expr_stmt|;
name|site_css
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
name|CSS_FILENAME
argument_list|)
expr_stmt|;
name|site_header
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
name|HEADER_FILENAME
argument_list|)
expr_stmt|;
name|site_footer
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
name|FOOTER_FILENAME
argument_list|)
expr_stmt|;
name|site_gitweb
operator|=
name|etc_dir
operator|.
name|resolve
argument_list|(
literal|"gitweb_config.perl"
argument_list|)
expr_stmt|;
comment|// For PolyGerrit UI.
name|site_theme
operator|=
name|static_dir
operator|.
name|resolve
argument_list|(
name|THEME_FILENAME
argument_list|)
expr_stmt|;
name|boolean
name|isNew
decl_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|files
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|site_path
argument_list|)
init|)
block|{
name|isNew
operator|=
name|Iterables
operator|.
name|isEmpty
argument_list|(
name|files
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
name|isNew
operator|=
literal|true
expr_stmt|;
block|}
name|this
operator|.
name|isNew
operator|=
name|isNew
expr_stmt|;
block|}
comment|/**    * Resolve an absolute or relative path.    *    *<p>Relative paths are resolved relative to the {@link #site_path}.    *    * @param path the path string to resolve. May be null.    * @return the resolved path; null if {@code path} was null or empty.    */
DECL|method|resolve (String path)
specifier|public
name|Path
name|resolve
parameter_list|(
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
name|Path
name|loc
init|=
name|site_path
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
operator|.
name|normalize
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|loc
operator|.
name|toRealPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|loc
operator|.
name|toAbsolutePath
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

