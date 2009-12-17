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
name|pgm
operator|.
name|util
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
comment|/** Initialize the GitRepositoryManager configuration section. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitGitManager
class|class
name|InitGitManager
implements|implements
name|InitStep
block|{
DECL|field|flags
specifier|private
specifier|final
name|InitFlags
name|flags
decl_stmt|;
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|gerrit
specifier|private
specifier|final
name|Section
name|gerrit
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitGitManager (final InitFlags flags, final ConsoleUI ui, final Section.Factory sections)
name|InitGitManager
parameter_list|(
specifier|final
name|InitFlags
name|flags
parameter_list|,
specifier|final
name|ConsoleUI
name|ui
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
name|flags
operator|=
name|flags
expr_stmt|;
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|gerrit
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"gerrit"
argument_list|)
expr_stmt|;
block|}
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
literal|"Git Repositories"
argument_list|)
expr_stmt|;
name|File
name|d
init|=
name|gerrit
operator|.
name|path
argument_list|(
literal|"Location of Git repositories"
argument_list|,
literal|"basePath"
argument_list|,
literal|"git"
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"gerrit.basePath is required"
argument_list|)
throw|;
block|}
if|if
condition|(
name|d
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|flags
operator|.
name|importProjects
operator|&&
name|d
operator|.
name|list
argument_list|()
operator|!=
literal|null
operator|&&
name|d
operator|.
name|list
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|flags
operator|.
name|importProjects
operator|=
name|ui
operator|.
name|yesno
argument_list|(
literal|true
argument_list|,
literal|"Import existing repositories"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|d
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot create "
operator|+
name|d
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

