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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|gpg
operator|.
name|PublicKeyStore
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
name|Repository
import|;
end_import

begin_class
DECL|class|Schema_181
specifier|public
class|class
name|Schema_181
implements|implements
name|NoteDbSchemaVersion
block|{
annotation|@
name|Override
DECL|method|upgrade (Arguments args, UpdateUI ui)
specifier|public
name|void
name|upgrade
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|Exception
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Rebuild GPGP note map to build subkey to master key map"
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|args
operator|.
name|repoManager
operator|.
name|openRepository
argument_list|(
name|args
operator|.
name|allUsers
argument_list|)
init|;
name|PublicKeyStore
name|store
operator|=
operator|new
name|PublicKeyStore
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|store
operator|.
name|rebuildSubkeyMasterKeyMap
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

