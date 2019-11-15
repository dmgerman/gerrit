begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.api.plugin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|plugin
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
name|AbstractDaemonTest
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
name|NoHttpd
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
name|server
operator|.
name|plugins
operator|.
name|MissingMandatoryPluginsException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
annotation|@
name|NoHttpd
DECL|class|PluginLoaderIT
specifier|public
class|class
name|PluginLoaderIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|testDescription
name|Description
name|testDescription
decl_stmt|;
annotation|@
name|Override
DECL|method|beforeTest (Description description)
specifier|protected
name|void
name|beforeTest
parameter_list|(
name|Description
name|description
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|testDescription
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTest ()
specifier|protected
name|void
name|afterTest
parameter_list|()
throws|throws
name|Exception
block|{}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MissingMandatoryPluginsException
operator|.
name|class
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"plugins.mandatory"
argument_list|,
name|value
operator|=
literal|"my-mandatory-plugin"
argument_list|)
DECL|method|shouldFailToStartGerritWhenMandatoryPluginsAreMissing ()
specifier|public
name|void
name|shouldFailToStartGerritWhenMandatoryPluginsAreMissing
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|beforeTest
argument_list|(
name|testDescription
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

