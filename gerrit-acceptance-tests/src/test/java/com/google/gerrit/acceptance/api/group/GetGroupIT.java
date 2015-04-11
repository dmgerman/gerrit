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
DECL|package|com.google.gerrit.acceptance.api.group
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
name|group
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
name|acceptance
operator|.
name|api
operator|.
name|group
operator|.
name|GroupAssert
operator|.
name|assertGroupInfo
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
name|RestResponse
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
name|common
operator|.
name|GroupInfo
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|GetGroupIT
specifier|public
class|class
name|GetGroupIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|testGetGroup ()
specifier|public
name|void
name|testGetGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountGroup
name|adminGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
argument_list|)
decl_stmt|;
comment|// by UUID
name|testGetGroup
argument_list|(
literal|"/groups/"
operator|+
name|adminGroup
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|adminGroup
argument_list|)
expr_stmt|;
comment|// by name
name|testGetGroup
argument_list|(
literal|"/groups/"
operator|+
name|adminGroup
operator|.
name|getName
argument_list|()
argument_list|,
name|adminGroup
argument_list|)
expr_stmt|;
comment|// by legacy numeric ID
name|testGetGroup
argument_list|(
literal|"/groups/"
operator|+
name|adminGroup
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|adminGroup
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetGroup (String url, AccountGroup expectedGroup)
specifier|private
name|void
name|testGetGroup
parameter_list|(
name|String
name|url
parameter_list|,
name|AccountGroup
name|expectedGroup
parameter_list|)
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|GroupInfo
name|group
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|GroupInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertGroupInfo
argument_list|(
name|expectedGroup
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

