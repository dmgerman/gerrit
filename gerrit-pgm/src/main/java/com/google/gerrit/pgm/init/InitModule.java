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
name|server
operator|.
name|config
operator|.
name|FactoryModule
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
name|binder
operator|.
name|LinkedBindingBuilder
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
name|internal
operator|.
name|UniqueAnnotations
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_comment
comment|/** Injection configuration for the site initialization process. */
end_comment

begin_class
DECL|class|InitModule
specifier|public
class|class
name|InitModule
extends|extends
name|FactoryModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|SitePaths
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|InitFlags
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Libraries
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|LibraryDownloader
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|Section
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Steps are executed in the order listed here.
comment|//
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|UpgradeFrom2_0_x
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitGitManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitDatabase
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitAuth
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitSendEmail
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitContainer
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitSshd
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitHttpd
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitCache
operator|.
name|class
argument_list|)
expr_stmt|;
name|step
argument_list|()
operator|.
name|to
argument_list|(
name|InitPlugins
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|step ()
specifier|protected
name|LinkedBindingBuilder
argument_list|<
name|InitStep
argument_list|>
name|step
parameter_list|()
block|{
specifier|final
name|Annotation
name|id
init|=
name|UniqueAnnotations
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|bind
argument_list|(
name|InitStep
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit

