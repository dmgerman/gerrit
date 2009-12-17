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
import|import static
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
name|InitUtil
operator|.
name|die
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
name|File
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
DECL|method|InitCache (final SitePaths site, final Section.Factory sections)
name|InitCache
parameter_list|(
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
argument_list|)
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
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
specifier|final
name|File
name|loc
init|=
name|site
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|loc
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|loc
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"cannot create cache.directory "
operator|+
name|loc
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

