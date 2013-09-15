begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_class
DECL|class|AbstractDaemonTest
specifier|public
specifier|abstract
class|class
name|AbstractDaemonTest
block|{
DECL|field|server
specifier|protected
name|GerritServer
name|server
decl_stmt|;
annotation|@
name|Rule
DECL|field|testRunner
specifier|public
name|TestRule
name|testRunner
init|=
operator|new
name|TestRule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Statement
name|apply
parameter_list|(
specifier|final
name|Statement
name|base
parameter_list|,
specifier|final
name|Description
name|description
parameter_list|)
block|{
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|beforeTest
argument_list|(
name|config
argument_list|(
name|description
argument_list|)
argument_list|)
expr_stmt|;
name|base
operator|.
name|evaluate
argument_list|()
expr_stmt|;
name|afterTest
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
DECL|method|config (Description description)
specifier|private
specifier|static
name|Config
name|config
parameter_list|(
name|Description
name|description
parameter_list|)
block|{
name|GerritConfigs
name|cfgs
init|=
name|description
operator|.
name|getAnnotation
argument_list|(
name|GerritConfigs
operator|.
name|class
argument_list|)
decl_stmt|;
name|GerritConfig
name|cfg
init|=
name|description
operator|.
name|getAnnotation
argument_list|(
name|GerritConfig
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfgs
operator|!=
literal|null
operator|&&
name|cfg
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Use either @GerritConfigs or @GerritConfig not both"
argument_list|)
throw|;
block|}
if|if
condition|(
name|cfgs
operator|!=
literal|null
condition|)
block|{
return|return
name|ConfigAnnotationParser
operator|.
name|parse
argument_list|(
name|cfgs
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|cfg
operator|!=
literal|null
condition|)
block|{
return|return
name|ConfigAnnotationParser
operator|.
name|parse
argument_list|(
name|cfg
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|beforeTest (Config cfg)
specifier|private
name|void
name|beforeTest
parameter_list|(
name|Config
name|cfg
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|=
name|GerritServer
operator|.
name|start
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|server
operator|.
name|getTestInjector
argument_list|()
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|afterTest ()
specifier|private
name|void
name|afterTest
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

