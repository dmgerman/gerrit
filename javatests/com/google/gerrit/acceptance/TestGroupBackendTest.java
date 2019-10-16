begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|ExtensionRegistry
operator|.
name|Registration
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
name|server
operator|.
name|account
operator|.
name|UniversalGroupBackend
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
name|testing
operator|.
name|TestGroupBackend
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TestGroupBackendTest
specifier|public
class|class
name|TestGroupBackendTest
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|universalGroupBackend
annotation|@
name|Inject
specifier|private
name|UniversalGroupBackend
name|universalGroupBackend
decl_stmt|;
DECL|field|extensionRegistry
annotation|@
name|Inject
specifier|private
name|ExtensionRegistry
name|extensionRegistry
decl_stmt|;
DECL|field|testGroupBackend
specifier|private
specifier|final
name|TestGroupBackend
name|testGroupBackend
init|=
operator|new
name|TestGroupBackend
argument_list|()
decl_stmt|;
DECL|field|testUUID
specifier|private
specifier|final
name|AccountGroup
operator|.
name|UUID
name|testUUID
init|=
name|AccountGroup
operator|.
name|uuid
argument_list|(
literal|"testbackend:test"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|handlesTestGroup ()
specifier|public
name|void
name|handlesTestGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|testGroupBackend
operator|.
name|handles
argument_list|(
name|testUUID
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|universalGroupBackendHandlesTestGroup ()
specifier|public
name|void
name|universalGroupBackendHandlesTestGroup
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|Registration
name|registration
init|=
name|extensionRegistry
operator|.
name|newRegistration
argument_list|()
operator|.
name|add
argument_list|(
name|testGroupBackend
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|universalGroupBackend
operator|.
name|handles
argument_list|(
name|testUUID
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|doesNotHandleLDAP ()
specifier|public
name|void
name|doesNotHandleLDAP
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|testGroupBackend
operator|.
name|handles
argument_list|(
name|AccountGroup
operator|.
name|uuid
argument_list|(
literal|"ldap:1234"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doesNotHandleNull ()
specifier|public
name|void
name|doesNotHandleNull
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|testGroupBackend
operator|.
name|handles
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|returnsNullWhenGroupDoesNotExist ()
specifier|public
name|void
name|returnsNullWhenGroupDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|testGroupBackend
operator|.
name|get
argument_list|(
name|testUUID
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|returnsNullForNullGroup ()
specifier|public
name|void
name|returnsNullForNullGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|testGroupBackend
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|returnsKnownGroup ()
specifier|public
name|void
name|returnsKnownGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|testGroupBackend
operator|.
name|create
argument_list|(
name|testUUID
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testGroupBackend
operator|.
name|get
argument_list|(
name|testUUID
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

