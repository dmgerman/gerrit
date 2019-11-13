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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
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
name|acceptance
operator|.
name|config
operator|.
name|ConfigAnnotationParser
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
name|acceptance
operator|.
name|config
operator|.
name|GerritConfig
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
name|acceptance
operator|.
name|config
operator|.
name|GerritConfigs
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
name|RunWith
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
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
DECL|class|GerritServerTests
specifier|public
class|class
name|GerritServerTests
block|{
DECL|field|config
annotation|@
name|ConfigSuite
operator|.
name|Parameter
specifier|public
name|Config
name|config
decl_stmt|;
DECL|field|configName
annotation|@
name|ConfigSuite
operator|.
name|Name
specifier|private
name|String
name|configName
decl_stmt|;
annotation|@
name|Rule
DECL|field|testRunner
specifier|public
name|TestRule
name|testRunner
init|=
parameter_list|(
name|base
parameter_list|,
name|description
parameter_list|)
lambda|->
block|{
name|GerritConfig
name|gerritConfig
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
name|gerritConfig
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|ConfigAnnotationParser
operator|.
name|parse
argument_list|(
name|config
argument_list|,
name|gerritConfig
argument_list|)
expr_stmt|;
block|}
name|GerritConfigs
name|gerritConfigs
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
if|if
condition|(
name|gerritConfigs
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|ConfigAnnotationParser
operator|.
name|parse
argument_list|(
name|config
argument_list|,
name|gerritConfigs
argument_list|)
expr_stmt|;
block|}
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
name|base
operator|.
name|evaluate
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
decl_stmt|;
block|}
end_class

end_unit

