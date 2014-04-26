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
name|common
operator|.
name|FileUtil
operator|.
name|chmod
import|;
end_import

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
name|extract
import|;
end_import

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
name|mkdir
import|;
end_import

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
name|savePublic
import|;
end_import

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
name|saveSecure
import|;
end_import

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
name|version
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
name|BaseInit
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
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|OutgoingEmail
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
name|Binding
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
name|Injector
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
name|TypeLiteral
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
comment|/** Initialize (or upgrade) an existing site. */
end_comment

begin_class
DECL|class|SitePathInitializer
specifier|public
class|class
name|SitePathInitializer
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
DECL|field|steps
specifier|private
specifier|final
name|List
argument_list|<
name|InitStep
argument_list|>
name|steps
decl_stmt|;
annotation|@
name|Inject
DECL|method|SitePathInitializer (final Injector injector, final ConsoleUI ui, final InitFlags flags, final SitePaths site)
specifier|public
name|SitePathInitializer
parameter_list|(
specifier|final
name|Injector
name|injector
parameter_list|,
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
name|steps
operator|=
name|stepsOf
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Gerrit Code Review %s"
argument_list|,
name|version
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|site
operator|.
name|isNew
condition|)
block|{
if|if
condition|(
operator|!
name|ui
operator|.
name|yesno
argument_list|(
literal|true
argument_list|,
literal|"Create '%s'"
argument_list|,
name|site
operator|.
name|site_path
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"aborted by user"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|site
operator|.
name|site_path
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|site
operator|.
name|site_path
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot make directory "
operator|+
name|site
operator|.
name|site_path
argument_list|)
throw|;
block|}
name|flags
operator|.
name|deleteOnFailure
operator|=
literal|true
expr_stmt|;
block|}
name|mkdir
argument_list|(
name|site
operator|.
name|bin_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|etc_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|lib_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|tmp_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|logs_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|mail_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|static_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|plugins_dir
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|site
operator|.
name|data_dir
argument_list|)
expr_stmt|;
for|for
control|(
name|InitStep
name|step
range|:
name|steps
control|)
block|{
if|if
condition|(
name|step
operator|instanceof
name|InitPlugins
operator|&&
name|flags
operator|.
name|skipPlugins
condition|)
block|{
continue|continue;
block|}
name|step
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
name|savePublic
argument_list|(
name|flags
operator|.
name|cfg
argument_list|)
expr_stmt|;
name|saveSecure
argument_list|(
name|flags
operator|.
name|sec
argument_list|)
expr_stmt|;
name|extract
argument_list|(
name|site
operator|.
name|gerrit_sh
argument_list|,
name|BaseInit
operator|.
name|class
argument_list|,
literal|"gerrit.sh"
argument_list|)
expr_stmt|;
name|chmod
argument_list|(
literal|0755
argument_list|,
name|site
operator|.
name|gerrit_sh
argument_list|)
expr_stmt|;
name|chmod
argument_list|(
literal|0700
argument_list|,
name|site
operator|.
name|tmp_dir
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"Abandoned.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"ChangeFooter.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"ChangeSubject.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"Comment.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"CommentFooter.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"Footer.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"Merged.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"MergeFail.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"NewChange.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"RegisterNewEmail.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"ReplacePatchSet.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"Restored.vm"
argument_list|)
expr_stmt|;
name|extractMailExample
argument_list|(
literal|"Reverted.vm"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|postRun ()
specifier|public
name|void
name|postRun
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|InitStep
name|step
range|:
name|steps
control|)
block|{
if|if
condition|(
name|step
operator|instanceof
name|InitPlugins
operator|&&
name|flags
operator|.
name|skipPlugins
condition|)
block|{
continue|continue;
block|}
name|step
operator|.
name|postRun
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|extractMailExample (String orig)
specifier|private
name|void
name|extractMailExample
parameter_list|(
name|String
name|orig
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|ex
init|=
operator|new
name|File
argument_list|(
name|site
operator|.
name|mail_dir
argument_list|,
name|orig
operator|+
literal|".example"
argument_list|)
decl_stmt|;
name|extract
argument_list|(
name|ex
argument_list|,
name|OutgoingEmail
operator|.
name|class
argument_list|,
name|orig
argument_list|)
expr_stmt|;
name|chmod
argument_list|(
literal|0444
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
DECL|method|stepsOf (final Injector injector)
specifier|private
specifier|static
name|List
argument_list|<
name|InitStep
argument_list|>
name|stepsOf
parameter_list|(
specifier|final
name|Injector
name|injector
parameter_list|)
block|{
specifier|final
name|ArrayList
argument_list|<
name|InitStep
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Binding
argument_list|<
name|InitStep
argument_list|>
name|b
range|:
name|all
argument_list|(
name|injector
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|b
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|all (final Injector injector)
specifier|private
specifier|static
name|List
argument_list|<
name|Binding
argument_list|<
name|InitStep
argument_list|>
argument_list|>
name|all
parameter_list|(
specifier|final
name|Injector
name|injector
parameter_list|)
block|{
return|return
name|injector
operator|.
name|findBindingsByType
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|InitStep
argument_list|>
argument_list|()
block|{}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

