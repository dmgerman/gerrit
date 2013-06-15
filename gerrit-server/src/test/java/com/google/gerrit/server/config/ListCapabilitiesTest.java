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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|annotations
operator|.
name|Exports
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
name|config
operator|.
name|CapabilityDefinition
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
name|registration
operator|.
name|DynamicMap
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
name|ListCapabilities
operator|.
name|CapabilityInfo
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
name|AbstractModule
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
name|Guice
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
name|org
operator|.
name|junit
operator|.
name|Before
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ListCapabilitiesTest
specifier|public
class|class
name|ListCapabilitiesTest
block|{
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractModule
name|mod
init|=
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|CapabilityDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CapabilityDefinition
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
literal|"printHello"
argument_list|)
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|CapabilityDefinition
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Print Hello"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|injector
operator|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|mod
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|CapabilityInfo
argument_list|>
name|m
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ListCapabilities
operator|.
name|class
argument_list|)
operator|.
name|apply
argument_list|(
operator|new
name|ConfigResource
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|GlobalCapability
operator|.
name|getAllNames
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
literal|"contains "
operator|+
name|id
argument_list|,
name|m
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|id
operator|+
literal|" has name"
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
name|String
name|pluginCapability
init|=
literal|"gerrit-printHello"
decl_stmt|;
name|assertTrue
argument_list|(
literal|"contains "
operator|+
name|pluginCapability
argument_list|,
name|m
operator|.
name|containsKey
argument_list|(
name|pluginCapability
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pluginCapability
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|pluginCapability
argument_list|)
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Print Hello"
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|pluginCapability
argument_list|)
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

