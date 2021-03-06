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
name|gerrit
operator|.
name|common
operator|.
name|FileUtil
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
name|ConsoleUI
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
name|InitFlags
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
name|InitStep
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
name|Section
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
name|SitePaths
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
name|Path
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

begin_comment
comment|/** Initialize the {@code cache} configuration section. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitCache
class|class
name|InitCache
implements|implements
name|InitStep
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|flags
specifier|private
specifier|final
name|InitFlags
name|flags
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|Section
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitCache ( final ConsoleUI ui, final InitFlags flags, final SitePaths site, final Section.Factory sections)
name|InitCache
parameter_list|(
specifier|final
name|ConsoleUI
name|ui
parameter_list|,
specifier|final
name|InitFlags
name|flags
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|flags
operator|=
name|flags
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"cache"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Cache"
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|cache
operator|.
name|get
argument_list|(
literal|"directory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Explicitly set to empty implies the administrator has
comment|// disabled the on disk cache and doesn't want it enabled.
comment|//
return|return;
block|}
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
literal|"cache"
expr_stmt|;
name|cache
operator|.
name|set
argument_list|(
literal|"directory"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
name|Path
name|loc
init|=
name|site
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|mkdirsOrDie
argument_list|(
name|loc
argument_list|,
literal|"cannot create cache.directory"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Path
argument_list|>
name|cacheFiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|stream
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|loc
argument_list|,
literal|"*.{lock,h2,trace}.db"
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|entry
range|:
name|stream
control|)
block|{
name|cacheFiles
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"IO error during cache directory scan"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|cacheFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Path
name|entry
range|:
name|cacheFiles
control|)
block|{
if|if
condition|(
name|flags
operator|.
name|deleteCaches
operator|||
name|ui
operator|.
name|yesno
argument_list|(
literal|false
argument_list|,
literal|"Delete cache file %s"
argument_list|,
name|entry
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Could not delete "
operator|+
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

