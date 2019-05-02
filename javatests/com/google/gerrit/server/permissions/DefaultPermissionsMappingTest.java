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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|Truth8
operator|.
name|assertThat
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
name|permissions
operator|.
name|DefaultPermissionMappings
operator|.
name|refPermission
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
name|Permission
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
DECL|class|DefaultPermissionsMappingTest
specifier|public
class|class
name|DefaultPermissionsMappingTest
block|{
annotation|@
name|Test
DECL|method|stringToRefPermission ()
specifier|public
name|void
name|stringToRefPermission
parameter_list|()
block|{
name|assertThat
argument_list|(
name|refPermission
argument_list|(
literal|"doesnotexist"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|refPermission
argument_list|(
literal|""
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|refPermission
argument_list|(
name|Permission
operator|.
name|VIEW_PRIVATE_CHANGES
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|RefPermission
operator|.
name|READ_PRIVATE_CHANGES
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

