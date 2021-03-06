begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|PROJECT_OWNERS
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
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
name|assertFalse
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
name|assertNull
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|AdditionalMatchers
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|entities
operator|.
name|AccountGroup
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
name|entities
operator|.
name|AccountGroup
operator|.
name|UUID
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
name|DynamicSet
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
name|IdentifiedUser
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
name|group
operator|.
name|SystemGroupBackend
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
name|plugincontext
operator|.
name|PluginContext
operator|.
name|PluginMetrics
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
name|plugincontext
operator|.
name|PluginSetContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_class
DECL|class|UniversalGroupBackendTest
specifier|public
class|class
name|UniversalGroupBackendTest
block|{
DECL|field|OTHER_UUID
specifier|private
specifier|static
specifier|final
name|AccountGroup
operator|.
name|UUID
name|OTHER_UUID
init|=
name|AccountGroup
operator|.
name|uuid
argument_list|(
literal|"other"
argument_list|)
decl_stmt|;
DECL|field|backend
specifier|private
name|UniversalGroupBackend
name|backend
decl_stmt|;
DECL|field|user
specifier|private
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|backends
specifier|private
name|DynamicSet
argument_list|<
name|GroupBackend
argument_list|>
name|backends
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|user
operator|=
name|mock
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
expr_stmt|;
name|backends
operator|=
operator|new
name|DynamicSet
argument_list|<>
argument_list|()
expr_stmt|;
name|backends
operator|.
name|add
argument_list|(
literal|"gerrit"
argument_list|,
operator|new
name|SystemGroupBackend
argument_list|(
operator|new
name|Config
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|backend
operator|=
operator|new
name|UniversalGroupBackend
argument_list|(
operator|new
name|PluginSetContext
argument_list|<>
argument_list|(
name|backends
argument_list|,
name|PluginMetrics
operator|.
name|DISABLED_INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|handles ()
specifier|public
name|void
name|handles
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|backend
operator|.
name|handles
argument_list|(
name|ANONYMOUS_USERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|backend
operator|.
name|handles
argument_list|(
name|PROJECT_OWNERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|backend
operator|.
name|handles
argument_list|(
name|OTHER_UUID
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|get ()
specifier|public
name|void
name|get
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Registered Users"
argument_list|,
name|backend
operator|.
name|get
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Project Owners"
argument_list|,
name|backend
operator|.
name|get
argument_list|(
name|PROJECT_OWNERS
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|backend
operator|.
name|get
argument_list|(
name|OTHER_UUID
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|suggest ()
specifier|public
name|void
name|suggest
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|backend
operator|.
name|suggest
argument_list|(
literal|"X"
argument_list|,
literal|null
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|backend
operator|.
name|suggest
argument_list|(
literal|"project"
argument_list|,
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|backend
operator|.
name|suggest
argument_list|(
literal|"reg"
argument_list|,
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sytemGroupMemberships ()
specifier|public
name|void
name|sytemGroupMemberships
parameter_list|()
block|{
name|GroupMembership
name|checker
init|=
name|backend
operator|.
name|membershipsOf
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|OTHER_UUID
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|PROJECT_OWNERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|knownGroups ()
specifier|public
name|void
name|knownGroups
parameter_list|()
block|{
name|GroupMembership
name|checker
init|=
name|backend
operator|.
name|membershipsOf
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|UUID
argument_list|>
name|knownGroups
init|=
name|checker
operator|.
name|getKnownGroups
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|knownGroups
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|knownGroups
operator|.
name|contains
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|knownGroups
operator|.
name|contains
argument_list|(
name|ANONYMOUS_USERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|otherMemberships ()
specifier|public
name|void
name|otherMemberships
parameter_list|()
block|{
specifier|final
name|AccountGroup
operator|.
name|UUID
name|handled
init|=
name|AccountGroup
operator|.
name|uuid
argument_list|(
literal|"handled"
argument_list|)
decl_stmt|;
specifier|final
name|AccountGroup
operator|.
name|UUID
name|notHandled
init|=
name|AccountGroup
operator|.
name|uuid
argument_list|(
literal|"not handled"
argument_list|)
decl_stmt|;
specifier|final
name|IdentifiedUser
name|member
init|=
name|mock
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|IdentifiedUser
name|notMember
init|=
name|mock
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
decl_stmt|;
name|GroupBackend
name|backend
init|=
name|mock
argument_list|(
name|GroupBackend
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|backend
operator|.
name|handles
argument_list|(
name|eq
argument_list|(
name|handled
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|backend
operator|.
name|handles
argument_list|(
name|not
argument_list|(
name|eq
argument_list|(
name|handled
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|backend
operator|.
name|membershipsOf
argument_list|(
name|any
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|GroupMembership
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|GroupMembership
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|GroupMembership
name|membership
init|=
name|mock
argument_list|(
name|GroupMembership
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|membership
operator|.
name|contains
argument_list|(
name|eq
argument_list|(
name|handled
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
operator|==
name|member
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|membership
operator|.
name|contains
argument_list|(
name|eq
argument_list|(
name|notHandled
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|membership
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|backends
operator|=
operator|new
name|DynamicSet
argument_list|<>
argument_list|()
expr_stmt|;
name|backends
operator|.
name|add
argument_list|(
literal|"gerrit"
argument_list|,
name|backend
argument_list|)
expr_stmt|;
name|backend
operator|=
operator|new
name|UniversalGroupBackend
argument_list|(
operator|new
name|PluginSetContext
argument_list|<>
argument_list|(
name|backends
argument_list|,
name|PluginMetrics
operator|.
name|DISABLED_INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
name|GroupMembership
name|checker
init|=
name|backend
operator|.
name|membershipsOf
argument_list|(
name|member
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|OTHER_UUID
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|handled
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|notHandled
argument_list|)
argument_list|)
expr_stmt|;
name|checker
operator|=
name|backend
operator|.
name|membershipsOf
argument_list|(
name|notMember
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|handled
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|checker
operator|.
name|contains
argument_list|(
name|notHandled
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

