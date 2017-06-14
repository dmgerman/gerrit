begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|ImmutableSet
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
name|GeneralPreferencesInfo
operator|.
name|DownloadCommand
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
name|CoreDownloadSchemes
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
name|change
operator|.
name|ArchiveFormat
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|EnumSet
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
comment|/**  * Download protocol from {@code gerrit.config}.  *  *<p>Only used to configure the built-in set of schemes and commands in the core download-commands  * plugin; not used by other plugins.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|DownloadConfig
specifier|public
class|class
name|DownloadConfig
block|{
DECL|field|downloadSchemes
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|downloadSchemes
decl_stmt|;
DECL|field|downloadCommands
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
decl_stmt|;
DECL|field|archiveFormats
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|ArchiveFormat
argument_list|>
name|archiveFormats
decl_stmt|;
annotation|@
name|Inject
DECL|method|DownloadConfig (@erritServerConfig Config cfg)
name|DownloadConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|String
index|[]
name|allSchemes
init|=
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"download"
argument_list|,
literal|null
argument_list|,
literal|"scheme"
argument_list|)
decl_stmt|;
if|if
condition|(
name|allSchemes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|downloadSchemes
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|CoreDownloadSchemes
operator|.
name|SSH
argument_list|,
name|CoreDownloadSchemes
operator|.
name|HTTP
argument_list|,
name|CoreDownloadSchemes
operator|.
name|ANON_HTTP
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|normalized
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|allSchemes
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|allSchemes
control|)
block|{
name|String
name|core
init|=
name|toCoreScheme
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|core
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"not a core download scheme: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|normalized
operator|.
name|add
argument_list|(
name|core
argument_list|)
expr_stmt|;
block|}
name|downloadSchemes
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|normalized
argument_list|)
expr_stmt|;
block|}
name|DownloadCommand
index|[]
name|downloadCommandValues
init|=
name|DownloadCommand
operator|.
name|values
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DownloadCommand
argument_list|>
name|allCommands
init|=
name|ConfigUtil
operator|.
name|getEnumList
argument_list|(
name|cfg
argument_list|,
literal|"download"
argument_list|,
literal|null
argument_list|,
literal|"command"
argument_list|,
name|downloadCommandValues
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOnlyNull
argument_list|(
name|allCommands
argument_list|)
condition|)
block|{
name|downloadCommands
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|downloadCommandValues
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|downloadCommands
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|allCommands
argument_list|)
expr_stmt|;
block|}
name|String
name|v
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"download"
argument_list|,
literal|null
argument_list|,
literal|"archive"
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|archiveFormats
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|EnumSet
operator|.
name|allOf
argument_list|(
name|ArchiveFormat
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|v
operator|.
name|isEmpty
argument_list|()
operator|||
literal|"off"
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
name|archiveFormats
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|archiveFormats
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|ConfigUtil
operator|.
name|getEnumList
argument_list|(
name|cfg
argument_list|,
literal|"download"
argument_list|,
literal|null
argument_list|,
literal|"archive"
argument_list|,
name|ArchiveFormat
operator|.
name|TGZ
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isOnlyNull (List<?> list)
specifier|private
specifier|static
name|boolean
name|isOnlyNull
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
literal|null
return|;
block|}
DECL|method|toCoreScheme (String s)
specifier|private
specifier|static
name|String
name|toCoreScheme
parameter_list|(
name|String
name|s
parameter_list|)
block|{
try|try
block|{
name|Field
name|f
init|=
name|CoreDownloadSchemes
operator|.
name|class
operator|.
name|getField
argument_list|(
name|s
operator|.
name|toUpperCase
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|m
init|=
name|Modifier
operator|.
name|PUBLIC
operator||
name|Modifier
operator|.
name|STATIC
operator||
name|Modifier
operator|.
name|FINAL
decl_stmt|;
if|if
condition|(
operator|(
name|f
operator|.
name|getModifiers
argument_list|()
operator|&
name|m
operator|)
operator|==
name|m
operator|&&
name|f
operator|.
name|getType
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
decl||
name|SecurityException
decl||
name|IllegalArgumentException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/** Scheme used to download. */
DECL|method|getDownloadSchemes ()
specifier|public
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|getDownloadSchemes
parameter_list|()
block|{
return|return
name|downloadSchemes
return|;
block|}
comment|/** Command used to download. */
DECL|method|getDownloadCommands ()
specifier|public
name|ImmutableSet
argument_list|<
name|DownloadCommand
argument_list|>
name|getDownloadCommands
parameter_list|()
block|{
return|return
name|downloadCommands
return|;
block|}
comment|/** Archive formats for downloading. */
DECL|method|getArchiveFormats ()
specifier|public
name|ImmutableSet
argument_list|<
name|ArchiveFormat
argument_list|>
name|getArchiveFormats
parameter_list|()
block|{
return|return
name|archiveFormats
return|;
block|}
block|}
end_class

end_unit

